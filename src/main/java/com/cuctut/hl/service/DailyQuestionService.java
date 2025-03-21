package com.cuctut.hl.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cuctut.hl.common.ErrorCodeEnum;
import com.cuctut.hl.common.RestResp;
import com.cuctut.hl.mapper.DailyQuestionMapper;
import com.cuctut.hl.model.dto.resp.DailyQuestionViewableRespDto;
import com.cuctut.hl.model.entity.DailyQuestion;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
* @author 86134
* @description 针对表【daily_question(每日一题表)】的数据库操作Service
* @createDate 2025-03-19 15:27:00
*/
@Service
@AllArgsConstructor
public class DailyQuestionService extends ServiceImpl<DailyQuestionMapper, DailyQuestion> {

    private final FilesService filesService;

    public RestResp<Void> addDailyQuestion(DailyQuestion dailyQuestion) {
        LambdaQueryWrapper<DailyQuestion> query = new LambdaQueryWrapper<>();
        query.eq(DailyQuestion::getPublishDate, dailyQuestion.getPublishDate());
        Long count = baseMapper.selectCount(query);
        if (count > 0) return RestResp.fail(ErrorCodeEnum.DQ_EXIST);
        save(dailyQuestion);
        return RestResp.ok();
    }

    public RestResp<List<DailyQuestion>> getDailyQuestions4User(int year, int month) {
        LambdaQueryWrapper<DailyQuestion> query = new LambdaQueryWrapper<>();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = LocalDateTime.of(year, month, 1, 0, 0);
        if (start.isAfter(now)) return RestResp.ok(Collections.emptyList());
        LocalDateTime end = start.plusMonths(1).minusSeconds(1);
        end = end.isAfter(now) ? now : end;
        query.between(DailyQuestion::getPublishDate, start, end).orderByDesc(DailyQuestion::getPublishDate);
        List<DailyQuestion> dailyQuestions = baseMapper.selectList(query);
        return RestResp.ok(dailyQuestions);
    }

    public RestResp<List<DailyQuestionViewableRespDto>> getDailyQuestions4Admin(int year, int month) {
        LambdaQueryWrapper<DailyQuestion> query = new LambdaQueryWrapper<>();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = LocalDateTime.of(year, month, 1, 0, 0);
        LocalDateTime end = start.plusMonths(1).minusSeconds(1);
        query.between(DailyQuestion::getPublishDate, start, end).orderByDesc(DailyQuestion::getPublishDate);
        List<DailyQuestion> dailyQuestions = baseMapper.selectList(query);
        if (CollectionUtils.isEmpty(dailyQuestions)) return RestResp.ok(Collections.emptyList());
        List<DailyQuestionViewableRespDto> result = dailyQuestions.stream().map(dq -> {
            DailyQuestionViewableRespDto dto = new DailyQuestionViewableRespDto();
            BeanUtils.copyProperties(dq, dto); // 复制属性
            dto.setViewable(dq.getPublishDate().isBefore(now) ? 1 : 0); // 如果 publishDate 在 now 之前，则可见
            return dto;
        }).toList();
        return RestResp.ok(result);
    }

    public RestResp<Void> deleteDailyQuestion(int id) {
        DailyQuestion q = getById(id);
        Long fileId = q.getFileId();
        filesService.deleteFile(fileId);
        removeById(id);
        return RestResp.ok();
    }

}
