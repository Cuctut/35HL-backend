package com.cuctut.hl.controller;

import com.cuctut.hl.common.RestResp;
import com.cuctut.hl.model.dto.resp.BookRespDto;
import com.cuctut.hl.model.dto.resp.DirectoryRespDto;
import com.cuctut.hl.service.BookService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;

    @GetMapping("/directory")
    public RestResp<List<DirectoryRespDto>> getDirectory() {
        return bookService.getDirectory();
    }

    @GetMapping("/books")
    public RestResp<List<BookRespDto>> getBooks() {
        return bookService.getBooks();
    }
}
