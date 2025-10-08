package com.dms.kalari.events.service.kafka;



import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.dms.kalari.events.service.event.CertificateGenerateEvent;

@Component
public class CertificateProducer {

    private static final String TOPIC = "certificate-generate-topic";
    private final KafkaTemplate<String, CertificateGenerateEvent> kafkaTemplate;

    public CertificateProducer(KafkaTemplate<String, CertificateGenerateEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendCertificateEvent(CertificateGenerateEvent event) {
        kafkaTemplate.send(TOPIC, event);
    }
}
