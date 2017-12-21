package ua.com.gup.rent.service.rent;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ua.com.gup.common.model.enumeration.CommonStatus;
import ua.com.gup.rent.filter.RentOfferFilter;
import ua.com.gup.rent.service.abstracted.generic.RentOfferGenericService;
import ua.com.gup.rent.service.dto.rent.RentOfferModerationReportDTO;
import ua.com.gup.rent.service.dto.rent.offer.RentOfferCategoryCountDTO;
import ua.com.gup.rent.service.dto.rent.offer.RentOfferCreateDTO;
import ua.com.gup.rent.service.dto.rent.offer.RentOfferDTO;
import ua.com.gup.rent.service.dto.rent.offer.RentOfferUpdateDTO;
import ua.com.gup.rent.service.dto.rent.offer.statistic.RentOfferStatisticByDateDTO;
import ua.com.gup.rent.service.dto.rent.offer.view.RentOfferViewCoordinatesDTO;
import ua.com.gup.rent.service.dto.rent.offer.view.RentOfferViewDetailsDTO;
import ua.com.gup.rent.service.dto.rent.offer.view.RentOfferViewShortDTO;
import ua.com.gup.rent.service.dto.rent.offer.view.RentOfferViewShortWithModerationReportDTO;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface RentOfferService extends RentOfferGenericService<RentOfferDTO, String> {

    Optional<RentOfferViewDetailsDTO> findOne(String id);

    RentOfferViewDetailsDTO save(RentOfferModerationReportDTO offerModerationReportDTO);

    RentOfferViewDetailsDTO save(RentOfferCreateDTO rentOfferCreateDTO);

    RentOfferViewDetailsDTO update(String offerId, RentOfferUpdateDTO offerUpdateDTO);

    Page<RentOfferViewShortDTO> findAll(RentOfferFilter offerFilter, Pageable pageable);

    List<RentOfferViewCoordinatesDTO> findCoordinatesByFilter(RentOfferFilter offerFilter, Pageable pageable);

    Page<RentOfferViewShortWithModerationReportDTO> findAllByStatusAndUserId(CommonStatus status, String authorId, Pageable pageable);

    Page<RentOfferViewShortWithModerationReportDTO> findAllByStatusAndUserPublicId(CommonStatus status, String userPublicId, Pageable pageable);

    Page<RentOfferViewShortWithModerationReportDTO> findAllByStatus(CommonStatus status, Pageable pageable);

    Optional<RentOfferViewDetailsDTO> findOneBySeoUrl(String seoUrl);

    Page<RentOfferViewShortDTO> findRelevantBySeoUrl(String seoUrl, Pageable pageable);

    void incrementPhoneViews(String id);

    void delete(String id);

    boolean exists(String id);

    @Deprecated
    boolean hasPermissionForUpdate(String offerId, String authrorId);

    Optional<RentOfferViewDetailsDTO> updateStatus(String id, CommonStatus status);

    Boolean isCanUpdateStatus(String id, CommonStatus status);

    List<RentOfferCategoryCountDTO> searchCategoriesByString(String string, int page, int size);

    Optional<List<RentOfferStatisticByDateDTO>> findOfferStatisticBySeoUrlAndDateRange(String seoUrl, LocalDate dateStart, LocalDate dateEnd);

    Optional<RentOfferViewDetailsDTO> findOfferByIdAndAuthorId(String offerId, String authorId);

    Collection<String> getOfferContactInfoPhoneNumbersById(String offerId);
}
