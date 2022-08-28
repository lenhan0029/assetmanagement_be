package com.nashtech.rookies.AssetManagement.model.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "assignments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Assignment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_assignment")
    private int assignmentId;

    @Column(name = "assigned_date")
    private Date assignedDate;

    @Column(name = "note")
    private String note;

    @Column(name = "states")
    private String state;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_account")
    private Accounts accounts;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="creator")
    private Accounts creators;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="id_asset")
    private Asset asset;

    @Column(name = "return_day")
    private Date returnedDay;

    @OneToOne(mappedBy = "requestAssignment", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private Request request;

    public Assignment(Date assignedDate, String note, String state, Date returnedDay) {
        this.assignedDate = assignedDate;
        this.note = note;
        this.state = state;
        this.returnedDay = returnedDay;
    }

}
