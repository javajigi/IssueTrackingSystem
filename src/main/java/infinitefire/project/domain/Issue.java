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
	private List<Milestone> milestoneList;
	
	@OneToMany(mappedBy = "issue")
	private List<Comment> commentList;
	
	public Issue() {}

	public Issue(String subject, String contents, User writer, int label, String state, 
			List<User> assigneeList, List<Comment> commentList, List<Milestone> milestoneList) {
		super();
		this.subject = subject;
		this.contents = contents;
		this.writer = writer;
		this.label = label;
		this.state = state;
		this.assigneeList = assigneeList;
		this.commentList = commentList;
		this.milestoneList = milestoneList;
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

	@Override
	public String toString() {
		return "Issue [id=" + id + ", subject=" + subject + ", contents=" + contents + ", writeDate=" + writeDate
				+ ", writer=" + writer + ", label=" + label + ", state=" + state + ", assigneeList="+"]";
	}
}
