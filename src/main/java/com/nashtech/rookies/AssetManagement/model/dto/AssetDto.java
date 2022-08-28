package com.nashtech.rookies.AssetManagement.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nashtech.rookies.AssetManagement.model.dto.respond.AssignmentRespondDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.sql.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssetDto {

	@Pattern(regexp = "\\d*", message = "Asset id must be a number")
	private Integer assetId;
	@NotEmpty(message = "Asset code must not be empty")
	private String assetCode;
	@NotEmpty(message = "Asset name must not be empty")
	private String assetName;
	@NotEmpty(message = "Specification must not be empty")
	private String specification;

	@JsonFormat(pattern = "dd/MM/yyyy")
	@NotEmpty(message = "Installed date must not be empty")
	private Date installedDate;
	@NotEmpty(message = "Location must not be empty")
	private String location;
	@NotEmpty(message = "State must not be empty")
	private String state;
	@NotEmpty(message = "Category must not be empty")
	private String categoryName;

	private List<AssignmentRespondDTO> assignmentDtoList;
}
