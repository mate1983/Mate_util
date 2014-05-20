package com.easymesoft.util.security;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ObjectUtils;

public class DefaultSecurityContext extends AbstractSecurityContext {

	private static final long serialVersionUID = -6548663642643394381L;
	// 功能点对应的品牌
	private Map<String, List<Brand>> functionBrand = new HashMap<String, List<Brand>>();

	public DefaultSecurityContext(Long userId) {
		this.functionBrand = new HashMap<String, List<Brand>>();
		// this.build(userId, content);
	}

	public boolean checkPermission(String funcId) {
		if (functionBrand.get(funcId) != null) {
			return true;
		} else {
			return false;
		}
	}

	public boolean checkPermission(String funcId, Brand brand) {
		List<Brand> funcBrand = functionBrand.get(funcId);
		if (funcBrand == null || funcBrand.size() == 0)
			return false;
		else {
			for (int i = 0; i < funcBrand.size(); i++)
				if (funcBrand.get(i).equalsOrSuperior(brand))
					return true;
			return false;
		}
	}
/*
	*//**
	 * 增加构建方法，从角色表和用户功能表两个地方读取功能点
	 * 
	 * @param sysUserId
	 * @param content
	 *//*
	private void build(Long sysUserId, ServletContext content) {
		ApplicationContext ctx = WebApplicationContextUtils
				.getRequiredWebApplicationContext(content);
		Map<String, List<Brand>> functionBrand = new HashMap<String, List<Brand>>();
		ISysUserFunctionService sysUserFunctionService = (ISysUserFunctionService) ctx
				.getBean("sysUserFunctionService");
		// 将数据库数据加入到content中
		List<Map> dataList = sysUserFunctionService
				.selectOperProvinceCity(sysUserId);
		if (dataList != null && dataList.size() > 0) {
			String funcId;
			Map map = null;
			List<Brand> brandList = null;
			for (int i = 0; i < dataList.size(); i++) {
				map = (Map) dataList.get(i);
				funcId = map.get("SYS_OPER_ITEM_ID").toString();
				if (functionBrand.get(funcId) == null) {
					brandList = new ArrayList<Brand>();
					functionBrand.put(funcId, brandList);
				}
				functionBrand.get(funcId).add(
						new Brand(ObjectUtils.toString(map.get("PROVINCE_ID")),
								ObjectUtils.toString(map.get("CITY_ID"))));
			}
		}
		this.functionBrand = functionBrand;
	}
*/
	public boolean addPermission(String funcId,
			List<Map<String, Object>> dataList) {

		try {

			List<Brand> brandList = null;
			Map<String, Object> map = null;
			for (int i = 0; i < dataList.size(); i++) {
				map = dataList.get(i);
				brandList = new ArrayList<Brand>();
				if (this.functionBrand.get(funcId) == null) {
					brandList = new ArrayList<Brand>();
					this.functionBrand.put(funcId, brandList);
				}
				this.functionBrand.get(funcId).add(
						new Brand(ObjectUtils.toString(map.get("PROVINCE_ID")),
								ObjectUtils.toString(map.get("CITY_ID"))));

			}
			return true;
		} catch (Exception e) {
			return false;
		}

	}
	
	public boolean addNoPermission(String funcId) {

		try {

			List<Brand> brandList = new ArrayList<Brand>();
			this.functionBrand.put(funcId, brandList);
			return true;
		} catch (Exception e) {
			return false;
		}

	}

	public List<Brand> getPermissionBrand(String funcId) {
		return functionBrand.get(funcId);
	}

	public void setFunctionBrand(Map<String, List<Brand>> functionBrand) {
		this.functionBrand = functionBrand;
	}
}
