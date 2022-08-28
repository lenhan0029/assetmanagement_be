package TestService;

import com.nashtech.rookies.AssetManagement.exceptions.ResourceNotFoundException;
import com.nashtech.rookies.AssetManagement.model.dto.request.ChangePasswordDTO;
import com.nashtech.rookies.AssetManagement.model.dto.respond.MessageResponse;
import com.nashtech.rookies.AssetManagement.model.entities.Accounts;
import com.nashtech.rookies.AssetManagement.model.entities.Role;
import com.nashtech.rookies.AssetManagement.repository.AccountsRepository;
import com.nashtech.rookies.AssetManagement.repository.AssignmentRepository;
import com.nashtech.rookies.AssetManagement.security.UserLocal;
import com.nashtech.rookies.AssetManagement.services.impl.AccountsServiceImpl;
import com.nashtech.rookies.AssetManagement.utils.MyDateUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

public class AccountsServiceTest {
    private  AccountsServiceImpl accountsServiceImpl;

    private  AccountsRepository accountsRepository;

    private  AssignmentRepository assignmentRepository;

    private  MyDateUtils myDateUtils;

    private PasswordEncoder encoder;
    private  UserLocal userLocal;

    private Accounts account;

    private Role role;
    private ChangePasswordDTO changePasswordDTO;


    public AccountsServiceTest(){  }

    @BeforeEach
    public void beforeEach(){
        accountsRepository = mock(AccountsRepository.class);
        encoder = mock(PasswordEncoder.class);
        assignmentRepository = mock(AssignmentRepository.class);
        myDateUtils = mock(MyDateUtils.class);
        userLocal = mock(UserLocal.class);
        accountsServiceImpl = new AccountsServiceImpl(accountsRepository,encoder,assignmentRepository,myDateUtils,userLocal);
        account = mock(Accounts.class);
        role = mock(Role.class);

    }

    @Test
    public  void changePassword_NotFound_Account(){
        changePasswordDTO = mock(ChangePasswordDTO.class);
        when(accountsRepository.findById(1)).thenReturn(Optional.empty());
        ResourceNotFoundException exception = Assertions.assertThrows(ResourceNotFoundException.class,
                () -> accountsServiceImpl.changePassword(1,changePasswordDTO));
        assertThat(exception.getMessage() ,is( "Account Not Found"));
    }

    @Test
    public void changPassword_AccountNotFirstLoginAndPasswordIsIncorrect_IsBadRequest(){
        Accounts accounts = new Accounts("khanh", "Test12345@",true,false,role);
        when(accountsRepository.findById(1)).thenReturn(Optional.of(accounts));
        changePasswordDTO = new ChangePasswordDTO("Test12345@", "Test1234@");
        when(encoder.matches(changePasswordDTO.getOld_password() , accounts.getPassword())).thenReturn(false);
        ResponseEntity<?> result = accountsServiceImpl.changePassword(1,changePasswordDTO);
        assertThat(result.getStatusCode(), is(HttpStatus.BAD_REQUEST));
    }

    @Test
    public void changePassword_AccountNotFirstLoginandPasswordNotChanged_IsBadRequest(){
        Accounts accounts = new Accounts("khanh", "Test12345@",true,false,role);
        when(accountsRepository.findById(1)).thenReturn(Optional.of(accounts));
        changePasswordDTO = new ChangePasswordDTO("Test12345@", "Test12345@");
        when(encoder.matches(changePasswordDTO.getNew_password() , accounts.getPassword())).thenReturn(true);
        ResponseEntity<?> result = accountsServiceImpl.changePassword(1,changePasswordDTO);
        assertThat(result.getStatusCode(), is(HttpStatus.BAD_REQUEST));
    }

    @Test
    public void changePassword_AccountNotFirstLoginandLongerThaNewPasswordMoreThan15Characters_IsBadRequest(){
        Accounts accounts = new Accounts("khanh", "Test12345@",true,false,role);
        changePasswordDTO = new ChangePasswordDTO("Test12345@", "Test12345666666666666666666@");
        when(accountsRepository.findById(1)).thenReturn(Optional.of(accounts));
        when(encoder.matches(changePasswordDTO.getOld_password() , accounts.getPassword())).thenReturn(true);
        ResponseEntity<?> result = accountsServiceImpl.changePassword(1,changePasswordDTO);
        assertThat(result.getStatusCode(), is(HttpStatus.BAD_REQUEST));
    }
//    @Test
//    public void changePassword_AccountNotFirstLoginandLongerThaNewPasswordMinThan8Characters_IsBadRequest(){
//        Accounts accounts = new Accounts("khanh", "Test12345@",true,false,role);
//        when(accountsRepository.findById(1)).thenReturn(Optional.of(accounts));
//        changePasswordDTO = new ChangePasswordDTO("Test123@", "Te77@");
//        when(changePasswordDTO.getNew_password().length() < 8).thenReturn(false);
//        ResponseEntity<?> result = accountsServiceImpl.changePassword(1,changePasswordDTO);
//        assertThat(result.getStatusCode(), is(HttpStatus.BAD_REQUEST));
//    }

}
