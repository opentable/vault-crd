package de.koudingspawn.vault.kubernetes.scheduler;

import de.koudingspawn.vault.crd.Vault;

public interface RequiresRefresh {

    boolean refreshIsNeeded(Vault resource) throws SecretNotAccessibleException;

}
