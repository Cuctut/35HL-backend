package com.cuctut.hl.model.dto.resp;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ChapterRespDto {

    private Long id;
    private String title;
    private List<ChapterRespDto> subChapters;
}
