package com.albireo.nebula.repository;

import com.albireo.nebula.model.Album;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AlbumRepository extends JpaRepository<Album, Long> {
    Optional<Album> findByAlbumId(Long id);
    List<Album> findByTitleIgnoreCaseContaining(String title);
    List<Album> findByArtistIgnoreCaseContaining(String artist);
}
