package com.dms.kalari.events.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CertificateGenerationResultDTO {
    private String fileName;
    private boolean newFile;

}