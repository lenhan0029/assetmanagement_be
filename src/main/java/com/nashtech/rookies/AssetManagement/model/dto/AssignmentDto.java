package com.nashtech.rookies.AssetManagement.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nashtech.rookies.AssetManagement.model.dto.AccountDto;
import com.nashtech.rookies.AssetManagement.model.dto.AssetDto;
import com.nashtech.rookies.AssetManagement.model.entities.Assignment;
import lombok.*;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssignmentDto {

	private String assetCode;

	private String assetName;

	private String assignedTo;

	private String assignedBy;

	@Pattern(regexp = "\\d*", message = "Assignment id must be a number")
	private Integer assignmentId;

	private String specification;

	@NotEmpty(message = "Assigned date must not be empty")
	@JsonFormat(pattern = "dd/MM/yyyy")
	private Date assignedDate;

	@NotEmpty(message = "Asset code must not be empty")
	private String note;

	@NotEmpty(message = "State must not be empty")
	private String state;

	public AssignmentDto(Assignment assignment){

		ModelMapper modelMapper = new ModelMapper();

		modelMapper.map(assignment, this);
		this.setAssignedTo(assignment.getAccounts().getUserName());
		this.setAssignedBy(assignment.getCreators().getUserName());
		this.setAssetName(assignment.getAsset().getAssetName());
		this.setAssetCode(assignment.getAsset().getAssetCode());
		this.setSpecification(assignment.getAsset().getSpecification());

	}



}
