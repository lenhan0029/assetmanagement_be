package com.nashtech.rookies.AssetManagement.services.impl;

import com.nashtech.rookies.AssetManagement.exceptions.ApiDeniedException;
import com.nashtech.rookies.AssetManagement.exceptions.ConstraintViolateException;
import com.nashtech.rookies.AssetManagement.exceptions.ResourceNotFoundException;
import com.nashtech.rookies.AssetManagement.model.dto.AssignmentDto;
import com.nashtech.rookies.AssetManagement.model.dto.request.AssignmentUpdate;
import com.nashtech.rookies.AssetManagement.model.dto.request.CreateAssignmentRequest;
import com.nashtech.rookies.AssetManagement.model.dto.respond.AssignmentDetailDto;
import com.nashtech.rookies.AssetManagement.model.dto.respond.AssignmentDetailRespondDTO;
import com.nashtech.rookies.AssetManagement.model.dto.respond.MessageResponse;
import com.nashtech.rookies.AssetManagement.model.entities.Accounts;
import com.nashtech.rookies.AssetManagement.model.entities.Asset;
import com.nashtech.rookies.AssetManagement.model.entities.Assignment;
import com.nashtech.rookies.AssetManagement.model.entities.Information;
import com.nashtech.rookies.AssetManagement.repository.AccountsRepository;
import com.nashtech.rookies.AssetManagement.repository.AssetRepository;
import com.nashtech.rookies.AssetManagement.repository.AssignmentRepository;
import com.nashtech.rookies.AssetManagement.security.UserLocal;
import com.nashtech.rookies.AssetManagement.services.AssignmentService;
import com.nashtech.rookies.AssetManagement.utils.MyDateUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class AssignmentServiceImpl implements AssignmentService {

    private ModelMapper modelMapper;

    private AssignmentRepository assignmentRepository;

    private UserLocal userLocal;

    private AssetRepository assetRepository;

    private AccountsRepository accountsRepository;

    private MyDateUtils myDateUtils;


    @Autowired
    public AssignmentServiceImpl(ModelMapper modelMapper, AssignmentRepository assignmentRepository, UserLocal userLocal,
                                 AssetRepository assetRepository, AccountsRepository accountsRepository,
                                 MyDateUtils myDateUtils) {
        this.modelMapper = modelMapper;
        this.assignmentRepository = assignmentRepository;
        this.userLocal = userLocal;
        this.assetRepository = assetRepository;
        this.accountsRepository = accountsRepository;
        this.myDateUtils = myDateUtils;
    }

    @Override
    public AssignmentDto createNewAssignment(CreateAssignmentRequest createAssignmentRequest) {
        Assignment assignment = modelMapper.map(createAssignmentRequest, Assignment.class);
        String userName = userLocal.getLocalUserName();

        Optional<Accounts> assignmentCreator = this.accountsRepository.findByuserName(userName);
        Optional<Asset> assetAssigned = this.assetRepository.findById(createAssignmentRequest.getAssetId());

        assetAssigned.orElseThrow(
                () -> new ResourceNotFoundException("Asset Not Found With ID: " + createAssignmentRequest.getAssetId())
        );

        Optional<Accounts> assignmentAccount = this.accountsRepository.findById(createAssignmentRequest.getAssignedToId());

        assignmentAccount.orElseThrow(
                () -> new ResourceNotFoundException("Account Not Found With ID: " + createAssignmentRequest.getAssignedToId())
        );

        assetAssigned.filter(asset -> asset.getState().equalsIgnoreCase("AVAILABLE"))
                .orElseThrow(
                        () -> new ConstraintViolateException("Asset not in available !")
                );

        assignment.setCreators(assignmentCreator.get());
        assignment.setAccounts(assignmentAccount.get());
        Asset asset = assetAssigned.get();
        asset.setState("ASSIGNED");
        assignment.setAsset(asset);
        assignment.setState("WAITING_FOR_ACCEPTANCE");

        Assignment savedAssignment = this.assignmentRepository.save(assignment);

        Optional<Assignment> assignmentOptional = Optional.of(savedAssignment);

        Optional<AssignmentDto> assignmentDtoOptional = assignmentOptional.map(AssignmentDto::new);

        return assignmentDtoOptional.get();


    }

    @Override
    public AssignmentDetailRespondDTO getAssignmentById(int id) {

        Optional<Assignment> assignmentOptional = this.assignmentRepository.findById(id);

        assignmentOptional.orElseThrow(
                () -> new ResourceNotFoundException("Assignment Not Found With ID: " + id)
        );

        Optional<AssignmentDetailRespondDTO> detailRespondDTO = assignmentOptional.map(AssignmentDetailRespondDTO::new);

        return  detailRespondDTO.get();
    }



    @Override
    public AssignmentDto editAssignment(int id, AssignmentUpdate assignmentUpdate) {

        Assignment newAssignment = modelMapper.map(assignmentUpdate, Assignment.class);
        Optional<Assignment> oldAssignment = this.assignmentRepository.findById(id);

        oldAssignment.orElseThrow(
                () -> new ResourceNotFoundException("Not Found Any Assignment With ID: " + id)
        );

        oldAssignment.filter(old -> old.getState().equals("WAITING_FOR_ACCEPTANCE"))
                .orElseThrow(
                        () -> new ConstraintViolateException("Cannot edit Assignment not in Waiting state")
                );

        Optional<Accounts> accountsOptional = this.accountsRepository.findById(assignmentUpdate.getAssignedId());

        accountsOptional.orElseThrow(
                () -> new ResourceNotFoundException("Account not found to Assign")
        );

        Optional<Asset> assetOptional = this.assetRepository.findById(assignmentUpdate.getAssetId());

        assetOptional.orElseThrow(
                () -> new ResourceNotFoundException("Asset Not Found To Assign ")
        );

        Optional<Asset> oldAsset = oldAssignment.map(Assignment::getAsset);
        Asset asset = assetOptional.get();
        Asset old = oldAsset.get();
        if(old.getAssetId() != asset.getAssetId())
        {
            if(!asset.getState().equals("AVAILABLE"))
            {
                throw new ConstraintViolateException("Asset not in available !");
            }
            old.setState("AVAILABLE");
            this.assetRepository.save(old);
            asset.setState("ASSIGNED");
            newAssignment.setAsset(asset);
        }
        else
        {
            newAssignment.setAsset(asset);
        }

        newAssignment.setCreators(oldAssignment.get().getCreators());
        newAssignment.setState(oldAssignment.get().getState());
        newAssignment.setAccounts(accountsOptional.get());
        newAssignment.setAssignmentId(id);

        Assignment savedAssignment = this.assignmentRepository.save(newAssignment);

        Optional<Assignment> assignmentOptional = Optional.of(savedAssignment);

        Optional<AssignmentDto> assignmentDtoOptional = assignmentOptional.map(AssignmentDto::new);

        return assignmentDtoOptional.get();
    }


    @Override
    public List<AssignmentDto> ListAssignmentByUser() {

        String userName = userLocal.getLocalUserName();
        Optional<Accounts> accountsOptional = this.accountsRepository.findByuserName(userName);

        Optional<List<Assignment>> assignmentList = accountsOptional.map(Accounts::getAssignments);

        assignmentList.orElseThrow(
                () -> new ResourceNotFoundException("Not Found Any Assignment Belong To You")
        );

        Date now = new Date();

        List<AssignmentDto> assignmentDtoList = assignmentList.get()
                .stream()
                .filter(assignment -> assignment.getAssignedDate().before(now))
                .map(AssignmentDto::new)
                .collect(Collectors.toList());

        assignmentDtoList.stream().findAny().orElseThrow(
                () -> new ResourceNotFoundException("Not Found Any Assignment Before Now")
        );

        return assignmentDtoList;
    }



    @Override
    public ResponseEntity<?> deleteAssigment(Integer id) {

        Optional<Assignment> assignmentOptional = assignmentRepository.findById(id);
        assignmentOptional.orElseThrow(
                () -> new ResourceNotFoundException("Assignment Not Found With ID: " + id)
        );

        assignmentOptional.filter(assignment -> assignment.getState().equals("ACCEPTED"))
                .orElseThrow(
                        () -> new ApiDeniedException("Can't delete Assigment because Assigment be Accepted")
        );

        assignmentRepository.delete(assignmentOptional.get());

        return ResponseEntity.ok( new MessageResponse("Delete Assignment Successfully", HttpStatus.OK.value()));
    }


    @Override
    public ResponseEntity<?> respondAssigment(Integer id) {
        Optional<Assignment> optional = assignmentRepository.findById(id);
        if(!optional.isPresent()) {
            throw new ResourceNotFoundException("Assignment Not Found");
        }
        Assignment assignment = optional.get();
        if(!assignment.getState().equals("WAITING_FOR_ACCEPTANCE")){
            return ResponseEntity.badRequest().body(new MessageResponse("Can't respond Assigment",
                    HttpStatus.BAD_REQUEST.value()));
        }
        assignment.setState("ACCEPTED");
        assignmentRepository.save(assignment);
        return ResponseEntity.ok().body(String.format("Respond Assigment successfully"));
    }

    @Override
    public  ResponseEntity<?>  listAssignment(String searchCode, int page, String filter1, String filter2, String filter3, String day, String sort){
        Pageable newPage = createPage(page, 20,sort);
        Page<AssignmentDetailDto> pageAccount;
        pageAccount = this.assignmentRepository
                .listAssignmentBySearch(searchCode.toLowerCase(), newPage, filter1,filter2,filter3,day);
        if (pageAccount.hasContent()) {
            return ResponseEntity.ok(pageAccount);
        }
        throw new ResourceNotFoundException("This page is empty");
    }


    public Pageable createPage(int page, int size , String sortBy) {

        char[] sortByte = sortBy.toCharArray();

        char sortMode = sortByte[1];
        char sortCol = sortByte[0];

        String sortName ;

        Sort sort ;

        switch (sortCol){
            case 'a':
                sortName = "assignmentId";
                break;
            case 'd':
                sortName = "assignedDate";
                break;
            case 'c':
                sortName = "st.assetCode";
                break;
            case 'n':
                sortName = "st.assetName";
                break;
            case 't':
                sortName = "ac.userName";
                break;
            case 'b':
                sortName = "cr.userName";
                break;
            case 'e':
                sortName = "state";
                break;
            default:
                throw new ResourceNotFoundException("NO COLUMN SORT FOUND");
        }

        switch (sortMode){
            case 'a' :
                sort = Sort.by(Sort.Direction.ASC, sortName);
                break;
            case 'd':
                sort = Sort.by(Sort.Direction.DESC, sortName);
                break;
            default:
                throw new ResourceNotFoundException("NO MODE SORT FOUND");
        }

        return PageRequest.of(page, size, sort);
    }

}



