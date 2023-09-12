package dat3.car.repository;

import dat3.car.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface CarRepository extends JpaRepository<Car,Integer> {
    //Find all cars with a certain brand and model
    List<Car> getByBrandAndModel(String brand, String model);




    //Find all cars that have not been reserved
  List<Car> findAllByReservationsIsNull(boolean carRepository);

    //Find all cars with the best discount
   // List<Car> findTopByBestDiscountDesc();

    //Find the average price per day of all cars in the system
//    Double findAveragePricePerDay();





}
