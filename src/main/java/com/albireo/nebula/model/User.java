package com.albireo.nebula.model;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class User {
    @Id
    String username;

    @Column
    String password;

    @Column
    String email;

    @Setter
    @Enumerated(EnumType.STRING)
    Role role;

    public enum Role {
        MEMBER, ADMIN
    }

    public User(User user) {
        this.username = user.username;
        this.password = user.password;
        this.email = user.email;
        this.role = Role.MEMBER;
    }

    @Builder
    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = Role.MEMBER;
    }

    public void update(User user) {
        this.password = user.password;
        this.email = user.password;
    }
}
