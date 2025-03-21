package com.cuctut.hl.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cuctut.hl.common.RestResp;
import com.cuctut.hl.mapper.BookMapper;
import com.cuctut.hl.mapper.ChapterMapper;
import com.cuctut.hl.model.dto.resp.BookRespDto;
import com.cuctut.hl.model.dto.resp.ChapterRespDto;
import com.cuctut.hl.model.dto.resp.DirectoryRespDto;
import com.cuctut.hl.model.entity.Book;
import com.cuctut.hl.model.entity.Chapter;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class BookService {

    private final BookMapper bookMapper;
    private final ChapterMapper chapterMapper;

    public RestResp<List<DirectoryRespDto>> getDirectory() {

        List<Book> allBooks = bookMapper.selectList(null);
        if (allBooks.isEmpty()) return RestResp.ok(new ArrayList<>());

        List<DirectoryRespDto> result = new ArrayList<>();
        LambdaQueryWrapper<Chapter> query = new LambdaQueryWrapper<>();
        for (Book book : allBooks) {
            query.eq(Chapter::getBookId, book.getId()).eq(Chapter::getLevel, 1).orderByAsc(Chapter::getId);
            List<Chapter> chapters = chapterMapper.selectList(query);
            query.clear();

            List<ChapterRespDto> chaptersRespDtos = new ArrayList<>();
            for (Chapter chapter : chapters) {
                query.eq(Chapter::getParentId, chapter.getId()).orderByAsc(Chapter::getId);
                List<ChapterRespDto> subChapters = chapterMapper.selectList(query)
                        .stream().map( subChapter -> subChapter.convertToChapterRespDto(null) )
                        .toList();
                query.clear();
                ChapterRespDto chapterRespDto = chapter.convertToChapterRespDto(subChapters);
                chaptersRespDtos.add(chapterRespDto);
            }

            DirectoryRespDto dto = DirectoryRespDto.builder()
                    .book(book.convert2dto())
                    .chapters(chaptersRespDtos)
                    .build();
            result.add(dto);
        }
        return RestResp.ok(result);
    }

    public RestResp<List<BookRespDto>> getBooks() {
        List<Book> books = bookMapper.selectList(null);
        if (books.isEmpty()) return RestResp.ok(new ArrayList<>());
        List<BookRespDto> result = books.stream().map(Book::convert2dto).toList();
        return RestResp.ok(result);
    }
}
