package vacation.model;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import vacation.utils.PtoType;
import vacation.utils.RequestPtoStatus;

@Entity
@IdClass(RequestedPtoKey.class)
@Table(name = "REQUESTED_PTO")
@NamedQueries({
	@NamedQuery(name = RequestedPto.GET_REQ_PTO_BY_EMPID, query = "SELECT rp FROM RequestedPto rp where rp.employeeId LIKE :rpId"),
	@NamedQuery(name = RequestedPto.GET_REQ_PTO_BY_EMPID_BEFORE_DAY, query = "SELECT rp FROM RequestedPto rp where rp.employeeId LIKE :rpId and rp.day <= :day and rp.status LIKE :status"),
	@NamedQuery(name = RequestedPto.GET_REQ_PTO_BY_EMPID_AFTER_DAY, query = "SELECT rp FROM RequestedPto rp where rp.employeeId LIKE :rpId and rp.day > :day"),
	@NamedQuery(name = RequestedPto.GET_REQ_PTO_BY_EMPID_PENDING, query = "SELECT rp FROM RequestedPto rp where rp.employeeId LIKE :rpId and rp.status NOT LIKE :status"),
	@NamedQuery(name = RequestedPto.GET_ALL_PENDING1_REQ_BY_EMP, query = "SELECT rp FROM RequestedPto rp where rp.employeeId IN :emplIds and rp.status LIKE :status"),
	@NamedQuery(name = RequestedPto.GET_ALL_REQ_PTO, query = "SELECT rp FROM RequestedPto rp") })

public class RequestedPto implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@Column(name = "EMPLOYEE_ID")
	private Long employeeId;
	
	@Id
	@Column(name = "DAY")
	private Date day;
	
	@Column(name = "TYPE", nullable=false)
	@Enumerated(EnumType.STRING)
	private PtoType type;
	
	@Column(name = "STATUS", nullable=false)
	@Enumerated(EnumType.STRING)
	private RequestPtoStatus status;
	
	@Column(name = "MANAGER_ID_APPROVAL")
	private Long managerIdApproval;
	
	@Column(name = "NOTES")
	private String notes;
	
	public final static String GET_ALL_REQ_PTO = "GET_ALL_REQ_PTO";
	public final static String GET_REQ_PTO_BY_EMPID = "GET_REQ_PTO_BY_EMPID";
	public final static String GET_REQ_PTO_BY_EMPID_BEFORE_DAY = "GET_REQ_PTO_BY_EMPID_BEFORE_DAY";
	public final static String GET_REQ_PTO_BY_EMPID_AFTER_DAY = "GET_REQ_PTO_BY_EMPID_AFTER_DAY";
	public final static String GET_REQ_PTO_BY_EMPID_PENDING = "GET_REQ_PTO_BY_EMPID_PENDING";
	public final static String GET_ALL_PENDING1_REQ_BY_EMP = "GET_ALL_PENDING1_REQ_BY_EMP";
	
	public RequestedPto(Long employeeId, Date day, PtoType type, RequestPtoStatus status) {
		super();
		this.employeeId = employeeId;
		this.day = day;
		this.type = type;
		this.status = status;
	}
	
	public RequestedPto() {
		this.status = RequestPtoStatus.PENDING;
	}

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	public Date getDay() {
		return day;
	}

	public void setDay(Date day) {
		this.day = day;
	}

	public PtoType getType() {
		return type;
	}

	public void setType(PtoType type) {
		this.type = type;
	}

	public RequestPtoStatus getStatus() {
		return status;
	}

	public void setStatus(RequestPtoStatus status) {
		this.status = status;
	}

	public Long getManagerIdApproval() {
		return managerIdApproval;
	}

	public void setManagerIdApproval(Long managerIdApproval) {
		this.managerIdApproval = managerIdApproval;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}
	
	public String getCompositeKey() {
		return this.day+"."+this.employeeId;
	}
}
