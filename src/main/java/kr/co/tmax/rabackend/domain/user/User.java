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
@Document(collection = "users")
public class User {

    @Id
    private String id;
    private String name;
    private String email;
    private String imageUrl;
    private Role role;
    private AuthProvider provider;
    private Boolean emailVerified;
    private String providerId;

    @Builder
    public User(String id, String name, String email, String imageUrl, Role role, AuthProvider provider, Boolean emailVerified, String providerId) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.imageUrl = imageUrl;
        this.role = role;
        this.provider = provider;
        this.emailVerified = emailVerified;
        this.providerId = providerId;
    }
}