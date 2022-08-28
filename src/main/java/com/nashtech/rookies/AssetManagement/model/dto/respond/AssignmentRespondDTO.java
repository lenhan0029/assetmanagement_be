package com.nashtech.rookies.AssetManagement.model.dto.respond;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssignmentRespondDTO {

    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date assignedDate;

    private String assignedTo;

    private String assignedBy;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date returnedDay;
}
