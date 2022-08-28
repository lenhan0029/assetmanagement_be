package com.nashtech.rookies.AssetManagement.controller;

import com.nashtech.rookies.AssetManagement.model.dto.AssetDto;
import com.nashtech.rookies.AssetManagement.model.dto.request.AssetUpdateDTO;
import com.nashtech.rookies.AssetManagement.model.dto.request.CreateAssetRequest;
import com.nashtech.rookies.AssetManagement.model.entities.Asset;
import com.nashtech.rookies.AssetManagement.model.entities.Category;
import com.nashtech.rookies.AssetManagement.repository.CategoryRepository;
import com.nashtech.rookies.AssetManagement.services.AdminService;
import com.nashtech.rookies.AssetManagement.services.AssetService;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/asset")
public class AssetController {


    @Autowired
    private AssetService assetService;

    @Operation(summary  = "Create New Asset",
            description = " Please notice that some validation we need"
                    + "has been processed in front end, so I wont repeat it in API. Here are few situation that can happend when using API: "
                    + "If the response status code is 404 -> You have entered an non-exist or not availabel state. " +
                    "When create new asset we only have two state : available and unavailable"
                    + "If the response status is 200 -> The new Asset have created successfully")
    @PostMapping
    public AssetDto createNewAsset(@RequestBody CreateAssetRequest createAssetRequest){
        return this.assetService.createNewAsset(createAssetRequest);
    }



    @Operation(summary = "Edit Asset",
            description = " Please notice that some validation we need " +
                    "has been processed in front end, so I wont repeat it in API. Here are few situation that can happend when using API: " +
                    "If the response status code is 404 -> You have entered an non-exist or not availabel state. " +
                    "When edit asset we only have four state : available, unavailable, waiting_for_recycle and recycled " +
                    "If the response status is 200 -> The new Asset have created successfully"
    )
    @PutMapping("/{id}")
    public ResponseEntity<?> editAssetInfo(@PathVariable Integer id,@RequestBody AssetUpdateDTO assetUpdateDTO){
        return this.assetService.editAssetInfo(id,assetUpdateDTO);
    }


    @GetMapping
    public ResponseEntity<?>  listAssetDetailBySearch(
            @RequestParam(name = "code",defaultValue = "%") String searchCode,
            @RequestParam(name = "state", defaultValue = "AVAILABLE", required = false) String state,
            @RequestParam(name = "category", defaultValue = "0", required = false) String categoryId,
            @RequestParam(name = "page", defaultValue = "0", required = false) String page,
            @RequestParam(name = "sort", defaultValue = "ca", required = false) String sort){
        int pageConverted = Integer.parseInt(page);
        return assetService.listAssetDetailBySearch(searchCode, categoryId, state, pageConverted, sort);

        //Please make sure the string start from right after the '=' in the url. For Ex:
        //We have : category=1 2 3, not category= 1 2 3. The first space can lead to crash. You can space many times you want with next parameter, but not with the first parameter.
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAssetById(@PathVariable Integer id){
        return assetService.getAssetById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAsset(@PathVariable("id") int id){
        return this.assetService.deleteAsset(id);
    }

    @Operation(summary = "Get all asset with status is available",
            description = " Please notice that some validation we need " +
                    "If the response status code is 404 -> You have entered an non-exist or not availabel state. " +
                    "If the response status is 200 -> The new Asset have created successfully"
    )
    @GetMapping("/available")
    public ResponseEntity<?> getAllAssetAvailable(){
        return assetService.getAllAssetAvailable();
    }
}
