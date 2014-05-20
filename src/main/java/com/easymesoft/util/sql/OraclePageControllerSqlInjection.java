package com.easymesoft.util.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.support.JdbcUtils;


public class OraclePageControllerSqlInjection extends OraclePageController {
	private static final Log log = LogFactory.getLog(OraclePageControllerSqlInjection.class);

	public PageQueryResult doQuery(Connection conn,List paramsList,String rowQuery,ResultSetObjectBuilder builder,Class objType) throws SQLException {
        PageQueryResult pr=new PageQueryResult();
        pr.setPageSize(this.getPageSize());
        pr.setPageIndex(this.getPageIndex());
        PreparedStatementSetter pss = this.buildStatementSetter(paramsList);
        if (this.getPageSize()==-1) {//无需分页
        	log.debug(rowQuery);
        	
        	queryRowsAndBuildResult(conn, paramsList, rowQuery, builder,objType, pr,pss);
        }
        else {//分页        	
        	 long fromRowNum=this.getPageSize()*(this.getPageIndex()-1)+1;
             long toRowNum=this.getPageSize()*this.getPageIndex();
             String pageSql=buildPageSql(rowQuery,fromRowNum,toRowNum);
             log.debug(pageSql);
             
             queryRowsAndBuildResult(conn, paramsList, pageSql, builder,objType, pr,pss);
        }
        
        if (this.isQueryCount()) {
            String countQuery="select count(*) from ("+rowQuery+")";            
            log.debug(countQuery);
            
            PreparedStatement pst =conn.prepareStatement(countQuery);	
            if(pss != null)
    			pss.setValues(pst);	         
            ResultSet rsCount=pst.executeQuery();
            rsCount.next();
            pr.setRowCount(rsCount.getInt(1));
            coloseResultSetAndStatement(pst, rsCount);           
        }               
        
        return pr;
    }
	

	/**
	 * 查询数据行且组建返回结果
	 * @param conn
	 * @param paramsList
	 * @param rowQuery
	 * @param builder
	 * @param objType
	 * @param pr
	 * @param pss
	 * @throws SQLException
	 */
	private void queryRowsAndBuildResult(Connection conn, List paramsList,
			String rowQuery, ResultSetObjectBuilder builder, Class objType,
			PageQueryResult pr,PreparedStatementSetter pss) throws SQLException {	
		PreparedStatement pst =conn.prepareStatement(rowQuery);		
		if(pss != null)
			pss.setValues(pst);	        	              	
		ResultSet rsRow=pst.executeQuery();
		while (rsRow.next()) {
		       pr.addRow(builder.buildObj(rsRow,objType));
		}
		coloseResultSetAndStatement(pst, rsRow);
	}

	/**
	 * 创建Setter器
	 * @param paramsList
	 * @param pss
	 * @return
	 */
	private PreparedStatementSetter buildStatementSetter(List paramsList) {
		PreparedStatementSetter pss = null;
		if(paramsList != null && paramsList.size() >0)
			pss = ((PreparedStatementSetter) (new ArgPreparedStatementSetter(paramsList.toArray(new Object[paramsList.size()]))));
		else 
			return null;
		return pss;
	}

	/**
	 * 关闭PreparedStatement和ResultSet
	 * @param pst
	 * @param rsRow
	 */
	private void coloseResultSetAndStatement(PreparedStatement pst,
			ResultSet rsRow) {
		JdbcUtils.closeResultSet(rsRow);
		JdbcUtils.closeStatement(pst);
	}	
}
