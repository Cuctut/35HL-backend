package com.cuctut.hl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cuctut.hl.model.entity.Note;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NoteMapper extends BaseMapper<Note> {
}