package TestService;

import com.nashtech.rookies.AssetManagement.exceptions.ResourceNotFoundException;
import com.nashtech.rookies.AssetManagement.model.dto.respond.AssignmentDetailRespondDTO;
import com.nashtech.rookies.AssetManagement.model.entities.Assignment;
import com.nashtech.rookies.AssetManagement.repository.AccountsRepository;
import com.nashtech.rookies.AssetManagement.repository.AssetRepository;
import com.nashtech.rookies.AssetManagement.repository.AssignmentRepository;
import com.nashtech.rookies.AssetManagement.security.UserLocal;
import com.nashtech.rookies.AssetManagement.services.impl.AssignmentServiceImpl;
import com.nashtech.rookies.AssetManagement.utils.MyDateUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class AssignmentServiceTest {

    private ModelMapper modelMapper;

    private AssignmentRepository assignmentRepository;

    private UserLocal userLocal;

    private AssetRepository assetRepository;

    private AccountsRepository accountsRepository;

    private MyDateUtils myDateUtils;

    private AssignmentServiceImpl assignmentServiceImpl;


    @BeforeEach
    public void initTest(){
        modelMapper = mock(ModelMapper.class);
        assignmentRepository = mock(AssignmentRepository.class);
        userLocal = mock(UserLocal.class);
        assetRepository = mock(AssetRepository.class);
        accountsRepository = mock(AccountsRepository.class);
        myDateUtils = mock(MyDateUtils.class);
        assignmentServiceImpl = new AssignmentServiceImpl(modelMapper,assignmentRepository,userLocal,assetRepository,accountsRepository,myDateUtils);
    }


    @Test
    public void getAssignmentById_shouldThrowResourceNotFoundException_whenNotFoundAssignment(){
        when(assignmentRepository.findById(0)).thenReturn(Optional.empty());
        ResourceNotFoundException exception = Assertions.assertThrows(ResourceNotFoundException.class,
                () -> assignmentServiceImpl.getAssignmentById(0)
        );
        assertThat(exception.getMessage(), is("Assignment Not Found With ID: 0"));
    }

//    @Test
//    public void getAssignmentById_shouldReturnAssignment_whenAssignmentFound(){
//
////        Assignment assignment = new Assignment(myDateUtils.getDate("27/08/2022"),
////                "Lunar New Year Festival often falls between late January andrly a month Lunar New Year Festival often falls " +
////                        "between late January andrly a month Lunar New Year Festival often falls between late January andrly ", "WAITING_FOR_ACCEPTANCE",
////                null);
////
////        Optional<Assignment> assignmentOptional = Optional.of(assignment);
////
////        when(assignmentRepository.findById(54)).thenReturn(assignmentOptional);
////
////        Optional<AssignmentDetailRespondDTO> detailRespondDTO = assignmentOptional.map(AssignmentDetailRespondDTO::new);
////
////        when(assignmentOptional.map(AssignmentDetailRespondDTO::new)).thenReturn(re);
//
//
//    }


}
