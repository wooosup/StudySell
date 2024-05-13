package hello.hello.yju.service;

import hello.hello.yju.dto.CustomOAuth2User;
import hello.hello.yju.dto.GoogleResponse;
import hello.hello.yju.dto.OAuth2Response;
import hello.hello.yju.entity.ItemEntity;
import hello.hello.yju.entity.UserEntity;
import hello.hello.yju.repository.UserRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
    private final UserRepository userRepository;

    public CustomOAuth2UserService(UserRepository userRepository) {

        this.userRepository = userRepository;
    }


    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException{

        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println(oAuth2User.getAttributes());

        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        OAuth2Response oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());

        // 이메일 도메인 검증
        if (!oAuth2Response.getEmail().endsWith("@g.yju.ac.kr")) {
            throw new OAuth2AuthenticationException(new OAuth2Error("invalid_domain"), "유효하지 않은 이메일 도메인입니다.");
        }

        String googleId = oAuth2Response.getProvider()+" "+oAuth2Response.getProviderId();
        UserEntity existData = userRepository.findByGoogleId(googleId);

        String email = oAuth2Response.getEmail();
        String role;

        String adminEmail = "wss3325@g.yju.ac.kr";
        if (email.equals(adminEmail)) {
            role = "ROLE_ADMIN"; // 관리자 역할 부여
        } else {
            role = "ROLE_USER"; // 일반 사용자 역할 부여
        }

        if (existData == null) {

            UserEntity userEntity = new UserEntity();
            userEntity.setGoogleId(googleId);
            userEntity.setEmail(email);
            userEntity.setRole(role);

            userRepository.save(userEntity);
        }
        else {

            existData.setGoogleId(googleId);
            existData.setEmail(oAuth2Response.getEmail());

            role = existData.getRole();

            userRepository.save(existData);
        }

        return new CustomOAuth2User(oAuth2Response, role);
    }
}
