package kr.co.tmax.rabackend.domain.user;

import kr.co.tmax.rabackend.security.oauth2.AuthProvider;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "users")
@NoArgsConstructor
@ToString
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