// Get List Assignment By User
//        String userName = userLocal.getLocalUserName();
//        Accounts userAccounts = this.accountsRepository.findByuserName(userName).get();
//        List<Assignment> listUserAssignment = this.assignmentRepository.getAssignmentByAssignedUser(userAccounts.getAccountId());
//
//        Date timeNow = new Date();
//
//        List<AssignmentDto> listAssignmentDetail = listUserAssignment.stream()
//                .filter(assignment -> assignment.getAssignedDate().before(timeNow))
//                .map(this::convertToDTO).collect(Collectors.toList());
//
//        if(listAssignmentDetail.isEmpty()){
//            throw new ResourceNotFoundException("Not Found Any Assignment Of This User");
//        }
//        return listAssignmentDetail;


// Delete Assignment
//        Optional<Assignment> optional = assignmentRepository.findById(id);
//        if(!optional.isPresent()) {
//            throw new ResourceNotFoundException("Assignment Not Found");
//        }
//        Assignment assignment = optional.get();
//        if(assignment.getState().equals("ACCEPTED") )
//        {
//            return ResponseEntity.badRequest().body(new MessageResponse("Can't delete Assigment because Assigment be Accepted",
//                    HttpStatus.BAD_REQUEST.value()));
//        }
//        assignmentRepository.delete(assignment);
//        return ResponseEntity.ok().body(String.format("Delete Assigment successfully"));



