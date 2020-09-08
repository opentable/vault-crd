package de.koudingspawn.vault.vault;

import com.codahale.metrics.health.HealthCheck;

import org.springframework.stereotype.Component;

@Component
public class VaultHealthCheck extends HealthCheck {

    private final VaultCommunication vaultCommunication;

    public VaultHealthCheck(VaultCommunication vaultCommunication) {
        this.vaultCommunication = vaultCommunication;
    }


    @Override
    protected Result check() throws Exception {
       if (this.vaultCommunication.isHealthy()) {
           return Result.healthy();
       }
       return Result.unhealthy("down");
    }
}
