package com.albireo.nebula;

import com.albireo.nebula.model.Album;
import com.albireo.nebula.model.Order;
import com.albireo.nebula.model.User;
import com.albireo.nebula.service.AccountService;
import com.albireo.nebula.service.AlbumService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.event.annotation.BeforeTestClass;

import java.util.List;

@SpringBootTest
public class DiscServiceTests {
    @Autowired
    AlbumService albumService;

    @Autowired
    AccountService accountService;

    @Test
    void testAlbumFind() {
        Album album = Album.builder().title("On Every Street").artist("Dire Straits").description("lorem ipsum").build();
        Album res = albumService.save(album);
        System.out.println(res.getTitle());
    }

    @Test
    void testSearch() {
        albumService.save(Album.builder().title("On Every Street").artist("Dire Straits").description("lorem ipsum").build());
        albumService.save(Album.builder().title("Random Access Memories").artist("Daft Punk").description("dolor sit amet").build());

        var albums = albumService.search("artist", "daft punk");
        System.out.println(albums.get(0).getTitle());

        albums = albumService.search("title", "street");
        System.out.println(albums.get(0).getTitle());
    }
}
