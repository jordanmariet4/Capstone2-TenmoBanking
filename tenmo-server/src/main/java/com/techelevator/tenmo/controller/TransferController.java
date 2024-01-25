package com.techelevator.tenmo.controller;
import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
public class TransferController {

    private TransferDao transferDao;
    private AccountDao accountDao;

    public TransferController (TransferDao transferDao,AccountDao accountDao) {
        this.transferDao = transferDao;
        this.accountDao = accountDao;

        if (this.accountDao == null) {
            // Log a message or throw an exception to indicate that accountDao is null
            throw new RuntimeException("AccountDao is not properly injected into TransferController");
        }
    }

    @RequestMapping(path = "/transfers/insertTransfer/{accountTo}/{accountFrom}/{amount}", method = RequestMethod.PUT)
    public Transfer insertTransfer(
            @Valid @RequestBody Transfer transfer,
            @PathVariable int accountTo,
            @PathVariable int accountFrom,
            @PathVariable BigDecimal amount) {
        int accountToId = accountDao.getAccountIdByUserId(accountTo);
        int accountFromId = accountDao.getAccountIdByUserId(accountFrom);

        transfer.setAccount_to(accountToId);
        transfer.setAccount_from(accountFromId);
        transfer.setAmount(amount);
        transfer.setTransfer_id(0);
        transfer.setTransfer_type_id(2);
        transfer.setTransfer_status_id(2);

        try {
            transferDao.insertTransfer(transfer.getAccount_to(), transfer.getAccount_from(),
                    transfer.getAmount());
            return transfer;
        } catch (DataAccessException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error during money transfer", e);
        }
    }
@RequestMapping(path = "/transfers/updateBalance", method = RequestMethod.POST)//changed to post
public Transfer updateBalance(@Valid @RequestBody Transfer transfer) {
    try {
//        int accountToId = accountDao.getAccountIdByUserId(transfer.getAccount_to());
//        int accountFromId = accountDao.getAccountIdByUserId(transfer.getAccount_from());
//
//        transfer.setAccount_to(accountToId);
//        transfer.setAccount_from(accountFromId);
//        transfer.setTransfer_type_id(2);
//        transfer.setTransfer_status_id(2);

        transferDao.updateBalance(transfer.getAccount_to(), transfer.getAccount_from(), transfer.getAmount());

        return transfer;
    } catch (DataAccessException e) {
        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error during money transfer", e);
    }
}
    @RequestMapping(path = "/transfers/sendBucks", method = RequestMethod.PUT)//changed to post
    public Transfer sendBucks(@Valid @RequestBody Transfer transfer) {
        try {
        int accountToId = accountDao.getAccountIdByUserId(transfer.getAccount_to());
        int accountFromId = accountDao.getAccountIdByUserId(transfer.getAccount_from());

        transfer.setAccount_to(accountToId);
        transfer.setAccount_from(accountFromId);
        transfer.setTransfer_type_id(2);
        transfer.setTransfer_status_id(2);

            transferDao.updateBalance(transfer.getAccount_to(), transfer.getAccount_from(), transfer.getAmount());

            return transfer;
        } catch (DataAccessException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error during money transfer", e);
        }
    }
    @RequestMapping(path = "/transfers/viewRequest/{userId}", method = RequestMethod.GET)
    public List<Transfer> listRequest(@PathVariable int userId) {
        return transferDao.viewPendingRequests(userId);

    }
    @RequestMapping(path = "/transfers/requestbucks/{accountTo}/{accountFrom}/{amount}", method = RequestMethod.PUT)
    public Transfer requestBucks(
            @Valid @RequestBody Transfer transfer,
            @PathVariable int accountTo,
            @PathVariable int accountFrom,
            @PathVariable BigDecimal amount) {

        transfer.setAccount_to(accountTo);
        transfer.setAccount_from(accountFrom);
        transfer.setAmount(amount);
        transfer.setTransfer_id(0);
        transfer.setTransfer_type_id(1);
        transfer.setTransfer_status_id(1);

        try {
            transferDao.requestBucks(transfer.getAccount_to(), transfer.getAccount_from(), transfer.getAmount());
            return transfer;
        } catch (DataAccessException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error during money transfer", e);
        }
    }


    @RequestMapping(path = "/transfers/{userId}", method = RequestMethod.GET)
    public List<Transfer> list( @PathVariable int userId){
        return transferDao.viewTransferHistory(userId);

    }
    @RequestMapping(path = "/transfers/viewDetails/{transferId}", method = RequestMethod.GET)
    public Transfer viewTransferDetails(@PathVariable int transferId){
        return transferDao.viewTransferDetails(transferId);
    }

    @RequestMapping(path = "/transfers/updateTransfer/{transferStatus}/{transferId}", method = RequestMethod.PUT)
    public void updateTransfers(@Valid @RequestBody Transfer transfer,
                                @PathVariable int transferStatus,
                                @PathVariable int transferId) {
        transfer.setTransfer_id(transferId);
        transfer.setTransfer_status_id(transferStatus);

        transferDao.updateTransferStatus(transferStatus, transferId);
    }



}

