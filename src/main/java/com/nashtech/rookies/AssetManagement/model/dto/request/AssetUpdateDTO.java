package com.nashtech.rookies.AssetManagement.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssetUpdateDTO {

    @NotEmpty(message = "asset name is required")
    private String assetName;

    private String specification;

    @NotEmpty(message = "install day is required")
    private String installedDate;

    @NotEmpty(message = "state is required")
    private String state;
}
