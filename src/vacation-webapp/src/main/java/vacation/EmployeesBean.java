package vacation;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import vacation.model.Department;
import vacation.model.Employee;
import vacation.model.User;
import vacation.persistance.PersistDataEJB;
import vacation.utils.UserRole;

@Named
@RequestScoped
public class EmployeesBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@EJB
	private PersistDataEJB persistDataEJB;

	private boolean viewRetired;
	String searchCriteria;
	private Date utilDate;
	private User user;
	private Employee employee;
	private Employee manager;
	private Department department;
	private List<Employee> selectedEmployees;
	private List<Employee> allActiveEmployees;
	private List<Employee> allInactiveEmployees;

	@PostConstruct
	public void init() {
		utilDate = null;
		employee = new Employee();
		manager = new Employee();
		employee.setManager(manager);
		department = new Department();
		employee.setDepartment(department);
		user = new User();
		employee.setUser(user);
		allActiveEmployees = persistDataEJB.getAllActiveEmployees();
	}

	public boolean isViewRetired() {
		return viewRetired;
	}

	public void setViewRetired(boolean viewRetired) {
		this.viewRetired = viewRetired;
	}

	public String getSearchCriteria() {
		return searchCriteria;
	}

	public void setSearchCriteria(String searchCriteria) {
		this.searchCriteria = searchCriteria;
	}

	public Date getUtilDate() {
		return utilDate;
	}

	public void setUtilDate(Date utilDate) {
		this.utilDate = utilDate;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
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

	public List<Employee> getSelectedEmployees() {
		return selectedEmployees;
	}

	public void setSelectedEmployees(List<Employee> selectedEmployees) {
		this.selectedEmployees = selectedEmployees;
	}

	public List<Employee> getAllActiveEmployees() {
		return allActiveEmployees;
	}

	public void setAllActiveEmployees(List<Employee> allActiveEmployees) {
		this.allActiveEmployees = allActiveEmployees;
	}

	public List<Employee> getAllInactiveEmployees() {
		return allInactiveEmployees;
	}

	public void setAllInactiveEmployees(List<Employee> allInactiveEmployees) {
		this.allInactiveEmployees = allInactiveEmployees;
	}

	public String getHash(String str) {
		StringBuffer sbStr = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(str.getBytes());
			byte[] digest = md.digest();
			sbStr = new StringBuffer();
			for (byte b : digest) {
				sbStr.append(String.format("%02x", b & 0xff));
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			System.err.println("MD5 - error for password!");
		}
		return sbStr.toString();
	}

	public void addNewEmployee() {
		if (!employee.getUser().getPassword().equals(employee.getUser().getConfirmationPassword())) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Data Not Saved", "Enter the same password!"));
			employee = new Employee();
			manager = new Employee();
			employee.setManager(manager);
			department = new Department();
			employee.setDepartment(department);
			user = new User();
			employee.setUser(user);
			utilDate = null;
			return;
		}
		String hPass = getHash(employee.getUser().getPassword());
		employee.getUser().setPassword(hPass);
		employee.setHireDate(new java.sql.Date(utilDate.getTime()));
		boolean result = persistDataEJB.saveEmployee(employee);
		if (result == true) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Data Saved", "A new employee and user account were added!"));
		} else {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"Data Not Saved. Complete all the fields with correct values!", "Error on insert!"));
		}
		utilDate = null;
		employee = new Employee();
		manager = new Employee();
		employee.setManager(manager);
		department = new Department();
		employee.setDepartment(department);
		user = new User();
		employee.setUser(user);
	}

	public void softDeleteEmployeeAndUserAccount() {
		if (selectedEmployees.size() > 0) {
			persistDataEJB.softDeleteEmployees(selectedEmployees);
			allActiveEmployees = persistDataEJB.getAllActiveEmployees();
			selectedEmployees = new ArrayList<>();
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Data Removed", "The data was removed!"));
		} else
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"No employee is selected", "You should select an employee!"));
	}

	public void activateAccounts() {
		if (selectedEmployees.size() > 0) {
			persistDataEJB.activateAccountsAndEmployees(selectedEmployees);
			allInactiveEmployees = persistDataEJB.getAllInactiveEmployees();
			selectedEmployees = new ArrayList<>();
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO,
					"Data Activated!", "The selected employee was activated!"));
		} else
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
					"No employee is selected", "You should select an employee!"));
	}

	public UserRole[] getUserRole() {
		return UserRole.values();
	}
	
	public void getEmployeeByField(String inputValue, String typeSearch) {
		/* pressed Search button */
		if (typeSearch != null && typeSearch.toString().equals("1")) {
			if (searchCriteria != null && !searchCriteria.toString().equals("0")) {
				/* input text search activated (criterion selected) */
				if (inputValue != null && !(inputValue.toString().equals(""))) {
					/* unchecked view retired */
					if (viewRetired == false)
						allActiveEmployees = persistDataEJB.getAllEmployees();
					else
						allActiveEmployees = persistDataEJB.getAllActiveEmployees();
					if (searchCriteria.equals("1"))
						allActiveEmployees = allActiveEmployees.stream().filter(a -> a.getFirstName().contains(inputValue) || a.getLastName().contains(inputValue))
								.collect(Collectors.toList());
					else if (searchCriteria.equals("2"))
						allActiveEmployees = allActiveEmployees.stream().filter(a -> a.getManager().getFirstName().contains(inputValue) || a.getManager().getLastName().contains(inputValue))
								.collect(Collectors.toList());
					else if (searchCriteria.equals("3"))
						allActiveEmployees = allActiveEmployees.stream().filter(a -> a.getDepartment().getDepartmentName().contains(inputValue))
								.collect(Collectors.toList());
				} else { /* selected search criterion, but empty input text */
					FacesContext.getCurrentInstance().addMessage(null,
							new FacesMessage(FacesMessage.SEVERITY_INFO, "Input Value", "Enter input text for searching!"));
				}
			} else { /* pressed search without a search criterion */
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, "Search Criteria", "Select a search criterion!"));
			}
		} else {
			/*
			 * C1.pressed Clear button: idApp==clearSearch, search=0;
			 * C2.checked/unchecked view retired and input search is activated
			 * and clean
			 */
			if (inputValue != null && (inputValue.toString().equals("clearSearch") || inputValue.toString().equals(""))) {
				/* unchecked view retired or else branch */
				if (viewRetired == false)
					allActiveEmployees = persistDataEJB.getAllActiveEmployees();
				else
					allActiveEmployees = persistDataEJB.getAllEmployees();
			} else if (inputValue != null && !(inputValue.toString().equals(""))) {
				/* checked/unchecked view retired and activated input search */
				allActiveEmployees = persistDataEJB.getAllEmployees();
				if (viewRetired == false)
					allActiveEmployees = persistDataEJB.getAllActiveEmployees();
				
				if (searchCriteria.equals("1"))
					allActiveEmployees = allActiveEmployees.stream().filter(a -> a.getFirstName().contains(inputValue) || a.getLastName().contains(inputValue))
							.collect(Collectors.toList());
				else if (searchCriteria.equals("2"))
					allActiveEmployees = allActiveEmployees.stream().filter(a -> a.getManager().getFirstName().contains(inputValue) || a.getManager().getLastName().contains(inputValue))
							.collect(Collectors.toList());
				else if (searchCriteria.equals("3"))
					allActiveEmployees = allActiveEmployees.stream().filter(a -> a.getDepartment().getDepartmentName().contains(inputValue))
							.collect(Collectors.toList());
			}
		}
	}
	
	/*
	 * Reset function: rest variables
	 */
	 public void resetVars() {
		 this.searchCriteria = new String();
		 this.viewRetired = false;
	 }
}
