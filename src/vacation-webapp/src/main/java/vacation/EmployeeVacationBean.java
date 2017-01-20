package vacation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import vacation.model.Employee;
import vacation.model.PublicHoliday;
import vacation.model.RequestedPto;
import vacation.persistance.PersistDataEJB;
import vacation.utils.PtoType;

@Named
@RequestScoped
public class EmployeeVacationBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@EJB
	private PersistDataEJB persistDataEJB;

	private Long employeeId;
	private Employee employee;
	private RequestedPto requestedPto;
	private Date startDate;
	private Date endDate;
	private List<RequestedPto> futureAbs;
	private List<RequestedPto> previousAbs;
	private List<RequestedPto> pendingAbs;
	private List<PublicHoliday> publicHoliday;

	@Inject
	private UserSessionBean userSessionBean;
	
	@PostConstruct
	public void init() {
		requestedPto = new RequestedPto();
		employeeId = userSessionBean.getCurrentUser().getEmployee().getEmployeeId();
		futureAbs = persistDataEJB.getFutureAbs(userSessionBean.getCurrentUser().getEmployee().getEmployeeId());
		previousAbs = persistDataEJB.getPreviousAbs(userSessionBean.getCurrentUser().getEmployee().getEmployeeId());
		pendingAbs = persistDataEJB.getPendingAbs(userSessionBean.getCurrentUser().getEmployee().getEmployeeId());
	}

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public RequestedPto getRequestedPto() {
		return requestedPto;
	}

	public void setRequestedPto(RequestedPto requestedPto) {
		this.requestedPto = requestedPto;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public List<RequestedPto> getFutureAbs() {
		return futureAbs;
	}

	public void setFutureAbs(List<RequestedPto> futureAbs) {
		this.futureAbs = futureAbs;
	}

	public List<RequestedPto> getPreviousAbs() {
		return previousAbs;
	}

	public void setPreviousAbs(List<RequestedPto> previousAbs) {
		this.previousAbs = previousAbs;
	}

	public List<RequestedPto> getPendingAbs() {
		return pendingAbs;
	}

	public void setPendingAbs(List<RequestedPto> pendingAbs) {
		this.pendingAbs = pendingAbs;
	}

	public List<PublicHoliday> getPublicHoliday() {
		return publicHoliday;
	}

	public void setPublicHoliday(List<PublicHoliday> publicHoliday) {
		this.publicHoliday = publicHoliday;
	}

	public PtoType[] getPtoType() {
		return PtoType.values();
	}

	public void saveNewRequestPto() {
		List<Date> result = new ArrayList<Date>();
	    
	    if(!startDate.after(new Date()) || endDate.before(startDate)) {
	    	FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_WARN, "Incorrect interval", "Choose a correct interval!"));
	    	return;
	    }
		Calendar start = Calendar.getInstance();
	    start.setTime(startDate);
	    Calendar end = Calendar.getInstance();
	    end.setTime(endDate);
	    end.add(Calendar.DAY_OF_YEAR, 1);
	    
	    while (start.before(end)) {
	    	int day = start.get(Calendar.DAY_OF_WEEK);
	    	if (day == Calendar.SUNDAY || day == Calendar.SATURDAY) {
	    		start.add(Calendar.DAY_OF_YEAR, 1);
	    		continue;
	    	}
	        result.add(start.getTime());
	        start.add(Calendar.DAY_OF_YEAR, 1);
	    }
	    
	    publicHoliday = persistDataEJB.getPublicHolydayAfterDay(startDate);
	    if(publicHoliday != null & !publicHoliday.isEmpty()) {
	    	List<Date> phDays = publicHoliday.stream().map(d -> d.getDay()).collect(Collectors.toList());
	    	result = result.stream().filter(day -> !phDays.contains(day)).collect(Collectors.toList());
	    }
	    if (result != null && !result.isEmpty()) {
	    	for(Date dayOff : result) {
	    		boolean res = persistDataEJB.saveRequestedPto(new RequestedPto(employeeId, new java.sql.Date(dayOff.getTime()), requestedPto.getType(), requestedPto.getStatus()));
	    		if(res == false) {
	    		FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_WARN, "Data Not Saved", "Wrong day or already off day!"));
	    		} else {
	    			FacesContext.getCurrentInstance().addMessage(null,
	    					new FacesMessage(FacesMessage.SEVERITY_INFO, "Data Saved", "A new request PTO was added!"));
	    		}
	    	}
	    	FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Data Saved", "A new request PTO was added!"));
	    } else {
	    	FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_WARN, "Incorrect days of already off", "Choose another interval!"));
	    }
	    requestedPto = new RequestedPto();
	    startDate = null;
	    endDate = null;
	    futureAbs = persistDataEJB.getFutureAbs(userSessionBean.getCurrentUser().getEmployee().getEmployeeId());
	    pendingAbs = persistDataEJB.getPendingAbs(userSessionBean.getCurrentUser().getEmployee().getEmployeeId());
	}
}
