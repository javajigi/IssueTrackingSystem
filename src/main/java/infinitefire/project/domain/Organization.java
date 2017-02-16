package infinitefire.project.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonIgnore;

import infinitefire.project.utils.DateTimeUtils;

@Entity
public class Organization {
	private static final Logger log = LoggerFactory.getLogger(Organization.class);
	
	@Id
	@GeneratedValue
	private Long id;
	
	@Column(name = "name", length = 30, nullable = false, updatable = false)
	private String groupName;
	
	@ManyToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_organization_maker"))
	private User organizationMaker;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "createDate", nullable = false, updatable = false)
	private Date createDate;
	
//	@Enumerated(EnumType.STRING)
//	@Column(name = "state", nullable = false)
//	private OrganizationState organizationState;
	
	@JsonIgnore
	@OneToMany(mappedBy = "organization")
	@OrderBy("id ASC")
	private List<Milestone> milestoneList;
	
	@JsonIgnore
	@OneToMany(mappedBy = "organization")
	private List<Issue> issueList;
	
	@JsonIgnore
	@OneToMany(mappedBy = "organization")
	private List<Label> labelList;
	
	
	@JsonIgnore
	@ManyToMany(cascade = CascadeType.ALL)
	private List<User> asigneeList;

	public Organization() {
		createDate = new Date();
	}

	public Organization(String groupName, User organizationMaker, Date createDate) {
		super();
		this.groupName = groupName;
		this.organizationMaker = organizationMaker;
		this.createDate = createDate;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public User getOrganizationMaker() {
		return organizationMaker;
	}

	public void setOrganizationMaker(User organizationMaker) {
		this.organizationMaker = organizationMaker;
	}

	public String getCreateDate() {
		return DateTimeUtils.format(createDate, "yyyy.MM.dd HH:mm:ss");
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public List<Milestone> getMilestoneList() {
		return milestoneList;
	}

	public void setMilestoneList(List<Milestone> milestoneList) {
		this.milestoneList = milestoneList;
	}

	public List<User> getAsigneeList() {
		return asigneeList;
	}

	public void setAsigneeList(List<User> asigneeList) {
		this.asigneeList = asigneeList;
	}
	
	public List<Issue> getIssueList() {
		return issueList;
	}

	public void setIssueList(List<Issue> issueList) {
		this.issueList = issueList;
	}

	public List<Label> getLabelList() {
		return labelList;
	}

	public void setLabelList(List<Label> labelList) {
		this.labelList = labelList;
	}
	
	public boolean isMatchWriter(User matchUser) {
		return this.organizationMaker.equals(matchUser);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		Organization other = (Organization) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		String str = "\n-------------------Group----------------------------\n";
		str +=  "Group [id=" + id + ", groupName=" + groupName + ", organization=" + organizationMaker + ", createDate="
				+ createDate + "]";
		return str;
	}
}