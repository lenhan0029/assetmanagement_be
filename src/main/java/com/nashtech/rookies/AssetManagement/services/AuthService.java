package com.nashtech.rookies.AssetManagement.services;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.nashtech.rookies.AssetManagement.model.dto.request.AccountLoginDto;

@Service
public interface AuthService {
    public ResponseEntity<?> loginAccount(AccountLoginDto accountLoginDto);
}
