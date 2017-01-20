package vacation.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "DEPARTMENT")
@NamedQueries({
	@NamedQuery(name = Department.GET_DEPT_BY_ID, query = "SELECT d FROM Department d where d.departmentId LIKE :dId"),
	@NamedQuery(name = Department.GET_ALL_DEPARTMENTS, query = "SELECT d FROM Department d") })
public class Department implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "DEPARTMENT_ID")
	private Long departmentId;
	
	@Column(name = "DEPARTMENT_NAME", nullable=false)
	private String departmentName;
	@Column(name = "MANAGER_ID")
	private Long managerId;
	
	public final static String GET_ALL_DEPARTMENTS = "GET_ALL_DEPARTMENTS";
	public final static String GET_DEPT_BY_ID = "GET_DEPT_BY_ID";
	
	public Department(Long departmentId, String departmentName, Long managerId) {
		super();
		this.departmentId = departmentId;
		this.departmentName = departmentName;
		this.managerId = managerId;
	}

	public Department() {}

	public Long getDepartmentId() {
		return departmentId;
	}

	public void setDepartmentId(Long departmentId) {
		this.departmentId = departmentId;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public Long getManagerId() {
		return managerId;
	}

	public void setManagerId(Long managerId) {
		this.managerId = managerId;
	}	
}
