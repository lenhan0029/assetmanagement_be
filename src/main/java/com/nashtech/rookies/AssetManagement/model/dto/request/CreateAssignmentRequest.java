package com.nashtech.rookies.AssetManagement.model.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAssignmentRequest {

    private String note;

    @NotNull(message = "assigned account is required")
    private int assignedToId;


    @NotNull(message = "assign day is required")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date assignedDate;

    @NotNull(message = "asset is required")
    private int assetId;
}
