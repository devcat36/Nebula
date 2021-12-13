package com.albireo.nebula.model;

import com.albireo.nebula.dto.ContributionRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@NoArgsConstructor
public class Contribution {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long contributionId;

    @ManyToOne
    @JoinColumn(name="username")
    User user;

    @Column
    @Enumerated(EnumType.STRING)
    Type type;

    @ManyToOne
    @JoinColumn(name="albumId")
    Album album;

    @Column
    String title;

    @Column
    String artist;

    @Column
    String description;

    @Column
    String notes;

    @Setter
    @Enumerated(EnumType.STRING)
    Status status;

    @Temporal(TemporalType.TIMESTAMP)
    Date created;

    public enum Type {
        CREATE, UPDATE, DELETE
    }

    public enum Status {
        OPEN, APPROVED, DENIED
    }

    public Contribution (ContributionRequest contributionRequest, User user, Album album) {
        this.type = contributionRequest.getType();
        this.title = contributionRequest.getTitle();
        this.artist = contributionRequest.getArtist();
        this.description = contributionRequest.getDescription();
        this.notes = contributionRequest.getNotes();
        this.created = new Date(System.currentTimeMillis());
        this.user = user;
        this.album = album;
        this.status = Status.OPEN;
    }
}