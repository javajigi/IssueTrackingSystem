package infinitefire.project.domain;

import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Transient;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class User {
	public static final GuestUser GUEST_USER = new GuestUser();
	
	@Id
	@GeneratedValue
	private Long id;

	@Column(name = "userId", length = 16, nullable = false, updatable = false, unique = true)
	private String userId;
	
	@Column(name = "name", length = 16, nullable = false)
	private String name;
	
	@JsonIgnore
	@Column(name = "password", nullable = false)
	private String password;

	@Column(name = "profile")
	private String profile;
	
	@JsonIgnore
	@Column(name = "state", nullable = false)
	private UserState state;
	
	@JsonIgnore
	@ManyToMany(cascade = CascadeType.ALL, mappedBy = "memberList")
	private Set<Organization> organizationList;
	
	@Transient
	private BCryptPasswordEncoder passwordEncoder;
	
	public User() {
//		this.profileUrl = "none.jpg";
		this.state = UserState.JOIN;
		passwordEncoder = new BCryptPasswordEncoder();
	}

	public User(String userId, String name, String password, String profile, UserState userState, List<Organization> organizationList) {
		
		super();
		this.userId = userId;
		this.name = name;
		this.password = passwordEncoder.encode(password);
		this.profile = profile;
		this.state = UserState.JOIN;
	}

	public boolean isMatchId(Long inputId) {
		if (inputId == null) {
			return false;
		}

		return inputId.equals(id);
	}
	
	public boolean isMatchPassword(User user) {
		return passwordEncoder.matches(user.getPassword(), this.password);
	}
	
	public boolean isMatchPassword(String password) {
		return passwordEncoder.matches(password, this.password);
	}
	
	public boolean isMatchPassword(String password, String userPassword) {
		return passwordEncoder.matches(password, userPassword);
	}
	
	public void modify(User modifiedUser) {
		this.password = modifiedUser.password;
		this.name = modifiedUser.name;
		this.profile = modifiedUser.profile;
	}
	
	public void withdraw() {
		this.state = UserState.WITHDRAW;
		this.profile = "none.png";
	}
	
	public boolean isWithdraw() {
		return state.isWithdraw();
	}
	
	public Long getId() {
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
		this.password = passwordEncoder.encode(password);
	}

	public String getProfile() {
		return profile;
	}

	public void setProfile(String profile) {
		this.profile = profile;
	}
	
	public void setDefaultProfile() {
		this.profile = "default.png";
	}
	
	public boolean isDefaultProfile() {
		return this.profile.equals("default.png");
	}
	
	public void setNoneProfile() {
		this.profile = "none.png";
	}

	public UserState getState() {
		return state;
	}

	public void setState(UserState state) {
		this.state = state;
	}
	
	public boolean isGuestUser() {
		return false;
	}
	
	
	
	public Set<Organization> getOrganizationList() {
		return organizationList;
	}
	public void setOrganizationList(Set<Organization> organizationList) {
		this.organizationList = organizationList;
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
		return "User [id=" + id + ", userId=" + userId + ", name=" + name + ", password=" + password + ", profile="
				+ profile + ", state=" + state + "]";
	}

}
