package com.dms.kalari.core.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import com.dms.kalari.common.BaseDTO;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CoreFileDTO extends BaseDTO {

    private Long fileId;
    private String fileSrc;
    private Long fileRefId;
    private String fileActualName;
    private String fileExten;
    private Long fileSize;
}
