package infinitefire.project.storage;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("storage")
public class StorageProperties {
	// Folder location for storing files
    // TODO 설정 파일에서 관리하도록 개선
    private String location = "src/main/resources/static/file";

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

}
