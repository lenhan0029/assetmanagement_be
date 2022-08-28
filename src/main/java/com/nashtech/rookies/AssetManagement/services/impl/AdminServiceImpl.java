package com.nashtech.rookies.AssetManagement.services.impl;

import com.nashtech.rookies.AssetManagement.exceptions.ConstraintViolateException;
import com.nashtech.rookies.AssetManagement.exceptions.ResourceNotFoundException;
import com.nashtech.rookies.AssetManagement.model.dto.request.CreateAccountRequest;
import com.nashtech.rookies.AssetManagement.model.dto.request.CreateUserRequest;
import com.nashtech.rookies.AssetManagement.model.dto.respond.UserRespondDTO;
import com.nashtech.rookies.AssetManagement.model.entities.*;
import com.nashtech.rookies.AssetManagement.repository.*;
import com.nashtech.rookies.AssetManagement.security.UserLocal;
import com.nashtech.rookies.AssetManagement.services.AdminService;
import com.nashtech.rookies.AssetManagement.utils.MyDateUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {
    final long TIME_IN_18_YO = Long.parseLong("568025136000");
    // Because  the standard is larger than 18yo, I transfer 18y into miliseconds to compare;

    private AccountsRepository accountsRepository;
    private ModelMapper modelMapper;
    private RoleRepository roleRepository;
    private InformationRepository informationRepository;
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    private MyDateUtils myDateUtils;
    private UserLocal userLocal;
    String newPass = "";


    @Autowired
    public AdminServiceImpl(AccountsRepository accountsRepository, ModelMapper modelMapper,
                            RoleRepository roleRepository, InformationRepository informationRepository,
                            MyDateUtils myDateUtils, UserLocal userLocal) {
        this.accountsRepository = accountsRepository;
        this.modelMapper = modelMapper;
        this.roleRepository = roleRepository;
        this.informationRepository = informationRepository;
        this.myDateUtils = myDateUtils;
        this.userLocal = userLocal;
    }

    @Override
    public UserRespondDTO createNewUser(CreateUserRequest createNewUserRequest)
    {
        String userName = userLocal.getLocalUserName();
        String prefix = createNewUserRequest.getPrefix();
        String roleName = createNewUserRequest.getRoleName();

        Date dateOfBirthInput = createNewUserRequest.getDateOfBirth();
        Date joinDateInput = createNewUserRequest.getJoinDate();

        boolean isDayWeekend = myDateUtils.isWeekendDay(joinDateInput);
        Role role = this.roleRepository.findByRoleName(roleName);
        long timeInJoinDate = joinDateInput.getTime() - dateOfBirthInput.getTime();

        if(role == null) {
            throw new ResourceNotFoundException("Cannot find that role");
        }

        if(joinDateInput.getTime() < dateOfBirthInput.getTime()) {
            throw new ConstraintViolateException("Joined date is not later than Date of Birth." +
                    "Please select a different date");
        }

        if(timeInJoinDate < TIME_IN_18_YO) {
            throw new ConstraintViolateException("User is under 18. Please select a different date");
        }

        if(isDayWeekend) {
            throw new ConstraintViolateException("Joined date is Saturday or Sunday. Please select a different date");
        }

        Information info = modelMapper.map(createNewUserRequest, Information.class);


        String firstNameAfter = createNewUserRequest.getFirstName().replaceAll("\\s+", " ");
        String lastNameAfter = createNewUserRequest.getLastName().trim();
        info.setLastName(lastNameAfter);
        info.setFirstName(firstNameAfter);

        String fullName = info.getFirstName() + " " + info.getLastName();
        String dateInString = myDateUtils.getString(info.getDateOfBirth());
        CreateAccountRequest createAccountRequest = new CreateAccountRequest(fullName,dateInString, role);
        Accounts accounts = createNewAccount(createAccountRequest);
        info.setAccounts(accounts);
        info.setInformationId(0);
        System.out.println("Chick");
        info.setStaffCode(generateCode(prefix, "0000"));
        System.out.println("Chuck");
        String locations = createNewUserRequest.getLocations();
        if(roleName.equals("staff")){
            String adminLocation = this.informationRepository.getLocationByUserName(userName);
            locations = adminLocation;
        }
        info.setLocations(locations);

        Information newInfo =  this.informationRepository.save(info);
        UserRespondDTO userRespondDTO = modelMapper.map(newInfo, UserRespondDTO.class);
        userRespondDTO.setUserName(accounts.getUserName());
        userRespondDTO.setPass(newPass);
        userRespondDTO.setRoleName(roleName);
        userRespondDTO.setJoinedDate(newInfo.getJoinDate());


        userRespondDTO.setAccountId(accounts.getAccountId());
        return userRespondDTO;
    }

    @Override
    public ResponseEntity<?> deleteDuplicate() {
        this.accountsRepository.deleteDuplicateStaffCode();
        return ResponseEntity.ok("Good Delete");
    }


    public Accounts createNewAccount(CreateAccountRequest createAccountRequest) {
        String lastName = createAccountRequest.getFullName();
        System.out.println("Check");
        String userName = generateUserName(lastName);
        System.out.println("Chock");
        String passwordBefore = userName + "@" + createAccountRequest.getDateOfBirth();
        passwordBefore = passwordBefore.replaceAll("/","");
        newPass = passwordBefore;
        String password = encoder.encode(passwordBefore);
        Role role = createAccountRequest.getRole();
        Accounts accounts = new Accounts(userName,password,role);
        accounts.setStatus(true);
        accounts.setFirtLogin(true);
        accounts.setAccountId(0);
        return this.accountsRepository.save(accounts);
    }



    public String generateUserName(String fullName)
    {
        String[] nameSplitted = fullName.split("\\s+");
        //khoi bui xuan
        String userName = nameSplitted[0].toLowerCase();
        //khoi

        for(int i=1; i<nameSplitted.length;i++){
            userName = userName + nameSplitted[i].toLowerCase().charAt(0);
            //khoi=khoibx
        }

        List<Accounts> accountWithSameName = this.accountsRepository.findAccountWithSameName(userName);//khoibx khoibxc khoibxx


        for (int i =0; i<accountWithSameName.size();i++) {
            String checkUserName = accountWithSameName.get(i).getUserName();//khoibxc1
            checkUserName = checkUserName.replaceAll("\\d", "");//khoibxc

            if (!(checkUserName.equals(userName))) {
                accountWithSameName.remove(accountWithSameName.get(i));
                i--;
            }
        }

        if(accountWithSameName.size() == 1)//khoibxc1
        {
            return userName + 1;//khoibxc1
        }
        else if (accountWithSameName.size() == 0){
            return userName;
        }

        Accounts lastAccount = accountWithSameName.get(accountWithSameName.size()-1);
        String suffixUserName = lastAccount.getUserName().replaceAll("[A-Za-z]+","");
        suffixUserName = suffixUserName.replaceAll("\\s+","");

        userName += (Integer.parseInt(suffixUserName) + 1);

        return userName;
    }


    public String generateCode(String prefix, String number)
    {
        String staffCodeBefore = prefix + number;
        List<Information> listInforSamePrefix = this.informationRepository.getListInforSamePrefix(prefix);
        int staffCodeAdded = listInforSamePrefix.size() + 1; //If 0 then 1, If 1 then 2

        if(listInforSamePrefix.size() == 0 )
        {
            return prefix + "0001";
        }

        for(int i = staffCodeAdded; i >0; i /= 10)
        {
            staffCodeBefore = staffCodeBefore.substring(0, staffCodeBefore.length()-1); // If 1 then 000
        }


        Information lastInfoOfList = listInforSamePrefix.get(listInforSamePrefix.size()-1);//khoibx khoibx1 khoibx2 khoibx3 : 4 => khoibx khoibx1 khoibx3 => khoibx3
        String lastStaffCode = lastInfoOfList.getStaffCode();// khoibx3
        int staffCodeCompare = Integer.parseInt(lastStaffCode.replaceAll("[A-Za-z]+", "")); // 3
        System.out.println(staffCodeCompare + 1);//4

        return staffCodeBefore + (staffCodeCompare + 1); // If 1 then 00001
    }

}

// first name, username, role name.
