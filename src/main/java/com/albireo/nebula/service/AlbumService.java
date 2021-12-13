package com.albireo.nebula.service;

import com.albireo.nebula.model.Album;
import com.albireo.nebula.repository.AlbumRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@RequiredArgsConstructor
@Service
public class AlbumService {
    private final AlbumRepository albumRepository;

    @Transactional
    public Album findById(Long id){
        return albumRepository.findByAlbumId(id)
                .orElseThrow(() -> new IllegalArgumentException("Not Found. id=" + id));
    }

    @Transactional
    public List<Album> search(String type, String term) {
        switch (type){
            case "title":
                return albumRepository.findByTitleIgnoreCaseContaining(term);
            case "artist":
                return albumRepository.findByArtistIgnoreCaseContaining(term);
            default:
                throw new IllegalArgumentException("No such search type.");
        }
    }

    @Transactional
    public Album save(Album album) {
        return albumRepository.save(album);
    }

    @Transactional
    public Album update(Long id, Album album) {
        Album albumToUpdate = albumRepository.findByAlbumId(id)
                .orElseThrow(() -> new IllegalArgumentException("Not Found. id=" + id));
        albumToUpdate.update(album);
        return album;
    }
}
