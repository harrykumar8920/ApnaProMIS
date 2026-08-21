package com.pams.dto;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import com.pams.entity.AddAccused;
import com.pams.entity.AddAct;
import com.pams.entity.AddActSec;
import com.pams.entity.AddCourt;
import com.pams.entity.AddDesignation;
import com.pams.entity.AddState;
import com.pams.entity.AddSubSec;
import com.pams.entity.AssignedTaskPuh;
import com.pams.entity.AssignedTaskPuhAfterCOurt;
import com.pams.entity.CaseCompany;
import com.pams.entity.Clause;
import com.pams.entity.DetailsType;
import com.pams.entity.District;
import com.pams.entity.PairaviOfficer;
import com.pams.entity.PairaviType;
import com.pams.entity.Status;
import com.pams.entity.ProCourtCaseDetails;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.persistence.Transient;
import lombok.Data;

@Data
public class NCLTTaskDTO {
	// PerformaParty
	private int typeofOrder = 1;
	private String pPCompany;
	private String pPCompCin;
	private String pPRespondentName;
	private String pPRespondentDesgination;
	private String pPAddress;
	private Long performaID;
	private Long responseOfRespondentId;
	private String RejectRemarkforResponseOfRespondent;

	private String rejectRemarkFreez;
	private Long rejectfreezeID;
	private Long rejectuploadID;

	private int id;
	private Long courtId;
	private Long caseId;
	private Long hearingEditId;
	private Long courtCaseID;

	private String rejectRemarkforCourtcase;

	private String rejectRemarkforAccused;
	private Long accusedIDforDelete;

	private CaseCompany caseCompanyForaccusedStatus;
	private Long accusedIdEdit;
	private AddAccused accusedIdtest;
	private String misRespondent;
	private String sfiorespondentfile;
	private MultipartFile misFile1;
	private Long FyId;
	private ProCourtCaseDetails proCourtDtl;
	private AssignedTaskPuhAfterCOurt assignedTask;
	private String typeOfCase;
	private int tabId;
	// complaintdetl
	private Long complanitId;
	private String IOName;
	private String InvCaseNo;
	private AddDesignation complanitdesignation;
	private AddDesignation desigInvesOffi;
	private String complanitName;
	private String complanitEmail;
	private String complaintMobile;
	private String complaintPetinoner;

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date complaintPetinonerDate;

	private int approve_status;
	private String rejectRemark;

	// company
	private Long companyId;
	private String compName;
	private String cin;
	private String cinRespondent;
	private String compAddess;
	private String rejectRemarkforCompany;
//	private int accusedCompId;

	// pairavidtl
	private Long performaPartyId;
	private String rejectRemarkForPerformaParty;

	private Long pairaviId;
	private String pairaviName;
	private String pairaviemail;
	private AddDesignation pairavidesignation;
	private String pairaviMobile;
	private DetailsType pairavidetailsType;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date pairavifromDate;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date pairavitoDate;
	private PairaviType pairaviType;
	private String rejectRemarkforPairavi;
	private PairaviOfficer pairaviOfficer;

	// Hearing
	private String briefHD;
	private String effectiveBoolean;
	private PairaviOfficer counselNameH;
	private PairaviOfficer officerH;
	private Long hearingID;
	private String rejectRemarkforHearing;
	/*
	 * private String counselName; private String counselEmail; private
	 * AddDesignation counselDesignation; private AddDesignation
	 * counselDesignation123; private String counselMobileNo; private String
	 * counselName1; private String counselEmail1; private AddDesignation
	 * counselDesignation1; private AddDesignation counselDesignation2; private
	 * String counselMobileNo1;
	 */

	private Integer foreEditStatus = 0;

	private MultipartFile additionalDoc;
	private MultipartFile orderCopyOfTransfer;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date lastHearingDate;
	private String detailsOfOfficeToWhichCaseisTransfered;

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date transferDateOfOrder;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date transferDateOfTransfer;

	private MultipartFile orderCopyForWithdraw;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date withdrawDateOfOrder;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date withdrawDateOfWithdraw;

	private MultipartFile stayOrderCopy;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date stayDateOfOrder;
	private String durattionOfStay;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private String reasonnOfStay;
	private String stayRemark;

	private MultipartFile detailsOfOrderForWindingUp;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date windingUpClosingDate;

	private MultipartFile orderCopyOfDisposedOff;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date dateOfDisposed;

	private Status caseStatus;
	private Status accusedstatus1;
	private AddAccused addAccused;
	// private String addAccused;

	private Long AccusedStatusID;
	private Long AccusedStatusID2;

	private Integer editHearing;

	private AddState state;

	private District city;

	private AddCourt courtType;

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date nextHearingDate;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date dateofCaseStatusUpdate;
	private Status status;

	// private Status accusedstatus;

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date councilfromDate;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date counciltoDate;
	private PairaviType councilType;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date caseStatusDate;

	// private AddDisposalState disposed;

	// casesProcessing
	private Long reportId;
	private Long caseproseingID;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date invOrder;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date suppInv;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date invReport;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date suppInvReport;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private Date dateFilling;

	private String instructionFy;

	private int approveStatus;
	private String rejectRemarkforcaseProseing;

	// AccusedDtl
	private int accusedCompId;
	private String accusedName;
	private String accusedAddress;
	private String accDesc;
	private String accComp;
	private String accCIN;
	private Long rpcId;
	private Long compId;
	private String accPan;
	private String respondentNumber;
	private String performaPartyRespondent;
	private String accEmail;
	private Status accusedstatus;
	private String accusedType;

	private String addAccusedAddress;
	private Long[] accusedACT;
	private Long[] accusedSection;
	private Long[] accusedSubSection;
	private String[] accusedClause;
	// private Integer[] aaccusedPunishment;
	// private String[] accusedCompoundability;
	// private String[] accusedDescription;
	private String accusedDescription;
	private String individualRelateTo;

	private String accDesination1;
	private Clause clause;
	String actName;
	String sectionName;
	String subsectionName;

	// private Punishment1 punishment1;
	// private String compoundability;

	// Act & Section

	private AddAct act;
	private AddActSec section;
	private AddSubSec subsection;

	private String accDesination;
	private AddDesignation accDesination2;

	// private String Accused Pan
	@Transient
	private MultipartFile complaintReportUpload1;
	private long genreportID;
	private int approveStatusGenReport = 0;
	private String rejectRemarkGenReport;

	@Transient
	private int typeofreport = 0;

	// for save the file
	private Long uploadID;
	private Long misUploadID;
	private String typeAccusedAndCompany;
	private String companyName;
//	private String companyID1;
	private long compID123;
	private MultipartFile file1;
	// private String accusedNameForFileUpload;
	// private String accusedIDForFileUpload;
	private String rejectRemarkforAddUploadFile;

	// Update Court case No with File
	private MultipartFile courtCaseDtlFile;
	private String courtCaseName;

}
