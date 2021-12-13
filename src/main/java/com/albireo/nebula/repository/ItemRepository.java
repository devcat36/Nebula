package com.albireo.nebula.repository;

import com.albireo.nebula.model.Item;
import com.albireo.nebula.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ItemRepository extends JpaRepository<Item, Long> {
    Optional<Item> findItemByItemId(Long itemId);
    List<Item> findItemsBySeller(User seller);
    List<Item> findItemsByAlbum_TitleIgnoreCaseContaining(String title);
    List<Item> findItemsByAlbum_ArtistIgnoreCaseContaining(String artist);
    List<Item> findItemsBySeller_UsernameIgnoreCaseContaining(String username);
}
