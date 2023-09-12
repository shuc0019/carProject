package dat3.car.service;

import dat3.car.dto.MemberRequest;
import dat3.car.dto.MemberResponse;
import dat3.car.entity.Member;
import dat3.car.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class MemberServiceH2Test {

  @Autowired
  MemberRepository memberRepository;

  MemberService memberService;

  Member m1, m2;

  @BeforeEach
  void setUp() {
    m1 = memberRepository.save(new Member("user1", "pw1", "email1", "fn1", "ln1",  "street1", "city1", "zip1"));
    m2 = memberRepository.save(new Member("user2", "pw2", "email2", "fn2", "ln2", "street2", "city2", "zip2"));
    memberService = new MemberService(memberRepository);
  }

  @Test
  void testGetMembersAllDetails() {
    List<MemberResponse> responses = memberService.getMembers(true);
    assertEquals(2, responses.size(), "Expected 2 members");
    LocalDateTime created = responses.get(0).getCreated();
    assertNotNull(created, "Dates must be set since TRUE was passed to getMembers");
  }

  @Test
  void testGetMembersNoDetails() {
    List<MemberResponse> responses = memberService.getMembers(false);
    assertEquals(2, responses.size(), "Expected 2 members");
    LocalDateTime created = responses.get(0).getCreated();
    assertNull(created, "Dates must NOT be set since FALSE was passed to getMembers");
  }

  @Test
  void testFindByIdFound() {
    MemberResponse response = memberService.findById("user1");
    assertEquals("user1", response.getUsername());
  }

  @Test
  void testFindByIdNotFound() {
    ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> memberService.findById("I dont exist"));
    assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
  }

  @Test
    /* Remember MemberRequest comes from the API layer, and MemberResponse is returned to the API layer
     * Internally addMember save a Member entity to the database*/
  void testAddMember_UserDoesNotExist() {
    //Add @AllArgsConstructor to MemberRequest and @Builder to MemberRequest for this to work
    //Make sure to include at least all fields which are not nullable
    MemberRequest request = MemberRequest.builder().
            username("user3").
            password("pw3").
            email("aa@bb.dk").
            firstName("fn3").
            lastName("ln3").
            build();
    MemberResponse res = memberService.addMember(request);
    assertEquals("user3", res.getUsername());
    //Here we use the repository to verify that the member was actually saved
    assertTrue(memberRepository.existsById("user3"));
  }

  @Test
  void testAddMember_UserDoesExist() {
    MemberRequest request = new MemberRequest();
    request.setUsername("user1");
    ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> memberService.addMember(request));
    assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
  }

  @Test
  void testEditMemberWithExistingUsername() {
    //Create a MemberRequest from an existing member we can edit
    MemberRequest request = new MemberRequest(m1);
    request.setFirstName("New First Name");
    request.setLastName("New Last Name");

    memberService.editMember(request, "user1");

    memberRepository.flush();
    MemberResponse response = memberService.findById("user1");

    assertEquals("user1", response.getUsername());
    assertEquals("New First Name", response.getFirstName());
    assertEquals("New Last Name", response.getLastName());
    assertEquals("email1", response.getEmail());
    assertEquals("street1", response.getStreet());
    assertEquals("city1", response.getCity());
    assertEquals("zip1", response.getZip());
  }

  @Test
  void testEditMemberNON_ExistingUsername() {
    MemberRequest memberRequest = new MemberRequest();
    ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> memberService.editMember(memberRequest, "I dont exist"));
    assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
  }

  @Test
  void testEditMemberChangePrimaryKey() {
    //Create a MemberRequest from an existing member we can edit
    MemberRequest request = new MemberRequest(m1);
    request.setUsername("New Username");
    ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> memberService.editMember(request, "user1"));
    assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
    assertEquals("Cannot change username", ex.getReason());
  }

  @Test
  void testSetRankingForUser() {
    memberService.setRankingForUser("user1", 5);
    MemberResponse response = memberService.findById("user1");
    assertEquals(5, response.getRanking());
  }

  @Test
  void testSetRankingForNoExistingUser() {
    ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> memberService.setRankingForUser("I dont exist", 5));
    assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
  }
  @Test
  void testDeleteMemberByUsername() {
    memberService.deleteMemberByUsername("user1");
    assertFalse(memberRepository.existsById("user1"));
  }

  @Test
  void testDeleteMember_ThatDontExist() {
    ResponseStatusException ex = assertThrows(ResponseStatusException.class, () -> memberService.deleteMemberByUsername("I dont exist"));
    assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
  }
}