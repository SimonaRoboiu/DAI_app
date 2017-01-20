package vacation.model;

import java.io.Serializable;
import java.sql.Date;

public class RequestedPtoKey implements Serializable {

	private static final long serialVersionUID = 1L;

	protected Long employeeId;
	protected Date day;
	
	public RequestedPtoKey(Long employeeId, Date day) {
		super();
		this.employeeId = employeeId;
		this.day = day;
	}
	
	public RequestedPtoKey() {
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((day == null) ? 0 : day.hashCode());
		result = prime * result + ((employeeId == null) ? 0 : employeeId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RequestedPtoKey other = (RequestedPtoKey) obj;
		if (day == null) {
			if (other.day != null)
				return false;
		} else if (!day.equals(other.day))
			return false;
		if (employeeId == null) {
			if (other.employeeId != null)
				return false;
		} else if (!employeeId.equals(other.employeeId))
			return false;
		return true;
	}
}
