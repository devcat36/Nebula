package com.albireo.nebula.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@NoArgsConstructor
@Table(name="OrderRequest")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long orderId;

    @ManyToOne
    @JoinColumn(name="itemId")
    Item item;

    @ManyToOne
    @JoinColumn(name="username")
    User buyer;

    @Column
    String message;

    @Setter
    @Enumerated(EnumType.STRING)
    Status status;

    @Temporal(TemporalType.TIMESTAMP)
    Date created;

    @Builder
    public Order (Item item, User buyer, String message) {
        this.item = item;
        this.buyer = buyer;
        this.message = message;
        this.status = Status.OPEN;
        this.created = new Date(System.currentTimeMillis());
    }

    public enum Status {
        OPEN, IN_PROGRESS, COMPLETE, CLOSED
    }
}
