package de.koudingspawn.vault;

import org.springframework.scheduling.annotation.EnableScheduling;

import com.opentable.credentials.client.api.EnableCredentialsClient;
import com.opentable.server.OTApplication;

@EnableScheduling
@EnableCredentialsClient
public class VaultApplication {

	public static void main(String[] args) {
		OTApplication.run(VaultApplication.class, args);
	}
}
