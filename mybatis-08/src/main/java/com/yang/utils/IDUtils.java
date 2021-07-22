package com.yang.utils;

import org.junit.Test;

import java.util.UUID;

public class IDUtils {
    public static String getid(){
        return UUID.randomUUID().toString().replaceAll("-"," ");

    }
    @Test
    public void test(){
        System.out.println(IDUtils.getid());
        System.out.println(IDUtils.getid());
        System.out.println(IDUtils.getid());
    }
}
