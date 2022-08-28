package com.nashtech.rookies.AssetManagement.model.dto.respond;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.nashtech.rookies.AssetManagement.model.entities.Assignment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssignmentDetailRespondDTO {

    @NotNull(message = "Assigned name must not be null")
    private String assignedBy; // user nhan

    private int assignedId;
    @NotNull(message = "assigned id must not be null")
    private String assignedTo;

    private String assignedName;

    @NotNull(message = "asset code must not be null")
    private String assetCode;

    @NotNull(message = "Asset name must not be null")
    private String assetName;

    @NotNull(message = "note must not be null")
    private String note;

    @NotNull(message = "asset code must not be null")
    private String state;

    @NotNull(message = "Asset id must not be null")
    private int assetId;


    @JsonFormat(pattern = "dd/MM/YYYY")
    @NotNull(message = "Assigned Date must not be null")
    private Date assignedDate;

    @NotNull(message = "specification must not be null !")
    private String specification;

    public AssignmentDetailRespondDTO(Assignment assignment){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.map(assignment, this);
        this.setAssignedBy(assignment.getCreators().getUserName());
        this.setAssignedId(assignment.getAccounts().getAccountId());
        this.setAssignedTo(assignment.getAccounts().getUserName());
        this.setAssetCode(assignment.getAsset().getAssetCode());
        this.setAssetName(assignment.getAsset().getAssetName());
        this.setAssetId(assignment.getAsset().getAssetId());
        this.setSpecification(assignment.getAsset().getSpecification());
        this.setAssignedName(assignment.getAccounts().getInformation().getFirstName() + " " + assignment.getAccounts().getInformation().getLastName());
    }




}
