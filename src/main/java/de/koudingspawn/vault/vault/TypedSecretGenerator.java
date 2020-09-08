package de.koudingspawn.vault.vault;

import de.koudingspawn.vault.crd.Vault;
import de.koudingspawn.vault.crd.VaultSpec;

public interface TypedSecretGenerator {

    VaultSecret generateSecret(Vault resource) ;

    String getHash(VaultSpec spec);

}
