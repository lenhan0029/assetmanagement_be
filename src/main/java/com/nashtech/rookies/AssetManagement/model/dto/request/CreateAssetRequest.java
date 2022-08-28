package com.nashtech.rookies.AssetManagement.model.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAssetRequest {

    @JsonProperty("prefix")
    @NotEmpty(message = "Prefix is required")
    private String assetPrefix;

    @JsonProperty("name")
    @NotEmpty(message = "Name is required")
    private String assetName;

    @JsonProperty("specification")
    private String specification;

    @JsonProperty("install")
    @NotEmpty(message = "Install Date is required")
    private String installedAt;

    @NotEmpty(message = "status is required")
    private String status;

}
