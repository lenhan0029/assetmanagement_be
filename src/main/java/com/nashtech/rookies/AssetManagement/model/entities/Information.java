package com.nashtech.rookies.AssetManagement.model.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "information")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Information {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_information")
    private int informationId;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "date_of_birth")
    private Date dateOfBirth;

    @Column(name = "staff_code")
    private String staffCode;

    @Column(name = "locations")
    private String locations;

    @Column(name="gender")
    private String gender;

    @Column(name="join_date")
    private Date joinDate;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_account")
//    @JsonIgnore
    private Accounts accounts;

}
