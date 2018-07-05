package com.sb.services.common.util;


import java.util.ArrayList;
import java.util.List;

import com.sb.services.common.generator.ModelMapperGenerator;
import com.sb.services.common.entity.model.Page;

import org.springframework.util.CollectionUtils;


public class ModelConvertor {

	public static <S, D> Page<D> convertPage(Page<S> sourcePage, Class<D> destClass) {
		Page<D> destPage = new Page<D>();
		List<D> destItems = new ArrayList<D>();
		destPage.setTotalRows(sourcePage.getTotalRows());
		destPage.setNextPageExists(sourcePage.isNextPageExists());
		destPage.setPreviousPageExists(sourcePage.isPreviousPageExists());
		if (!CollectionUtils.isEmpty(sourcePage.getItems())) {
			for (S srcItem : sourcePage.getItems()) {
				destItems.add(ModelMapperGenerator.getMapper().map(srcItem, destClass));
			}
			destPage.setItems(destItems);
		}
		return destPage;
	}
	
	public static <S, D> D convertObject(Object entity, Class<D> model) {
    	return ModelMapperGenerator.getMapper().map(entity, model);
	}
	
	
}
