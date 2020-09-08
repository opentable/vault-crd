package de.koudingspawn.vault.kubernetes;

import de.koudingspawn.vault.crd.Vault;
import de.koudingspawn.vault.vault.VaultSecret;
import de.koudingspawn.vault.vault.VaultService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;

import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class EventHandlerTest {

    @Mock
    private VaultService vaultService;

    @Mock
    private KubernetesService kubernetesService;

    @InjectMocks
    private EventHandler eventHandler;

    @Test
    public void shouldGenerateKubernetesSecret() throws SecretNotAccessibleException {
        Vault vault = new Vault();
        VaultSecret vaultSecret = new VaultSecret(new HashMap<>(), "COMPARE");

        when(vaultService.generateSecret(vault)).thenReturn(vaultSecret);
        eventHandler.addHandler(vault);

        verify(kubernetesService, times(1)).createSecret(vault, vaultSecret);
    }

    @Test
    public void shouldDoNotingIfSecretForVaultAlreadyExists() {
        Vault vault = new Vault();

        when(kubernetesService.exists(vault)).thenReturn(true);
        eventHandler.addHandler(vault);

        verify(kubernetesService, never()).createSecret(any(), any());
    }

    @Test
    public void shouldDoNothingIfGenerateSecretFails() throws SecretNotAccessibleException {
        Vault vault = new Vault();

        when(vaultService.generateSecret(vault)).thenThrow(SecretNotAccessibleException.class);
        eventHandler.addHandler(vault);

        verify(kubernetesService, never()).createSecret(any(), any());
    }

    @Test
    public void shouldModifySecret() throws SecretNotAccessibleException {
        Vault vault = new Vault();
        VaultSecret vaultSecret = new VaultSecret(new HashMap<>(), "COMPARE");

        when(vaultService.generateSecret(vault)).thenReturn(vaultSecret);
        eventHandler.modifyHandler(vault);

        verify(kubernetesService, times(1)).modifySecret(vault, vaultSecret);
    }

    @Test
    public void shouldDoNothingIfCreateSecretForModificationFails() throws SecretNotAccessibleException {
        Vault vault = new Vault();

        when(vaultService.generateSecret(vault)).thenThrow(SecretNotAccessibleException.class);
        eventHandler.modifyHandler(vault);

        verify(kubernetesService, never()).modifySecret(any(), any());
    }

}
