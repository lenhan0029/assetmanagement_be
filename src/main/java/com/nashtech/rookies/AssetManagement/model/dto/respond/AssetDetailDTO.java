package com.nashtech.rookies.AssetManagement.model.dto.respond;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssetDetailDTO {

    @NotNull(message = "asset id must not be null")
    private int assetId;

    @NotNull(message = "asset code must not be null")
    private String assetCode;

    @NotNull(message = "asset name must not be null")
    private String assetName;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date installedDate;

    @NotNull(message = "location must not be null")
    private String location;

    @NotNull(message = "state must not be null")
    private String state;

    @NotNull(message = "category name must not be null")
    private String categoryName;

}
