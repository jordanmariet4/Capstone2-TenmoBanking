package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.model.*;
import org.springframework.web.bind.annotation.*;

import com.techelevator.tenmo.dao.UserDao;

import java.util.List;

@RestController
public class UserController {

    //private Tenmo_UserDao tenmo_userDao;
    private UserDao userDao;

    public UserController(UserDao userDao) {
        this.userDao = userDao;
    }

    @RequestMapping(path = "/users", method = RequestMethod.GET)
    public List<User> list(@RequestParam(defaultValue = "0") int user_id,
                                 @RequestParam(defaultValue = "") String username) {
        return userDao.getUsers();


    }


}
