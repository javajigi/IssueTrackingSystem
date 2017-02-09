package infinitefire.project.domain;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Milestone {
	@Id
	@GeneratedValue
	private Long id;
	
	@Column(name = "subject", length = 100, nullable = false)
	private String subject;
	
	@Lob
	private String description;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "startDate", updatable = false)
	private Date startDate;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "endDate")
	private Date endDate;
	
	@JsonIgnore
	@OneToMany(mappedBy = "milestone")
	@OrderBy("id ASC")
	private List<Issue> issueList;
	
	@Transient
	private double openedIssuePs;
	
	private static final Logger log = LoggerFactory.getLogger(Milestone.class);
	
	public Milestone() {
		startDate = new Date();
	}

	public Milestone(String subject, String description, List<Issue> issueList, Date startDate, Date endDate) {
		super();
		this.subject = subject;
		this.description = description;
		this.issueList = issueList;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	
	public void setFormatStartDate(String startDate) {
		String strDate = startDate;
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
		try {			
			this.startDate = dateFormat.parse(strDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	public void setFormatEndDate(String endDate) {
		String strDate = endDate;		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
				
		try {			
			this.endDate = dateFormat.parse(strDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public List<Issue> getIssueList() {
		return issueList;
	}

	public void setIssueList(List<Issue> issueList) {
		this.issueList = issueList;
	}
	
	public double getOpenedIssuePs() {
		return openedIssuePs;
	}

	public void setOpenedIssuePs(double openedIssuePs) {
		this.openedIssuePs = openedIssuePs;
	}

	public void countOpenIssue() {
		double totalOpenState = 0.0;
		
		for(Issue issue : issueList) {
			if(issue.getState() == IssueState.OPEN) {
				totalOpenState += 1;
			}
		}
		
		if(issueList.size() == 0)
			this.openedIssuePs = 0;
		else
			this.openedIssuePs = totalOpenState/issueList.size();
	}
	

	@Override
	public String toString() {
		String str = "\n-------------------MilestoneList----------------------------\n";
		str += "Milestone [id="+id+", subject=" + subject + ", description=" + description + ", startDate=" + startDate
				+ ", endDate=" + endDate + ", openIssueSize="+openedIssuePs+"]";
		str += "\n-------------------IssueList----------------------------\n";
		if (issueList != null) {
			for (Issue issue : issueList) {
				str += issue + "\n";
			}
		}
		return str;
	}

}
