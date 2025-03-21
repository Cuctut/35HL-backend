package com.cuctut.hl.controller;

import com.cuctut.hl.common.RestResp;
import com.cuctut.hl.model.dto.resp.DailyQuestionViewableRespDto;
import com.cuctut.hl.model.entity.DailyQuestion;
import com.cuctut.hl.service.DailyQuestionService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/DQ")
public class DailyQuestionController {

    private final DailyQuestionService dailyQuestionService;

    @PostMapping
    public RestResp<Void> addDailyQuestion(@RequestBody DailyQuestion dailyQuestion) {
        return dailyQuestionService.addDailyQuestion(dailyQuestion);
    }

    @GetMapping("/{year}/{month}")
    public RestResp<List<DailyQuestion>> getDailyQuestions4User(@PathVariable int year, @PathVariable int month) {
        return dailyQuestionService.getDailyQuestions4User(year, month);
    }

    @GetMapping("/op/{year}/{month}")
    public RestResp<List<DailyQuestionViewableRespDto>> getDailyQuestions4Admin(@PathVariable int year, @PathVariable int month) {
        return dailyQuestionService.getDailyQuestions4Admin(year, month);
    }

    @DeleteMapping("/{id}")
    public RestResp<Void> deleteDailyQuestion(@PathVariable int id) {
        return dailyQuestionService.deleteDailyQuestion(id);
    }

}
