package com.yugabyte.ybactiveactiveautoswitchdemo.model;

import java.sql.Timestamp;

public class Transaction {
    private Long transactionId;
    private String fromAcct;
    private String toAcct;
    private String fromRoute;
    private String toRoute;
    private Integer amount;
    private Timestamp timestamp;

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public String getFromAcct() {
        return fromAcct;
    }

    public void setFromAcct(String fromAcct) {
        this.fromAcct = fromAcct;
    }

    public String getToAcct() {
        return toAcct;
    }

    public void setToAcct(String toAcct) {
        this.toAcct = toAcct;
    }

    public String getFromRoute() {
        return fromRoute;
    }

    public void setFromRoute(String fromRoute) {
        this.fromRoute = fromRoute;
    }

    public String getToRoute() {
        return toRoute;
    }

    public void setToRoute(String toRoute) {
        this.toRoute = toRoute;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }



}
