package com.sb.services.common.entity.filter;

import com.sb.services.common.enums.ProductTypeEnum;

public class ProductFilter extends BaseFilter{

    private String byProductId;
    private String byName;
    private ProductTypeEnum byProductType;

    public String getByProductId() {
        return byProductId;
    }

    public void setByProductId(String byProductId) {
        this.byProductId = byProductId;
    }

    public String getByName() {
        return byName;
    }

    public void setByName(String byName) {
        this.byName = byName;
    }

    public ProductTypeEnum getByProductType() {
        return byProductType;
    }

    public void setByProductType(ProductTypeEnum byProductType) {
        this.byProductType = byProductType;
    }

    private ProductSortCriteria sortCriteria;

    public ProductSortCriteria getSortCriteria() {
        if(null == sortCriteria) {
            sortCriteria = new ProductSortCriteria();
        }
        return sortCriteria;
    }


    public void setSortCriteria(ProductSortCriteria sortCriteria) {
        this.sortCriteria = sortCriteria;
    }

    @Override
    public String toString() {
        return "ProductFilter{" +
                "byProductId='" + byProductId + '\'' +
                ", byName='" + byName + '\'' +
                ", byProductType=" + byProductType +
                ", sortCriteria=" + sortCriteria +
                '}';
    }
}
