package hello.hello.yju.repository;

import hello.hello.yju.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
     UserEntity findByGoogleId(String googleId);

}
