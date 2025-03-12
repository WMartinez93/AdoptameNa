package com.fiuni.adoptamena.api.dto.favorite;

import java.io.Serial;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fiuni.adoptamena.api.dto.base.BaseDTO;
import com.fiuni.adoptamena.api.dto.post.ResponsePostDTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class FavoriteDTO extends BaseDTO {

    @Serial
    private static final long serialVersionUID = 1L;

    private Integer postId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private ResponsePostDTO post;

}
