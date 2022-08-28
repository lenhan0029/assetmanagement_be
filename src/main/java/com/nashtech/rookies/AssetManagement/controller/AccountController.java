package com.nashtech.rookies.AssetManagement.controller;

import javax.validation.Valid;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nashtech.rookies.AssetManagement.model.dto.request.ChangePasswordDTO;
import com.nashtech.rookies.AssetManagement.model.dto.request.CreateUserRequest;
import com.nashtech.rookies.AssetManagement.model.dto.respond.UserRespondDTO;
import com.nashtech.rookies.AssetManagement.repository.AccountsRepository;
import com.nashtech.rookies.AssetManagement.services.AccountsService;
import com.nashtech.rookies.AssetManagement.services.AdminService;
import com.nashtech.rookies.AssetManagement.services.InformationService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/account")
public class AccountController {
    private final AccountsService accountsService;

    private final AccountsRepository accountsRepository;

    private final AdminService adminService;

    @Autowired
    private InformationService informationService;

    @Autowired
    public AccountController(AccountsService accountsService, AccountsRepository accountsRepository, AdminService adminService) {
        this.accountsService = accountsService;
        this.accountsRepository = accountsRepository;
        this.adminService = adminService;
    }

    @Operation( summary = "Change Password" ,
            description = "To use this API, please notice that the id we use is account id. " +
                    "In this API, you have to enter both old password and new password, and account id in url. " +
                    "For Ex : http://{url}/api/account/99. In this case, 99 is account id that we need to provide " +
                    "If the response status code is 400, you will see the error description message" +
                    "If the response status code is 200, the password has been changed successfully "

    )
    @PutMapping("/{id}")
    public ResponseEntity<?> updateAccount(@PathVariable Integer id, @RequestBody @Valid ChangePasswordDTO changePasswordDTO){
        return accountsService.changePassword(id,changePasswordDTO);
    }

    @Operation(summary = "Create New User",
            description = "API to create new user after fill all required form. Please notice that some validation we need" +
                    "has been processed in front end, so I wont repeat it in API. Here are few situation that can happend when using API:" +
                    "Fail case : " +
                    "If the response status code is 404, this mean the role you entered is wrong and not exist (only admin and staff available)" +
                    "If the response status code is 400, this mean one of required field is empty and you need to fill it all." +
                    "Or if you already fill all the field but still get the 400 errors, it will be one of the following case in the AC " +
                    "* Notice : I provide two fields about username and password after generate if create new user success to make it easier for you to test, however it will not appears in offical version"
    )
    @PostMapping
    public UserRespondDTO createNewUser(@Valid @RequestBody CreateUserRequest createUserRequest){
        return adminService.createNewUser(createUserRequest);
    }


    @Operation(summary  = "Get list Account",
            description = "provide code is mean 'StaffCode or 'usernamer', page is mean 'number page', filter is mean 'namerole'"
                    + "response : 200 OK -> list account "
                    + "response : 404 Not found -> page empty"
                    + "response : 400 Bad request -> provide is error")

    @GetMapping
    public ResponseEntity<?>  listAccountDetailBySearch(
            @RequestParam(name = "code",defaultValue = "%", required = false) String searchCode,
            @RequestParam(name = "page", defaultValue = "0", required = false) String page,
            @RequestParam(name = "filter", defaultValue = "%", required = false) String filter,
            @RequestParam(name = "sort", defaultValue = "fa", required = false) String sort){
        int pageConverted = Integer.parseInt(page.trim());
        return accountsService.listAccountDetailBySearch(searchCode, pageConverted, filter, sort);
    }



    @Operation(summary  = "Disable Account",
            description = "provide code is mean 'id account' do you want to delete"
                    + "response : 200 OK -> disable account success "
                    + "response : 404 Not found -> page empty"
                    + "response : 400 Bad request -> provide is error")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> disableAccount(@PathVariable Integer id){
        return accountsService.disableAccount(id);
    }


    @Operation( summary = "Delete User By user name",
            description = "Here are some case when testing : " +
                    "If the response status code is 405, this mean you cannot delete yourself when you still log in your account " +
                    "If the status code is 404, this mean the username you enter is not exist " +
                    "If the status code is 200, this mean the account with this username has been deleted successfully"
    )
    @DeleteMapping
    public ResponseEntity<?> deleteUser(@RequestParam(name = "username", defaultValue = "abcdefg") String userName)
    {
        return this.accountsService.deleteUser(userName);
    }

    @DeleteMapping("/duplicate")
    public ResponseEntity<?> deleteDuplicate(){
        return this.adminService.deleteDuplicate();
    }

    @DeleteMapping("/duplicate/username")
    public ResponseEntity<?> deleteDuplicateUserName(){
        this.accountsRepository.deleteDuplicateUserName();
        return ResponseEntity.ok("Complete Delete Duplicate User Name");
    }
}
