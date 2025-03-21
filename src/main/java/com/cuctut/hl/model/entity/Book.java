package com.cuctut.hl.model.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.cuctut.hl.model.dto.resp.BookRespDto;
import lombok.Data;

@Data
@TableName("books")
public class Book {

    @TableId
    private Long id;
    
    private String title;
    
    private String createdAt;
    
    private String updatedAt;

    public BookRespDto convert2dto() {
        return BookRespDto.builder()
                .id(id)
                .title(title)
                .build();
    }
}
