package vacation.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import vacation.utils.UserRole;

@Entity
@Table(name = "APP_USER")
@NamedQueries({
		@NamedQuery(name = User.GET_USER_BY_CREDENTIALS, query = "SELECT u FROM User u where u.userName LIKE :uname and u.password LIKE :passwd"),
		@NamedQuery(name = User.GET_ALL_ACTIVE_USERS, query = "SELECT u FROM User u where u.deleted=0"),
		@NamedQuery(name = User.GET_ALL_USERS, query = "SELECT u FROM User u") })
public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "USER_ID")
	private Long userId;
	
	@Column(name = "USERNAME", unique = true)
	private String userName;
	@Column(name = "PASSWORD")
	private String password;
	@Column(name = "ROLE")
	@Enumerated(EnumType.STRING)
	private UserRole role;
	@Column(name = "DELETED")
	private Integer deleted;
	
	@Transient
	private String confirmationPassword;
	
	@OneToOne(fetch=FetchType.LAZY, mappedBy="user")
	private Employee employee;
	
	public final static String GET_USER_BY_CREDENTIALS = "GET_USER_BY_CREDENTIALS";
	public final static String GET_ALL_ACTIVE_USERS = "GET_ALL_ACTIVE_USERS";
	public final static String GET_ALL_USERS = "GET_ALL_USERS";

	public User(String userName, String password, UserRole role, Integer deleted) {
		super();
		this.userName = userName;
		this.password = password;
		this.role = role;
		this.deleted = deleted;
	}

	public User() {
		this.deleted = 0;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long uid) {
		this.userId = uid;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmationPassword() {
		return confirmationPassword;
	}

	public void setConfirmationPassword(String confPassword) {
		this.confirmationPassword = confPassword;
	}

	public UserRole getRole() {
		return role;
	}

	public void setRole(UserRole role) {
		this.role = role;
	}

	public Integer getDeleted() {
		return deleted;
	}

	public void setDeleted(Integer deleted) {
		this.deleted = deleted;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}
}
