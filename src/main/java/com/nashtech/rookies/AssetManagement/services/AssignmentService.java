package com.nashtech.rookies.AssetManagement.services;

import com.nashtech.rookies.AssetManagement.model.dto.AssignmentDto;
import com.nashtech.rookies.AssetManagement.model.dto.request.AssignmentUpdate;
import com.nashtech.rookies.AssetManagement.model.dto.request.CreateAssignmentRequest;
import com.nashtech.rookies.AssetManagement.model.dto.respond.AssignmentDetailDto;
import com.nashtech.rookies.AssetManagement.model.dto.respond.AssignmentDetailRespondDTO;
import com.nashtech.rookies.AssetManagement.model.entities.Assignment;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.util.List;


public interface AssignmentService {

    AssignmentDto createNewAssignment(CreateAssignmentRequest createAssignmentRequest);

    AssignmentDetailRespondDTO getAssignmentById(int id);

    AssignmentDto editAssignment(int id, AssignmentUpdate assignmentUpdate);

    List<AssignmentDto> ListAssignmentByUser();



    ResponseEntity<?> deleteAssigment(Integer id);

    ResponseEntity<?> respondAssigment(Integer id);


    ResponseEntity<?>  listAssignment(String searchCode, int page, String filter1, String filter2, String filter3, String day, String sort);
}
