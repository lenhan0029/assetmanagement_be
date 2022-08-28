package com.nashtech.rookies.AssetManagement.services;

import com.nashtech.rookies.AssetManagement.model.dto.RequestDto;
import com.nashtech.rookies.AssetManagement.model.entities.Request;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface RequestService {
    public List<RequestDto> listRequest(String stateSearch, String searchCode);
    public ResponseEntity<?> cancelRequest(int id);
    public ResponseEntity<?> completeRequest(int id);
    ResponseEntity<?> createRequestReturnAsset(int id, int userId);

}
