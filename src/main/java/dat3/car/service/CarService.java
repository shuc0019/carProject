package dat3.car.service;

import dat3.car.dto.CarRequest;
import dat3.car.dto.CarResponse;
import dat3.car.entity.Car;
import dat3.car.repository.CarRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CarService {

  CarRepository carRepository;

  public CarService(CarRepository carRepository) {
    this.carRepository = carRepository;
  }


  public List<CarResponse> getCars(boolean includeAll) {
    List<Car> cars = carRepository.findAll();
    return cars.stream().map(car -> new CarResponse(car, includeAll)).collect(Collectors.toList());
  }

  public CarResponse findCarById(int id, boolean includeAll) {
    Car found = carRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Car not found"));
    return new CarResponse(found, includeAll);
  }

  public CarResponse addCar(CarRequest body) {

    Car newCar = Car.builder().brand(body.getBrand()).
            model(body.getModel()).
            pricePrDay(body.getPricePrDay()).
            bestDiscount(body.getBestDiscount()).build();
    newCar = carRepository.save(newCar);
    return new CarResponse(newCar, true);
  }

  public CarResponse editCar(CarRequest body, int id) {
    Car carToEdit = getCarIfExists(id);
    //ID can not be changed
    carToEdit.setBrand(body.getBrand());
    carToEdit.setModel(body.getModel());
    carToEdit.setPricePrDay(body.getPricePrDay());
    carToEdit.setBestDiscount(body.getBestDiscount());
    Car saved = carRepository.save(carToEdit);
    return new CarResponse(saved,true);
  }

  public void setPrice(int id, double newPrice) {
    Car carToEdit = getCarIfExists(id);
    carToEdit.setPricePrDay(newPrice);
    carRepository.save(carToEdit);
  }

  public void setDiscount(int id, int newDiscount) {
    Car carToEdit = getCarIfExists(id);
    carToEdit.setBestDiscount(newDiscount);
    carRepository.save(carToEdit);
  }

  public ResponseEntity<Boolean> deleteCar(int id) {
    if(!carRepository.existsById(id)){
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Car with this ID does not exist");
    }
    try {
      carRepository.deleteById(id);
      return ResponseEntity.ok(true);
    } catch (Exception e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Could not delete car. Most likely because it part of a rental/reservation");
    }
  }

  private Car getCarIfExists(int id){
    return carRepository.findById(id).orElseThrow(() ->
            new ResponseStatusException(HttpStatus.BAD_REQUEST, "Car with this ID does not exist"));
  }

}
