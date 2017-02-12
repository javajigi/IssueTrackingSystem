package infinitefire.project.domain;

public enum OrganizationState {
	INITIALIZE,
	ACTIVE,
	DOMANT,
	CLOSED;
	
	private String stateCheck;
	
	private OrganizationState() {	}
	
	private OrganizationState(String stateCheck) {
		this.stateCheck = stateCheck;
	}
	
	public String getStateCheck() {
		return stateCheck;
	}
}
