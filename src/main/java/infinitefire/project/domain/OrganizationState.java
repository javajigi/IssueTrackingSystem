package infinitefire.project.domain;

public enum OrganizationState {	
	STAR("favorite"),
	DEFAULT("favorite_border");
	
	private String stateCheck;
	
	private OrganizationState() {
		
	}
	
	private OrganizationState(String stateCheck) {
		this.stateCheck = stateCheck;
	}
	
	public String getStateCheck() {
		return stateCheck;
	}
}
