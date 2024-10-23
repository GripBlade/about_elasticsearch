package com.example.simple_demo.service;
import com.example.simple_demo.pojo.Book;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @program: about_elasticsearch
 * @description: BookService
 * @author: henryDai
 * @create: 2024-10-22 23:06
 **/
public interface BookService  {
    // 可以定义一些自定义的服务方法
    List<Book> getAllBooks();


   /**
   * @Description: 保存书籍
   * @Param:
   * @return:
   * @Author: henryDai
   * @Date: 2024/10/23
   */
    boolean saveBook(List<Book> books);


    List<Map<String, Object>> searchBooks(String keyword,int page,int size);

}
