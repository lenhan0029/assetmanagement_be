package com.nashtech.rookies.AssetManagement.model.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "request")
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_request")
    private int requestId;

    @Column(name = "states")
    private String state;

    @Column(name = "accept_day")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date acceptDay;

    @JoinColumn(name = "request_by")
    @ManyToOne(cascade = CascadeType.PERSIST)
    private Accounts requestBy;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "accept_by")
    private Accounts acceptBy;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "id_assignment")
    private Assignment requestAssignment;


    public Request(String state, Accounts requestBy, Assignment requestAssignment) {
        this.state = state;
        this.requestBy = requestBy;
        this.requestAssignment = requestAssignment;
    }
}
