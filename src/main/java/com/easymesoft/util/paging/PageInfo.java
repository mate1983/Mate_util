package com.easymesoft.util.paging;

import java.io.Serializable;

public class PageInfo implements Serializable {
	private int pageSize;
	private int pageIndex;
	private boolean queryCount;

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

	public boolean isQueryCount() {
		return queryCount;
	}

	public void setQueryCount(boolean queryCount) {
		this.queryCount = queryCount;
	}

}
