package com.example.postscategory.common;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Pagination {
	private int page;
	private int pageSize;
	private int rowCount;
	private String searchValues;
	private int category_id;

	public int getPageCount() {
		int pageCount = rowCount / pageSize;
		if (rowCount % pageSize > 0)
			pageCount++;
		return pageCount;
	}

	public List<Integer> helper(int pageInput) {
		List<Integer> listPage = new ArrayList<Integer>();
		int pageCount = getPageCount();
		if (pageInput <= pageCount) {// if page nhập vào <= pageCount
			if (page > 3) {
				if (page > 4 && pageCount > 2) {
					listPage.add(1);
					listPage.add(-1);
				}
				for (int i = page - 3; i <= page && i <= pageCount; i++)
					listPage.add(i);

				for (int i = page + 1; i <= page + 2 && i <= pageCount; i++)
					listPage.add(i);

				if (page + 2 < pageCount)
					listPage.add(-1);
			} else {
				for (int i = 1; i <= 6 && i <= pageCount; i++) {
					listPage.add(i);
				}
				if (pageCount > 6)
					listPage.add(-1);
			}
			return listPage;
		}else {
			return listPage;
		}
	}
}