// Get Assignment By ID
//        Optional<Assignment> assignmentOptional = this.assignmentRepository.findById(id);
//        if(assignmentOptional.isPresent()){
//            Assignment assignment = assignmentOptional.get();
//            Accounts accountAssignedInfo = assignment.getAccounts();
//            Asset asset = assignment.getAsset();
//            Accounts accountCreator = assignment.getCreators();
//
//            String assginedToName = accountAssignedInfo.getUserName();
//            String assignedNameTo = accountAssignedInfo.getInformation().getFirstName() + " "+ accountAssignedInfo.getInformation().getLastName();
//            String assignedName = accountCreator.getUserName();
//            String assetName = assignment.getAsset().getAssetName();
//            String assetCode = assignment.getAsset().getAssetCode();
//            int assetId = asset.getAssetId();
//            String note = assignment.getNote();
//            String state = assignment.getState();
//            String specification = asset.getSpecification();
//
//            Date assignedDate = assignment.getAssignedDate();
//
//            return new AssignmentDetailRespondDTO(assignedName,accountAssignedInfo.getAccountId() ,assginedToName, assignedNameTo,assetCode, assetName, note, state, assetId, assignedDate, specification);
//
//        }
//        throw new ResourceNotFoundException("Assignment Not Found With ID: " + id);



