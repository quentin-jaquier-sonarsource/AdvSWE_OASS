package coms.w4156.moviewishlist.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import coms.w4156.moviewishlist.security.jwt.JwtTokenUtil;
import coms.w4156.moviewishlist.security.jwt.JwtRequest;
import coms.w4156.moviewishlist.security.jwt.JwtResponse;
import coms.w4156.moviewishlist.services.UserService;

@RestController
public class AuthController {
    @Autowired
    private JwtTokenUtil jwtUtility;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<JwtResponse> signUp(@RequestParam("email") String email, @RequestParam("username") String username, @RequestParam("password") String password) {
        UserDetails userDetails = userService.createUserAndReturnDetails(email, username, password);

        final String token = jwtUtility.generateToken(userDetails);

        return new ResponseEntity<JwtResponse>(new JwtResponse(token), HttpStatus.OK);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<JwtResponse> authenticate(@RequestBody JwtRequest request) {

        System.out.println("authenticate");

        String username = request.getUsername();
        String password = request.getPassword();

        System.out.println(username);
        System.out.println(password);
        
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
            );
        } catch (BadCredentialsException e) {
            System.out.println("Bad credentials!!!!");
            return new ResponseEntity<JwtResponse>(new JwtResponse(""), HttpStatus.UNAUTHORIZED);
        }

        final UserDetails userDetails = userService.loadUserByUsername(username);

        final String token = jwtUtility.generateToken(userDetails);

        return new ResponseEntity<JwtResponse>(new JwtResponse(token), HttpStatus.OK);
    }
}
