package ua.com.gup.rental.algorithm;

import ua.com.gup.rental.algorithm.type.OfferType;
import ua.com.gup.rental.resource.Resource;
import ua.com.gup.rental.resource.ResourceBundleType;
import ua.com.gup.rental.dao.OfferStoreDAO;

public class GetAvailableOffers implements Command {
    private OfferStoreDAO store;

    public GetAvailableOffers(OfferStoreDAO store) {
        this.store = store;
    }

    @Override
    public void execute() {
        System.out.println(Resource.getInstance(ResourceBundleType.LABELS.getBundle()).getString("available.offers"));
        for (OfferType offerType : store.getAvailableOffers())
            System.out.println(offerType.toString());
    }
}