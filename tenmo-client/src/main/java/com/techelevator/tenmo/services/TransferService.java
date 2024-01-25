package com.techelevator.tenmo.services;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.util.BasicLogger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.UserCredentials;

import java.math.BigDecimal;

public class TransferService {

    public String API_BASE_URL = "http://localhost:8080/transfers";

    private final RestTemplate restTemplate = new RestTemplate();
    private AuthenticatedUser currentUser;
    private String authToken = null;

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public Transfer getTransferById(int transfer_id) {
        Transfer transfer = null;
        transfer = restTemplate.getForObject(API_BASE_URL + "/" + transfer_id, Transfer.class);

        return transfer;
    }

    public boolean updateBalance(Transfer transfer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        HttpEntity<Transfer> entity = new HttpEntity<>(transfer, headers);

        boolean success = false;
        try {
            restTemplate.exchange(API_BASE_URL + "/updateBalance", HttpMethod.POST, entity, Transfer.class).getBody();
            success = true;
            BasicLogger.log("Balance updated successfully for transfer: " + transfer.getTransfer_id());
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log("Error updating balance for transfer " + transfer.getTransfer_id() + ": " + e.getMessage());
        }
        return success;
    }
    public boolean sendBucks(Transfer transfer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        HttpEntity<Transfer> entity = new HttpEntity<>(transfer, headers);

        boolean success = false;
        try {
            restTemplate.exchange(API_BASE_URL + "/sendBucks", HttpMethod.PUT, entity, Transfer.class).getBody();
            success = true;
            BasicLogger.log("Balance updated successfully for transfer: " + transfer.getTransfer_id());
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log("Error updating balance for transfer " + transfer.getTransfer_id() + ": " + e.getMessage());
        }
        return success;
    }

    public boolean insertTransfer(Transfer transfer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        HttpEntity<Transfer> entity = new HttpEntity<>(transfer, headers);

        boolean success = false;
        try {
            restTemplate.exchange(API_BASE_URL + "/insertTransfer/" + transfer.getAccount_to() + "/" + transfer.getAccount_from() + "/" + transfer.getAmount(),
                    HttpMethod.PUT, entity, Transfer.class).getBody();
            success = true;
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return success;
    }
    public boolean requestBucks(Transfer transfer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        HttpEntity<Transfer> entity = new HttpEntity<>(transfer, headers);

        boolean success = false;
        try {
            restTemplate.exchange(API_BASE_URL + "/requestbucks/"+ transfer.getAccount_to() + "/" + transfer.getAccount_from() + "/" + transfer.getAmount(),
                    HttpMethod.PUT, entity, Transfer.class).getBody();

            success = true;
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log("Error during POST request: " + e.getMessage());
        }
        return success;
    }


    public Transfer[] viewTransferHistory(int userId) {
        Transfer[] transfers = null;
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        HttpEntity entity = new HttpEntity(headers);
        try {
            transfers = restTemplate.exchange(API_BASE_URL + "/" + userId, HttpMethod.GET,
                    entity, Transfer[].class).getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return transfers;
    }

    public Transfer viewTransferDetails(int transferId){
        Transfer transfer = null;
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        HttpEntity entity = new HttpEntity(headers);

        try {
        transfer = restTemplate.exchange(API_BASE_URL + "/viewDetails/" + transferId, HttpMethod.GET,
                entity,Transfer.class).getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return transfer;
    }
    public Transfer[] viewPendingRequests(int userId){
        Transfer[] transfers = null;
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        HttpEntity entity = new HttpEntity(headers);
        try {
            transfers = restTemplate.exchange(API_BASE_URL + "/viewRequest/" +userId,
                    HttpMethod.GET, entity, Transfer[].class).getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return transfers;
    }
    public boolean updateTransferStatus(Transfer transfer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authToken);
        HttpEntity<Transfer> entity = new HttpEntity<>(transfer, headers);

        boolean success = false;
        try {

            restTemplate.exchange(API_BASE_URL + "/updateTransfer/" + transfer.getTransfer_status_id() +"/"+ transfer.getTransfer_id(), HttpMethod.PUT, entity, boolean.class);

            success = true;
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log("Error during PUT request: " + e.getMessage());
        }
        return success;
    }
    }



