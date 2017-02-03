package infinitefire.project.domain;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import infinitefire.project.utils.DateTimeUtils;

@Entity
public class Issue {
	@Id
	@GeneratedValue
	private Long id;
	
	@Column(name = "subject", length = 100, nullable = false)
	private String subject;
	
	@Lob
	private String contents;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "writeDate", nullable = false, updatable = false)
	private Date writeDate;
	
	@ManyToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_issue_writer"))
	private User writer;
	
	@Column(name = "label", nullable = false)
	private int label;
	
	@Column(name = "state", nullable = false)
	private String state;
	
	@ManyToMany
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_issue_assignee"))
	private List<User> assigneeList;
	
	@ManyToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_issue_milestone"))
	private Milestone milestone;
	
	@OneToMany(mappedBy = "issue")
	private List<Comment> commentList;
	
	public Issue() {}

	public Issue(String subject, String contents, User writer, int label, String state) { 
//			Milestone milestone, List<User> assigneeList, List<Comment> commentList) {
		super();
		this.subject = subject;
		this.contents = contents;
		this.writer = writer;
		this.label = label;
		this.state = state;
//		this.milestone = milestone;
//		this.assigneeList = assigneeList;
//		this.commentList = commentList;
		this.writeDate = new Date();
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

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public String getFormattedWriteDate() {
		return DateTimeUtils.format(writeDate, "yyyy.MM.dd HH:mm:ss");
	}

	public void setWriteDate(Date writeDate) {
		this.writeDate = writeDate;
	}

	public User getWriter() {
		return writer;
	}

	public void setWriter(User writer) {
		this.writer = writer;
	}

	public int getLabel() {
		return label;
	}

	public void setLabel(int label) {
		this.label = label;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public List<User> getAssigneeList() {
		return assigneeList;
	}

	public void setAssigneeList(List<User> assigneeList) {
		this.assigneeList = assigneeList;
	}

	public List<Comment> getCommentList() {
		return commentList;
	}

	public void setCommentList(List<Comment> commentList) {
		this.commentList = commentList;
	}

	public Milestone getMilestone() {
		return milestone;
	}

	public void setMilestone(Milestone milestone) {
		this.milestone = milestone;
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
		Issue other = (Issue) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "Issue [id=" + id + ", subject=" + subject + ", contents=" + contents + ", writeDate=" + writeDate
				+ ", writer=" + writer + ", label=" + label + ", state=" + state+"]";
	}
}
