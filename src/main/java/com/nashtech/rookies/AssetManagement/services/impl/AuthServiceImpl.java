package com.nashtech.rookies.AssetManagement.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.nashtech.rookies.AssetManagement.exceptions.ResourceNotFoundException;
import com.nashtech.rookies.AssetManagement.model.dto.request.AccountLoginDto;
import com.nashtech.rookies.AssetManagement.model.dto.respond.JwtResponse;
import com.nashtech.rookies.AssetManagement.model.entities.Accounts;
import com.nashtech.rookies.AssetManagement.repository.AccountsRepository;
import com.nashtech.rookies.AssetManagement.security.jwt.JwtUtils;
import com.nashtech.rookies.AssetManagement.security.service.UserDetailsImpl;
import com.nashtech.rookies.AssetManagement.services.AuthService;

@Component
public class AuthServiceImpl implements AuthService {
	final AuthenticationManager authenticationManager;
	final AccountsRepository accountRepository;

	final PasswordEncoder encoder;
	final JwtUtils jwtUtils;

	final ModelMapper modelMapper;

	public AuthServiceImpl(AuthenticationManager authenticationManager, AccountsRepository accountRepository, PasswordEncoder encoder, JwtUtils jwtUtils, ModelMapper modelMapper) {
		this.authenticationManager = authenticationManager;
		this.accountRepository = accountRepository;
		this.encoder = encoder;
		this.jwtUtils = jwtUtils;
		this.modelMapper = modelMapper;
	}

	@Override
	public ResponseEntity<?> loginAccount(AccountLoginDto accountLoginDto) {
		Optional<Accounts> optional = accountRepository.findByuserName(accountLoginDto.getUsername());
		if(!optional.isPresent()) {
			throw new ResourceNotFoundException("Username or password is incorrect. Please try again");
		}
		if(!optional.get().isStatus()== true) {
			throw new ResourceNotFoundException("Account is disable can not Login .");
		}
		if(!BCrypt.checkpw(accountLoginDto.getPassword(),optional.get().getPassword())){
			throw new ResourceNotFoundException("Username or password is incorrect. Please try again");
		}
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(accountLoginDto.getUsername(), accountLoginDto.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateJwtToken(authentication);
		UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
		List<String> roles = userDetails.getAuthorities().stream().map(item -> item.getAuthority())
				.collect(Collectors.toList());
		return ResponseEntity.ok(new JwtResponse(jwt, userDetails.getId(), userDetails.getUsername(), roles.get(0), userDetails.isFirst_login()));
	}

}
