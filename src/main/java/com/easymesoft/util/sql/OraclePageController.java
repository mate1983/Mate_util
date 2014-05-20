package com.easymesoft.util.sql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class OraclePageController extends PageController {
    private static final Log log = LogFactory.getLog(OraclePageController.class);
        
    public static String buildPageSql(String sql,long fromRowNum,long toRowNum) {
        StringBuffer sbNewSql=new StringBuffer();

        sbNewSql.append("select x.*");
        sbNewSql.append(" from (select rownum x_rownum,o.* from (").append(sql).append(") o) x");
       	sbNewSql.append(" where x_rownum>=").append(fromRowNum).append(" and x_rownum<=").append(toRowNum);
       	sbNewSql.append(" order by x.x_rownum");
       	       	
        return sbNewSql.toString();        
    }
    public PageQueryResult doQuery(Statement st,String rowQuery,String countQuery,ResultSetObjectBuilder builder,Class objType) throws SQLException {
        PageQueryResult pr=new PageQueryResult();
        pr.setPageSize(this.getPageSize());
        pr.setPageIndex(this.getPageIndex());
        
        if (this.getPageSize()==-1) {//无需分页
            log.debug(rowQuery);
           	
            ResultSet rsRow=st.executeQuery(rowQuery);
            while (rsRow.next()) {
                pr.addRow(builder.buildObj(rsRow,objType));
            }
            rsRow.close();            
        }
        else {//分页
            long fromRowNum=this.getPageSize()*(this.getPageIndex()-1)+1;
            long toRowNum=this.getPageSize()*this.getPageIndex();
            String pageSql=buildPageSql(rowQuery,fromRowNum,toRowNum);
            
            log.debug(pageSql);
            
            ResultSet rsRow=st.executeQuery(pageSql);
            while (rsRow.next()) {
                pr.addRow(builder.buildObj(rsRow,objType));
            }
            rsRow.close();                        
        }
        if (this.isQueryCount()) {
            if (countQuery==null) {
                countQuery="select count(*) from ("+rowQuery+")";
            }
            
            log.debug(countQuery);
            
            ResultSet rsCount=st.executeQuery(countQuery);
            rsCount.next();
            pr.setRowCount(rsCount.getInt(1));
            rsCount.close();    
        }
                
        return pr;
    }
}
