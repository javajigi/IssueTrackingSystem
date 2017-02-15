package infinitefire.project.domain;

import java.util.Calendar;
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
import infinitefire.project.utils.RegularExpressionUtils;

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
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_issue_organization"))
	private Organization organization;
	
	@ManyToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_issue_milestone"))
	private Milestone milestone;

	@JsonIgnore
	@OneToMany(mappedBy = "issue", cascade = CascadeType.ALL)
	private List<Comment> commentList;

	@JsonIgnore
	@Column(name = "countComment", columnDefinition = "Decimal(10) default '0'")
	private int countComment;
	
	@JsonIgnore
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "ISSUE_LABEL", joinColumns = { @JoinColumn(name = "ISSUE_ID") }, inverseJoinColumns = { @JoinColumn(name = "LABEL_ID") })
	private List<Label> labelList;

	public Issue() {
		this.state = IssueState.OPEN;
		this.writeDate = new Date();
		if(commentList != null)
			countComment = commentList.size();
	}

	public Issue(String subject, String contents, User writer, int label, IssueState state, 
			List<User> assigneeList, List<Label> labelList, Milestone milestone, List<Comment> commentList,
			int countComment) {
		super();
		this.subject = subject;
		this.contents = contents;
		this.writer = writer;
		this.state = state;
		this.assigneeList = assigneeList;
		this.milestone = milestone;
		this.labelList = labelList;
		this.commentList = commentList;
		this.countComment = countComment;
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
		this.subject = RegularExpressionUtils.convertString(subject);
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

	public int getCountComment() {
		return countComment;
	}

	public void setCountComment() {
		this.countComment = commentList.size();
	}

	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	public Milestone getMilestone() {
		return milestone;
	}

	public void setMilestone(Milestone milestone) {
		this.milestone = milestone;
	}
	
	public boolean deleteMilestone(Milestone milestone) {
		if(this.milestone.equals(milestone)) {
			this.milestone = null;
			return true;
		}		
		return false;
	}

	public List<Label> getLabelList() {
		return labelList;
	}
	
	public void setLabelList(List<Label> labelList) {
		this.labelList = labelList;
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

	public boolean addLabel(Label label) {
		if(labelList.contains(label)) {
			return false;
		}
		labelList.add(label);
		return true;
	}
	public boolean deleteLabel(Label label) {
		
		return labelList.remove(label);
	}
		
	private static final long SECOND_MILLIS = 1000;
	private static final long MINUTE_MILLIS = 60 * SECOND_MILLIS;
	private static final long HOUR_MILLIS = 60 * MINUTE_MILLIS;
	private static final long DAY_MILLIS = 24 * HOUR_MILLIS;
	private static final long MONTH_MILLIS = 30 * DAY_MILLIS;
	private static final long YEAR_MILLIS = 12 * MONTH_MILLIS;
	public String getDueDate() {
		long writeMillis = writeDate.getTime();
		long currentMillis =  new Date().getTime();
		if (writeMillis > currentMillis || writeMillis <= 0) {
	      return null;
		}
		final long diff = currentMillis - writeMillis;
		if (diff < MINUTE_MILLIS) {
		    return "just now";
		}
		else if (diff < HOUR_MILLIS) {
			long minutes = (int)(diff/MINUTE_MILLIS);        	
			return minutes <= 1 ? "a minute ago" : minutes + " minutes ago";
		}
		else if (diff < DAY_MILLIS) {
			long hours = (int)(diff/HOUR_MILLIS);        	
			return hours <= 1 ? "a hour ago" : hours + " hours ago";        	
		} else if(diff < MONTH_MILLIS){
			long days = (int)(diff/DAY_MILLIS);
			return days <= 1 ? "a day ago" : days + " days ago";
		} else if(diff < YEAR_MILLIS){      
			long months = (int)(diff/MONTH_MILLIS);
			return months <= 1 ? "a month ago" : months + " months ago";
		} else {      
			long years = (int)(diff/YEAR_MILLIS);
			return years <= 1 ? "a year ago" : years + " years ago";
		}
		//        return currentMillis + "/" + writeMillis;
		//        Calendar calendar = Calendar.getInstance();
		//        calendar.setTimeInMillis(writeMills);//
		//        if(calendar.get(Calendar.YEAR) == Calendar.getInstance().get(Calendar.YEAR)) {
		//            return calendar.get(Calendar.MONTH) + "월 " + calendar.get(Calendar.DAY_OF_MONTH) + "일 "
		//                    + calendar.get(Calendar.AM_PM) + " " + calendar.get(Calendar.HOUR_OF_DAY) + ":" + String.format("%02d", calendar.get(Calendar.MINUTE));
		//        } else {
		//        	return  calendar.get(Calendar.YEAR) + "년 " + calendar.get(Calendar.MONTH) + "월 " + calendar.get(Calendar.DAY_OF_MONTH) + "일 "
		//                    + calendar.get(Calendar.AM_PM) + " " + calendar.get(Calendar.HOUR_OF_DAY) + ":" + String.format("%02d", calendar.get(Calendar.MINUTE));
		//        }
	}
	
	public void incCountComment() {
		this.countComment++;
	}
	
	public void decCountComment() {
		this.countComment--;
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
		str += "-------------------CommentList, count="+countComment+"----------------------------\n";
		if (commentList != null) {
			for (Comment comment : commentList) {
				str += comment + "\n";
			}
		}

		return str;
	}
}
