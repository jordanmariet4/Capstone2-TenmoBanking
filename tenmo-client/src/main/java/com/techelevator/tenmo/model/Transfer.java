package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Transfer {

    private int transfer_id;

    private int transfer_type_id;

    private int transfer_status_id;

    private int account_from;

    private int account_to;

    private BigDecimal amount;
    private String username;
    private int userId;
    private String transfer_type_desc;
    private String transfer_status_desc;
    private String from_username;
    private String to_username;

    public String getTransfer_type_desc() {
        return transfer_type_desc;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setTransfer_type_desc(String transfer_type_desc) {
        this.transfer_type_desc = transfer_type_desc;
    }

    public String getTransfer_status_desc() {
        return transfer_status_desc;
    }

    public void setTransfer_status_desc(String transfer_status_desc) {
        this.transfer_status_desc = transfer_status_desc;
    }

    public String getFrom_username() {
        return from_username;
    }

    public void setFrom_username(String from_username) {
        this.from_username = from_username;
    }

    public String getTo_username() {
        return to_username;
    }

    public void setTo_username(String to_username) {
        this.to_username = to_username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getTransfer_id() {
        return transfer_id;
    }

    public void setTransfer_id(int transfer_id) {
        this.transfer_id = transfer_id;
    }

    public int getTransfer_type_id() {
        return transfer_type_id;
    }

    public void setTransfer_type_id(int transfer_type_id) {
        this.transfer_type_id = transfer_type_id;
    }

    public int getTransfer_status_id() {
        return transfer_status_id;
    }

    public void setTransfer_status_id(int transfer_status_id) {
        this.transfer_status_id = transfer_status_id;
    }

    public int getAccount_from() {
        return account_from;
    }

    public void setAccount_from(int account_from) {
        this.account_from = account_from;
    }

    public int getAccount_to() {
        return account_to;
    }

    public void setAccount_to(int account_to) {
        this.account_to = account_to;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
