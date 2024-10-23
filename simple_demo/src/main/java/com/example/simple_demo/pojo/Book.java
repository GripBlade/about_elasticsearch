package com.example.simple_demo.pojo;


import lombok.Data;

import com.baomidou.mybatisplus.annotation.TableName;

@Data
@TableName("books")
public class Book {
    private Integer id;
    private String title;
    private Double price;
    private String author;
    // 省略其他字段和getter/setter方法
}
