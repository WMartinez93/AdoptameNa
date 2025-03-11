package com.fiuni.adoptamena.api.dto.post;
import com.fiuni.adoptamena.api.dto.base.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
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
