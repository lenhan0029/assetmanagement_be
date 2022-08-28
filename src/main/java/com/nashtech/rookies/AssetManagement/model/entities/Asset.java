package com.nashtech.rookies.AssetManagement.model.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.nashtech.rookies.AssetManagement.model.AssetState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "assets")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Asset {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_asset")
    private int assetId;

    @Column(name = "asset_code")
    private String assetCode;

    @Column(name = "asset_name")
    private String assetName;

    @Column(name = "specification")
    private String specification;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
	@Temporal(TemporalType.DATE)
    @Column(name = "installed_date")
    private Date installedDate;

    @Column(name = "locations")
    private String location;

    @Column(name = "states")
//    @Enumerated(EnumType.STRING)
    private String state;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_categery")
//    @JsonIgnore
    private Category category;

    @OneToMany(mappedBy = "asset", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    @JsonIgnore
    private List<Assignment> assignments;

}
