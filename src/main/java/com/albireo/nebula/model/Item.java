package com.albireo.nebula.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@NoArgsConstructor
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long itemId;

    @ManyToOne
    @JoinColumn(name="albumId")
    Album album;

    @Column
    String media;

    @ManyToOne
    @JoinColumn(name="username")
    @OnDelete(action = OnDeleteAction.CASCADE)
    User seller;

    @Column
    String description;

    @Enumerated(EnumType.STRING)
    Condition condition;

    @Column
    Long price;

    @Setter
    @Enumerated(EnumType.STRING)
    Status status;

    @Temporal(TemporalType.TIMESTAMP)
    Date created;

    public enum Condition {
        POOR, FAIR, GOOD, MINT
    }

    public enum Status {
        LISTED, ON_ORDER, SOLD
    }

    @Builder
    public Item (Album album, String media, User seller, String description, Condition condition, Long price) {
        this.album = album;
        this.media = media;
        this.seller = seller;
        this.description = description;
        this.condition = condition;
        this.price = price;
        this.created = new Date(System.currentTimeMillis());
        this.status = Status.LISTED;
    }

}
