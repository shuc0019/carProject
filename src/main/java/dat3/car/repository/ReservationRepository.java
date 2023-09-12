package dat3.car.repository;

import dat3.car.entity.Car;
import dat3.car.entity.Member;
import dat3.car.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation,Integer> {
    boolean existsByCar_IdAndRentalDate(int carId, LocalDate date);
    //Find all reservations made by a certain member
  //  List<Reservation> findByMember(int id);

   // List<Reservation> findReservationByCar();
}
