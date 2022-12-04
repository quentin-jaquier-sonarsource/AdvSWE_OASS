package coms.w4156.moviewishlist.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import coms.w4156.moviewishlist.exceptions.ClientAlreadyExistsException;

import coms.w4156.moviewishlist.security.jwt.JwtResponse;
import coms.w4156.moviewishlist.security.jwt.JwtTokenUtil;
import coms.w4156.moviewishlist.services.ClientService;

@RestController
public class AuthController {
    @Autowired
    private JwtTokenUtil jwtUtility;

    @Autowired
    private ClientService clientService;

    @PostMapping("/new-client")
    public ResponseEntity<JwtResponse> newClient(@RequestParam("email") String email) {
        UserDetails clientDetails;

        try {
            clientDetails = clientService.createClientAndReturnDetails(email);
        } catch (ClientAlreadyExistsException e) {
            return new ResponseEntity<>(new JwtResponse(""), HttpStatus.BAD_REQUEST);
        }

        final String token = jwtUtility.generateToken(clientDetails);

        return new ResponseEntity<JwtResponse>(new JwtResponse(token), HttpStatus.OK);
    }
}
