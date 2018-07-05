package com.sb.services.common.entity;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

@MappedSuperclass
public class SBBase implements Serializable{

    @Id
    @Column(name="OBJECTID")
    protected String uid;

    public String getUid() {
        return uid;
    }
    public void setUid(String uid) {
        this.uid = uid;
    }

    @Override
    public int hashCode() {
        if (uid == null)
            return super.hashCode();
        else
            return uid.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }

        if (!(o instanceof SBBase))
            return false;

        if (o == this)
            return true;

        if (((SBBase)o).uid == null) {
            return false;
        }
        return ((SBBase)o).uid.equals(uid);
    }
}
