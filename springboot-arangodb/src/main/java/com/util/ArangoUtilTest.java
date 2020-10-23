package com.util;

import org.junit.Test;

import com.arangodb.entity.BaseDocument;

/***Created by moyongzhuo
 *On 2017/10/31  ***16:58.
 ******/
public class ArangoUtilTest {
    ArangoUtil arangoUtil = new ArangoUtil();
    @Test
    public void testConnection() {
        arangoUtil.initArango("127.0.0.1", 8529, "root", "root");
        //判断数据库是否已经存在，不存在就新创建
        arangoUtil.initDbs("AQLTest");
        arangoUtil.initDbs("Damo");
        arangoUtil.initDbs("Da");
        BaseDocument document = new BaseDocument();
        document.addAttribute("id", 01);
        document.addAttribute("name", "Damo");
        document.addAttribute("tag", "10");
       // ArangoUtil.insert(document);
    }
}