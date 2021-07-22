package com.yang.pojo;

import javax.xml.crypto.Data;
import java.util.Date;

@lombok.Data
public class Blog {
    private String  id;
    private String title;
    private String author;
    private Date createTime;    //属性名和字段名不一致
    private int views;
}
