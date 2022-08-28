package com.nashtech.rookies.AssetManagement.controller;

import com.nashtech.rookies.AssetManagement.model.dto.AssignmentDto;
import com.nashtech.rookies.AssetManagement.model.dto.request.AssignmentUpdate;
import com.nashtech.rookies.AssetManagement.model.dto.request.CreateAssignmentRequest;
import com.nashtech.rookies.AssetManagement.model.dto.respond.AssignmentDetailDto;
import com.nashtech.rookies.AssetManagement.model.dto.respond.AssignmentDetailRespondDTO;
import com.nashtech.rookies.AssetManagement.model.entities.Assignment;
import com.nashtech.rookies.AssetManagement.services.AccountsService;
import com.nashtech.rookies.AssetManagement.services.AssignmentService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/assignment")
public class AssignmentController {


    @Autowired
    private AccountsService accountsService;

    @Autowired
    private AssignmentService assignmentService;




    @Operation(summary = "API to check that if account have assigment or completed all assignments " +
            "If the status code is 400, this mean the account already has assignment" +
            "If the response status code is 200, this mean the account dont have any assignment or all their assignment has been complete")
    @GetMapping
    public ResponseEntity<?> isAccountAssigned(@RequestParam(name = "account", required = true ) String id){
        int IdConverted = Integer.parseInt(id);
        return this.accountsService.isAssigned(IdConverted);
    }

    @GetMapping("/user")
    public List<AssignmentDto> getListAssignByUser(){
        return this.assignmentService.ListAssignmentByUser();
    }

    @Operation(summary = "Create new assignment",
            description = "you need to pass accountId as assignedToId, assetId, assigned date with assignedAt, note fields to create a new assignment" +
                    "Here are few situation that can happend when using API: " +
                    "If the response status code is 400 -> Your data is invalid or inccorect " +
                    "If the response status is 200 -> The new assignment have been created successfully"
    )
    @PostMapping
    public AssignmentDto createNewAssignment(@RequestBody CreateAssignmentRequest createAssignmentRequest){
        return this.assignmentService.createNewAssignment(createAssignmentRequest);
    }

    @GetMapping("/{id}")
    public AssignmentDetailRespondDTO getAssignmentDetailById(@PathVariable("id") int id){
        return this.assignmentService.getAssignmentById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAssigment(@PathVariable Integer id)
    {
        return this.assignmentService.deleteAssigment(id);
    }
    @GetMapping("/list")
    public ResponseEntity<?> listAssignmentDetailBySearch(
            @RequestParam(name = "code",defaultValue = "%", required = false) String searchCode,
            @RequestParam(name = "page", defaultValue = "0", required = false) String page,
            @RequestParam(name = "fl1", defaultValue = "%", required = false) String filter1,
            @RequestParam(name = "fl2", defaultValue = "%", required = false) String filter2,
            @RequestParam(name = "fl3", defaultValue = "%", required = false) String filter3,
            @RequestParam(name = "d", defaultValue = "%", required = false) String filterday,
            @RequestParam(name = "sort", defaultValue = "aa", required = false) String sort){
        int pageConverted = Integer.parseInt(page.trim());
        return assignmentService.listAssignment(searchCode, pageConverted, filter1, filter2,filter3,filterday,sort);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> respondAssigment(@PathVariable Integer id)
    {
        return this.assignmentService.respondAssigment(id);
    }

    @PatchMapping("/{id}")
    public AssignmentDto editAssignment( @PathVariable("id") int id,  @RequestBody AssignmentUpdate assignmentDetailRespondDTO){
        return  this.assignmentService.editAssignment(id,assignmentDetailRespondDTO);
    }
}
