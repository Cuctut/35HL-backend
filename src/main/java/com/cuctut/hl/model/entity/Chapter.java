package com.cuctut.hl.model.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cuctut.hl.model.dto.resp.ChapterRespDto;
import lombok.Data;

import java.util.List;

@Data
@TableName("chapters")
public class Chapter {

    @TableId
    private Long id;
    private Long bookId;
    private Long parentId;
    private String title;
    private Integer level;
    private String createdAt;
    private String updatedAt;

    public ChapterRespDto convertToChapterRespDto(List<ChapterRespDto> subChapters) {
        return ChapterRespDto.builder()
                .id(id)
                .title(title)
                .subChapters(subChapters)
                .build();
    }
}
