package com.easymesoft.util.paging;

import java.util.List;

public interface IPageInfo {
    public int getPageSize();
    public void setPageSize(int pageSize);
    
    public int getPageCount();
    //pageCount是计算出来的，因此，没有set方法
    
    public int getPageIndex();
    public void setPageIndex(int pageIndex);
    
    //总行数
    public int getRowCount();
    public void setRowCount(int rowCount);
    
    //当前页上的行
    public List getRows();
    //存放行的List不允许修改，因此，没有set方法
    
    public void addRow(Object o);
    public void deleteRow(int index);
}
