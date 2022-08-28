package com.nashtech.rookies.AssetManagement.controller;

import com.nashtech.rookies.AssetManagement.model.dto.RequestDto;
import com.nashtech.rookies.AssetManagement.services.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/request")
public class RequestController {
    @Autowired
    private RequestService requestService;

    @GetMapping("")
    public List<RequestDto> getListRequest(
            @RequestParam(name = "code",defaultValue = "") String searchCode,
            @RequestParam(name = "state", defaultValue = "WAITING_FOR_RETURNING,COMPLETED", required = false) String state
    ){
        return requestService.listRequest(state, searchCode);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancelRequest(@PathVariable int id){
        return requestService.cancelRequest(id);
    }
    @PutMapping("/{id}")
    public ResponseEntity<?> completeRequest(@PathVariable int id){
        return requestService.completeRequest(id);
    }
    

    @PostMapping("/{id}")
    public ResponseEntity<?> createReturnRequest( @PathVariable("id") int assignmentId,
                                                  @RequestParam(name = "user", required = true) String userId){

        int userIdConverted = Integer.parseInt(userId);
        return this.requestService.createRequestReturnAsset(assignmentId, userIdConverted);
    }
}
