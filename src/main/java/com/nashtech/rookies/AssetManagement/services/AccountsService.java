package com.nashtech.rookies.AssetManagement.services;

import com.nashtech.rookies.AssetManagement.model.dto.request.ChangePasswordDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
public interface AccountsService {
    ResponseEntity<?> changePassword(Integer id, ChangePasswordDTO changePasswordDTO);

    ResponseEntity<?> listAccountDetailBySearch(String searchCode, int page, String filter, String sort);

    ResponseEntity<?> disableAccount(Integer id);

    ResponseEntity<?> isAssigned(int id);

    public ResponseEntity<?> deleteUser(String userName);





}

