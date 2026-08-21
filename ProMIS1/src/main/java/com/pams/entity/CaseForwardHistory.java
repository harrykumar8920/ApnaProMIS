package com.pams.entity;



import java.time.LocalDate;


import jakarta.persistence.*;
import lombok.Data;
@Data
@Entity
@Table(name = "case_forward_history")
public class CaseForwardHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "court_case_id", nullable = false
    )
    private ProCourtCaseDetails proCourtCaseDetails;

    // From which user
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userName", nullable = false)
    private UserDetails userName;


@Transient  
    private UserDetails forwardedTo;

    @Column(name = "remarks", length = 1000)
    private String remarks;

    @Column(name = "from_date", nullable = false)
    private LocalDate fromDate;
    @Column(name = "to_date")
    private LocalDate toDate;
    
    @Transient
	private UnitDetails unit;

}
