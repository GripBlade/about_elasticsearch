package com.example.simple_demo.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.simple_demo.mapper.BookMapper;
import com.example.simple_demo.pojo.Book;
import com.example.simple_demo.service.BookService;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * @program: about_elasticsearch
 * @description: BookServiceImpl
 * @author: henryDai
 * @create: 2024-10-22 23:07
 **/

@Service
public class BookServiceImpl extends ServiceImpl<BookMapper, Book> implements BookService {

    @Autowired
    private RestHighLevelClient client;

    @Override
    public List<Book> getAllBooks() {
        return list(); // 使用 MyBatis-Plus 提供的 list() 方法获取所有记录
    }


    /**
     * @Description: 保存书籍
     * @Param: [java.util.List<com.example.simple_demo.pojo.Book>]
     * @return: boolean
     * @Author: henryDai
     * @Date: 2024/10/23
     */
    @Override
    public boolean saveBook(List<Book> books) {
        // 将数据插入es索引中
        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.timeout("2m");
        for (Book book : books) {

            bulkRequest.add(new IndexRequest("book").source(JSON.toJSONString(book), XContentType.JSON));
        }
        try {
            client.bulk(bulkRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            return false;
        }


        return true;
    }
}
