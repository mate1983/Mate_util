package com.easymesoft.util.sql;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

abstract public class PageController {
    private int pageSize=-1;//默认不分页
    private int pageIndex=1;//默认定位在第一页
    private boolean queryCount=true;//默认计数
    
    public PageController() {
    }    
    public PageController(int pageSize) {
        this.pageSize=pageSize;
    }
    public PageController(int pageSize,boolean queryCount) {
        this.pageSize=pageSize;
        this.queryCount=queryCount;
    }    
    public int getPageIndex() {
        return pageIndex;
    }
    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }
    public int getPageSize() {
        return pageSize;
    }
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
    public boolean isQueryCount() {
        return queryCount;
    }
    public void setQueryCount(boolean queryCount) {
        this.queryCount = queryCount;
    }
    
    abstract public PageQueryResult doQuery(Statement st,String rowQuery,String countQuery,ResultSetObjectBuilder builder,Class objType) throws SQLException;
        
    public PageQueryResult doQuery(Statement st,String rowQuery,String countQuery) throws SQLException {        
        return this.doQuery(st,rowQuery,countQuery,new ResultSetMapBuilder(),HashMap.class);
    }
}
