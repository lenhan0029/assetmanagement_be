package com.nashtech.rookies.AssetManagement.services.impl;

import com.nashtech.rookies.AssetManagement.exceptions.ApiDeniedException;
import com.nashtech.rookies.AssetManagement.exceptions.ConstraintViolateException;
import com.nashtech.rookies.AssetManagement.exceptions.ResourceNotFoundException;
import com.nashtech.rookies.AssetManagement.model.dto.request.ChangePasswordDTO;
import com.nashtech.rookies.AssetManagement.model.dto.respond.AccountDetailDto;
import com.nashtech.rookies.AssetManagement.model.dto.respond.MessageResponse;
import com.nashtech.rookies.AssetManagement.model.entities.Accounts;
import com.nashtech.rookies.AssetManagement.model.entities.Assignment;
import com.nashtech.rookies.AssetManagement.repository.AccountsRepository;
import com.nashtech.rookies.AssetManagement.repository.AssignmentRepository;
import com.nashtech.rookies.AssetManagement.security.UserLocal;
import com.nashtech.rookies.AssetManagement.services.AccountsService;
import com.nashtech.rookies.AssetManagement.utils.MyDateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class AccountsServiceImpl implements AccountsService {
	private final AccountsRepository accountsRepository;

	private final AssignmentRepository assignmentRepository;

	private final MyDateUtils myDateUtils;



	private final UserLocal userLocal;

	private final PasswordEncoder encoder;

	public AccountsServiceImpl(AccountsRepository accountsRepository, PasswordEncoder encoder,
							   AssignmentRepository assignmentRepository, MyDateUtils myDateUtils, UserLocal userLocal) {
		this.accountsRepository = accountsRepository;
		this.assignmentRepository = assignmentRepository;
		this.encoder = encoder;
		this.myDateUtils = myDateUtils;
		this.userLocal = userLocal;
	}

	@Override
	public ResponseEntity<?> changePassword(Integer id, ChangePasswordDTO changePasswordDTO) {
		Optional<Accounts> accounts = accountsRepository.findById(id);
		if (!accounts.isPresent()) {
			throw new ResourceNotFoundException("Account Not Found");
		}
		if (!accounts.get().isFirtLogin()) {
			if (!encoder.matches(changePasswordDTO.getOld_password(), accounts.get().getPassword())) {
				return ResponseEntity.badRequest()
						.body(new MessageResponse("Password is incorrect", HttpStatus.BAD_REQUEST.value()));
			}
			if (encoder.matches(changePasswordDTO.getNew_password(), accounts.get().getPassword())) {
				return ResponseEntity.badRequest()
						.body(new MessageResponse("Password not changed", HttpStatus.BAD_REQUEST.value()));
			}
			if (changePasswordDTO.getNew_password().length() > 15 || changePasswordDTO.getNew_password().length() < 8) {
				return ResponseEntity.badRequest().body(new MessageResponse("Password characters longer than specified",
						HttpStatus.BAD_REQUEST.value()));
			}
			Pattern p = Pattern.compile("[^A-Za-z0-9]");
			Matcher m = p.matcher(changePasswordDTO.getNew_password());
			boolean b = m.find();
			if (!b)
				return ResponseEntity.badRequest().body(new MessageResponse(
						"Invalid password - Must have a special character", HttpStatus.BAD_REQUEST.value()));
			char[] password = changePasswordDTO.getNew_password().toCharArray();
			boolean check_Lower = false, check_Upper = false;
			boolean check_Number = false;
			for (char c : password) {
				if (Character.isLowerCase(c))
					check_Lower = true;
				if (Character.isUpperCase(c))
					check_Upper = true;
				if (Character.isDigit(c))
					check_Number = true;
			}
			if (!check_Lower)
				return ResponseEntity.badRequest().body(new MessageResponse(
						"Invalid password - Must have a Lower Case character", HttpStatus.BAD_REQUEST.value()));
			if (!check_Upper)
				return ResponseEntity.badRequest().body(new MessageResponse(
						"Invalid password - Must have an Upper Case character.", HttpStatus.BAD_REQUEST.value()));
			if (!check_Number)
				return ResponseEntity.badRequest().body(new MessageResponse(
						"Invalid password - Must have an Number character.", HttpStatus.BAD_REQUEST.value()));
			String passwordNew = encoder.encode(changePasswordDTO.getNew_password());
			accounts.get().setPassword(passwordNew);
			accountsRepository.save(accounts.get());
		} else {
			if (encoder.matches(changePasswordDTO.getNew_password(), accounts.get().getPassword())) {
				return ResponseEntity.badRequest()
						.body(new MessageResponse("Password not changed", HttpStatus.BAD_REQUEST.value()));
			}
			if (changePasswordDTO.getNew_password().length() > 15 || changePasswordDTO.getNew_password().length() < 8) {
				return ResponseEntity.badRequest().body(new MessageResponse("Password characters longer than specified",
						HttpStatus.BAD_REQUEST.value()));
			}
			Pattern p = Pattern.compile("[^A-Za-z0-9]");
			Matcher m = p.matcher(changePasswordDTO.getNew_password());
			boolean b = m.find();
			if (!b)
				return ResponseEntity.badRequest().body(new MessageResponse(
						"Invalid password - Must have a special character", HttpStatus.BAD_REQUEST.value()));
			char[] password = changePasswordDTO.getNew_password().toCharArray();
			boolean check_Lower = false, check_Upper = false;
			boolean check_Number = false;
			for (char c : password) {
				if (Character.isLowerCase(c))
					check_Lower = true;
				if (Character.isUpperCase(c))
					check_Upper = true;
				if (Character.isDigit(c))
					check_Number = true;
			}
			if (!check_Lower)
				return ResponseEntity.badRequest().body(new MessageResponse(
						"Invalid password - Must have a Lower Case character", HttpStatus.BAD_REQUEST.value()));
			if (!check_Upper)
				return ResponseEntity.badRequest().body(new MessageResponse(
						"Invalid password - Must have an Upper Case character.", HttpStatus.BAD_REQUEST.value()));
			if (!check_Number)
				return ResponseEntity.badRequest().body(new MessageResponse(
						"Invalid password - Must have an Number character.", HttpStatus.BAD_REQUEST.value()));
			String passwordNew = encoder.encode(changePasswordDTO.getNew_password());
			if (accounts.get().isFirtLogin()) {
				accounts.get().setFirtLogin(false);
			}
			accounts.get().setPassword(passwordNew);
			accountsRepository.save(accounts.get());
		}
		return ResponseEntity.ok().body(String.format("Your password has been changed successfully"));
	}

	@Override
	public ResponseEntity<?> listAccountDetailBySearch(String searchCode, int page, String filter, String sort) {
		String userName = userLocal.getLocalUserName();

		String locations = this.accountsRepository.findLocationByUserName(userName);

		if (!searchCode.equals("")) {
			Pageable newPage = createPage(page, 20, sort);

			String[] searchCodeArr = searchCode.split("\\s+");
			String searchCodeAfter = "";

			for(String s : searchCodeArr)
			{
				if(!s.equals(""))
				{
					searchCodeAfter = searchCodeAfter + s.toLowerCase() + " ";
					//Make the string become fullname with all in lower case to search.
				}
			}

			searchCodeAfter = searchCodeAfter.replaceAll(".$","");
			// Because always have an " " at the end of string, so I delete it

			System.out.println(searchCodeAfter);

			Page<AccountDetailDto> pageAccount = this.accountsRepository
					.listAccountDetailBySearch(searchCodeAfter, newPage, locations, filter, userName);
			if (pageAccount.hasContent())
			{
				return ResponseEntity.ok(pageAccount);
			}
			throw new ResourceNotFoundException("This page is empty");
		}
		throw new ConstraintViolateException("Please enter at least one character");
	}

	public Pageable createPage(int page, int size, String sortBy) {

		char[] sortByte = sortBy.toCharArray();

		char sortMode = sortByte[1];
		char sortCol = sortByte[0];

		String sortName ;

		Sort sort ;

		switch (sortCol){
			case 'f':
				sortName = "i.firstName";
				break;
			case 's':
				sortName = "i.staffCode";
				break;
			case 'j' :
				sortName = "i.joinDate";
				break;
			case 'u':
				sortName = "r.roleName";
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

	@Override
	public ResponseEntity<?> disableAccount(Integer id) {
		Optional<Accounts> accounts = accountsRepository.findById(id);
		if (!accounts.isPresent()) {
			throw new ResourceNotFoundException("Account Not Found");
		}
		Accounts accounts2 = accounts.get();
		if(accounts2.isStatus()==false) {
			throw new ResourceNotFoundException("Account Not Found");
		}
		List<Assignment> listAsignment = accounts2.getAssignments();
		for (int i = 0; i < listAsignment.size(); i++) {
			String stateAssignment = listAsignment.get(i).getState().toString().toLowerCase();
			if (stateAssignment.equals("accepted") || stateAssignment.equals("waiting_for_acceptance") || stateAssignment.equals("waiting_for_returning")) {
				return ResponseEntity.badRequest().body(new MessageResponse(
						"Account have Assignment disable account fail !", HttpStatus.BAD_REQUEST.value()));
			}
		}
		accounts2.setStatus(false);
		accountsRepository.save(accounts2);
		return ResponseEntity.ok().body(new MessageResponse(
				"Disable Account successfully !", HttpStatus.OK.value()));

	}

	@Override
	public ResponseEntity<?> isAssigned(int id) {
		List<Assignment> listAssignmentBelongToAccount = this.assignmentRepository.getListAssignmentByAccountId(id);
		Optional<Accounts> accounts = this.accountsRepository.findById(id);

		if(!accounts.isPresent()){
			throw new ResourceNotFoundException("User not detected");
		}
		if(!listAssignmentBelongToAccount.isEmpty())
		{
			for(int i = 0; i <listAssignmentBelongToAccount.size(); i++)
			{
				String stateAssignment = listAssignmentBelongToAccount.get(i).getState().toString().toLowerCase();
				if(stateAssignment.equals("accepted") ||  stateAssignment.equals("waiting_for_acceptance") || stateAssignment.equals("waiting_for_returning"))
				{
					throw new ConstraintViolateException("User cannot delete due to assignment already exist"); // Co assignment
				}
			}
		}
		return ResponseEntity.ok(new MessageResponse("Can disable user", HttpStatus.OK.value())); // Khong co

	}

	@Override
	public ResponseEntity<?> deleteUser(String userName) {
		String localUserName = userLocal.getLocalUserName();
		Optional<Accounts> accountsOptional = this.accountsRepository.findByuserName(userName);

		if(!localUserName.equals(userName))
		{
			if (accountsOptional.isPresent())
			{
				this.accountsRepository.delete(accountsOptional.get());
				return ResponseEntity.ok(new MessageResponse("Account Delete Success", HttpStatus.OK.value()));
			}
			throw new ResourceNotFoundException("Account Not Found with use name : " + userName);
		}
		throw new ApiDeniedException("You have to sign out and log in another account before delete yourself");
	}


}
