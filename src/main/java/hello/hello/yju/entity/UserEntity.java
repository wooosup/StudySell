package hello.hello.yju.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user")
public class UserEntity{

    @Id
    @Column(name = "user_id")
    private String googleId;

    private String email;

    private String name;

    private String role;

    @OneToMany(mappedBy = "user", cascade = ALL)
    private final List<ItemEntity> items = new ArrayList<>();

    @OneToMany(mappedBy = "seller", cascade = ALL, orphanRemoval = true)
    private List<ChatRoom> seller;

    @OneToMany(mappedBy = "buyer", cascade = ALL, orphanRemoval = true)
    private List<ChatRoom> buyer;

    @Builder
    private UserEntity(String googleId, String email, String name, String role) {
        this.googleId = googleId;
        this.email = email;
        this.name = name;
        this.role = role;
    }

    public void updateGoogleIdAndEmail(String googleId, String email) {
        this.googleId = googleId;
        this.email = email;
    }
}
