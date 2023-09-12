package dat3.car.dto;

import dat3.car.entity.Car;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarRequest {
  String brand;
  String model;
  double pricePrDay;
  int bestDiscount;

  public static Car carFromCarRequest(CarRequest cr){
    return Car.builder().brand(cr.brand).model(cr.model).pricePrDay(cr.pricePrDay).bestDiscount(cr.bestDiscount).build();
  }
}