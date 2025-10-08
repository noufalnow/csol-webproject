package com.dms.kalari.events.service;

import org.springframework.stereotype.Service;

import com.dms.kalari.events.entity.MemberEventItem;
import com.dms.kalari.events.repository.MemberEventItemRepository;

import jakarta.transaction.Transactional;

@Service
public class CertificateProcessingService {

    private final MemberEventItemRepository repository;
    private final CertificateService certificateService;

    public CertificateProcessingService(MemberEventItemRepository repository,
                                       CertificateService certificateService) {
        this.repository = repository;
        this.certificateService = certificateService;
    }

    @Transactional
    public byte[] generateCertificateForEvent(Long meiId) throws Exception {
        MemberEventItem mei = repository.findByIdWithEagerAssociations(meiId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid meiId: " + meiId));
        return certificateService.generateOrGetSignedCertificate(mei);
    }
}
