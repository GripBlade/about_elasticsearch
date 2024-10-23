package com.example.simple_demo.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.simple_demo.mapper.BookMapper;
import com.example.simple_demo.pojo.Book;
import com.example.simple_demo.service.BookService;
import org.apache.lucene.util.QueryBuilder;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.core.TimeValue;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.naming.directory.SearchResult;
import javax.swing.text.Highlighter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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
     * @Description: 保存至es索引中
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

    /**
     * @Description: 搜索功能
     * @Param: [java.lang.String, int, int]
     * @return: java.util.List<java.util.Map < java.lang.String, java.lang.Object>>
     * @Author: henryDai
     * @Date: 2024/10/23
     */
    @Override
    public List<Map<String, Object>> searchBooks(String keyword, int page, int size) {
        List<Map<String, Object>> resultList = new ArrayList<>();

        if (page < 1) {
            page = 1;
        }

        //条件搜索
        SearchRequest searchRequest = new SearchRequest("book");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        // 分页
        sourceBuilder.from(page);
        sourceBuilder.size(size);

        // 条件匹配
        TermQueryBuilder titleQuery = QueryBuilders.termQuery("title", keyword);
        TermQueryBuilder authorQuery = QueryBuilders.termQuery("author", keyword);
        sourceBuilder.query(QueryBuilders.boolQuery().must(titleQuery).should(authorQuery)).timeout(new TimeValue(60, TimeUnit.SECONDS));

        // 执行搜索
        searchRequest.source(sourceBuilder);

        try {
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            // 封装结果
            for (SearchHit searchHit : searchResponse.getHits().getHits()) {
                Map<String, Object> sourceMap = searchHit.getSourceAsMap();

                // 获取高亮字段
                Map<String, HighlightField> highlightFields = searchHit.getHighlightFields();
                if (highlightFields != null) {
                    if (highlightFields.containsKey("title")) {
                        sourceMap.put("title", highlightFields.get("title").fragments()[0].string());
                    }
                    if (highlightFields.containsKey("author")) {
                        sourceMap.put("author", highlightFields.get("author").fragments()[0].string());
                    }
                }

                resultList.add(sourceMap);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return resultList;
    }
}
