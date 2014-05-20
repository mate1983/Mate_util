package com.easymesoft.util.security;

import java.util.List;
import java.util.Map;

public interface SecurityContext {
    //功能点接口
    public String getCurrentFunc();
    public void setCurrentFunc(String funcId);
    
    //权限检查接口
    public boolean checkPermission(String funcId);
    public boolean checkPermission(String funcId,Brand brand);
    public List<Brand> getPermissionBrand(String funcId);
    
    public boolean checkPermission();
    public boolean checkPermission(Brand brand);
    public List<Brand> getPermissionBrand();
    
    //权限增删接口
    public boolean addPermission(String funcId);//添加成功则返回true，如果此权限已经在SecurityContext中，则返回false
    public boolean addPermission(String funcId,Brand brand);
    
    public boolean addPermission(String funcId,List<Map<String, Object>> dataList);//添加成功则返回true，如果此权限已经在SecurityContext中，则返回false
    
    public boolean removePermission(String funcId);//删除成功则返回true，如果此权限不在SecurityContext中，则返回false
    public boolean removePermission(String funcId,Brand brand);
    public boolean addNoPermission(String funcId);
}