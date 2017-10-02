package ua.com.gup.repository.offer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import ua.com.gup.domain.offer.Offer;
import ua.com.gup.domain.enumeration.OfferStatus;

import java.util.Optional;

/**
 * Repository for the offer entity.
 */
public interface OfferRepository extends MongoRepository<Offer, String>, OfferRepositoryCustom {


    Optional<Offer> findOneBySeoUrl(String seoUrl);

    Page<Offer> findAllByStatusAndAuthorId(OfferStatus status, String authorId, Pageable pageable);

    Page<Offer> findAllByStatus(OfferStatus status, Pageable pageable);

    Offer findOfferByIdAndAuthorId(String offerId,String authorId);

}
