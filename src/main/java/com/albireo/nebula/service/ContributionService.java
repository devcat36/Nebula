package com.albireo.nebula.service;

import com.albireo.nebula.dto.ContributionRequest;
import com.albireo.nebula.model.Album;
import com.albireo.nebula.model.Contribution;
import com.albireo.nebula.model.User;
import com.albireo.nebula.repository.AlbumRepository;
import com.albireo.nebula.repository.ContributionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ContributionService {
    private final ContributionRepository contributionRepository;
    private final AlbumRepository albumRepository;
    private final AccountService accountService;
    private final AlbumService albumService;

    public Contribution contribute(String sessionId, ContributionRequest contributionRequest) {
        User user = accountService.getUserFromSessionId(sessionId);
        Album album = null;
        if (contributionRequest.getType() == Contribution.Type.UPDATE ||
                contributionRequest.getType() == Contribution.Type.DELETE)
            album = albumService.findById(contributionRequest.getAlbumId());
        return contributionRepository.save(new Contribution(contributionRequest, user, album));
    }

    public List<Contribution> getContributionsBySessionId(String sessionId) {
        User user = accountService.getUserFromSessionId(sessionId);
        return contributionRepository.findByUser(user);
    }

    public Contribution getContributionById(Long id) {
        return contributionRepository.findByContributionId(id)
                .orElseThrow(() -> new IllegalArgumentException("Not Found. id=" + id));
    }

    public List<Contribution> getAllContributions(String sessionId) {
        accountService.authenticateAsAdmin(sessionId);
        return contributionRepository.findAll(Sort.by(Sort.Direction.DESC, "created"));
    }

    public Contribution approveContribution(String sessionId, Long contributionId) {
        accountService.authenticateAsAdmin(sessionId);
        Contribution contribution = contributionRepository.findByContributionId(contributionId)
                .orElseThrow(() -> new IllegalArgumentException("Not Found. id=" + contributionId));
        switch (contribution.getType()) {
            case CREATE:
                albumRepository.save(new Album(contribution));
                break;
            case UPDATE:
                contribution.getAlbum().update(new Album(contribution));
                break;
            case DELETE:
                albumRepository.delete(contribution.getAlbum());
                break;
            default:
                throw new IllegalArgumentException("Unknown contribution type");
        }
        contribution.setStatus(Contribution.Status.APPROVED);
        return contribution;
    }

    public Contribution denyContribution(String sessionId, Long contributionId) {
        accountService.authenticateAsAdmin(sessionId);
        Contribution contribution = contributionRepository.findByContributionId(contributionId)
                .orElseThrow(() -> new IllegalArgumentException("Not Found. id=" + contributionId));
        contribution.setStatus(Contribution.Status.DENIED);
        return contribution;
    }
}
