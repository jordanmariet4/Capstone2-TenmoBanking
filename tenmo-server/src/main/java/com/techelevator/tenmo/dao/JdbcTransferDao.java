package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


//    public Transfer getTransferById(int transfer_id) {
//        Transfer transfer = null;
//        String sql = "SELECT transfer_id, transfer_type_id, transfer_status_id, account_from, " +
//                "account to, amount FROM transfer WHERE transfer_id = ?";
//        try {
//            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transfer_id);
//            if (results.next()) {
//                transfer = mapRowToTransfer(results);
//            }
//        } catch (CannotGetJdbcConnectionException e) {
//            throw new DaoException("Unable to connect to server or database", e);
//        }
//        return transfer;
//
//    }
    @Override
    public void updateBalance(int accountTo, int accountFrom, BigDecimal amount){
        String sql= "UPDATE account SET balance = balance - ? WHERE account_id = ?; " +
                "UPDATE account SET balance = balance + ? WHERE account_id = ?;";
        jdbcTemplate.update(sql, amount, accountFrom, amount, accountTo);
    }


    @Override//took off transactional
    public void insertTransfer(int accountTo, int accountFrom, BigDecimal amount) {
        String sql =
                "INSERT INTO transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount)\n" +
                "VALUES (?, ?, ?, ?, ?);\n" +
                "COMMIT;";
        jdbcTemplate.update(sql,2,2, accountFrom, accountTo, amount);
    }

    @Override
    public List<Transfer> viewTransferHistory(int userId) {
        List<Transfer> transfers = new ArrayList<>();
//        String sql = "SELECT t.transfer_id, tu.username, t.transfer_type_id,t.transfer_status_id, t.account_from, t.account_to, t.amount " +
//                "FROM transfer t " +
//                "JOIN account a ON a.account_id =t.account_from " +
//                "JOIN tenmo_user tu ON tu.user_id = a.user_id " +
//                "WHERE account_to = ? OR account_from = ?;";
        String sql = "SELECT t.transfer_id, " +
                "CASE " +
                "WHEN a_from.user_id = ? THEN 'To: ' || tu_to.username " +
                "ELSE 'From: ' || tu_from.username " +
                "END AS username, " +
                "t.amount, t.transfer_type_id, t.transfer_status_id, t.account_from, t.account_to " +
                "FROM transfer t " +
                "JOIN account a_from ON a_from.account_id = t.account_from " +
                "JOIN tenmo_user tu_from ON tu_from.user_id = a_from.user_id " +
                "JOIN account a_to ON a_to.account_id = t.account_to " +
                "JOIN tenmo_user tu_to ON tu_to.user_id = a_to.user_id " +
                "WHERE a_from.user_id = ? OR a_to.user_id = ? " +
                "ORDER BY t.transfer_id";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId, userId, userId);
            while (results.next()) {
                Transfer transfer = mapRowToTransfer(results);
                transfers.add(transfer);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }

        return transfers;
    }

    @Override
    public List<Transfer> viewPendingRequests(int userId) {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT t.transfer_id, t.transfer_type_id, t.transfer_status_id, t.account_from, t.account_to, t.amount, tu_to.username, tu_to.user_id "+
        "FROM transfer t "+
                "JOIN account a_from ON a_from.account_id = t.account_from "+
        "JOIN tenmo_user tu_from ON tu_from.user_id = a_from.user_id "+
        "JOIN account a_to ON a_to.account_id = t.account_to "+
        "JOIN tenmo_user tu_to ON tu_to.user_id = a_to.user_id "+
        "WHERE tu_from.user_id = ? AND t.transfer_type_id = 1;";

        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
            while (results.next()) {
                Transfer transfer = mapRowToTransfer3(results);
                transfers.add(transfer);

            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }

        return transfers;
    }

    public Transfer viewTransferDetails(int transferId) {
        Transfer transfer = null;
        String sql = "SELECT t.transfer_id,t.transfer_type_id, tt.transfer_type_desc, t.transfer_status_id, "+
                        "ts.transfer_status_desc, t.account_from, tu_from.username AS from_username,t.account_to, tu_to.username AS to_username, t.amount " +
                "FROM transfer t " +
                "JOIN transfer_type tt ON tt.transfer_type_id = t.transfer_type_id " +
                "JOIN transfer_status ts ON ts.transfer_status_id = t.transfer_status_id " +
                "JOIN account a_from ON t.account_from = a_from.account_id " +
                "JOIN tenmo_user tu_from ON a_from.user_id = tu_from.user_id " +
                "JOIN account a_to ON a_to.account_id = t.account_to " +
                "JOIN tenmo_user tu_to ON tu_to.user_id = a_to.user_id " +
                "WHERE t.transfer_id = ?; ";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, transferId);
            if (results.next()) {
                transfer = mapRowToTransfer2(results);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return transfer;
    }
    @Override
 public void requestBucks(int accountTo, int accountFrom, BigDecimal amount) {
        String sendMoneySql =
                "INSERT INTO transfer (transfer_type_id, transfer_status_id, account_from, account_to, amount) " +
                        "VALUES (?, ?, ?, ?, ?);";
        jdbcTemplate.update(sendMoneySql, 1, 1, accountFrom, accountTo, amount);

    }
    @Override
    public void updateTransferStatus(int transferStatusId, int transferId) {
        String sql = "UPDATE transfer SET transfer_status_id = ? WHERE transfer_id = ?;";
        jdbcTemplate.update(sql, transferStatusId, transferId);
    }


        private Transfer mapRowToTransfer(SqlRowSet rs) {
        Transfer transfer = new Transfer();
        transfer.setTransfer_id(rs.getInt("transfer_id"));
        transfer.setAmount(rs.getBigDecimal("amount"));
        transfer.setTransfer_type_id(rs.getInt("transfer_type_id"));
        transfer.setTransfer_status_id(rs.getInt("transfer_status_id"));
        transfer.setAccount_from(rs.getInt("account_from"));
        transfer.setAccount_to(rs.getInt("account_to"));
        transfer.setUsername(rs.getString("username"));
//

        return transfer;
    }

    private Transfer mapRowToTransfer2(SqlRowSet rs) {
        Transfer transfer = new Transfer();
        transfer.setTransfer_id(rs.getInt("transfer_id"));
        transfer.setTransfer_type_desc(rs.getString("transfer_type_desc"));
        transfer.setTransfer_status_desc(rs.getString("transfer_status_desc"));
        transfer.setFrom_username(rs.getString("from_username"));
        transfer.setTo_username(rs.getString("to_username"));
        transfer.setAmount(rs.getBigDecimal("amount"));
        transfer.setTransfer_type_id(rs.getInt("transfer_type_id"));
        transfer.setTransfer_status_id(rs.getInt("transfer_status_id"));
        transfer.setAccount_from(rs.getInt("account_from"));
        transfer.setAccount_to(rs.getInt("account_to"));

        return transfer;
    }
    private Transfer mapRowToTransfer3(SqlRowSet rs) {
        Transfer transfer = new Transfer();
        transfer.setTransfer_id(rs.getInt("transfer_id"));
        transfer.setAmount(rs.getBigDecimal("amount"));
        transfer.setTransfer_type_id(rs.getInt("transfer_type_id"));
        transfer.setTransfer_status_id(rs.getInt("transfer_status_id"));
        transfer.setAccount_from(rs.getInt("account_from"));
        transfer.setAccount_to(rs.getInt("account_to"));
        transfer.setUsername(rs.getString("username"));
        transfer.setUserId(rs.getInt("user_id"));

        return transfer;
    }

    }



