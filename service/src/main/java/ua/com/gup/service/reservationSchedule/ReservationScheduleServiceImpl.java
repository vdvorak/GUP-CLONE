package ua.com.gup.service.reservationSchedule;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.com.gup.model.reservationSchedule.ReservationSchedule;
import ua.com.gup.repository.dao.reservationSchedule.ReservationScheduleRepository;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class ReservationScheduleServiceImpl implements ReservationScheduleService {

    @Autowired
    private ReservationScheduleRepository reservationScheduleRepository;

    private static final String RES_KEY = "reservationSchedule";


    @Override
    public ReservationSchedule findOne() {
        return reservationScheduleRepository.findById(RES_KEY);
    }


    @Override
    public void add(String id) {
        Long time = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli() + 86400000;
        ReservationSchedule reservationSchedule = findOne();
        reservationSchedule.getPoints().put(id, time);
        reservationScheduleRepository.update(reservationSchedule);
    }

    @Override
    public void removeOverdue() {

        System.err.println("Задание выполнилось в: " + LocalDateTime.now().toInstant(ZoneOffset.UTC));


        ReservationSchedule reservationSchedule = findOne();

        LinkedHashMap<String, Long> reservationSchedulePoints = reservationSchedule.getPoints();

        Long timeNow = LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli();

        Iterator<Map.Entry<String, Long>> iterator = reservationSchedulePoints.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<String, Long> entry = iterator.next();

            if (timeNow > entry.getValue()) {
                iterator.remove();
            } else {
                break;
            }
        }

        reservationScheduleRepository.update(reservationSchedule);
    }
}