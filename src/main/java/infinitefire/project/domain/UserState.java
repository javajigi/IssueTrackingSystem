package infinitefire.project.domain;

public enum UserState {
	JOIN,
	WITHDRAW;
	
	public boolean isWithdraw(User user) {
		return this == UserState.WITHDRAW;
	}

}
