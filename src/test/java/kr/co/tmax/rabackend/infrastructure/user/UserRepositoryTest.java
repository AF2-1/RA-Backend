package kr.co.tmax.rabackend.infrastructure.user;

import kr.co.tmax.rabackend.domain.user.User;
import kr.co.tmax.rabackend.security.oauth2.AuthProvider;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Disabled
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    @Test
    void save() {
        User user = User.builder().
                id(1L)
                .name("hottj")
                .email("jhj13062004@naver.com")
                .emailVerified(true)
                .provider(AuthProvider.google)
                .build();
        userRepository.save(user);

        String email = "jhj13062004@naver.com";
        userRepository.findByEmail(email).equals(user.getEmail());
    }
}