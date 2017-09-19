package ua.com.gup.repository.dao.reservationSchedule;

import ua.com.gup.model.reservationSchedule.ReservationSchedule;

public interface ReservationScheduleRepository {

    ReservationSchedule findById(String id);

    ReservationSchedule update(ReservationSchedule reservationSchedule);
}
