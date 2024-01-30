package sunuser.mylife.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties("storage")
public class StorageProperties {
	private String location = "upload-dir";

	public void setLocation(String loc) {
		this.location = loc;
	}

	public String getLocation() {
		return location;
	}
}
