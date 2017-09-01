package ua.com.gup.service.dto.offer.view;

import io.swagger.annotations.ApiModelProperty;
import ua.com.gup.domain.offer.OfferStatistic;
import ua.com.gup.service.dto.offer.OfferAddressShortDTO;
import ua.com.gup.service.dto.offer.OfferLandsDTO;

import java.util.List;

public class OfferViewShortDTO extends OfferViewBaseDTO {

    @ApiModelProperty(position = 60)
    private OfferAddressShortDTO address;

    @ApiModelProperty(position = 140)
    private OfferStatistic statistic;

    @ApiModelProperty(position = 150)
    private List<OfferLandsDTO> lands;

    public OfferAddressShortDTO getAddress() {
        return address;
    }

    public void setAddress(OfferAddressShortDTO address) {
        this.address = address;
    }

    public OfferStatistic getStatistic() {
        return statistic;
    }

    public void setStatistic(OfferStatistic statistic) {
        this.statistic = statistic;
    }

    public List<OfferLandsDTO> getLands() {
        return lands;
    }

    public void setLands(List<OfferLandsDTO> lands) {
        this.lands = lands;
    }
}
