package com.nashtech.rookies.AssetManagement.services;

import com.nashtech.rookies.AssetManagement.model.dto.request.CreateUserRequest;
import com.nashtech.rookies.AssetManagement.model.dto.respond.UserRespondDTO;
import org.springframework.http.ResponseEntity;

public interface AdminService {
    UserRespondDTO createNewUser(CreateUserRequest createUserRequest);

    ResponseEntity<?> deleteDuplicate();


}
