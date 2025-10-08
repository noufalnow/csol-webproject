package com.dms.kalari.events.service;

import com.dms.kalari.events.entity.MemberEventItem;

import com.dms.kalari.events.repository.MemberEventItemRepository;
import com.dms.kalari.events.service.event.CertificateGenerateEvent;
import com.dms.kalari.events.service.kafka.CertificateProducer;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CertificateBatchService {

    private final MemberEventItemRepository memberEventItemRepository;
    private final CertificateProducer certificateProducer;

    public CertificateBatchService(MemberEventItemRepository memberEventItemRepository,
                                   CertificateProducer certificateProducer) {
        this.memberEventItemRepository = memberEventItemRepository;
        this.certificateProducer = certificateProducer;
    }

    @Transactional(readOnly = true)
    public int processCertificateBatch(Long eventId) {

        // 1️⃣ Get all member event items with grade not null
        List<MemberEventItem> participants = memberEventItemRepository.findByEventIdWhereGradeNotEmpty(eventId);

        if (participants.isEmpty()) {
            return 0;
        }

        // 2️⃣ Publish each participant to Kafka
        for (MemberEventItem item : participants) {
        	CertificateGenerateEvent event = new CertificateGenerateEvent(
        		    item.getMeiId(),
        		    eventId,
        		    item.getMemberEventMember().getUserFname(),
        		    item.getMemberEventGrade() != null ? item.getMemberEventGrade().name() : null,
        		    item.getMemberEventMember().getUserEmail()
        		);
            certificateProducer.sendCertificateEvent(event);
        }

        // 3️⃣ Return how many messages sent
        return participants.size();
    }
}

