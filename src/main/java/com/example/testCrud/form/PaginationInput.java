package com.example.testCrud.form;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class  PaginationInput {
	private int page;
	private int pageSize;
	private int rowCount;
	private String searchValues;
	private int category_id;
	public int getPageCount() {
		int pageCount = rowCount/pageSize;
		if(rowCount % pageSize >0)
			pageCount++;
		return pageCount;
	}
}
