package com.albireo.nebula.controller;

import com.albireo.nebula.dto.ContributionRequest;
import com.albireo.nebula.model.Album;
import com.albireo.nebula.model.Contribution;
import com.albireo.nebula.service.AlbumService;
import com.albireo.nebula.service.ContributionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class DiscController {
    private final AlbumService albumService;
    private final ContributionService contributionService;

    @GetMapping("/disco/album/{id}")
    Album getAlbum(@PathVariable long id){
        return albumService.findById(id);
    }

    @GetMapping("/disco/search")
    List<Album> searchAlbum(@RequestParam String type, @RequestParam String term){
        return albumService.search(type, term);
    }

    @PostMapping("/disco/contribution")
    Contribution contribute(@RequestBody ContributionRequest contributionRequest, @RequestHeader String sessionId){
        return contributionService.contribute(sessionId, contributionRequest);
    }

    @GetMapping("/disco/contributions")
    List<Contribution> getContributions(@RequestHeader String sessionId) {
        return contributionService.getContributionsBySessionId(sessionId);
    }

    @GetMapping("/disco/contribution/{request_id}")
    Contribution getContribution(@PathVariable Long request_id) {
        return contributionService.getContributionById(request_id);
    }

    @PostMapping("/debug")
    Album debug(){
        Album album = Album.builder().title("On Every Street").artist("Dire Straits").description("lorem ipsum").build();
        return albumService.save(album);
    }
}
