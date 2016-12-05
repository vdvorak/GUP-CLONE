package ua.com.itproekt.gup.server.api.rest.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ua.com.itproekt.gup.exception.ResourceNotFoundException;
import ua.com.itproekt.gup.model.offer.Offer;
import ua.com.itproekt.gup.model.order.Order;
import ua.com.itproekt.gup.model.order.OrderStatus;
import ua.com.itproekt.gup.model.order.filter.OrderFilterOptions;
import ua.com.itproekt.gup.service.activityfeed.ActivityFeedService;
import ua.com.itproekt.gup.service.offers.OffersService;
import ua.com.itproekt.gup.service.order.OrderService;
import ua.com.itproekt.gup.service.profile.ProfilesService;
import ua.com.itproekt.gup.util.SecurityOperations;
import ua.com.itproekt.gup.util.TransportCompany;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/api/rest/orderService")
public class OrderRestController {

    private final ResponseEntity<String> ok = new ResponseEntity<>(HttpStatus.OK);

    @Autowired
    OrderService orderService;

    @Autowired
    ProfilesService profilesService;

    @Autowired
    OffersService offersService;

    @Autowired
    ActivityFeedService activityFeedService;


    //------------------------------------------ Read -----------------------------------------------------------------

    /**
     * @param id - order id
     * @return - return order and status code, or just redirect on 404 if order isn't exist
     */
    @CrossOrigin
    @RequestMapping(value = "/order/read/{id}", method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Order> getOrderById(@PathVariable String id) {
        Order order = orderService.findById(id);

        if (order == null) {
            throw new ResourceNotFoundException();
        }
        return new ResponseEntity<>(order, HttpStatus.OK);
    }

    /**
     * Controller return only user's orders.
     *
     * @param orderFilterOptions - order filter options
     * @return - return List of orders and status code 200
     */
    @CrossOrigin
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/order/read/all", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Order>> getOrderAll(@RequestBody OrderFilterOptions orderFilterOptions) {
        return new ResponseEntity<>(orderService.getAllOrders(orderFilterOptions), HttpStatus.OK);
    }


    //------------------------------------------ Create -------------------------------------------------------------

