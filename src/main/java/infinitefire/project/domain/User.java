package infinitefire.project.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class User {
	public static final GuestUser GUEST_USER = new GuestUser();
	
	@Id
	@GeneratedValue
	private Long id;

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

	public User(String userId, String name, String password, String profileUrl, String state) {
		super();
		this.userId = userId;
		this.name = name;
		this.password = password;
		this.profileUrl = profileUrl;
		this.state = state;
	}

	public boolean isMatchId(Long inputId) {
		if (inputId == null) {
			return false;
		}

		return inputId.equals(id);
	}
	
	public boolean isMatchPassword(String password) {
		return this.password.equals(password);
	}
	
	public void modify(User modifiedUser) {
		this.password = modifiedUser.password;
		this.name = modifiedUser.name;
		this.profileUrl = modifiedUser.profileUrl;
	}
	
	public void withdraw(String newState) {
		this.state = newState;
	}
	
	public boolean isWithdraw() {
		return this.state.equals("withdraw");
	}
	
	public long getId() {
		return id;
	}

	public void setId(Long id) {
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
	
	public boolean isGuestUser() {
		return false;
	}
	
	private static class GuestUser extends User {
		@Override
		public boolean isGuestUser() {
			return true;
		}
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
		User other = (User) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return "User [id=" + id + ", userId=" + userId + ", name=" + name + ", password=" + password + ", profileUrl="
				+ profileUrl + ", state=" + state + "]";
	}
}
