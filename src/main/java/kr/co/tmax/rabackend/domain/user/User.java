package kr.co.tmax.rabackend.domain.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kr.co.tmax.rabackend.security.oauth2.AuthProvider;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Document(collection = "users")
@NoArgsConstructor
public class User {

    @Id
    private String id;

    private String name;

    private String email;

    private String imageUrl;

    private Boolean emailVerified = false;

    private String password;

    private AuthProvider provider;


    private String providerId;

    @Builder
    public User(String id, String name, AuthProvider provider, String email, Boolean emailVerified) {
        this.id = id;
        this.name = name;
        this.provider = provider;
        this.email = email;
        this.emailVerified = emailVerified;
    }
}