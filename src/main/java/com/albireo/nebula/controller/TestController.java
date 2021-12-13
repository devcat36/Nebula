package com.albireo.nebula.controller;

import com.albireo.nebula.dto.ItemDto;
import com.albireo.nebula.model.Album;
import com.albireo.nebula.model.Item;
import com.albireo.nebula.model.User;
import com.albireo.nebula.repository.ItemRepository;
import com.albireo.nebula.repository.UserRepository;
import com.albireo.nebula.service.AccountService;
import com.albireo.nebula.service.AlbumService;
import com.albireo.nebula.service.ContributionService;
import com.albireo.nebula.service.MarketplaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class TestController {
    private final AccountService accountService;
    private final AlbumService albumService;
    private final ContributionService contributionService;
    private final MarketplaceService marketplaceService;

    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @GetMapping("/mockdata")
    public void generateMockData() {
        generateUsers();
        generateAlbums();
        generateItems();
    }

    private void generateUsers() {
        User admin = new User("admin", "password", "admin@admin.com");
        admin.setRole(User.Role.ADMIN);
        userRepository.save(admin);

        User jake = new User("jake", "pass", "jake@example.com");
        userRepository.save(jake);

        User finn = new User("finn", "word", "finn@example.com");
        userRepository.save(finn);
    }

    private void generateAlbums() {
        Album ds = Album.builder().title("On Every Street").artist("Dire Straits").description("lorem ipsum").build();
        albumService.save(ds);
        Album broken = Album.builder().title("Broken").artist("Miza").description("dolor sit amet").build();
        albumService.save(broken);
        Album ology = Album.builder().title("Ology").artist("Gallant").description("consectetur adipiscing elit,").build();
        albumService.save(ology);
    }

    private void generateItems() {
        User jake = userRepository.findByUsername("jake").orElse(null);
        Album ology = albumService.search("title", "ology").get(0);
        Item item = Item.builder()
                .album(ology)
                .seller(jake)
                .condition(Item.Condition.FAIR)
                .media("CD")
                .description("sed do eiusmod tempor incididunt ut labore et dolore magna aliqua")
                .price(24500L)
                .build();
        itemRepository.save(item);
    }
}
