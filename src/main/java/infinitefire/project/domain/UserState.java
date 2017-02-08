package infinitefire.project.domain;

public enum UserState {
	JOIN,
	WITHDRAW;
	
	public boolean isWithdraw() {
		return this == UserState.WITHDRAW;
	}

}
