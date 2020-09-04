package com.thechinterview.ldaprestapi.controller;

import com.thechinterview.ldaprestapi.model.User;

import com.thechinterview.ldaprestapi.repository.UserRepository;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController()
@ApiOperation(nickname = "User Endpoint", value = "manage users")
@RequestMapping(path = "/Users")
public class UserController {

    @Autowired
    UserRepository userRepo;

    @ApiOperation(value = "Add a new User entry")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User successfully inserted."),
            @ApiResponse(code = 409, message = "User already exists."),
            @ApiResponse(code = 500, message = "Unknown error.")
    })
    @PostMapping()
    public void addUser(@Valid @RequestBody User user) {
        System.out.println("user: " + user.getSn() + " " + user.getCn() + " " + user.getUid());
        userRepo.addUser(user);
    }

    @ApiOperation(value = "Get an User entry by its uid")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User successfully found."),
            @ApiResponse(code = 404, message = "User not found."),
            @ApiResponse(code = 500, message = "Unknown error.")
    })
    @GetMapping(path = "/{uid}")
    public User getUser(@PathVariable("uid") String uid) {
        return userRepo.getUser(uid);
    }

    @ApiOperation(value= "Get All User entries")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully returned all entries."),
            @ApiResponse(code = 500, message = "Unknown error.")
    })
    @GetMapping()
    public List<User> getUser() {
        return userRepo.getUsers();
    }

    @ApiOperation(value = "Delete using given its uid")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User successfully deleted."),
            @ApiResponse(code = 404, message = "User not found."),
            @ApiResponse(code = 500, message = "Unknown error.")
    })
    @DeleteMapping(path = "/{uid}")
    public void deleteUser(@PathVariable("uid") String uid) {
        userRepo.delete(uid);
    }

}
