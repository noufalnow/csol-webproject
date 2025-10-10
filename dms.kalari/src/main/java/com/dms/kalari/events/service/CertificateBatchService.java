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

        List<MemberEventItem> participants = memberEventItemRepository.findByEventIdWhereGradeNotEmpty(eventId,MemberEventItem.CertificateStatus.GENERATED);

        if (participants.isEmpty()) return 0;

        for (MemberEventItem item : participants) {

            CertificateGenerateEvent event = new CertificateGenerateEvent(
                    item.getMeiId(),                                               // meiId
                    eventId,                                                       // eventId
                    item.getMemberEventMember().getUserFname(),                    // participantName
                    item.getMemberEventGrade() != null ? item.getMemberEventGrade().name() : null, // grade / medalType
                    item.getMemberEventMember().getUserEmail(),                    // email
                    item.getMemberEvent().getEventYear(),                          // eventYear
                    item.getMemberEventHost().getNodeId(),                         // hostNodeId
                    item.getMemberEvent().getEventName(),                           // eventName
                    item.getMemberEventHost().getNodeName(),                       // hostName
                    item.getMemberEventItemName(),                                  // itemName
                    item.getApproveDateTime() != null ? item.getApproveDateTime().toString() : null, // resultDate
                    item.getTCreated() != null ? item.getTCreated().toString() : null, 
                    item.getTModified() != null ? item.getTModified().toString() : null, 
                    "https://app.indiankalaripayattufederation.com/verify?id=" + item.getMeiId() // verificationUrl
            );

            certificateProducer.sendCertificateEvent(event);
        }


        return participants.size();
    }
}
