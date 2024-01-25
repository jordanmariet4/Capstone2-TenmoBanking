package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.math.BigDecimal;

public interface AccountDao {
    Account getAccountByUserId(int user_id);
    public int getAccountIdByUserId(int userId);

}
