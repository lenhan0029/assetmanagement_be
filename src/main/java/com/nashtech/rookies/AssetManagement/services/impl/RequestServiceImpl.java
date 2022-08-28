package com.nashtech.rookies.AssetManagement.services.impl;

import com.nashtech.rookies.AssetManagement.exceptions.ApiDeniedException;
import com.nashtech.rookies.AssetManagement.exceptions.ConstraintViolateException;
import com.nashtech.rookies.AssetManagement.exceptions.ResourceNotFoundException;
import com.nashtech.rookies.AssetManagement.model.dto.RequestDto;
import com.nashtech.rookies.AssetManagement.model.dto.respond.AssetDetailDTO;
import com.nashtech.rookies.AssetManagement.model.dto.respond.MessageResponse;
import com.nashtech.rookies.AssetManagement.model.entities.*;
import com.nashtech.rookies.AssetManagement.repository.*;
import com.nashtech.rookies.AssetManagement.security.UserLocal;
import com.nashtech.rookies.AssetManagement.services.RequestService;
import io.swagger.v3.core.model.ApiDescription;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class RequestServiceImpl implements RequestService {
    @Autowired
    UserLocal userLocal;
    private RequestRepository requestRepository;
    private ModelMapper modelMapper;
    private AssignmentRepository assignmentRepository;
    private InformationRepository informationRepository;
    private AssetRepository assetRepository;
    private AccountsRepository accountsRepository;

    @Autowired
    public RequestServiceImpl(RequestRepository requestRepository,InformationRepository informationRepository,
                              ModelMapper modelMapper, AssignmentRepository assignmentRepository,
                              AssetRepository assetRepository, AccountsRepository accountsRepository){
        this.requestRepository = requestRepository;
        this.modelMapper = modelMapper;
        this.assignmentRepository = assignmentRepository;
        this.informationRepository = informationRepository;
        this.assetRepository = assetRepository;
        this.accountsRepository = accountsRepository;
    }
    public static boolean containsIgnoreCase(String src, String what) {
        final int length = what.length();
        if (length == 0)
            return true; // Empty string is contained

        final char firstLo = Character.toLowerCase(what.charAt(0));
        final char firstUp = Character.toUpperCase(what.charAt(0));

        for (int i = src.length() - length; i >= 0; i--) {
            // Quick check before calling the more expensive regionMatches() method:
            final char ch = src.charAt(i);
            if (ch != firstLo && ch != firstUp)
                continue;

            if (src.regionMatches(true, i, what, 0, length))
                return true;
        }

        return false;
    }

    @Override
    public List<RequestDto> listRequest(String stateSearch, String searchCode) {

        String[] stateArr = stateSearch.split(",");
        String userName = userLocal.getLocalUserName();
        String location = this.informationRepository.getLocationByUserName(userName);
        List<Request> listRequest = requestRepository.findAll();
        List<RequestDto> listRequestDto = new ArrayList<>();
        for(Request request : listRequest){
            RequestDto requestDto = modelMapper.map(request,RequestDto.class);
            if(request.getRequestAssignment().getReturnedDay()!=null)
                requestDto.setReturnedDate(request.getRequestAssignment().getReturnedDay());
            else requestDto.setReturnedDate(new Date("01/01/1000"));
            requestDto.setRequestedBy(request.getRequestBy().getUserName());
            if(request.getAcceptBy()!=null)
                requestDto.setAcceptedBy(request.getAcceptBy().getUserName());
            else requestDto.setAcceptedBy(" ");
            requestDto.setAssetCode(request.getRequestAssignment().getAsset().getAssetCode());
            requestDto.setAssetName(request.getRequestAssignment().getAsset().getAssetName());
            requestDto.setAssignedDate(request.getRequestAssignment().getAssignedDate());
            listRequestDto.add(requestDto);
        };
        if(listRequestDto.size()==0) throw new ResourceNotFoundException("This page is empty");
        List<RequestDto> lastList =  new ArrayList<>();
        if(searchCode.equals("")){
            if (stateArr.length==1 ){
                for(RequestDto requestDto : listRequestDto){
                    if(requestDto.getState().equalsIgnoreCase(stateArr[0])){
                        lastList.add(requestDto);
                    }
                }
            }
            else{
                for(RequestDto requestDto : listRequestDto){
                    lastList.add(requestDto);
                }
            }
        }
        else{
            for(RequestDto requestDto : listRequestDto){
                if(containsIgnoreCase(requestDto.getAssetCode(),searchCode) || containsIgnoreCase(requestDto.getAssetName(),searchCode) || containsIgnoreCase(requestDto.getRequestedBy(),searchCode)){
                    lastList.add(requestDto);
                }
            }
        }
        if(lastList.size()>0) {
            return lastList;
        }
        throw new ResourceNotFoundException("This page is empty");
    }

    @Override
    public ResponseEntity<?> cancelRequest(int id) {
        Optional<Request> optional = requestRepository.findById(id);
        if(!optional.isPresent()){
            throw new ResourceNotFoundException("Request not found");
        }
        Request request = optional.get();
        if(!request.getState().equals("WAITING_FOR_RETURNING")){
            throw new ApiDeniedException("Can not cancel request");
        }
        Assignment assignment = request.getRequestAssignment();
        assignment.setState("ACCEPTED");
        assignmentRepository.save(assignment);

        this.requestRepository.deleteByRequestId(id);
        return ResponseEntity.ok(new MessageResponse("Cancel Request Successfully !"));
    }

    @Override
    public ResponseEntity<?> completeRequest(int id) {
        Optional<Request> optional = requestRepository.findById(id);
        if(!optional.isPresent()){
            throw new ResourceNotFoundException("Request not found");
        }
        Request request = optional.get();
        if(!request.getState().equals("WAITING_FOR_RETURNING")){
            throw new ApiDeniedException("Can not complete request");
        }
        Assignment assignment = request.getRequestAssignment();
        assignment.setState("COMPLETED");
        assignment.setReturnedDay(new Date());
        assignmentRepository.save(assignment);
        request.setState("COMPLETED");
        Asset asset = request.getRequestAssignment().getAsset();
        asset.setState("AVAILABLE");
        requestRepository.save(request);
        assetRepository.save(asset);
        return ResponseEntity.ok(new MessageResponse("Completed Request Successfully !"));
    }

    @Override
    public ResponseEntity<?> createRequestReturnAsset(int id, int userId) {

        Optional<Assignment> assignmentOptional = this.assignmentRepository.findById(id);
        Optional<Accounts> accountsOptional = this.accountsRepository.findById(userId);
        assignmentOptional.orElseThrow(
                () -> new ResourceNotFoundException("Not Found Assignment With ID: " + id)
        );

        accountsOptional.orElseThrow(
                () -> new ResourceNotFoundException("Not Found User With ID: " + userId)
        );

        Accounts accounts = accountsOptional.get();
        Request request = new Request("WAITING_FOR_RETURNING", accounts, assignmentOptional.get());
        this.requestRepository.save(request);

        Assignment assignment = assignmentOptional.get();
        assignment.setState("WAITING_FOR_RETURNING");
        this.assignmentRepository.save(assignment);
        return ResponseEntity.ok(new MessageResponse("Request Created Successfully", HttpStatus.CREATED.value()));
    }
}
