package kr.co.tmax.rabackend.security.oauth2;

import kr.co.tmax.rabackend.domain.user.User;
import kr.co.tmax.rabackend.exception.OAuth2AuthenticationProcessingException;
import kr.co.tmax.rabackend.infrastructure.user.UserRepository;
import kr.co.tmax.rabackend.security.UserPrincipal;
import kr.co.tmax.rabackend.security.oauth2.user.OAuth2UserInfo;
import kr.co.tmax.rabackend.security.oauth2.user.OAuth2UserInfoFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

        @Override
        public OAuth2User loadUser(OAuth2UserRequest oAuth2UserRequest) throws OAuth2AuthenticationException {
            OAuth2User oAuth2User = super.loadUser(oAuth2UserRequest);

            try {
                return processOAuth2User(oAuth2UserRequest, oAuth2User);
            } catch (AuthenticationException ex) {
                throw ex;
            } catch (Exception ex) {
                // Throwing an instance of AuthenticationException will trigger the OAuth2AuthenticationFailureHandler
                throw new InternalAuthenticationServiceException(ex.getMessage(), ex.getCause());
            }
        }

        private OAuth2User processOAuth2User(OAuth2UserRequest oAuth2UserRequest, OAuth2User oAuth2User) {
            OAuth2UserInfo oAuth2UserInfo =
                    OAuth2UserInfoFactory.getOAuth2UserInfo(oAuth2UserRequest.getClientRegistration().getRegistrationId(), oAuth2User.getAttributes());

            if (!StringUtils.hasText(oAuth2UserInfo.getEmail())) {
                throw new OAuth2AuthenticationProcessingException("ID not found from OAuth2 provider");
            }

            Optional<User> userOptional = userRepository.findByEmail(oAuth2UserInfo.getEmail());

        if (userOptional.isEmpty()) {
            User newUser = registerNewUser(oAuth2UserRequest, oAuth2UserInfo);
            return UserPrincipal.create(newUser, oAuth2User.getAttributes());
        }

        User user = userOptional.get();

        AuthProvider requestProvider = AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId());
        AuthProvider userProvider = user.getProvider();
        checkProvider(userProvider, requestProvider);

        User updatedUser = updateExistingUser(user, oAuth2UserInfo);

        return UserPrincipal.create(updatedUser, oAuth2User.getAttributes());
    }

    private void checkProvider(AuthProvider userProvider, AuthProvider requestProvider) {
        if (!userProvider.equals(requestProvider))
            throw new OAuth2AuthenticationProcessingException("Looks like you're signed up with " + userProvider + " account. Please use your " + userProvider + " account to login.");
    }

    private User registerNewUser(OAuth2UserRequest oAuth2UserRequest, OAuth2UserInfo oAuth2UserInfo) {
        User user = new User();

        user.setProvider(AuthProvider.valueOf(oAuth2UserRequest.getClientRegistration().getRegistrationId()));
        user.setProviderId(oAuth2UserInfo.getId());
        user.setName(oAuth2UserInfo.getName());
        user.setEmail(oAuth2UserInfo.getEmail());
        user.setImageUrl(oAuth2UserInfo.getImageUrl());
        return userRepository.save(user);
    }

    private User updateExistingUser(User existingUser, OAuth2UserInfo oAuth2UserInfo) {
        existingUser.setName(oAuth2UserInfo.getName());
        existingUser.setImageUrl(oAuth2UserInfo.getImageUrl());
        return userRepository.save(existingUser);
    }
}
