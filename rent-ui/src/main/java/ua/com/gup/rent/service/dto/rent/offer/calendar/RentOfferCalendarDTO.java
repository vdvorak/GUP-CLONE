package ua.com.gup.rent.service.dto.rent.offer.calendar;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import ua.com.gup.rent.model.rent.calendar.RentOfferCalendarDay;

import java.io.Serializable;

/**
 * @author viktor.dvorak
 **/
@ApiModel(description = "DTO for Rent Offer Price")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class RentOfferCalendarDTO implements Serializable {

    @ApiModelProperty(position = 10, example = "[{'type':3,'price':10.1,{'type':1}}]")
    private RentOfferCalendarDay[] days;

    @ApiModelProperty(position = 20, example = "2017")
    private int year;

}