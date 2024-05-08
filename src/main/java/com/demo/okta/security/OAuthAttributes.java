package com.demo.okta.security;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;

@Getter
@ToString
public class OAuthAttributes {
    private Map<String, Object> attributes;
    private String providerId;
    private String nameAttributeKey;
    private String nickname;
    private String email;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes, String providerId, String nameAttributeKey, String nickname, String email) {
        this.attributes = attributes;
        this.providerId = providerId;
        this.nameAttributeKey = nameAttributeKey;
        this.nickname = nickname;
        this.email = email;
    }

    public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
        if(registrationId.equals(OAuth2ProviderEnums.OKTA.getRegistrationId())) {
            return ofOkta(userNameAttributeName, attributes);
        }

        return null;
    }

    public static OAuthAttributes ofOkta(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .providerId((String)attributes.get("sub"))
                .nickname((String)attributes.get("nickname"))
                .email((String)attributes.get("preferred_username"))
                .nameAttributeKey(userNameAttributeName) // sub
                .attributes(attributes)
                .build();
    }
}
