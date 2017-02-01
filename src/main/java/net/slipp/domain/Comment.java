package net.slipp.domain;

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

@Entity
public class Comment {
	@Id
	@GeneratedValue
	private long id;
	
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
	
	public Comment(){}

	public Comment(long id, String contents, Issue issue, User writer) {
		super();
		this.id = id;
		this.contents = contents;
		this.writeDate = new Date();
		this.issue = issue;
		this.writer = writer;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getContents() {
		return contents;
	}

	public void setContents(String contents) {
		this.contents = contents;
	}

	public Date getWriteDate() {
		return writeDate;
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

	@Override
	public String toString() {
		return "Comment [id=" + id + ", contents=" + contents + ", writeDate=" + writeDate + ", issue=" + issue
				+ ", writer=" + writer + "]";
	}
}


