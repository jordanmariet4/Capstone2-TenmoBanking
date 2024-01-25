package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.math.BigDecimal;
import java.util.List;

public interface TransferDao {

    //Transfer getTransferById (int transfer_id);
   void updateBalance(int accountTo, int accountFrom, BigDecimal amount);
    void insertTransfer(int accountTo, int accountFrom, BigDecimal amount);

    List<Transfer> viewTransferHistory(int userId);
    List <Transfer> viewPendingRequests(int userId);

    Transfer viewTransferDetails(int transferId);

    void requestBucks(int accountTo, int accountFrom, BigDecimal amount);
    void updateTransferStatus(int transferStatusId, int transferId);


}
