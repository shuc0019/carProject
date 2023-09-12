package dat3.car.repository;

import dat3.car.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member,String> {
//Find all members who have made a reservation
//    List<Member> findDistinctMemberByReservation();
}
