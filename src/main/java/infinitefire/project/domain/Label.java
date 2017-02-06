package infinitefire.project.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Label {
	@Id
	@GeneratedValue
	private Long id;
	
	@Column(name = "name", length = 20, nullable = false)
	private String name;
	
	@ManyToOne
	@JoinColumn(foreignKey = @ForeignKey(name = "fk_label_issue"))
	private Issue issue;
}
