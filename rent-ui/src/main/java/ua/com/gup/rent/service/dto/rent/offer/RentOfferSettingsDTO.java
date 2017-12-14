package ua.com.gup.rent.service.dto.rent.offer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

/**
 * @author victor dvorak
 **/
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class RentOfferSettingsDTO {
    @Min(1)
    private Integer minRentDays;
    @Max(30)
    private Integer maxRentDays;
    @Size(min = 1 ,max = 5)
    private String startDay;
    @Size(min = 1 ,max = 5)
    private String endDay;
}
