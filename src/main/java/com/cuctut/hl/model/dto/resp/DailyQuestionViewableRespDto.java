package com.cuctut.hl.model.dto.resp;

import com.cuctut.hl.model.entity.DailyQuestion;
import lombok.Data;

@Data
public class DailyQuestionViewableRespDto extends DailyQuestion {

    private int viewable;
}
