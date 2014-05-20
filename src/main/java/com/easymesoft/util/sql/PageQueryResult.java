package com.easymesoft.util.sql;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.easymesoft.util.paging.IPageInfo;


public class PageQueryResult implements IPageInfo, Serializable{
    private List rows;
    private int rowCount;
    private int pageSize=-1;
    private int pageIndex=1;
    
    //默认使用ArrayList作为记录容器
    public PageQueryResult() {
        this.rows=new ArrayList();
    }
    //允许指定使用其他List作为记录容器
    public PageQueryResult(List rows) {
        this.rows=rows;
    }    
    public int getRowCount() {
        return this.rowCount;
    }
    public void addRow(Object o) {
        this.rows.add(o);
    }
    public void deleteRow(int index) {
        this.rows.remove(index);
    }
    public void setRowCount(int rowCount) {
        this.rowCount=rowCount;
    }
    public int getPageCount() {
        if (this.pageSize>0) 
            return (int) (this.rowCount-1)/this.pageSize+1;
        else
            return -1;
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
    public List getRows() {
        return rows;
    }	
}
