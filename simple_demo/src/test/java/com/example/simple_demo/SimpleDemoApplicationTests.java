package com.example.simple_demo;


import com.example.simple_demo.pojo.Book;
import com.example.simple_demo.service.BookService;
import net.sf.jsqlparser.statement.create.index.CreateIndex;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

@SpringBootTest
class SimpleDemoApplicationTests {

    @Autowired
    private RestHighLevelClient client;

    @Autowired
    private BookService bookService;


    @Test
    void contextLoads() throws IOException {

        //创建索引
        CreateIndexRequest request = new CreateIndexRequest("book");
        // 执行请求
        CreateIndexResponse createIndexResponse = client.indices().create(request, RequestOptions.DEFAULT);
        System.out.println(createIndexResponse.toString());


    }

    @Test
    void testSaveBookFunc(){
        List<Book> allBooks = bookService.getAllBooks();
        System.out.println(bookService.saveBook(allBooks));

    }

}
