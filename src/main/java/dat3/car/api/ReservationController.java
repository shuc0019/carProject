package dat3.car.api;

import dat3.car.dto.ReservationRequest;
import dat3.car.dto.ReservationResponse;
import dat3.car.service.ReservationService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {
  ReservationService service;

  public ReservationController(ReservationService service) {
    this.service = service;
  }

  @PostMapping
  ReservationResponse makeReservation(@RequestBody ReservationRequest res){
    ReservationResponse r = service.reserveCar(res);
    return r;
  }
}
