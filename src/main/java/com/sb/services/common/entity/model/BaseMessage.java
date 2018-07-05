package com.sb.services.common.entity.model;

/**
 * @author: prinaray
 * @version $Id$
 */

public class BaseMessage {

    private String msgOpCode;

    private String jobId;

    public String getMsgOpCode() {
        return msgOpCode;
    }

    public void setMsgOpCode(String msgOpCode) {
        this.msgOpCode = msgOpCode;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }
}
