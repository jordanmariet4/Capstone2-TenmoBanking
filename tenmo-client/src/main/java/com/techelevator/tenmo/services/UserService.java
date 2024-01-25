package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.User;
import com.techelevator.util.BasicLogger;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

public class UserService {
    public String API_BASE_URL = "http://localhost:8080/users";

    private final RestTemplate restTemplate = new RestTemplate();

    public User[] listUsers() {
        User[] tenmo_users = null;
        try {
            tenmo_users = restTemplate.getForObject(API_BASE_URL, User[].class);
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return tenmo_users;
    }
}
