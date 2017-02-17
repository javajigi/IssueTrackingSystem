package infinitefire.project.domain;

public enum OrganizationState {	
	favorite("favorite"),
	ordinary("favorite_border");
	
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
