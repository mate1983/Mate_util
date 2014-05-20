package com.easymesoft.util.sql;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ResultSetMapBuilder implements ResultSetObjectBuilder {
    public Object buildObj(ResultSet rs, Class objType) throws SQLException {
        Map map=null;
        if (objType==HashMap.class) {
            map=new HashMap();
            ResultSetMetaData meta=rs.getMetaData();
            String value=null;
            for (int i=0;i<meta.getColumnCount();i++) {
            	if("BLOB".equalsIgnoreCase(meta.getColumnTypeName(i+1))){
            		if(rs.getBytes(i+1)!=null){
            			value=new String(rs.getBytes(i+1));
            			
            		}
            		map.put(meta.getColumnName(i+1),value);
            		continue;
            	}
                map.put(meta.getColumnName(i+1),rs.getString(i+1));
            }
        }
        return map;        
    }

}
