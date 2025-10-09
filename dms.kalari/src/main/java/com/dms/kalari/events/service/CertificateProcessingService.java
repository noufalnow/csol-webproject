package com.dms.kalari.events.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dms.kalari.events.dto.CertificateGenerationResultDTO;
import com.dms.kalari.events.entity.CertificateFileRecord;
import com.dms.kalari.events.entity.MemberEventItem;
import com.dms.kalari.events.entity.MemberEventItem.CertificateStatus;
import com.dms.kalari.events.repository.MemberEventItemRepository;
import com.dms.kalari.events.service.event.CertificateGenerateEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class CertificateProcessingService {
    private static final Logger log = LoggerFactory.getLogger(CertificateProcessingService.class);
    
    private final MemberEventItemRepository repository;
    private final CertificateService certificateService;
    private final ObjectMapper objectMapper;

    public CertificateProcessingService(MemberEventItemRepository repository,
                                       CertificateService certificateService,
                                       ObjectMapper objectMapper) {
        this.repository = repository;
        this.certificateService = certificateService;
        this.objectMapper = objectMapper;
    }

   
    public boolean generateCertificateFromEvent(CertificateGenerateEvent event) throws Exception {
        MemberEventItem mei = repository.findByIdWithEagerAssociations(event.getMeiId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid meiId: " + event.getMeiId()));

        List<CertificateFileRecord> records = Optional.ofNullable(mei.getCertificateHistory())
                .orElseGet(ArrayList::new);

        boolean newFileCreated = false;
        String fileName = null;

        try {
            // ✅ Generate or get the signed certificate
            CertificateGenerationResultDTO result = certificateService.generateOrGetSignedCertificate(mei);
            newFileCreated = result.isNewFile();
            fileName = result.getFileName();

            // 🧾 Only add a new record if a new file was created
            if (newFileCreated) {
                CertificateFileRecord newRecord = new CertificateFileRecord(
                    fileName, 
                    LocalDateTime.now(), 
                    CertificateStatus.ACTIVE.name()
                );
                records.add(newRecord);

                // Mark older entries as REVOKED
                if (records.size() > 1) {
                    records.subList(0, records.size() - 1)
                          .forEach(record -> record.setStatus(CertificateStatus.REVOKED.name()));
                }
            }

            // ✅ Convert history to JSON
            String historyJson = objectMapper.writeValueAsString(records);

            // ✅ Update status and history in DB
            repository.updateCertificateStatusAndHistory(
                    mei.getMeiId(),
                    MemberEventItem.CertificateStatus.GENERATED,
                    historyJson
            );

            return true;

        
        } catch (Exception e) {
            // Handle other exceptions
            log.error("Unexpected error during certificate generation for meiId: {}", mei.getMeiId(), e);
            updateFailureState(mei.getMeiId(), records);
            throw e;
        }
    }

    private void updateFailureState(Long meiId, List<CertificateFileRecord> records) throws JsonProcessingException {
        String historyJson = objectMapper.writeValueAsString(records);
        repository.updateCertificateStatusAndHistory(
                meiId,
                MemberEventItem.CertificateStatus.FAILED,
                historyJson
        );
    }
}