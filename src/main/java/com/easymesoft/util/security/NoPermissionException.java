package com.easymesoft.util.security;

public class NoPermissionException extends RuntimeException {
    private String funcId;
    private Brand brand;
    
    public NoPermissionException(String funcId) {
        this.funcId=funcId;
        this.brand=null;
    }
    public NoPermissionException(String funcId,Brand brand) {
        this.funcId=funcId;
        this.brand=brand;
    }
    public Brand getBrand() {
        return brand;
    }
    public void setBrand(Brand brand) {
        this.brand = brand;
    }
    public String getFuncId() {
        return funcId;
    }
    public void setFuncId(String funcId) {
        this.funcId = funcId;
    }
}
