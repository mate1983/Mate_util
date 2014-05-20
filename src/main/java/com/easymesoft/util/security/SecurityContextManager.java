package com.easymesoft.util.security;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SecurityContextManager {
	protected final static Log logger = LogFactory.getLog(SecurityContextManager.class); 
    private static ThreadLocal local=new ThreadLocal();
    
    public static SecurityContext getContext() {
        return (SecurityContext) local.get();
    }
    public static void setContext(SecurityContext ctx) {
        local.set(ctx);
        logger.info("SecurityContextManager.current Thread Id:" + Thread.currentThread().getId());
    }
}
