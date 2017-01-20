package vacation.model;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "EMPLOYEE")
@NamedQueries({
	@NamedQuery(name = Employee.GET_EMP_BY_ID, query = "SELECT e FROM Employee e where e.employeeId LIKE :eId"),
	@NamedQuery(name = Employee.GET_ALL_ACTIVE_EMPLOYEES, query = "SELECT e FROM Employee e where e.active=1"),
	@NamedQuery(name = Employee.GET_ALL_INACTIVE_EMPLOYEES, query = "SELECT e FROM Employee e where e.active=0"),
	@NamedQuery(name = Employee.GET_ALL_EMPLOYEES_BY_MGR, query = "SELECT e FROM Employee e where e.active=1 and e.manager.employeeId = :mgrId"),
	@NamedQuery(name = Employee.GET_ALL_EMPLOYEES, query = "SELECT e FROM Employee e") })
public class Employee implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "EMPLOYEE_ID")
	private Long employeeId;
	
	@Column(name = "FIRST_NAME", nullable=false)
	private String firstName;
	@Column(name = "LAST_NAME", nullable=false)
	private String lastName;
	@Column(name = "HIRE_DATE")
	private Date hireDate;
	
	@ManyToOne
	@JoinColumn(name = "MANAGER_ID", nullable = false)
	private Employee manager;
	
	@ManyToOne
	@JoinColumn(name = "DEPARTMENT_ID", nullable = false)
	private Department department;
	
	@Column(name = "PTO_AVAILABLE")
	private Integer ptoAvailable;
	@Column(name = "PTO_USED")
	private Integer ptoUsed;
	@Column(name = "ACTIVE")
	private Integer active;
	
	@OneToOne(fetch=FetchType.LAZY, cascade=CascadeType.ALL)
	@JoinColumn(name="USER_ID")
	private User user;
	
	@OneToMany(mappedBy = "manager")
	private Set<Employee> employees = new HashSet<Employee>();
	
	public final static String GET_ALL_EMPLOYEES = "GET_ALL_EMPLOYEES";
	public final static String GET_ALL_ACTIVE_EMPLOYEES = "GET_ALL_ACTIVE_EMPLOYEES";
	public final static String GET_ALL_INACTIVE_EMPLOYEES = "GET_ALL_INACTIVE_EMPLOYEES";
	public final static String GET_ALL_EMPLOYEES_BY_MGR = "GET_ALL_EMPLOYEES_BY_MGR";
	public final static String GET_EMP_BY_ID = "GET_EMP_BY_ID";

	public Employee(String firstName, String lastName, Date hireDate, Employee manager,
			Department department, Integer ptoAvailable, Integer ptoUsed, Integer active) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.hireDate = hireDate;
		this.manager = manager;
		this.department = department;
		this.ptoAvailable = ptoAvailable;
		this.ptoUsed = ptoUsed;
		this.active = active;
	}
	
	public Employee(){
		this.active = 1;
		this.ptoUsed = 0;
	}

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Date getHireDate() {
		return hireDate;
	}

	public void setHireDate(Date hireDate) {
		this.hireDate = hireDate;
	}

	public Employee getManager() {
		return manager;
	}

	public void setManager(Employee manager) {
		this.manager = manager;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	public Integer getPtoAvailable() {
		return ptoAvailable;
	}

	public void setPtoAvailable(Integer ptoAvailable) {
		this.ptoAvailable = ptoAvailable;
	}

	public Integer getPtoUsed() {
		return ptoUsed;
	}

	public void setPtoUsed(Integer ptoUsed) {
		this.ptoUsed = ptoUsed;
	}

	public Integer getActive() {
		return active;
	}

	public void setActive(Integer active) {
		this.active = active;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Set<Employee> getEmployees() {
		if (employees != null && employees.size() == 1) {
			List<Employee> list = new ArrayList<Employee>(employees);
			Employee emp = list.get(0);
			if (emp.employeeId == this.employeeId) {
				return null;
			}
		}
		return employees;
	}

	public void setEmployees(Set<Employee> employees) {
		this.employees = employees;
	}
}
