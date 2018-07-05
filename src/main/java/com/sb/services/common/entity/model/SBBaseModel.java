package com.sb.services.common.entity.model;

public class SBBaseModel {

    protected String uid;

    final public String getUid() {
        return uid;
    }
    final public void setUid(String uid) {
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

        if (!(o instanceof SBBaseModel))
            return false;

        if (o == this)
            return true;

        if (((SBBaseModel)o).uid == null) {
            return false;
        }
        return ((SBBaseModel)o).uid.equals(uid);
    }
}
