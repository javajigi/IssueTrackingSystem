package infinitefire.project.storage;

public enum FileType {
	PROFILE,
	ATTACHMENT;
	
	public boolean isProfile() {
		return this == FileType.PROFILE;
	}
	
	public boolean isAttachment() {
		return this == FileType.ATTACHMENT;
	}
}
