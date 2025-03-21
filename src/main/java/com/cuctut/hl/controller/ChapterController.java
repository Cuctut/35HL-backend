package com.cuctut.hl.controller;

import com.cuctut.hl.model.entity.Chapter;
import com.cuctut.hl.service.ChapterService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ChapterController {

    private final ChapterService chapterService;

    public ChapterController(ChapterService chapterService) {
        this.chapterService = chapterService;
    }

    @GetMapping("/books/{bookId}/chapters")
    public List<Chapter> getChaptersByBookId(@PathVariable Long bookId) {
        return chapterService.getChaptersByBookId(bookId);
    }

    @PostMapping("/chapters")
    public Chapter createChapter(@RequestBody Chapter chapter) {
        return chapterService.createChapter(chapter);
    }
}
