package ua.com.gup.server.api.rest.offers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import ua.com.gup.bank_api.BankSession;
import ua.com.gup.domain.Offer;
import ua.com.gup.model.offer.Reservation;
import ua.com.gup.model.profiles.Profile;
import ua.com.gup.service.offers.OffersService;
import ua.com.gup.service.profile.ProfilesService;
import ua.com.gup.service.reservationSchedule.ReservationScheduleService;
import ua.com.gup.util.OfferUserContactInfo;
import ua.com.gup.util.SecurityOperations;


/**
 * REST methods for offer reservation.
 *
 * @author Kobylyatskyy Alexander
 */
@RestController
@RequestMapping("/api/rest/offersService")
public class OfferReservationRestController {

    @Autowired
    private OffersService offersService;

    @Autowired
    private ProfilesService profilesService;

    @Autowired
    private BankSession bankSession;

    @Autowired
    private ReservationScheduleService reservationScheduleService;


    /**
     * With this method we can reserve offer for specific period.
     *
     * @param offerId - the offer ID.
     * @param period  - the period in days.
     * @return - the status.
     */
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/offer/id/{offerId}/{period}/reserve", method = RequestMethod.POST)
    public ResponseEntity<Void> reserveOffer(@PathVariable String offerId, Integer period) {
        if (!offersService.offerExists(offerId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Offer offer = offersService.findById(offerId);
        //todo vdvorak
        /*if (!offer.getCanBeReserved() || (offer.getReservation() != null)) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        if (period < 1 || period > offer.getMaximumReservedPeriod()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }*/

        String userId = SecurityOperations.getLoggedUserId();

        Profile profile = profilesService.findById(userId);

        Reservation reservation = new Reservation()
                .setProfileId(userId)
                .setPeriod(period)
                .setUserContactInfo(new OfferUserContactInfo()
                        .setContactName(profile.getUsername())
                        .setEmail(profile.getEmail())
                        .setPhoneNumbers(profile.getContact().getContactPhones()));


        if (bankSession.getUserBalance(userId) > 5 * period) {
            offersService.reserveOffer(offerId, reservation);
            bankSession.investInOrganization(5555, userId, (long) (5 * period), 30, "success");
            reservationScheduleService.add(offerId);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }


        return new ResponseEntity<>(HttpStatus.OK);
    }


    /**
     * Delete reservation.
     *
     * @param offerId - the offer ID.
     * @return - the status.
     */
    @PreAuthorize("isAuthenticated()")
    @RequestMapping(value = "/offer/id/{offerId}/deleteReservation", method = RequestMethod.POST)
    public ResponseEntity<Void> deleteReservation(@PathVariable String offerId) {
        if (!offersService.offerExists(offerId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        String authorId = offersService.findById(offerId).getAuthorId();
        String userId = SecurityOperations.getLoggedUserId();
        if (!userId.equals(authorId)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        offersService.deleteReservation(offerId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}