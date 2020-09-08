package de.koudingspawn.vault.spring;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import de.koudingspawn.vault.admissionreview.AdmissionReviewRestService;
import de.koudingspawn.vault.admissionreview.AdmissionReviewService;

@Configuration
@Import({
        AdmissionReviewRestService.class,
        AdmissionReviewService.class
})
public class AdminissionResourceConfiguration {
}
