package com.sb.services.common.entity;

import com.sb.services.common.enums.ProductTypeEnum;

import javax.persistence.*;

@Entity
@Table(name= "PRODUCT" )
public class Product extends SBBase{
    protected String description;
    protected String name ;
    protected ProductTypeEnum productType ;
    protected int unitPrice;
    protected int unitInStock;
    protected long createdTime;
    protected long lastModified;
    protected boolean discontinued;

    @OneToOne(cascade= CascadeType.ALL, fetch= FetchType.LAZY, orphanRemoval=true)
    @JoinColumn(unique=true)
    protected Category category;

    @OneToOne(cascade=CascadeType.ALL, fetch=FetchType.LAZY, orphanRemoval=true)
    @JoinColumn(unique=true)
    protected Supplier supplier;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProductTypeEnum getProductType() {
        return productType;
    }

    public void setProductType(ProductTypeEnum productType) {
        this.productType = productType;
    }

    public int getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(int unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getUnitInStock() {
        return unitInStock;
    }

    public void setUnitInStock(int unitInStock) {
        this.unitInStock = unitInStock;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

    public long getLastModified() {
        return lastModified;
    }

    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }

    public boolean isDiscontinued() {
        return discontinued;
    }

    public void setDiscontinued(boolean discontinued) {
        this.discontinued = discontinued;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Product product = (Product) o;

        if (getUnitPrice() != product.getUnitPrice()) return false;
        if (getUnitInStock() != product.getUnitInStock()) return false;
        if (getCreatedTime() != product.getCreatedTime()) return false;
        if (getLastModified() != product.getLastModified()) return false;
        if (isDiscontinued() != product.isDiscontinued()) return false;
        if (getDescription() != null ? !getDescription().equals(product.getDescription()) : product.getDescription() != null)
            return false;
        if (getName() != null ? !getName().equals(product.getName()) : product.getName() != null) return false;
        if (getProductType() != product.getProductType()) return false;
        if (getCategory() != null ? !getCategory().equals(product.getCategory()) : product.getCategory() != null)
            return false;
        return getSupplier() != null ? getSupplier().equals(product.getSupplier()) : product.getSupplier() == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getDescription() != null ? getDescription().hashCode() : 0);
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        result = 31 * result + (getProductType() != null ? getProductType().hashCode() : 0);
        result = 31 * result + getUnitPrice();
        result = 31 * result + getUnitInStock();
        result = 31 * result + (int) (getCreatedTime() ^ (getCreatedTime() >>> 32));
        result = 31 * result + (int) (getLastModified() ^ (getLastModified() >>> 32));
        result = 31 * result + (isDiscontinued() ? 1 : 0);
        result = 31 * result + (getCategory() != null ? getCategory().hashCode() : 0);
        result = 31 * result + (getSupplier() != null ? getSupplier().hashCode() : 0);
        return result;
    }
}
