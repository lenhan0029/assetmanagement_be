package com.nashtech.rookies.AssetManagement.services.impl;

import com.nashtech.rookies.AssetManagement.model.dto.AccountDto;
import com.nashtech.rookies.AssetManagement.model.dto.respond.AccountResponseDto;
import com.nashtech.rookies.AssetManagement.model.dto.InformationDto;
import com.nashtech.rookies.AssetManagement.model.dto.RoleDto;
import com.nashtech.rookies.AssetManagement.model.entities.Accounts;
import com.nashtech.rookies.AssetManagement.model.entities.Assignment;
import com.nashtech.rookies.AssetManagement.model.entities.Information;
import com.nashtech.rookies.AssetManagement.model.entities.Role;
import com.nashtech.rookies.AssetManagement.repository.AssignmentRepository;
import com.nashtech.rookies.AssetManagement.repository.InformationRepository;
import com.nashtech.rookies.AssetManagement.repository.RoleRepository;
import com.nashtech.rookies.AssetManagement.services.InformationService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.nashtech.rookies.AssetManagement.utils.MyDateUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class InformationServiceImpl implements InformationService {
	@Autowired
	private AssignmentRepository assignmentRepository;

	@Autowired
	private InformationRepository informationRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private MyDateUtils myDateUtils;
	//    @Override
//    public ResponseEntity<?> getInformationById(int id) {
//        return ResponseEntity.ok(this.informationRepository.getInformationByAccountsID(id));
//    }
	@Override
	public ResponseEntity<?> getInformationByAccountId(int id) {
		Information information = informationRepository.getInformationByAccountsID(id);
//    	RoleDto roleDto = modelMapper.map(information.getAccounts().getRole(), RoleDto.class);
//    	AccountDto accountDto = modelMapper.map(information.getAccounts(), AccountDto.class);
//    	accountDto.setRole(roleDto);
		InformationDto informationDto = modelMapper.map(information, InformationDto.class);
		informationDto.setRoleId(information.getAccounts().getRole().getRoleId());
		informationDto.setJoinedDate(information.getJoinDate());
		informationDto.setRoleName(information.getAccounts().getRole().getRoleName());
		informationDto.setUserName(information.getAccounts().getUserName());
		informationDto.setAccountId(id);
		return ResponseEntity.ok(informationDto);
	}


	@Override
	public ResponseEntity<?> updateInformation(int id, InformationDto informationDto){
		Optional<Information> information = informationRepository.findById(id);
		Role role = roleRepository.findById(informationDto.getRoleId()).get();
		Accounts account = information.get().getAccounts();
		account.setRole(role);

		Information newInformation = information.get();
		newInformation.setDateOfBirth(informationDto.getDateOfBirth());
		newInformation.setGender(informationDto.getGender());
		newInformation.setAccounts(account);
		newInformation.setJoinDate(informationDto.getJoinedDate());
		Information updatedInformation = this.informationRepository.save(newInformation);
		InformationDto updatedinformationDto = modelMapper.map(updatedInformation, InformationDto.class);
		updatedinformationDto.setRoleId(updatedInformation.getAccounts().getRole().getRoleId());
		updatedinformationDto.setAccountId(account.getAccountId());
		updatedinformationDto.setRoleName(role.getRoleName());
		updatedinformationDto.setUserName(account.getUserName());
		updatedinformationDto.setJoinedDate(updatedInformation.getJoinDate());


		Date joinDateNewInfo = updatedInformation.getJoinDate();
		List<Assignment> listAssignment = updatedInformation.getAccounts().getAssignments();

		if(listAssignment == null){
			return ResponseEntity.ok(updatedinformationDto);
		}

		listAssignment.forEach(assignment -> {
			if(assignment.getAssignedDate().getTime() < joinDateNewInfo.getTime() ){
				assignment.setAssignedDate(joinDateNewInfo);
				this.assignmentRepository.save(assignment);
			}
		});



		return ResponseEntity.ok(updatedinformationDto);
	}



	@Override
	public ResponseEntity<?> getInformationByLocation(int id) {
		List<Information> entities = informationRepository.getInformationByLocation(id);
		List<AccountResponseDto> dto  = entities.stream().map(information ->
				modelMapper.map(information, AccountResponseDto.class))
				.collect(Collectors.toList());
		for(int i = 0; i < dto.size(); i++) {
			dto.get(i).setAccountId(entities.get(i).getAccounts().getAccountId());
			dto.get(i).setRoleId(entities.get(i).getAccounts().getRole().getRoleId());
			dto.get(i).setRoleName(entities.get(i).getAccounts().getRole().getRoleName());
		}
		return ResponseEntity.ok(dto);
	}
}
