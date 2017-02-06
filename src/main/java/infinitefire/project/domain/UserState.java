package infinitefire.project.domain;

public enum UserState {
	JOIN,
	WITHDRAW;
	
	public boolean isWithdraw(User user) {
		return user.getState().equals(UserState.WITHDRAW);
	}

}
