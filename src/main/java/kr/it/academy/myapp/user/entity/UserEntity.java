package kr.it.academy.myapp.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_info")
@Getter
@Setter
public class UserEntity {

    @Id
    @Column(name = "user_id")
    private String userId;

    @Column(name = "user_name")
    private String userName;

    private String passwd;
    private String email;

    @Column(name = "create_date")
    private LocalDateTime createDate;

    @Column(name = "update_date")
    private LocalDateTime updateDate;

    private String nickname;
}
