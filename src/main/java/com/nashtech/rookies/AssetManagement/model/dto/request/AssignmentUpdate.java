package com.nashtech.rookies.AssetManagement.model.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssignmentUpdate {

    @NotNull(message = "assigned id is required")
    private int assignedId;

    @NotNull(message = "asset id is required")
    private int assetId;

    private String note;

    @NotNull(message = "assigned date is required")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date assignedDate;
}
