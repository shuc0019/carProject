package dat3.car.repository;

import dat3.car.entity.Car;
import dat3.car.entity.Member;
import dat3.car.entity.Reservation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CarRepositoryTest {

    @Autowired
    CarRepository carRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    ReservationRepository reservationRepository;
    boolean dataInitialized = false;
    int car1Id, car2Id;
    @BeforeEach
    void setUp() {
        if(!dataInitialized){
            Car car1 = carRepository.save(new Car("Volvo","V70",1999,20));
            Car car2 = carRepository.save(new Car("Volvo","V70",1999,20));
            Car car3 = carRepository.save(new Car("Volvo","V70",1999,20));
            car1Id = car1.getId();
            Car car5 = carRepository.save(new Car("XXX","YYY",100,20));
            Member m1 = new Member("Jan","test12","a@b.dk","Jan","Jensen","Lyngbyvej 1","Lyngby","2800");

            Reservation reservation = new Reservation(LocalDate.parse("2024-02-02"), car5, m1);
            car2Id = car2.getId();
            System.out.println(car5.getReservations()+ " hej");
            System.out.println(car2.getReservations()+ " hej");

        }
    }

    @Test
    public void testById() {
        Car car1 = carRepository.findById(car1Id).get();
        assertEquals("Volvo", car1.getBrand());
    }
    @Test
    public void testCount() {
        assertEquals(2, carRepository.count());
    }

    //Check if this brand and model exist
@Test
    public void checkByBrandAndModel() {
    assertEquals(1, carRepository.getByBrandAndModel("Audi", "A4").size());
}

@Test //test to find a car by brand and model
    public void testGetByBrandAndModel() {




        List<Car> cars = carRepository.getByBrandAndModel("Volvo", "V70");

        for (Car car : cars) {
            System.out.println("dsghuodshhgdadg  " + car.getBrand() + " " + car.getModel());
        }
        assertEquals(1, carRepository.getByBrandAndModel("Volvo", "V70").size());


    System.out.println("hej");
    }
    @Test //test to find available cars
    public void testFindAvailableCars() {

        List<Car> carsAll = carRepository.findAll();

        List<Car> cars = carRepository.findAllByReservationsIsNull(carRepository);
        //List<Car> cars = carRepository.findAll();

        for (Car car : cars) {
            //if (car.getReservations()==null){

            System.out.println("Tilgængelig biler " + car.getBrand()+""+ car.getModel());
           // }

        }




    }
    /*
    @Test //test to find the best discounts
    public void testFindTopByOrderByDiscountDesc() {
        List<Car> cars = carRepository.findTopByBestDiscountDesc();
        for (Car car : cars) {
            System.out.println("Tilgængelig biler " + car.getBrand() + "" + car.getModel() + "" + car.getBestDiscount());
        }
    }
        // test to find the average price per day of all cars in the system
        @Test
        public void testFindAveragePricePerDay() {
            Double averagePrice = carRepository.findAveragePricePerDay();
            System.out.println(averagePrice);
        }
        //test to find all members who have made a reservation
        @Test
        public void testFindMembersWithReservations() {
            List<Member> membersWithReservations = memberRepository.findDistinctMemberByReservation();

            // Assert that the list is not null and not empty
            assertNotNull(membersWithReservations);
            assertFalse(membersWithReservations.isEmpty());

            System.out.println(membersWithReservations);
        }
        //test to find all reservations made by a certain member
        @Test
        public void testFindReservationsByMember() {
        List<Reservation>reservations = reservationRepository.findByMember(1);

            // Assert that the list is not null and not empty
            assertNotNull(reservations);
            assertFalse(reservations.isEmpty());

            System.out.println(reservations);
    }

     */
    }