    /**
     * @param order - order include: offerId, orderAddress, paymentMethod, orderType, orderComment
     * @return - return status code if Ok, 400 - order not valid, 403 - if user is offer author, 404 - offer not found, 405 - if user is not buyer
     */
    @PreAuthorize("isAuthenticated()")
    @CrossOrigin
    @RequestMapping(value = "/order/create", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createOrder(@Valid @RequestBody Order order) {

        Offer offer = offersService.findById(order.getOfferId());
        if (offer == null) {
            return new ResponseEntity<>("Offer was not found", HttpStatus.NOT_FOUND);
        }

        String userId = SecurityOperations.getLoggedUserId();
        if (userId.equals(offer.getAuthorId())) {
            return new ResponseEntity<>("You are not an offer author.", HttpStatus.FORBIDDEN);
        }

        if (orderService.isOrderValid(order, offer)) {
            // create order
            orderService.create(userId, order, offer);
        } else {
            return new ResponseEntity<>("Order is not valid", HttpStatus.BAD_REQUEST);
        }
        return ok;
    }

//------------------------------------------ Update -------------------------------------------------------------

    /**
     * This method can only update order Address and/or payment method only before seller will accept Order.
     *
     * @param order - updated order.
     * @return - return status 200 code if Ok, 401 - not authorized, 400 - user is not buyer or not valid payment or shipping method,
     * 404 - not found order or offer, 405 - OrderStatus isn't NEW
     */
    @PreAuthorize("isAuthenticated()")
    @CrossOrigin
    @RequestMapping(value = "/order/update/2", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateOrder2(@Valid @RequestBody Order order) {

        String userId = SecurityOperations.getLoggedUserId();

        Offer offer = offersService.findById(order.getOfferId());
        if (offer == null) {
            return new ResponseEntity<>("Offer was not found", HttpStatus.NOT_FOUND);
        }

        Order oldOrder = orderService.findById(order.getId());
        if (oldOrder == null) {
            return new ResponseEntity<>("Order was not found", HttpStatus.NOT_FOUND);
        }

        if (!userId.equals(oldOrder.getBuyerId())) {
            return new ResponseEntity<>("Current user is not a buyer", HttpStatus.BAD_REQUEST);
        }

        if (oldOrder.getOrderStatus() != OrderStatus.NEW) {
            return new ResponseEntity<>("You can change address or payment only of the order which has status New", HttpStatus.BAD_REQUEST);
        }

        if (!orderService.isShippingMethodsValid(order, offer)) {
            return new ResponseEntity<>("Shipping method is not valid", HttpStatus.BAD_REQUEST);
        }

        if (!orderService.isPaymentMethodsValid(order, offer)) {
            return new ResponseEntity<>("Payment method is not valid", HttpStatus.BAD_REQUEST);
        }


        oldOrder.setOrderAddress(order.getOrderAddress());
        oldOrder.setPaymentMethod(order.getPaymentMethod());
        orderService.findAndUpdate(oldOrder);

        return ok;
    }


    /**
     * This method can only cancel order by buyer (before seller accept).
     *
     * @param order - updated order.
     * @return - return 200 status code if Ok, 400 - if status not NEW jr if user is not buyer, 401 - not authorized, 404 - not found order
     */
    @PreAuthorize("isAuthenticated()")
    @CrossOrigin
    @RequestMapping(value = "/order/update/3", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateOrder3(@RequestBody Order order) {

        String userId = SecurityOperations.getLoggedUserId();

        Order oldOrder = orderService.findById(order.getId());
        if (oldOrder == null) {
            return new ResponseEntity<>("Order was not found", HttpStatus.NOT_FOUND);
        }

        if (userId.equals(oldOrder.getBuyerId()) && oldOrder.getOrderStatus() == OrderStatus.NEW) {
            // cancel order and send notification to seller
            orderService.cancelOrderByBuyer(oldOrder);
        } else {
            return new ResponseEntity<>("Current user is not a buyer or previous Order Status is not a New", HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }


    /**
     * This method can only change Order Status to ACCEPT or ORDER_REJECTED_BY_SELLER (only by seller).
     *
     * @param order - updated order.
     * @return - return 200 status code if Ok, 400 - user is not seller, 401 - not authorized, 404 - not found order
     */
    @PreAuthorize("isAuthenticated()")
    @CrossOrigin
    @RequestMapping(value = "/order/update/4", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateOrder4(@Valid @RequestBody Order order) {

        Order oldOrder = orderService.findById(order.getId());
        if (oldOrder == null) {
            return new ResponseEntity<>("Order was not found", HttpStatus.NOT_FOUND);
        }

        String userId = SecurityOperations.getLoggedUserId();
        if (!userId.equals(oldOrder.getSellerId())) {
            return new ResponseEntity<>("Current user is not a seller", HttpStatus.BAD_REQUEST);
        }

        if (order.getOrderStatus() == OrderStatus.ACCEPT) {
            orderService.acceptOrderBySeller(oldOrder);
        }

        if (order.getOrderStatus() == OrderStatus.REJECTED_BY_SELLER) {
            orderService.rejectedOrderBySeller(oldOrder);
        }

        return ok;
    }


    /**
     * This method can only change order status to SENT and only by seller. Due to the TransportCompany type
     * you need or not put trackNumber.
     *
     * @param order - updated order.
     * @return - return 200 status code if Ok, 400 - order doesn't have track number, 401 - not authorized,
     * 404 - not found order, 405 - if TransportCompany was SELF_PICKED - you can't use this method,
     * 406 - if you are not seller
     */
    @PreAuthorize("isAuthenticated()")
    @CrossOrigin
    @RequestMapping(value = "/order/update/5", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateOrder5(@Valid @RequestBody Order order) {

        Order oldOrder = orderService.findById(order.getId());
        if (oldOrder == null) {
            return new ResponseEntity<>("Order was not found", HttpStatus.NOT_FOUND);
        }

        String userId = SecurityOperations.getLoggedUserId();

        if (!userId.equals(oldOrder.getSellerId())) {
            return new ResponseEntity<>("You are not an seller.", HttpStatus.FORBIDDEN);
        }

        if (oldOrder.getOrderAddress().getTransportCompany() != TransportCompany.SELF_PICKED && order.getTrackNumber() != null) {
            oldOrder.setTrackNumber(order.getTrackNumber());
            orderService.sendOrderBySeller(oldOrder);
        } else {
            return new ResponseEntity<>("In the previous order TransportCompany was Self_Picked and the truckNumber in the order is null", HttpStatus.BAD_REQUEST);
        }
        return ok;
    }


    /**
     * This method can only change order status to COMPLETED from the client (by seller or by buyer).
     *
     * @param order - updated order.
     * @return - return 200 status code if Ok, 400 - user neither seller nor buyer, 404 - not found order,
     * 406 - buyer can't mark this order like COMPLETED yet
     */
    @PreAuthorize("isAuthenticated()")
    @CrossOrigin
    @RequestMapping(value = "/order/update/6", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateOrder6(@Valid @RequestBody Order order) {

        Order oldOrder = orderService.findById(order.getId());
        if (oldOrder == null) {
            return new ResponseEntity<>("Order was not found", HttpStatus.NOT_FOUND);
        }

        String userId = SecurityOperations.getLoggedUserId();

        if (userId.equals(oldOrder.getSellerId())) {

            if (orderService.completeOrderBySeller(oldOrder)) {
                return ok;
            } else {
                return new ResponseEntity<>("Order can't be complete now. To early to make it complete.", HttpStatus.BAD_REQUEST);
            }

        } else {
            if (userId.equals(oldOrder.getBuyerId())) {

                if (orderService.completeOrderByBuyer(oldOrder)) {
                    return ok;
                } else {
                    return new ResponseEntity<>("Order can't be complete now. To early to make it complete.", HttpStatus.BAD_REQUEST);
                }

            } else {
                // you are neither buyer nor the seller
                return new ResponseEntity<>("You are neither buyer nor the seller.", HttpStatus.BAD_REQUEST);
            }
        }
    }


    /**
     * This method for add comment to the order.
     *
     * @param order - updated order. Can received only one comment from buyer or from seller to certain order.
     *              Comment length can be from 10 to 500 letters.
     * @return - return 200 status code if Ok, 400 - user neither seller nor buyer, 404 - not found order.
     */
    @PreAuthorize("isAuthenticated()")
    @CrossOrigin
    @RequestMapping(value = "/order/update/7", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateOrder7(@Valid @RequestBody Order order) {

        Order oldOrder = orderService.findById(order.getId());
        if (oldOrder == null) {
            return new ResponseEntity<>("Order was not found", HttpStatus.NOT_FOUND);
        }

        if (orderService.commentUpdateInOrder(oldOrder, order)) {
            return ok;
        }
        return new ResponseEntity<>("You are neither buyer nor the seller.", HttpStatus.BAD_REQUEST);
    }

    /**
     * Add and update seller note for specific order.
     *
     * @param orderId    - the seller ID.
     * @param sellerNote - the text of the seller note.
     * @return - status 200 (OK), 404 (Not Found) - if order was not found, 403 (Forbidden) - if user is not seller.
     */
    @CrossOrigin
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/order/update/note/{orderId}", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateSellerNote(@PathVariable String orderId, @RequestBody String sellerNote) {

        String userId = SecurityOperations.getLoggedUserId();

        Order order = orderService.findById(orderId);

        if (order == null) {
            return new ResponseEntity<>("Order was not found", HttpStatus.NOT_FOUND);
        }

        if (!userId.equals(order.getSellerId())) {
            new ResponseEntity<>("Current user is not seller in this order", HttpStatus.FORBIDDEN);
        }

        orderService.updateSellerNote(order, sellerNote);

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
