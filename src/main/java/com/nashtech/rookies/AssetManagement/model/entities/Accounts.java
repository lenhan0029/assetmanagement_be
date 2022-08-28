package com.nashtech.rookies.AssetManagement.model.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "accounts")
public class Accounts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_account")
    private int accountId;

    @Column(name = "user_name")
    @NotEmpty(message = "cannot generate username")
    private String userName;

    @Column(name = "passwords")
    @NotEmpty(message = "cannot generate password")
    private String password;

    @Column(name = "status")
    private boolean status;

    @Column(name="first_login")
    private boolean firtLogin;

    @ManyToOne(fetch = FetchType.LAZY)
//    @JsonIgnore
    @JoinColumn(name = "id_role")
    private Role role;

    @OneToOne(mappedBy = "accounts", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private Information information;

    @OneToMany(mappedBy = "accounts", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Assignment> assignments;

    @OneToMany(mappedBy = "creators", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Assignment> assignmentCreator;

    @OneToMany(mappedBy = "requestBy", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Request> requestsCreator;


    @OneToMany(mappedBy = "acceptBy", fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Request> requestAccept;



	public Accounts(String userName, String password, boolean status, boolean firtLogin, Role role) {
		this.userName = userName;
		this.password = password;
		this.status = status;
		this.firtLogin = firtLogin;
		this.role = role;
	}

    public Accounts(String userName, String password, Role role) {
        this.userName = userName;
        this.password = password;
        this.role = role;
    }

    

}
