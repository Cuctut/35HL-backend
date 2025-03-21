package com.cuctut.hl.model.dto.req;

import lombok.Data;

@Data
public class NoteReqDto {
    private String title;
    private String content;
    private Long chapterId; // 所属章节号
    private String noteType; // 笔记类型（materials 或 exercises）
}