package hello.hello.yju.service;

import hello.hello.yju.dto.user.CustomOAuth2User;
import hello.hello.yju.dto.user.GoogleResponse;
import hello.hello.yju.dto.user.OAuth2Response;
import hello.hello.yju.entity.UserEntity;
import hello.hello.yju.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException{

        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println(oAuth2User.getAttributes());

        OAuth2Response oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());

        String googleId = oAuth2Response.getProvider()+" "+oAuth2Response.getProviderId();
        UserEntity existData = userRepository.findByGoogleId(googleId);

        String email = oAuth2Response.getEmail();
        String role;

        String name = oAuth2Response.getName();

        String adminEmail = "useop0821@gmail.com";
        if (email.equals(adminEmail)) {
            role = "ROLE_ADMIN";
        } else {
            role = "ROLE_USER";
        }

        if (existData == null) {

            UserEntity userEntity = UserEntity.builder()
                    .googleId(googleId)
                    .email(email)
                    .name(name)
                    .role(role)
                    .build();
            userRepository.save(userEntity);
        }
        else {
            existData.updateGoogleIdAndEmail(googleId, oAuth2Response.getEmail());

            role = existData.getRole();

            userRepository.save(existData);
        }

        return new CustomOAuth2User(oAuth2Response, role);
    }
}
