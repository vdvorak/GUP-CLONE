package ua.com.itproekt.gup.service.offers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.itproekt.gup.dao.filestorage.StorageRepository;
import ua.com.itproekt.gup.dao.offers.OfferRepository;
import ua.com.itproekt.gup.model.offer.ModerationStatus;
import ua.com.itproekt.gup.model.offer.Offer;
import ua.com.itproekt.gup.model.offer.RentedOfferPeriodInfo;
import ua.com.itproekt.gup.model.offer.Reservation;
import ua.com.itproekt.gup.model.offer.filter.OfferFilterOptions;
import ua.com.itproekt.gup.util.EntityPage;
import ua.com.itproekt.gup.util.ServiceNames;

import java.util.Map;

@Service
public class OffersServiceImpl implements OffersService {

    @Autowired
    private OfferRepository offerRepository;

    @Autowired
    private StorageRepository storageRepository;

    @Override
    public void create(Offer offer) {
        Offer newOffer = new Offer()
                .setAuthorId(offer.getAuthorId())
                .setUserInfo(offer.getUserInfo())
                .setCreatedDateEqualsToCurrentDate()
                .setModerationStatus(ModerationStatus.NO)
                .setCategories(offer.getCategories())
                .setProperties(offer.getProperties())
                .setImagesIds(offer.getImagesIds())
                .setVideoUrl(offer.getVideoUrl())
                .setTitle(offer.getTitle())
                .setDescription(offer.getDescription())
                .setPrice(offer.getPrice())
                .setPriceCanBeNegotiated(offer.getPriceCanBeNegotiated())
                .setUrgent(offer.getUrgent())
                .setUsed(offer.getUsed())
                .setActive(offer.getActive())
                .setAddress(offer.getAddress())
                .setCurrency(offer.getCurrency())
                .setCanBeReserved(offer.getCanBeReserved())
                .setCanBeRented(offer.getCanBeRented());

        offerRepository.create(newOffer);

        offer.setId(newOffer.getId());
    }

    @Override
    public Offer findById(String offerId) {
        return offerRepository.findById(offerId);
    }

    @Override
    public Offer findOfferAndIncViews(String offerId) {
        offerRepository.incViewsAtOne(offerId);
        return offerRepository.findById(offerId);
    }

    @Override
    public boolean offerExists(String id) {
        return offerRepository.offerExists(id);
    }

    @Override
    public void delete(String id) {
        Map<String, String> imagesIds = findById(id).getImagesIds();
        if (imagesIds != null) {
            storageRepository.delete(ServiceNames.OFFERS.toString(), imagesIds.keySet());
        }

        offerRepository.delete(id);
    }

    @Override
    public EntityPage<Offer> findOffersWihOptions(OfferFilterOptions offerFilterOptions) {
        return offerRepository.findOffersWihOptions(offerFilterOptions);
    }

    @Override
    public Offer edit(Offer oldOffer) {
        Offer newOffer = new Offer()
                .setId(oldOffer.getId())
                .setModerationStatus(oldOffer.getModerationStatus())
                .setUserInfo(oldOffer.getUserInfo())
                .setCategories(oldOffer.getCategories())
                .setProperties(oldOffer.getProperties())
                .setImagesIds(oldOffer.getImagesIds())
                .setVideoUrl(oldOffer.getVideoUrl())
                .setTitle(oldOffer.getTitle())
                .setDescription(oldOffer.getDescription())
                .setPrice(oldOffer.getPrice())
                .setPriceCanBeNegotiated(oldOffer.getPriceCanBeNegotiated())
                .setUrgent(oldOffer.getUrgent())
                .setUsed(oldOffer.getUsed())
                .setActive(oldOffer.getActive())
                .setCanBeReserved(oldOffer.getCanBeReserved())
                .setAddress(oldOffer.getAddress())
                .setCurrency(oldOffer.getCurrency());

        return offerRepository.findAndUpdate(newOffer);
    }

    @Override
    public Offer reserveOffer(String offerId, Reservation reservation) {
        Reservation newReservation = new Reservation()
                .setProfileId(reservation.getProfileId())
                .setUserContactInfo(reservation.getUserContactInfo())
                .setCreatedDateEqualsToCurrentDate();

        Offer newOffer = new Offer()
            .setId(offerId)
            .setReservation(newReservation);

        return offerRepository.findAndUpdate(newOffer);
    }

    @Override
    public void deleteReservation(String offerId) {
        offerRepository.deleteReservation(offerId);
    }

    @Override
    public void rentOffer(String offerId, RentedOfferPeriodInfo rentedOfferPeriodInfo) {
        offerRepository.rentOffer(offerId, rentedOfferPeriodInfo);
    }

    @Override
    public void deleteRent(String offerId, String rentId) {
        offerRepository.deleteRent(offerId, rentId);
    }

    @Override
    public void setActive(String offerId, boolean isActive) {
        Offer offer = new Offer()
                .setId(offerId)
                .setActive(isActive);
        offerRepository.findAndUpdate(offer);
    }
}
