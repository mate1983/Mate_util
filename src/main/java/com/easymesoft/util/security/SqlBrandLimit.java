package com.easymesoft.util.security;

import java.util.List;

public class SqlBrandLimit {
	final public static String WHOLE_COUNTRY_ID = "*";
	public static String getSql(String provinceColName, String cityColName,
			List<Brand> brandList) {
		StringBuffer sb = new StringBuffer("(1=0)");

		if (brandList != null) {
			if (WHOLE_COUNTRY_ID.equals(brandList.get(0)
					.getProvinceId())) {
				return null;
			}
			for (int i = 0; i < brandList.size(); i++) {
				Brand brand = brandList.get(i);
				if (brand.getProvinceId() != null
						&& brand.getProvinceId().trim().length() > 0) {
					sb.append(" or (");
					// 省品牌一定存在
					sb.append(provinceColName).append("=")
							.append(brand.getProvinceId());
					// 市品牌不存在，表示可以处理省品牌下面的所有市，可以不加市限制
					if ((brand.getCityId() != null)
							&& !"".equals(brand.getCityId()))
						sb.append(" and ").append(cityColName).append("=")
								.append(brand.getCityId());

					sb.append(")");
				}
			}

			if (brandList.size() > 0)
				return "(" + sb.toString() + ")";
		}

		return sb.toString();
	}

	public static String getSql(List<Brand> brandList) {
		return getSql("province_id", "city_id", brandList);
	}
}
