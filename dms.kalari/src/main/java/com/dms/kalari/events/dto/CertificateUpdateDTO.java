package com.dms.kalari.events.dto;

import com.dms.kalari.events.entity.MemberEventItem.CertificateStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CertificateUpdateDTO {
    private Long meiId;
    private CertificateStatus status;
    private String certificateHistoryJson;
}

