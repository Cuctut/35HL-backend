package com.cuctut.hl.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cuctut.hl.auth.UserHolder;
import com.cuctut.hl.common.RestResp;
import com.cuctut.hl.mapper.NoteMapper;
import com.cuctut.hl.mapper.UserMapper;
import com.cuctut.hl.model.dto.req.NoteReqDto;
import com.cuctut.hl.model.dto.resp.NoteRespDto;
import com.cuctut.hl.model.entity.Note;
import com.cuctut.hl.model.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class NoteService {

    private final NoteMapper noteMapper;
    private final UserMapper userMapper;

    // 查询用户笔记
    public RestResp<List<NoteRespDto>> getUserNotes() {
        Long userId = UserHolder.getUserId();
        LambdaQueryWrapper<Note> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Note::getCreateBy, userId).orderByDesc(Note::getUpdatedAt);
        List<Note> notes = noteMapper.selectList(queryWrapper);
        List<NoteRespDto> result = notes.stream().map(this::convertToDto).toList();
        return RestResp.ok(result);
    }


    // 获取对应类型和章节下的笔记
    public RestResp<List<NoteRespDto>> getNotesByChapterAndType(Long chapterId, String noteType) {
        LambdaQueryWrapper<Note> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Note::getChapterId, chapterId).eq(Note::getNoteType, noteType).orderByDesc(Note::getUpdatedAt);
        List<Note> notes = noteMapper.selectList(queryWrapper);
        Set<Long> userIds = notes.stream().map(Note::getCreateBy).collect(Collectors.toSet());
        if (!userIds.isEmpty()) {
            Map<Long, String> userMap = userMapper.selectByIds(userIds).stream()
                    .collect(Collectors.toMap(User::getId, User::getUsername));
            List<NoteRespDto> result = notes.stream()
                    .map(note -> {
                        NoteRespDto dto = convertToDto(note);
                        dto.setCreator(userMap.getOrDefault(note.getCreateBy(), "未知用户"));
                        return dto;
                    })
                    .toList();
            return RestResp.ok(result);
        }
        return RestResp.ok(Collections.emptyList());
    }

    // 创建笔记
    public RestResp<NoteRespDto> createNote(NoteReqDto noteReqDto) {
        Long userId = UserHolder.getUserId();
        Note note = new Note();
        note.setTitle(noteReqDto.getTitle());
        note.setContent(noteReqDto.getContent());
        note.setChapterId(noteReqDto.getChapterId());
        note.setNoteType(noteReqDto.getNoteType());
        note.setCreateBy(userId);
        noteMapper.insert(note);
        NoteRespDto noteRespDto = convertToDto(note);
        noteRespDto.setCreator(userMapper.selectById(userId).getUsername());
        return RestResp.ok(noteRespDto);
    }

    // 删除笔记
    public RestResp<Void> deleteNote(Long id) {
        noteMapper.deleteById(id);
        return RestResp.ok();
    }

    // 实体类转DTO
    private NoteRespDto convertToDto(Note note) {
        NoteRespDto dto = new NoteRespDto();
        dto.setId(note.getId());
        dto.setTitle(note.getTitle());
        dto.setContent(note.getContent());
        dto.setCreatedAt(note.getCreatedAt());
        dto.setUpdatedAt(note.getUpdatedAt());
        return dto;
    }
}