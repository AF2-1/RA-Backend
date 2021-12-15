package kr.co.tmax.rabackend.security.oauth2.user;

import kr.co.tmax.rabackend.exception.OAuth2AuthenticationProcessingException;
import kr.co.tmax.rabackend.security.oauth2.AuthProvider;

import java.util.Map;

public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
        if (registrationId.equalsIgnoreCase(AuthProvider.google.toString()))
            return new GoogleOAuth2UserInfo(attributes);

        throw new OAuth2AuthenticationProcessingException("Sorry! Login with " + registrationId + " is not supported yet.");
    }
}
