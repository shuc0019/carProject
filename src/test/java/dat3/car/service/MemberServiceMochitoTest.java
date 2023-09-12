package dat3.car.service;

import dat3.car.dto.MemberRequest;
import dat3.car.dto.MemberResponse;
import dat3.car.entity.Member;
import dat3.car.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MemberServiceMockitoTest {

  //  @InjectMocks
  private MemberService memberService;

  @Mock
  private MemberRepository memberRepository;

  @BeforeEach
  void setUp() {
    memberService = new MemberService(memberRepository);
  }

  private Member makeMember(String username, String password, String firstName, String lastName, String email, String street, String city, String zip) {
    Member member = new Member(username, password, firstName, lastName, email, street, city, zip);
    member.setCreated(LocalDateTime.now());
    member.setEdited(LocalDateTime.now());
    return member;
  }

  private Member makeMember2() {
    Member m2 = new Member("user2", "pw2", "fn2", "ln2", "email2", "street2", "city2", "zip2");
    m2.setCreated(LocalDateTime.now());
    m2.setEdited(LocalDateTime.now());
    return m2;
  }

  @Test
  public void testGetMembers() {
    // Define a mock behavior
    Member m1 = makeMember("user1", "pw1", "fn1", "ln1", "email1", "street1", "city1", "zip1");
    Member m2 = makeMember("user2", "pw2", "fn2", "ln2", "email2", "street2", "city2", "zip2");
    when(memberRepository.findAll()).thenReturn(List.of(m1, m2));
    List<MemberResponse> responses = memberService.getMembers(true);
    // Assertions
    assertEquals(2, responses.size(), "Expected 2 members");
    assertNotNull(responses.get(0).getCreated(), "Dates must be set since true was passed to getMembers");
  }

  @Test
  public void testFindById() {
    when(memberRepository.findById("user1")).thenReturn(Optional.of(makeMember("user1", "pw1", "fn1", "ln1", "email1", "street1", "city1", "zip1")));
    MemberResponse response = memberService.findById("user1");
    // Assertions
    assertEquals("user1", response.getUsername());
    assertNotNull(response.getRanking(), "Expected ranking to be set, since true was used inside findById");
  }

  @Test
  public void testAddMember_UserExists() {
    when(memberRepository.existsById("user1")).thenReturn(true);
    assertThrows(ResponseStatusException.class, () -> {
      Member existingMember = makeMember("user1", "pw1", "fn1", "ln1", "email1", "street1", "city1", "zip1");
      //Make a MemberRequest with a username that already exists
      MemberRequest mr = new MemberRequest(existingMember);
      memberService.addMember(mr);
    });
  }

  @Test
  public void testAddMember_Success() {
    Member newMember = makeMember("userNew", "pwn", "fnn", "lnn", "emailn", "streetn", "cityn", "zipn");
    when(memberRepository.existsById("userNew")).thenReturn(false);
    when(memberRepository.save(any(Member.class))).thenReturn(newMember);

    //Make a MemberRequest for a new user (using the newMember object for convenience)
    MemberRequest mr = new MemberRequest(newMember);
    MemberResponse response = memberService.addMember(mr);
    // Assertions
    assertEquals("userNew", response.getUsername());
  }


  @Test
  void editMember() {
    //TODO
  }

  @Test
  public void testSetRankingForUser_MemberFound() {
    Member m1 = new Member();
    m1.setUsername("user1");
    when(memberRepository.findById("user1")).thenReturn(Optional.of(m1));
    int testRanking = 5;

    // Mock the behavior of memberRepository to save the testMember.
    when(memberRepository.save(m1)).thenReturn(m1);
    memberService.setRankingForUser("user1", testRanking);
    assertEquals(testRanking, m1.getRanking());

    // Verify that memberRepository's save method was called with the testMember.
    verify(memberRepository).save(m1);
  }

  @Test
  public void testSetRankingForUser_MemberNotFound() {
    String testUsername = "testUser";
    // Mock the behavior of memberRepository to return an empty Optional when findById is called with testUsername.
    when(memberRepository.findById(testUsername)).thenReturn(Optional.empty());
    // Assert that the method throws a ResponseStatusException with a BAD_REQUEST status.
    assertThrows(ResponseStatusException.class, () -> memberService.setRankingForUser(testUsername, 5));
  }


  @Test
  public void testDeleteMemberByUsername() {
    String testUsername = "testUser";
    Member testMember = new Member();
    testMember.setUsername(testUsername);

    // Mock the behavior of memberRepository to return the testMember when findById is called with testUsername.
    when(memberRepository.findById(testUsername)).thenReturn(Optional.of(testMember));

    // Call the method under test.
    memberService.deleteMemberByUsername(testUsername);

    // Verify that memberRepository's delete method was called with the testMember.
    verify(memberRepository).delete(testMember);
  }

  @Test
  public void testDeleteMemberByUsername_MemberNotFound() {
    String testUsername = "testUser";

    // Mock the behavior of memberRepository to return an empty Optional when findById is called with testUsername.
    when(memberRepository.findById(testUsername)).thenReturn(Optional.empty());

    // Assert that the method throws a ResponseStatusException with a BAD_REQUEST status.
    assertThrows(ResponseStatusException.class, () -> memberService.deleteMemberByUsername(testUsername));
  }
}