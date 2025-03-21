package com.cuctut.hl.controller;

import com.cuctut.hl.common.RestResp;
import com.cuctut.hl.model.dto.req.NoteReqDto;
import com.cuctut.hl.model.dto.resp.NoteRespDto;
import com.cuctut.hl.service.NoteService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/notes")
public class NoteController {

    private final NoteService noteService;

    // 获取当前用户的所有笔记
    @GetMapping("/user")
    public RestResp<List<NoteRespDto>> getUserNotes() {
        return noteService.getUserNotes();
    }


    // 获取所有笔记
    @GetMapping("/{noteType}/{chapterId}")
    public RestResp<List<NoteRespDto>> getNotesByChapterAndType(
            @PathVariable Long chapterId,
            @PathVariable String noteType
    ) {
        return noteService.getNotesByChapterAndType(chapterId, noteType);
    }

    // 创建笔记
    @PostMapping
    public RestResp<NoteRespDto> createNote(@RequestBody NoteReqDto noteReqDto) {
        return noteService.createNote(noteReqDto);
    }

    // 删除笔记
    @DeleteMapping("/{id}")
    public RestResp<Void> deleteNote(@PathVariable Long id) {
        return noteService.deleteNote(id);
    }
}
