package com.albireo.nebula.dto;

import com.albireo.nebula.model.Item;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ItemDto {
    Long albumId;
    String media;
    String description;
    Item.Condition condition;
    Long price;
}
