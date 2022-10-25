/*
 * package com.taskmate.utils.jwt;
 * 
 * import static org.junit.jupiter.api.Assertions.assertTrue;
 * 
 * import org.junit.jupiter.api.Test; import org.junit.jupiter.api.TestInstance;
 * import org.junit.jupiter.api.TestInstance.Lifecycle; import
 * org.junit.jupiter.api.extension.ExtendWith; import org.mockito.InjectMocks;
 * import org.mockito.junit.jupiter.MockitoExtension; import
 * org.mockito.junit.jupiter.MockitoSettings; import
 * org.mockito.quality.Strictness;
 * 
 * import com.taskmate.domain.Company; import
 * com.taskmate.utils.EnumUtils.UserType;
 * 
 * @TestInstance(Lifecycle.PER_CLASS)
 * 
 * @ExtendWith(MockitoExtension.class)
 * 
 * @MockitoSettings(strictness = Strictness.LENIENT) public class
 * JwtProviderTest {
 * 
 * 
 * @InjectMocks JwtProvider jwtProvider = new JwtProvider();
 * 
 * @Test void generateTokenTest() {
 * System.out.println("Printing jwt token ::\n"+
 * jwtProvider.generateToken(getUser())); assertTrue(true); }
 * 
 * private Company getUser() { Company user = Company.builder()
 * .email("test@taskmate.com") .userType(UserType.USER) .build(); return user; }
 * 
 * }
 */