package com.nashtech.rookies.AssetManagement.model.dto.respond;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nashtech.rookies.AssetManagement.model.AssignmentState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssignmentDetailDto {

    private int assignmentId;

    @JsonFormat(pattern = "dd/MM/YYYY")
    private Date assignedDate;

    @NotNull(message = "note must not be null")
    private String note;

    @NotNull(message = "state must not be null")
    private String state;

    @NotNull(message = "asset code must not be null")
    private String assetCode;

    @NotNull(message = "asset name must not be null")
    private String assetName;

    @NotNull(message = "assigned to name must not be null")
    private String assignedTo;

    @NotNull(message = "assigned by name must not be null")
    private String assignedBy;

    @NotNull(message = "assigned to id must not be null")
    private int assignedToIdUser;

    private int assignedByIdUser;

    private  String specification;

}
