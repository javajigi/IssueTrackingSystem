package infinitefire.project.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class User {
	@Id
	@GeneratedValue
	private long id;
	
	@Column(name = "userId", length = 16, nullable = false, updatable = false, unique = true)
	private String userId;
	
	@Column(name = "name", length = 20, nullable = false)
	private String name;
	
	@JsonIgnore
	@Column(name = "password", length = 20, nullable = false)
	private String password;
	
	@JsonIgnore
	@Column(name = "profileUrl")
	private String profileUrl;
	
	@JsonIgnore
	@Column(name = "state", nullable = false)
	private String state;
	
	public User() {}

	public User(long id, String userId, String name, String password, String profileUrl, String state) {
		super();
		this.id = id;
		this.userId = userId;
		this.name = name;
		this.password = password;
		this.profileUrl = profileUrl;
		this.state = state;
	}

	public boolean isMatchPassword(String password) {
		return this.password.equals(password);
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getProfileUrl() {
		return profileUrl;
	}

	public void setProfileUrl(String profileUrl) {
		this.profileUrl = profileUrl;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}
	
	@Override
	public String toString() {
		return "User [id=" + id + ", userId=" + userId + ", name=" + name + ", password=" + password + ", profileUrl="
				+ profileUrl + ", state=" + state + "]";
	}
}
