package ua.com.gup.rent.model.mongo.rent.calendar;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import ua.com.gup.rent.model.rent.calendar.RentOfferCalendarDay;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


@Data
//@JsonInclude(JsonInclude.Include.NON_NULL)
//@JsonIgnoreProperties(ignoreUnknown = true)
//@Document(collection = ObjectType.RENT_OFFER_CALENDAR)
public class RentOfferCalendar {//extends RentOfferCalendarInterval {

    //    @Id
//    private String Id;
//    @Indexed
//    private String offerId;
//    @JsonFormat(pattern = "dd-MM-yyyy")

    private String rentStartDate;
//    @JsonFormat(pattern = "dd-MM-yyyy")
    private String rentEndDate;
    private RentOfferCalendarDay[] days;

    public RentOfferCalendar() {
    }

    public RentOfferCalendar(LocalDate rentStartDate, LocalDate rentEndDate) {
        DateTimeFormatter pattern = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        this.rentStartDate = rentStartDate.format(pattern);
        this.rentEndDate = rentEndDate.format(pattern);
    }

    //    public RentOfferCalendar() {
//        super();
//    }

//    public RentOfferCalendar(RentOfferCalendarInterval calendarInterval) {
//        super(calendarInterval);
//    }


}
