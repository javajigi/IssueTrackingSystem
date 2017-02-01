package infinitefire.project.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Milestone {
	@Id
	@GeneratedValue
	private long id;
	
	@Column(name = "subjects", length = 100, nullable = false)
	private String subjects;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "startDate", nullable = false, updatable = false)
	private Date startDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "endDate", nullable = false)
	private Date endDate;
	
//	@OneToMany(mappedBy = "issue")
//	@OrderBy("id ASC")
//	private List<Issue> issueList;
	
	public Milestone() {}

	public Milestone(long id, String subjects) {
		super();
		this.id = id;
		this.subjects = subjects;
		//this.issueList = issueList;
		this.startDate = new Date();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getSubjects() {
		return subjects;
	}

	public void setSubjects(String subjects) {
		this.subjects = subjects;
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

//	public List<Issue> getIssueList() {
//		return issueList;
//	}
//
//	public void setIssueList(List<Issue> issueList) {
//		this.issueList = issueList;
//	}

	@Override
	public String toString() {
		return "Milestone [id=" + id + ", subjects=" + subjects + ", startDate=" + startDate + ", endDate=" + endDate
				+ ", issueList="+ "]";
	}
}
