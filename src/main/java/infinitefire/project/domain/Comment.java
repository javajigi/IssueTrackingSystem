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
	
	private String attachment;
	
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
	// TODO 이와 같은 속성을 사용하지 말고 handlebars의 helper를 등록해서 처리해 본다.
	private boolean isMyComment = false;
	
	private boolean isAttachmentExist = false;
	
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
		issue.incCountComment();
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
	
	public boolean isMatchWriter(User loginUser) {
		return this.writer.equals(loginUser);
	}
	
	public void setIsAttachmentExist(boolean isAttachmentExsist) {
		this.isAttachmentExist = isAttachmentExsist;
	}
	
	public boolean getIsAttachmentExist() {
		return isAttachmentExist;
	}
	
	public void decDelete(){
		this.issue.decCountComment();
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
		Comment other = (Comment) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "Comment [id=" + id + ", contents=" + contents + ", writeDate=" + writeDate +
				", writer=" + writer + ", isMyComment="+ isMyComment + "]";
	}

	public String getAttachment() {
		return attachment;
	}

	public void setAttachment(String attachment) {
		this.attachment = attachment;
	}
	
	public boolean isAttachmentEmpty() {
		return attachment.isEmpty();
	}
}


