package cn.dustray.webfilter;

import java.util.List;

public class FilterHelper {

    public void getAllKeyword() {

    }

    public void deleteKeyword() {
        addKeywordToTempCreateOrDeleteTable();//删除或添加一条关键词时向临时表添加消息
    }


    public void addKeywordToTempCreateOrDeleteTable() {

    }

    public void syncKeyword() {
        List<KeywordEntity> list = getKeywordFromTempCreateOrDeleteTable();
        for(KeywordEntity entity:list){

        }
    }

    public List<KeywordEntity> getKeywordFromTempCreateOrDeleteTable() {
        List<KeywordEntity> list=null;
        return list;
    }
}
