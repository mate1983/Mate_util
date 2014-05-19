package com.easymesoft.util;

import java.lang.reflect.InvocationTargetException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.session.Configuration;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.jdbc.support.JdbcUtils;

import com.besttone.zcx.util.security.SecurityContext;
import com.besttone.zcx.util.security.SecurityContextManager;
import com.besttone.zcx.util.security.SqlBrandLimit;
import com.besttone.zcx.util.sql.MyBatisParameterToList;
import com.besttone.zcx.util.sql.OraclePageController;
import com.besttone.zcx.util.sql.OraclePageControllerSqlInjection;
import com.besttone.zcx.util.sql.PageController;
import com.besttone.zcx.util.sql.PageQueryResult;
import com.besttone.zcx.util.sql.ResultSetMapBuilder;

public class DaoUtil extends SqlSessionDaoSupport {
	protected final static Log logger = LogFactory.getLog(DaoUtil.class);
	public static final int QUERY_MAX_ROWS = 20000;
	private JdbcTemplate jdbcTemplate;

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	/**
	 * 检查分页信息，如果不分页，则限制查询条数
	 * 
	 * @param pageInfo
	 */
	private void checkPageInfo(PageInfo pageInfo) {
		if (null != pageInfo) {
			if (-1 == pageInfo.getPageSize()
					|| pageInfo.getPageSize() > QUERY_MAX_ROWS) {
				pageInfo.setPageSize(QUERY_MAX_ROWS);
			}
			if (pageInfo.getPageIndex() <= 0) {
				pageInfo.setPageIndex(1);
			}
		}

	}

	/**
	 * 带分页jdbc查询
	 * 
	 * @param sql
	 * @param current_page
	 * @param length
	 * @return
	 * @throws Exception
	 * @throws SQLException
	 */
	public PageQueryResult doSqlFindPage(String sql, PageInfo pageInfo)
			throws SQLException, Exception {
		PageController opc = new OraclePageController();
		opc.setPageSize(pageInfo.getPageSize());
		opc.setPageIndex(pageInfo.getPageIndex());
		opc.setQueryCount(pageInfo.isQueryCount());

		// 查询
		Connection conn = getJdbcTemplate().getDataSource().getConnection();
		Statement st = conn.createStatement();
		logger.info(sql);
		PageQueryResult pageResult = opc.doQuery(st, sql, null);
		JdbcUtils.closeStatement(st);
		JdbcUtils.closeConnection(conn);

		return pageResult;
	}

	/**
	 * 带分页jdbc查询
	 * 
	 * @param sql
	 * @param current_page
	 * @param length
	 * @return
	 * @throws Exception
	 * @throws SQLException
	 */
	public PageQueryResult doSqlFindPage(String sql, List paramsList,
			PageInfo pageInfo) throws SQLException, Exception {
		checkPageInfo(pageInfo);
		if (paramsList == null || paramsList.size() == 0) {
			return doSqlFindPage(sql, pageInfo);
		} else {
			OraclePageControllerSqlInjection opc = new OraclePageControllerSqlInjection();
			opc.setPageSize(pageInfo.getPageSize());
			opc.setPageIndex(pageInfo.getPageIndex());
			opc.setQueryCount(pageInfo.isQueryCount());
			// 查询
			Connection conn = getJdbcTemplate().getDataSource().getConnection();
			logger.info(sql);
			logger.info(paramsList);
			PageQueryResult result = opc.doQuery(conn, paramsList, sql,
					new ResultSetMapBuilder(), HashMap.class);
			JdbcUtils.closeConnection(conn);

			return result;
		}
	}

	/**
	 * 分页查询
	 * 
	 * @param statement
	 * @param parameter
	 * @param pageInfo
	 * @return
	 * @throws Exception
	 */
	public PageQueryResult doMyBatisPageQuery(Class mapperClass,
			String selectName, Object parameter, PageInfo pageInfo)
			throws Exception {
		checkPageInfo(pageInfo);
		// 准备参数
		Configuration configuration = super.getSqlSession().getConfiguration();
		// SqlSessionTemplate temp = (SqlSessionTemplate)getSqlSession();
		// System.out.println("DaoUtil:"+getSqlSession().hashCode()+":"+temp.getExecutorType());
		MappedStatement ms = configuration.getMappedStatement(mapperClass
				.getName() + "." + selectName);

		BoundSql boundSql = ms.getBoundSql(parameter);
		MyBatisParameterToList parameterToList = new MyBatisParameterToList(ms,
				parameter, boundSql);
		List<SqlParameterValue> params = parameterToList.getList();
		// 查询
		return doSqlFindPage(boundSql.getSql(), params, pageInfo);
	}

	public String genSecuritySqlCondition() {
		SecurityContext ctx = SecurityContextManager.getContext();
		if (ctx != null)
			return SqlBrandLimit.getSql(ctx.getPermissionBrand());
		else
			return null;
	}

	public String genSecuritySqlCondition(String provinceColName,
			String cityColName) {
		SecurityContext ctx = SecurityContextManager.getContext();
		if (ctx != null)
			return SqlBrandLimit.getSql(provinceColName, cityColName,
					ctx.getPermissionBrand());
		else
			return null;
	}

	public void insertBatch(final List paramsList, Class mapperClass,
			String insert, String getId, final String keyName) {

		if (paramsList.size() == 0)
			return;

		Configuration configuration = getSqlSession().getConfiguration();
		// final SqlSession session =super.getDaoUtil().getSqlSession();
		MappedStatement ms = configuration.getMappedStatement(mapperClass
				.getName() + "." + insert);

		MappedStatement getIdms = configuration.getMappedStatement(mapperClass
				.getName() + "." + getId);

		final BoundSql boundSql = ms.getBoundSql(paramsList.get(0));
		final BoundSql getIdSql = getIdms.getBoundSql(null);
		final String sql = boundSql.getSql();
		final String idsql = getIdSql.getSql();

		getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps, int i)
					throws SQLException {
				// TODO Auto-generated method stub
				Object vo = paramsList.get(i);
				Long id = getJdbcTemplate().queryForLong(idsql);
				try {
					BeanUtils.setProperty(vo, keyName, id);
				} catch (IllegalAccessException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (InvocationTargetException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				List<ParameterMapping> list = boundSql.getParameterMappings();
				for (int j = 0; j < list.size(); j++) {
					ParameterMapping pm = list.get(j);

					try {
						if (pm.getJdbcType().name().equals("DATE")) {
							if (BeanUtils.getProperty(vo, pm.getProperty()) != null) {

								SimpleDateFormat sf = new SimpleDateFormat(
										"EEE MMM dd HH:mm:ss z yyyy", Locale.US);

								Date d = sf.parse(BeanUtils.getProperty(vo,
										pm.getProperty()));

								java.sql.Date dt = new java.sql.Date(d
										.getTime());

								ps.setObject(j + 1, dt,
										pm.getJdbcType().TYPE_CODE);
							} else {
								ps.setObject(j + 1, null);
							}

						} else {
							ps.setObject(
									j + 1,
									BeanUtils.getProperty(vo, pm.getProperty()),
									pm.getJdbcType().TYPE_CODE);
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}

			@Override
			public int getBatchSize() {
				// TODO Auto-generated method stub
				return paramsList.size();
			}
		});
	}

	public void runProcedure(String sql) throws Exception {
		// 查询
		Connection conn = getJdbcTemplate().getDataSource().getConnection();
		CallableStatement c = conn.prepareCall(sql);
		// 执行存储过程
		c.execute();

		JdbcUtils.closeConnection(conn);
	}

}
