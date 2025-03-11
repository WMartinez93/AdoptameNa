package com.fiuni.adoptamena.api.dto.post;
import com.fiuni.adoptamena.api.dto.base.BaseDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class ResponsePostDTO extends BaseDTO {

    private Integer idUser;


    private String title;

    private String content;


    private String postTypeName;

    private String locationCoordinates;


    private String contactNumber;

    private String status;

    private int sharedCounter;

    private Date publicationDate;

}
