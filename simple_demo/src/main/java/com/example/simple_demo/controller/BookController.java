package com.example.simple_demo.controller;

import com.example.simple_demo.pojo.Book;
import com.example.simple_demo.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @program: about_elasticsearch
 * @description: BookController
 * @author: henryDai
 * @create: 2024-10-22 23:14
 **/

@RestController
public class BookController {
    @Autowired
    private BookService bookService; // 使用接口

    @GetMapping("/books")
    public List<Book> getAllBooks() {
        return bookService.getAllBooks();
    }

    /**
    * @Description: 搜索
    * @Param: [java.lang.String, int, int]
    * @return: java.util.List<java.util.Map<java.lang.String,java.lang.Object>>
    * @Author: henryDai
    * @Date: 2024/10/23
    */
    @GetMapping("/search")
    public List<Map<String, Object>> searchBooks(@RequestParam String keyword,
                                                 @RequestParam int page,
                                                 @RequestParam int size) {
        return bookService.searchBooks(keyword, page, size);
    }



}
