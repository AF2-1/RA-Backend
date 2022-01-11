package kr.co.tmax.rabackend.domain.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import kr.co.tmax.rabackend.security.oauth2.AuthProvider;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document
public class User {

    @Id
    private String id;
    private String name;
    private String email;
    private String imageUrl;
    private Role role;
    @JsonIgnore
    private String password;
    private AuthProvider provider;
    private Boolean emailVerified = false;
    private String providerId;

    @Builder
    public User(String name, String email, String imageUrl, Role role, String password, AuthProvider provider, Boolean emailVerified, String providerId) {
        this.name = name;
        this.email = email;
        this.imageUrl = imageUrl;
        this.role = role;
        this.password = password;
        this.provider = provider;
        this.emailVerified = emailVerified;
        this.providerId = providerId;
    }
}