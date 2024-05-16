package com.demo.okta.security;


import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CustomOAuth2UserDetailsService extends OidcUserService {

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        DefaultOAuth2UserService delegate = new DefaultOAuth2UserService(); // delegate, 대리자
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();

        OAuthAttributes oAuthAttributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        String providerId = oAuthAttributes.getProviderId();
        String username = registrationId +"_" +providerId;
        String dummyPassword = UUID.randomUUID().toString();
        String email = oAuthAttributes.getEmail();

        User user = User.builder()
                .id(1L)
                .email(email)
                .username(username)
                .password(dummyPassword)
                .build();


        return new PrincipalDetail(user, oAuthAttributes.getAttributes());
    }

    /*@Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        DefaultOAuth2UserService delegate = new DefaultOAuth2UserService(); // delegate, 대리자

        OAuth2User oAuth2User = delegate.loadUser(userRequest);
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration()
                                                        .getProviderDetails()
                                                        .getUserInfoEndpoint()
                                                        .getUserNameAttributeName();

        OAuthAttributes oAuthAttributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());
        System.out.println("oAuthAttributes = " + oAuthAttributes);

        String providerId = oAuthAttributes.getProviderId();
        String username = registrationId +"_" +providerId;
        String dummyPassword = UUID.randomUUID().toString();
        String email = oAuthAttributes.getEmail();

        User user = User.builder()
                            .id(1L)
                            .email(email)
                            .username(username)
                            .password(dummyPassword)
                            .build();

        return new PrincipalDetail(user, oAuthAttributes.getAttributes());
    }*/

}
