package com.dms.kalari.events.service.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CertificateGenerateEvent {
    private Long meiId;
    private Long eventId;
    private String participantName;
    private String grade;
    private String email;
}
