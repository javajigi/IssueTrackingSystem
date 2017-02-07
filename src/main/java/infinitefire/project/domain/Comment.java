package infinitefire.project.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import infinitefire.project.utils.DateTimeUtils;

@Entity
public class Comment {
	@Id
	@GeneratedValue
	private Long id;
	
	@Lob
	private String contents;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "writeDate", nullable = false, updatable = false)
	private Date writeDate;
	
	@ManyToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_comment_issue"))
	private Issue issue;
	
	@ManyToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_comment_user"))
	private User writer;
	
	@Transient
	private boolean isMyComment = false;
	
	public Comment(){
		this.writeDate = new Date();
	}

	public Comment(String contents, Issue issue, User writer) {
		super();
		this.contents = contents;
		this.issue = issue;
		this.writer = writer;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Issue getIssue() {
		return issue;
	}

	public void setIssue(Issue issue) {
		this.issue = issue;
	}

	public User getWriter() {
		return writer;
	}

	public void setWriter(User writer) {
		this.writer = writer;
	}
	
	public void setIsMyComment(boolean isMyComment){
		this.isMyComment = isMyComment;
	}
	
	public boolean getIsMyComment() {
		return isMyComment;
	}

	public boolean isMyComment(Long writerId) {
		return writer.isMatchId(writerId);
	}
	
	@Override
	public String toString() {
		return "Comment [id=" + id + ", contents=" + contents + ", writeDate=" + writeDate +
				", writer=" + writer + ", isMyComment="+ isMyComment + "]";
	}
}


