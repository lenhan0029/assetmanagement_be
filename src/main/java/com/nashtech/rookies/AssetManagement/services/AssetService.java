package com.nashtech.rookies.AssetManagement.services;

import com.nashtech.rookies.AssetManagement.model.dto.AssetDto;
import com.nashtech.rookies.AssetManagement.model.dto.request.AssetUpdateDTO;
import com.nashtech.rookies.AssetManagement.model.dto.request.CreateAssetRequest;
import com.nashtech.rookies.AssetManagement.model.entities.Asset;
import org.springframework.http.ResponseEntity;

public interface AssetService {


	ResponseEntity<?> listAssetDetailBySearch(String assetCode, String categoryId, String stateSearch, int page, String sort);

	ResponseEntity<?> getAssetById(Integer id);

	public AssetDto createNewAsset(CreateAssetRequest createAssetRequest);

	ResponseEntity<?> editAssetInfo(Integer id, AssetUpdateDTO assetUpdateDTO);

	ResponseEntity<?> deleteAsset(int assetId);

	ResponseEntity<?> getAllAssetAvailable();

}
