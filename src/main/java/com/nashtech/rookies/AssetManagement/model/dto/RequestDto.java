package com.nashtech.rookies.AssetManagement.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.nashtech.rookies.AssetManagement.model.entities.Accounts;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Date;
@Getter
@Setter
public class RequestDto {
    private int requestId;
    private String state;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date returnedDate;
    private String requestedBy;
    private String acceptedBy;
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date assignedDate;
    private String assetCode;
    private String assetName;

}
