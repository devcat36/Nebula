package com.albireo.nebula.dto;

import com.albireo.nebula.model.Contribution;
import lombok.Data;

@Data
public class ContributionRequest {
    Contribution.Type type;
    Long albumId;
    String title;
    String artist;
    String description;
    String notes;
}
