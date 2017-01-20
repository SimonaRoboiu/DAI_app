package vacation.persistance;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolationException;

import vacation.model.Department;
import vacation.model.Employee;
import vacation.model.PublicHoliday;
import vacation.model.RequestedPto;
import vacation.model.User;
import vacation.utils.RequestPtoStatus;

@Stateless
public class PersistDataEJB {

	@PersistenceContext
	private EntityManager entityManager;

	// User
	public boolean saveUser(User user) {
		try {
			entityManager.persist(user);
			entityManager.flush();
			return true;
		} catch (ConstraintViolationException e) {
			System.err.println("Error insert new user in DB! (1)");
			return false;
		} catch (PersistenceException e) {
			System.err.println("Error insert new user in DB! (2)");
			return false;
		}
	}

	public void removeUsers(List<User> userList) {
		for (User u : userList) {
			try {
				entityManager.remove(entityManager.merge(u));
			} catch (ConstraintViolationException e) {
				System.err.println("Error insert new user in DB! (1)");

			} catch (PersistenceException e) {
				System.err.println("Error insert new user in DB! (2)");

			}
		}
	}

	public User getUserByCredentials(String uname, String passwd) {

		List<User> results = entityManager.createNamedQuery(User.GET_USER_BY_CREDENTIALS, User.class)
				.setParameter("uname", uname).setParameter("passwd", passwd).getResultList();

		if (results.isEmpty()) {
			return null;
		} else if (results.size() > 1) {
			throw new IllegalStateException("Cannot have more than one user with the same username!");
		} else {
			return results.get(0);
		}
	}

	public List<User> getAllActiveUsers() {
		return entityManager.createNamedQuery(User.GET_ALL_ACTIVE_USERS, User.class).getResultList();
	}

	public List<User> getAllUsers() {
		return entityManager.createNamedQuery(User.GET_ALL_USERS, User.class).getResultList();
	}

	// Employee
	public boolean saveEmployee(Employee employee) {
		try {
			entityManager.persist(employee);
			entityManager.flush();
			return true;
		} catch (ConstraintViolationException e) {
			System.err.println("Error insert new employee in DB! (1)");
			return false;
		} catch (PersistenceException e) {
			System.err.println("Error insert new employee in DB! (2)");
			return false;
		}
	}

	public void softDeleteEmployees(List<Employee> employees) {
		for (Employee e : employees) {
			e.setActive(0);
			e.getUser().setDeleted(1);
			try {
				entityManager.persist(e);
				entityManager.flush();
			} catch (ConstraintViolationException ex) {
				System.err.println("Error insert new user in DB! (1)");

			} catch (PersistenceException ex) {
				System.err.println("Error insert new user in DB! (2)");

			}
		}
	}

	public void activateAccountsAndEmployees(List<Employee> employees) {
		for (Employee e : employees) {
			e.setActive(1);
			e.getUser().setDeleted(0);
			try {
				entityManager.persist(e);
				entityManager.flush();
			} catch (ConstraintViolationException ex) {
				System.err.println("Error insert new user in DB! (1)");

			} catch (PersistenceException ex) {
				System.err.println("Error insert new user in DB! (2)");

			}
		}
	}

	public List<Employee> getAllActiveEmployees() {
		return entityManager.createNamedQuery(Employee.GET_ALL_ACTIVE_EMPLOYEES, Employee.class).getResultList();
	}

	public List<Employee> getAllInactiveEmployees() {
		return entityManager.createNamedQuery(Employee.GET_ALL_INACTIVE_EMPLOYEES, Employee.class).getResultList();
	}

	public List<Employee> getAllEmployees() {
		return entityManager.createNamedQuery(Employee.GET_ALL_EMPLOYEES, Employee.class).getResultList();
	}

	// Department
	public boolean saveDepartment(Department department) {
		try {
			entityManager.persist(department);
			entityManager.flush();
			return true;
		} catch (ConstraintViolationException e) {
			System.err.println("Error insert new department in DB! (1)");
			return false;
		} catch (PersistenceException e) {
			System.err.println("Error insert new department in DB! (2)");
			return false;
		}
	}

	// RequestPTO
	public List<RequestedPto> getFutureAbs(Long id) {
		return entityManager.createNamedQuery(RequestedPto.GET_REQ_PTO_BY_EMPID_AFTER_DAY, RequestedPto.class)
				.setParameter("rpId", id).setParameter("day", new Date()).getResultList();
	}

	public List<RequestedPto> getPreviousAbs(Long id) {
		return entityManager.createNamedQuery(RequestedPto.GET_REQ_PTO_BY_EMPID_BEFORE_DAY, RequestedPto.class)
				.setParameter("rpId", id).setParameter("day", new Date())
				.setParameter("status", RequestPtoStatus.APPROVED).getResultList();
	}

	public List<RequestedPto> getPendingAbs(Long id) {
		return entityManager.createNamedQuery(RequestedPto.GET_REQ_PTO_BY_EMPID_PENDING, RequestedPto.class)
				.setParameter("rpId", id).setParameter("status", RequestPtoStatus.APPROVED).getResultList();
	}

	// Public Holiday
	public boolean saveRequestedPto(RequestedPto requestedPto) {
		try {
			entityManager.persist(requestedPto);
			entityManager.flush();
			return true;
		} catch (ConstraintViolationException e) {
			System.err.println("Error insert new department in DB! (1)");
			return false;
		} catch (PersistenceException e) {
			System.err.println("Error insert new department in DB! (2)");
			return false;
		}
	}

	public List<PublicHoliday> getPublicHolydayAfterDay(Date day) {
		return entityManager.createNamedQuery(PublicHoliday.GET_PUBLIC_HOLIDAY_BY_DAY, PublicHoliday.class)
				.setParameter("day", day).getResultList();
	}

	// PR
	public List<RequestedPto> getPendingRequests(Long id) {
		List<Employee> empl = entityManager.createNamedQuery(Employee.GET_ALL_EMPLOYEES_BY_MGR, Employee.class)
				.setParameter("mgrId", id).getResultList();
		List<Long> emplIds = empl.stream().map(e -> e.getEmployeeId()).collect(Collectors.toList());
		return entityManager.createNamedQuery(RequestedPto.GET_ALL_PENDING1_REQ_BY_EMP, RequestedPto.class)
				.setParameter("emplIds", emplIds).setParameter("status", RequestPtoStatus.PENDING).getResultList();
	}

	public void approveRequests(List<RequestedPto> requestedPtos) {
		for (RequestedPto requestedPto : requestedPtos) {
			try {
				entityManager.persist(requestedPto);
				entityManager.flush();
			} catch (ConstraintViolationException e) {
				System.err.println("Error insert new user in DB! (1)");

			} catch (PersistenceException e) {
				System.err.println("Error insert new user in DB! (2)");

			}
		}
	}
}
