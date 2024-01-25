package com.techelevator.tenmo.dao;
import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

    @Component
    public class jdbcAccountDao implements AccountDao {
        private final JdbcTemplate jdbcTemplate;

        public jdbcAccountDao(JdbcTemplate jdbcTemplate) {
            this.jdbcTemplate = jdbcTemplate;
        }

        @Override
        public Account getAccountByUserId(int user_id){
            Account account = null;
            String sql = "SELECT account_id, user_id, balance FROM account WHERE user_id = ?";
            try {
                SqlRowSet results = jdbcTemplate.queryForRowSet(sql, user_id);
                if (results.next()) {
                    account = mapRowToAccount(results);
                }
            } catch (CannotGetJdbcConnectionException e) {
                throw new DaoException("Unable to connect to server or database", e);
            }
            return account;
        }
        public int getAccountIdByUserId(int userId) {
            String sql = "SELECT account_id FROM account WHERE user_id = ?";
            try {
                return jdbcTemplate.queryForObject(sql, Integer.class, userId);
            } catch (EmptyResultDataAccessException e) {
                return -1;
            }
        }
        private Account mapRowToAccount(SqlRowSet rs) {
            Account account = new Account();
            account.setAccount_id(rs.getInt("account_id"));
            account.setUser_id(rs.getInt("user_id"));
            account.setBalance(rs.getBigDecimal("balance"));

            return account;
        }

    }


