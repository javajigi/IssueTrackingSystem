package infinitefire.project.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Label {
	@Id
	@GeneratedValue
	private Long id;
	
	@Column(name = "name", length = 20, nullable = false)
	private String name;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "color", nullable = false)
	private LabelColor color;
	
	@JsonIgnore
	@ManyToMany(mappedBy = "labelList")
	private List<Issue> issueList;
	
	public Label() {
		
	}
	
	public Label(long id, String name, LabelColor color, List<Issue> issueList) {
		this.id = id;
		this.name = name;
		this.color = color;
		this.issueList = issueList;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getColor() {
		return color.getColorHex();
	}

	public List<Issue> getIssueList() {
		return issueList;
	}
	
}
