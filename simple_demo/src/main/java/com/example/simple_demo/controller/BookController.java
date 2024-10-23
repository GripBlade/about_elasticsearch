package com.example.simple_demo.controller;

import com.example.simple_demo.pojo.Book;
import com.example.simple_demo.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
}
