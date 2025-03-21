package com.cuctut.hl.model.dto.resp;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class DirectoryRespDto {

    private BookRespDto book;

    private List<ChapterRespDto> chapters;
}