//Edit Assignment
//        Assignment assignment = modelMapper.map(assignmentUpdate, Assignment.class);
//        Assignment oldAssignment = this.assignmentRepository.findById(id).get();
//
//        Optional<Accounts> accountsOptional = this.accountsRepository.findById(assignmentUpdate.getAssignedId());
//        if(accountsOptional.isPresent())
//        {
//            Asset oldAsset = oldAssignment.getAsset();
//            Optional<Asset> assetOptional = this.assetRepository.findById(assignmentUpdate.getAssetId());
//            if(assetOptional.isPresent())
//            {
//                Accounts accounts = accountsOptional.get();
//                Asset asset = assetOptional.get();
//
//
//
//                if(oldAsset.getAssetId() != asset.getAssetId())
//                {
//                    if(!asset.getState().equals("AVAILABLE")){
//                        throw new ConstraintViolateException("Asset not in available !");
//                    }
//                    oldAsset.setState("AVAILABLE");
//                    this.assetRepository.save(oldAsset);
//                    asset.setState("ASSIGNED");
//                    this.assetRepository.save(asset);
//                    assignment.setAsset(asset);
//                }else{
//                    assignment.setAsset(asset);
//                }
//
//
//                assignment.setAssignedDate(myDateUtils.getDate(assignmentUpdate.getDateAssigned()));
//                assignment.setCreators(oldAssignment.getCreators());
//                assignment.setState(oldAssignment.getState());
//                assignment.setAccounts(accounts);
//                assignment.setAssignmentId(id);
//
//                AssignmentDto assignmentDto = modelMapper.map(this.assignmentRepository.save(assignment), AssignmentDto.class);
//
//                assignmentDto.setAssetName(asset.getAssetName());
//                assignmentDto.setAssetCode(asset.getAssetCode());
//                assignmentDto.setAssignedBy(oldAssignment.getCreators().getUserName());
//                assignmentDto.setAssignedTo(oldAssignment.getAccounts().getUserName());
//                assignmentDto.setSpecification(asset.getSpecification());
//
//                return assignmentDto;
//            }
//            throw new ResourceNotFoundException("Asset not found to Assign !");
//        }
//        throw new ResourceNotFoundException("Account not found to Assign !");



// Create Assignment
//        String userName = userLocal.getLocalUserName();
//
//
//        Asset assetAssigned = this.assetRepository.findById(createAssignmentRequest.getAssetId()).get();
//        Accounts accountAssigned = this.accountsRepository.findById(createAssignmentRequest.getAssignedToId()).get();
//        Accounts accountAdminAssign = this.accountsRepository.findByuserName(userName).get();
//
//        if(!assetAssigned.getState().equals("AVAILABLE")){
//            throw new ConstraintViolateException("Asset not in available !");
//        }
//
//
//        Date date = myDateUtils.getDate(createAssignmentRequest.getAssignedAt());
//        assetAssigned.setState("ASSIGNED");
//
//        assignment.setAssignmentId(0);
//        assignment.setAssignedDate(date);
//        assignment.setCreators(accountAdminAssign);
//        assignment.setAccounts(accountAssigned);
//
//        assignment.setAsset(assetAssigned);
//        assignment.setState("WAITING_FOR_ACCEPTANCE");
//
//        Optional<Assignment> assignmentOptional = Optional.of(assignment);
//
//        Optional<AssignmentDto> assignmentDtoOptional = assignmentOptional.map(this::convertToDTO);
//
//        return assignmentDtoOptional.get();
