package com.cuctut.hl.model.dto.resp;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookRespDto {

    private Long id;

    private String title;
}
