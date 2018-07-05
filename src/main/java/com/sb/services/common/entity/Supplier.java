package com.sb.services.common.entity;

import com.sb.services.common.enums.CountryEnum;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "SUPPLIER")
public class Supplier extends SBBase{

    protected String description ;
    protected String name ;
    protected long createdTime ;
    protected long lastModified ;
    protected String companyName;
    protected String ontactName;
    protected String address;
    protected String postalCode;
    protected CountryEnum country ;
    protected String phone ;

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

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getOntactName() {
        return ontactName;
    }

    public void setOntactName(String ontactName) {
        this.ontactName = ontactName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public CountryEnum getCountry() {
        return country;
    }

    public void setCountry(CountryEnum country) {
        this.country = country;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
