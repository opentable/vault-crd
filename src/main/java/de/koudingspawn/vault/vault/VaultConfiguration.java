package de.koudingspawn.vault.vault;

import org.springframework.context.annotation.Configuration;

@Configuration
public class VaultConfiguration {

//    @Bean
//    public ServiceLocatorFactoryBean slfbForTypeRefresh() {
//        ServiceLocatorFactoryBean slfb = new ServiceLocatorFactoryBean();
//        slfb.setServiceLocatorInterface(TypedSecretGeneratorFactory.class);
//        return slfb;
//    }
//
//    @Configuration
//    @ConditionalOnProperty(name = "kubernetes.vault.auth", havingValue = "token")
//    class VaultTokenConnection extends AbstractVaultConfiguration {
//
//        private final String vaultToken;
//        private final String vaultUrl;
//
//        VaultTokenConnection(@Value("${kubernetes.vault.token}") String vaultToken,
//                             @Value("${kubernetes.vault.url}") String vaultUrl) {
//            this.vaultToken = vaultToken;
//            this.vaultUrl = vaultUrl;
//        }
//
//        @Override
//        public VaultEndpoint vaultEndpoint() {
//            return VaultEndpoint.from(getVaultUrlWithoutPath(vaultUrl));
//        }
//
//        @Override
//        public ClientAuthentication clientAuthentication() {
//            return new TokenAuthentication(vaultToken);
//        }
//
//    }
//
//    @Configuration
//    @ConditionalOnProperty(name = "kubernetes.vault.auth", havingValue = "serviceAccount")
//    class VaultServiceAccountConnection extends AbstractVaultConfiguration {
//
//        private final String vaultUrl;
//        private final String role;
//        private final String path;
//
//        VaultServiceAccountConnection(@Value("${kubernetes.vault.url}") String vaultUrl,
//                                      @Value("${kubernetes.vault.role}") String role,
//                                      @Value("${kubernetes.vault.path:kubernetes}") String path) {
//            this.vaultUrl = vaultUrl;
//            this.role = role;
//            this.path = path;
//        }
//
//        @Override
//        public VaultEndpoint vaultEndpoint() {
//            return VaultEndpoint.from(getVaultUrlWithoutPath(vaultUrl));
//        }
//
//        @Override
//        public ClientAuthentication clientAuthentication() {
//            KubernetesAuthenticationOptions options =
//                    KubernetesAuthenticationOptions.builder().path(path).role(role).build();
//
//            return new KubernetesAuthentication(options, restOperations());
//        }
//    }
//
//    private URI getVaultUrlWithoutPath(String vaultUrl) {
//        return URI.create(vaultUrl.replace("/v1/", ""));
//    }

}
