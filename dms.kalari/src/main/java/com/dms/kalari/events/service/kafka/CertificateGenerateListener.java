package com.dms.kalari.events.service.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import com.dms.kalari.events.service.CertificateProcessingService;
import com.dms.kalari.events.service.event.CertificateGenerateEvent;

import jakarta.transaction.Transactional;

@Service
public class CertificateGenerateListener {

    private final CertificateProcessingService processingService;
    private static final Logger log = LoggerFactory.getLogger(CertificateGenerateListener.class);

    public CertificateGenerateListener(CertificateProcessingService processingService) {
        this.processingService = processingService;
    }

    @KafkaListener(topics = "certificate-generate-topic", groupId = "certificate-generator-group")
    @Transactional
    public void consumeCertificateTask(CertificateGenerateEvent event) {
        try {
        	boolean status = processingService.generateCertificateFromEvent(event);
            log.info("✅ Certificate generated for MEI={} ({} bytes)", event.getMeiId(), status);
        } catch (Exception e) {
            log.error("❌ Failed to generate certificate for MEI={}", event.getMeiId(), e);
        }
    }
}
