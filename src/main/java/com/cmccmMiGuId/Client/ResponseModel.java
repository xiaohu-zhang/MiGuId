package com.cmccmMiGuId.Client;

import java.util.concurrent.CountDownLatch;

public class ResponseModel {
    private CountDownLatch latch = new CountDownLatch(1);
    private String resultId;
    private long clientId;
    public CountDownLatch getLatch() {
        return latch;
    }
    public void setLatch(CountDownLatch latch) {
        this.latch = latch;
    }
    public String getResultId() {
        return resultId;
    }
    public void setResultId(String resultId) {
        this.resultId = resultId;
    }
    public long getClientId() {
        return clientId;
    }
    public void setClientId(long clientId) {
        this.clientId = clientId;
    }

    
    
    
    
}
