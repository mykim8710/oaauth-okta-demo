package com.demo.okta.security;

import lombok.Getter;

@Getter
public enum OAuth2ProviderEnums {
    KAKAO("kakao"),
    NAVER("naver"),
    GOOGLE("google"),
    OKTA("okta"),
    ;

    private String registrationId;

    OAuth2ProviderEnums(String registrationId) {
        this.registrationId = registrationId;
    }
}
