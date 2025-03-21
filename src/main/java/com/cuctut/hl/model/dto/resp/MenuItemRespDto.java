package com.cuctut.hl.model.dto.resp;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MenuItemRespDto {

    private Long id;
    private String key;
    private String label;
}
