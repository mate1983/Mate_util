package com.easymesoft.util.security;

import java.io.Serializable;
import java.util.List;

public abstract class AbstractSecurityContext implements SecurityContext ,Serializable {
    private String funcId=null;
    
    public String getCurrentFunc() {
        return funcId;
    }
    public void setCurrentFunc(String funcId) {
        this.funcId=funcId;
    }    
    public boolean checkPermission() {
        return checkPermission(getCurrentFunc());
    }
    public boolean checkPermission(Brand brand) {
        return checkPermission(getCurrentFunc(),brand);
    }
    public List getPermissionBrand() {
        return getPermissionBrand(getCurrentFunc());
    }    
    public boolean addPermission(String funcId, Brand brand) {
        return false;
    }
    public boolean addPermission(String funcId) {
        return false;
    }
    public boolean removePermission(String funcId, Brand brand) {
        return false;
    }
    public boolean removePermission(String funcId) {
        return false;
    }
}
