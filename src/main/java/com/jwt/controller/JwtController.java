package com.jwt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jwt.domain.AuthenticationRequest;
import com.jwt.domain.AuthenticationResponse;
import com.jwt.service.UserService;
import com.jwt.util.JwtUtil;

@RestController
@RequestMapping("/api/v1")
public class JwtController {
	
	@Autowired
	private AuthenticationManager authManager;
	@Autowired
	private UserService userService;
	@Autowired
	private JwtUtil jwtUtil;

	@GetMapping("/hello")
	public String sayHello() {
		return "Hello World";
	}
	
	@PostMapping("/authenticate")
	public ResponseEntity<?> createAuthentication(@RequestBody AuthenticationRequest authRequest) throws Exception {
		try{
			authManager.authenticate(
				new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
				);
		}catch(BadCredentialsException bce) {
			throw new BadCredentialsException("Invalid credentails", bce);
		}
		final UserDetails userDetails=userService.loadUserByUsername(authRequest.getUsername());
		final String jwtToken=jwtUtil.generateToken(userDetails);
		return ResponseEntity.ok(new AuthenticationResponse(jwtToken));
	}
	
}