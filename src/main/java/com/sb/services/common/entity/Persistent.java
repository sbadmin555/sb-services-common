package com.sb.services.common.entity;

import java.util.Date;

/**
 * Super class of all database persistent class (i.e. Hibernate entities) which
 * enforce the contract of dateAdded and dateModified through Hibernate callback.
 * @author Zhongling Li
 * @version $Id$
 */
public interface Persistent {
    String DATE_ADDED = "dateAdded";
    String DATE_MODIFIED = "dateModified";

    Date getDateAdded();
    Date getDateModified();
    void setDateAdded(Date date);
    void setDateModified(Date date);
}
