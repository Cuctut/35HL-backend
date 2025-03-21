package com.cuctut.hl.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cuctut.hl.mapper.ChapterMapper;
import com.cuctut.hl.model.entity.Chapter;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChapterService extends ServiceImpl<ChapterMapper, Chapter> {

    public List<Chapter> getChaptersByBookId(Long bookId) {
        QueryWrapper<Chapter> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("book_id", bookId);
        return list(queryWrapper);
    }

    public Chapter createChapter(Chapter chapter) {
        save(chapter);
        return chapter;
    }
}
