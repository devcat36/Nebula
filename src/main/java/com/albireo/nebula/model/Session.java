package com.albireo.nebula.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Entity
@Getter
@NoArgsConstructor
public class Session {
    @Id
    String sessionId;

    @ManyToOne
    @JoinColumn(name="username")
    @OnDelete(action = OnDeleteAction.CASCADE)
    User user;

    @Temporal(TemporalType.TIMESTAMP)
    Date expire;

    public Session(User user){
        this.sessionId = UUID.randomUUID().toString();
        this.user = user;
        this.expire = new Date(System.currentTimeMillis() + TimeUnit.HOURS.toMillis(24));
    }
}
