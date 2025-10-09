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
    private String medalType;         // rename to medalType if you want consistency
    private String email;

    // For caching / file path reconstruction
    private Integer eventYear;
    private Long hostNodeId;

    // Certificate data
    private String eventName;
    private String hostName;
    private String itemName;
    private String resultDate;     // or LocalDateTime if you want proper type
    
    private String tCreated; 
    private String tModified;

    // Optional: verification URL for QR code
    private String verificationUrl;
    

}
