package com.voddemo;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class test2 {
    @Test
    public void test(){
        List<String> list = new ArrayList<>();
        list.add("111");
        list.add("222");
        System.out.println("list = " + list);
    }
}
