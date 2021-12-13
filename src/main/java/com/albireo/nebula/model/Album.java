package com.albireo.nebula.model;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
public class Album {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long albumId;

    @Column
    String title;

    @Column
    String artist;

    @Column(columnDefinition = "TEXT")
    String description;

    @Builder
    public Album(String title, String artist, String description) {
        this.title = title;
        this.artist = artist;
        this.description = description;
    }

    public Album(Contribution contribution) {
        this.title = contribution.title;
        this.artist = contribution.artist;
        this.description = contribution.description;
    }

    public void update(Album album) {
        this.title = album.title;
        this.artist = album.artist;
        this.description = album.description;
    }
}
