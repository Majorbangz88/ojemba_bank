package com.big_joe.ojemba_bank.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/api/user")
@Tag(name = "User Account management APIs")
public class UserController {

//    @Operation(
//            summary = "Create New User Account",
//            description = "Creating a new user and assigning an account ID"
//    )
//    @ApiResponse(
//            responseCode = "201",
//            description = "HTTP Status 201 Created"
//    )
}
