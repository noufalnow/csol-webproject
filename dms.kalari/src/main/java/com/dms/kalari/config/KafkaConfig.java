package com.dms.kalari.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.FixedBackOff;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import com.dms.kalari.events.service.event.CertificateGenerateEvent;

@Configuration
@EnableKafka
@ConditionalOnProperty(name = "spring.kafka.enabled", havingValue = "true", matchIfMissing = true)

public class KafkaConfig {

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, CertificateGenerateEvent> kafkaListenerContainerFactory(
            ConsumerFactory<String, CertificateGenerateEvent> consumerFactory,
            DeadLetterPublishingRecoverer recoverer) {

        ConcurrentKafkaListenerContainerFactory<String, CertificateGenerateEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(consumerFactory);
        factory.setCommonErrorHandler(errorHandler(recoverer));

        return factory;
    }

    @Bean
    public DefaultErrorHandler errorHandler(DeadLetterPublishingRecoverer recoverer) {
        // Retry 3 times, then send to dead letter topic
        return new DefaultErrorHandler(recoverer, new FixedBackOff(2000L, 3L));
    }

    @Bean
    public DeadLetterPublishingRecoverer recoverer(org.springframework.kafka.core.KafkaTemplate<Object, Object> template) {
        // messages failing after retries go to certificate-generate-topic.DLT
        return new DeadLetterPublishingRecoverer(template);
    }
}

