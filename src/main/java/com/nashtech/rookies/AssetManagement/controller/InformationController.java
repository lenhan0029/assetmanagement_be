package com.nashtech.rookies.AssetManagement.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nashtech.rookies.AssetManagement.model.dto.InformationDto;
import com.nashtech.rookies.AssetManagement.services.InformationService;

import io.swagger.v3.oas.annotations.Operation;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/information")
public class InformationController {


	private final InformationService informationService;

	@Autowired
	public InformationController(InformationService informationService) {
		this.informationService = informationService;
	}

	@Operation(summary  = "Get information by account id",
			description = "you must provide param accountid in url"
					+ "response : 200 OK -> get information success "
					+ "response : 404 Not found -> there is no information with given accountid")
	@GetMapping
	public ResponseEntity<?> getInformationByAccountId(@RequestParam(name = "accountid") Integer id){
		return informationService.getInformationByAccountId(id);
	}

	@Operation(summary  = "Edit information",
			description = "you must provide id information you want to edit in url"
					+ ", fields can change: dateOfBirth, gender, JoinedDate, Type"
					+ "response : 200 OK -> edit information success "
					+ "response : 400 Bad request -> Validation Error"
					+ "response : 500 Internal Server Error -> No value present"
	)
	@PutMapping("/{id}")
	public ResponseEntity<?> updateInformation(@PathVariable Integer id, @RequestBody @Valid InformationDto informationDto){
		return informationService.updateInformation(id,informationDto);
	}
	@Operation(summary = "Get All user with location same as user have id given",
			description = "accountid you want to get user have location same as must be given" +
					"Here are few situation that can happend when using API: " +
					"If the response status code is 404 -> You have entered an non-exist or not availabel state. " +
					"If the response status is 200 -> get list user successfully, return list user"
	)
	@GetMapping("/location")
	public ResponseEntity<?> getAllInformation(@RequestParam(name = "accountid") Integer id){
		return informationService.getInformationByLocation(id);
	}


}
