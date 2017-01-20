package vacation.model;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "PUBLIC_HOLIDAY")
@NamedQueries({ 
	@NamedQuery(name = PublicHoliday.GET_PUBLIC_HOLIDAY_BY_DAY, query = "SELECT ph from PublicHoliday ph where ph.day >= :day"),
	@NamedQuery(name = PublicHoliday.GET_ALL_PUBLIC_HOLIDAY, query = "SELECT ph from PublicHoliday ph") })
public class PublicHoliday implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "DAY")
	private Date day;

	@Column(name = "AUTHOR_ID")
	private Long authorId;
	@Column(name = "DESCRIPTION")
	private String description;

	public final static String GET_PUBLIC_HOLIDAY_BY_DAY = "GET_PUBLIC_HOLIDAY_BY_DAY";
	public final static String GET_ALL_PUBLIC_HOLIDAY = "GET_ALL_PUBLIC_HOLIDAY";

	public PublicHoliday(Date day, Long authorId, String description) {
		super();
		this.day = day;
		this.authorId = authorId;
		this.description = description;
	}

	public PublicHoliday() {
	}

	public Date getDay() {
		return day;
	}

	public void setDay(Date day) {
		this.day = day;
	}

	public Long getAuthorId() {
		return authorId;
	}

	public void setAuthorId(Long authorId) {
		this.authorId = authorId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
}
