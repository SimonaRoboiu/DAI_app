package vacation;

import javax.faces.context.FacesContext;
import javax.inject.Named;

import vacation.model.User;
import vacation.persistance.PersistDataEJB;
import vacation.utils.UserRole;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;

@Named
@SessionScoped
public class UserSessionBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@EJB
	private PersistDataEJB dataSessionEjb;

	private User currentUser;
	private boolean isAuthenticated;

	@PostConstruct
	public void init() {
		currentUser = new User();
	}

	public User getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}

	public boolean isAuthenticated() {
		return isAuthenticated;
	}

	public void setAuthenticated(boolean isAuthenticated) {
		this.isAuthenticated = isAuthenticated;
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

	public String login() {
		FacesMessage message = null;
		String redirectLogIn = null;
		User userLogIn = null;

		if (currentUser.getUserName() != null && currentUser.getPassword() != null) {
			userLogIn = dataSessionEjb.getUserByCredentials(currentUser.getUserName(),
					getHash(currentUser.getPassword()));
			if (userLogIn != null && userLogIn.getRole() != null) {
				if (userLogIn.getRole().equals(UserRole.ADMIN)) {
					redirectLogIn = "/templateAdmin?faces-redirect=true";
				} else if (userLogIn.getRole().equals(UserRole.ACCOUNTANT)) {
					redirectLogIn = "/templateAccountant?faces-redirect=true";
				} else if (userLogIn.getRole().equals(UserRole.MANAGER)) {
					redirectLogIn = "/templateManager?faces-redirect=true";
				} else if (userLogIn.getRole().equals(UserRole.EMPLOYEE)) {
					redirectLogIn = "/templateEmployee?faces-redirect=true";
				}
				currentUser = userLogIn;
				isAuthenticated = true;
			}
		}
		if (redirectLogIn == null) {
			redirectLogIn = "/index";
			message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Loggin Error", "Invalid credentials");
			FacesContext.getCurrentInstance().addMessage(null, message);
		}

		return redirectLogIn;
	}

	public String logout() {
		FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
		currentUser = null;
		isAuthenticated = false;
		return "/index?faces-redirect=true";
	}

	/*
	 * Redirect to home administrator
	 */
	public String goToHomeAdmin() {
		return "/templateAdmin?faces-redirect=true";
	}
	
	/*
	 * Redirect to home accountant
	 */
	public String goToHomeAccountant() {
		return "/templateAccountant?faces-redirect=true";
	}
	
	/*
	 * Redirect to home manager
	 */
	public String goToHomeManager() {
		return "/templateManager?faces-redirect=true";
	}
	
	/*
	 * Redirect to home employee
	 */
	public String goToHomeEmployee() {
		return "/templateEmployee?faces-redirect=true";
	}
}
