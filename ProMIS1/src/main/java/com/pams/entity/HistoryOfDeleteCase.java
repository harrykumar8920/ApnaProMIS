package com.pams.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate; // ✅ Use LocalDate instead of Date

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "history_of_delete_case", schema = "prosecution")
public class HistoryOfDeleteCase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "delete_id")
    private Long id;
    
    @Column(name = "court_case_no")
    private String courtCaseNo;
    
    @Column(name = "cause_title")
    private String causeTitle;
    
    @Column(name = "updated_date")
    @DateTimeFormat(pattern = "dd/MM/yyyy")
    private LocalDate updatedDate; // ✅ Changed from Date to LocalDate
    
    private Long caseid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", referencedColumnName = "id")
    private UserDetails createdBy;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updated_by", referencedColumnName = "id")
    private UserDetails updatedBy;
}