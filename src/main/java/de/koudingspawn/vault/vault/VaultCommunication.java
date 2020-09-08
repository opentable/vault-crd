package de.koudingspawn.vault.vault;

import de.koudingspawn.vault.crd.VaultDockerCfgConfiguration;
import de.koudingspawn.vault.crd.VaultPkiConfiguration;
import de.koudingspawn.vault.crd.VaultType;
import de.koudingspawn.vault.vault.impl.dockercfg.PullSecret;
import de.koudingspawn.vault.vault.impl.pki.PKIRequest;
import de.koudingspawn.vault.vault.impl.pki.PKIResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.vault.VaultException;
import org.springframework.vault.core.VaultTemplate;
import org.springframework.vault.core.VaultVersionedKeyValueOperations;
import org.springframework.vault.support.VaultResponseSupport;
import org.springframework.vault.support.Versioned;
import org.springframework.vault.support.Versioned.Version;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;

import java.util.HashMap;
import java.util.Optional;
import java.util.regex.Pattern;

import com.opentable.credentials.client.api.exceptions.CredentialsManagementException;
import com.opentable.credentials.client.internal.VaultOps;
import com.opentable.credentials.client.internal.VaultPath;
import com.opentable.credentials.client.internal.VaultPathResolver;
import com.opentable.credentials.client.internal.model.ReadVaultKVResponse;

@Component
public class VaultCommunication {

    private static final Logger log = LoggerFactory.getLogger(VaultCommunication.class);
    private static final Pattern keyValuePattern = Pattern.compile("^.*?\\/.*?$");

    private final VaultOps vaultTemplate;
    private final VaultPathResolver vaultPathResolver;

    public VaultCommunication(VaultPathResolver vaultPathResolver, VaultOps vaultTemplate) {
        this.vaultTemplate = vaultTemplate;
        this.vaultPathResolver = vaultPathResolver;
    }

    public PKIResponse createPki(String path, VaultPkiConfiguration configuration)  {
        PKIRequest pkiRequest = generateRequest(configuration);

        HttpEntity<PKIRequest> requestEntity = new HttpEntity<>(pkiRequest);
            return vaultTemplate.write(restOperations -> restOperations.postForObject(path, requestEntity, PKIResponse.class));

    }

    public PKIResponse getCert(String path) {
        return getRequest(path, PKIResponse.class);
    }

    public PullSecret getDockerCfg(String path, VaultDockerCfgConfiguration dockerCfgConfiguration) {
        if (dockerCfgConfiguration.getType().equals(VaultType.KEYVALUE)) {
            return getRequest(path, PullSecret.class);
        } else {
            return getVersionedSecret(path, Optional.ofNullable(dockerCfgConfiguration.getVersion()), PullSecret.class);
        }
    }

    public HashMap getKeyValue(String path) {
        return getRequest(path, HashMap.class);
    }

    private <T> T getRequest(VaultPath path, Class<T> clazz) throws SecretNotAccessibleException {
            VaultResponseSupport<T> response = vaultTemplate.readSecret(path, clazz);
            if (response != null) {
                return response.getData();
            } else {
                throw new SecretNotAccessibleException(String.format("The secret %s is not available or in the wrong format.", path));
            }

    }

    private PKIRequest generateRequest(VaultPkiConfiguration configuration) {
        PKIRequest pkiRequest = new PKIRequest();

        if (configuration != null) {
            if (!StringUtils.isEmpty(configuration.getCommonName())) {
                pkiRequest.setCommon_name(configuration.getCommonName());
            }
            if (!StringUtils.isEmpty(configuration.getAltNames())) {
                pkiRequest.setAlt_names(configuration.getAltNames());
            }
            if (!StringUtils.isEmpty(configuration.getIpSans())) {
                pkiRequest.setIp_sans(configuration.getIpSans());
            }
            if (!StringUtils.isEmpty(configuration.getTtl())) {
                pkiRequest.setTtl(configuration.getTtl());
            }
        }

        return pkiRequest;
    }

    public HashMap getVersionedSecret(String path, Optional<Integer> version) {
        return getVersionedSecret(path, version, HashMap.class);
    }

    private <T> T getVersionedSecret(VaultPath path, Optional<Integer> version, Class<T> clazz) {
        String mountPoint = extractMountPoint(path);
        String extractedKey = extractKey(path);

        ReadVaultKVResponse.VaultKV<T> versionedKV = vaultTemplate.readSecretAndMetadata(path, new ParamHashMap.class);
//        Versioned<T> versionedResponse;
//
//        try {
//            if (version.isPresent()) {
//                versionedResponse = versionedKV.get(extractedKey, Version.from(version.get()), clazz);
//            } else {
//                versionedResponse = versionedKV.get(extractedKey, clazz);
//            }
//
//            if (versionedResponse != null) {
//                return versionedResponse.getData();
//            }

         //   throw new SecretNotAccessibleException(String.format("The secret %s is not available or in the wrong format.", path));

     //   }
        return versionedKV.getSecret();
    }

    public boolean isHealthy() {
        try {
            vaultTemplate.lookupSelf();
        } catch (CredentialsManagementException e) {
            return false;
        }
        return true;
    }

    private String extractMountPoint(String path)  {
        if (keyValuePattern.matcher(path).matches()) {
            return path.split("/", 2)[0];
        }

        throw new RuntimeException(String.format("Could not extract mountpoint from path: %s. A valid path looks like 'mountpoint/key'", path));
    }

    private String extractKey(String path) {
        if (keyValuePattern.matcher(path).matches()) {
            return path.split("/", 2)[1];
        }

        throw new RuntimeException(String.format("Could not extract key from path: %s. A valid path looks like 'mountpoint/key'", path));
    }
}
