package infinitefire.project.domain;

public enum UserState {
	JOIN,
	WITHDRAW;
	
    // TODO User 사용하지 않고 있어 인자로 전달할 필요가 없음.
	public boolean isWithdraw(User user) {
		return this == UserState.WITHDRAW;
	}

}
