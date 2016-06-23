package ua.com.itproekt.gup.controller.offers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.com.itproekt.gup.service.reservationSchedule.ReservationScheduleService;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Controller
public class ReservationScheduleTestController {


    @Autowired
    ReservationScheduleService reservationScheduleService;


    @RequestMapping("/addTestSchedulePoints")
    public void addTestSchedulePoints() {

//        for (int i = 0; i < 40; i++) {
//            reservationScheduleService.add("offer_" + i, LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli() - 86400000);
//        }
//
//        for (int i = 41; i < 100; i++) {
//            reservationScheduleService.add("offer_" + i, LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli() + 86400000);
//        }
    }

    @RequestMapping("/readTestSchedulePoints")
    public void readTestSchedulePoints() {

        reservationScheduleService.removeOverdue();

    }
}
