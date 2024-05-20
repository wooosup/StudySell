package hello.hello.yju.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "user")
public class UserEntity{

    @Id
    @Column(name = "user_id")
    private String googleId;

    private String email;

    private String name;

    private String role;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<ItemEntity> items = new ArrayList<>();

    @OneToMany(mappedBy = "seller")
    private List<ChatRoom> seller;

    @OneToMany(mappedBy = "buyer")
    private List<ChatRoom> buyer;
}
