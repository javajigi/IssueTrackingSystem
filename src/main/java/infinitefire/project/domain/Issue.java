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
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.fasterxml.jackson.annotation.JsonIgnore;

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

	@Enumerated(EnumType.STRING)
	@Column(name = "state", nullable = false)
	private IssueState state;

/*	@ManyToMany
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_issue_assignee"))
	private List<User> assigneeList;*/

	@JsonIgnore
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "ISSUE_ASSIGNEE", joinColumns = { @JoinColumn(name = "ISSUE_ID") }, inverseJoinColumns = { @JoinColumn(name = "ASSIGNEE_ID") })
	private List<User> assigneeList;

	@ManyToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_issue_milestone"))
	private Milestone milestone;

	@JsonIgnore
	@OneToMany(mappedBy = "issue")
	private List<Comment> commentList;

	@JsonIgnore
	@ManyToMany(mappedBy = "issueList")
	private List<Label> labelList;

	public Issue() {
		this.state = IssueState.OPEN;
		this.writeDate = new Date();
	}

	public Issue(String subject, String contents, User writer, int label, IssueState state, 
			List<User> assigneeList, List<Label> labelList, Milestone milestone, List<Comment> commentList) {
		super();
		this.subject = subject;
		this.contents = contents;
		this.writer = writer;
		this.state = state;
		this.assigneeList = assigneeList;
		this.milestone = milestone;
		this.labelList = labelList;
		this.commentList = commentList;
		//this.writeDate = new Date();
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

	public String getWriteDate() {
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

	public String getStateCheck() {
		return state.getStateCheck();
	}

	public IssueState getState() {
		return state;
	}

	public void setState(IssueState state) {
		this.state = state;
	}
	public void toggleState(boolean state) {
		this.state = state ? IssueState.OPEN : IssueState.CLOSE;
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

	public List<Label> getLabelList() {
		return labelList;
	}

	public boolean isMatchWriter(User matchUser) {
		return this.writer.equals(matchUser);
	}

	public boolean addAssignee(User user) {
		if(assigneeList.contains(user)) {
			return false;
		}
		assigneeList.add(user);
		return true;
	}
	
	public boolean deleteAssignee(User user) {
		
		return assigneeList.remove(user);
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
		String str = "Issue [id=" + id + ", subject=" + subject + ", contents=" + contents + ", writeDate=" + writeDate
				+ ", writer=" + writer + ", label=" + ", state=" + state+"\n";
		str += "-------------------CommentList----------------------------\n";
		if (commentList != null) {
			for (Comment comment : commentList) {
				str += comment + "\n";
			}
		}

		return str;
	}
}
