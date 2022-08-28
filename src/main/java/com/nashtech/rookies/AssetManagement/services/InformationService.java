package com.nashtech.rookies.AssetManagement.services;

import org.springframework.http.ResponseEntity;

import com.nashtech.rookies.AssetManagement.model.dto.InformationDto;

public interface InformationService {

    ResponseEntity<?> getInformationByAccountId(int id);

    ResponseEntity<?> updateInformation(int id ,InformationDto information);

    ResponseEntity<?> getInformationByLocation(int id);
    
}
