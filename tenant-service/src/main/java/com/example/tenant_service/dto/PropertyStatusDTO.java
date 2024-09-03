package com.example.tenant_service.dto;

import java.sql.Date;

import com.example.tenant_service.common.BaseDTO;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PropertyStatusDTO extends BaseDTO {
    private Long pstsId;
    private Short pstsType = 1;
    private Long pstsPropId;
    private String pstsRemarks;
    private Date pstsStatusDate;
    private Long pstsAttachProp;
}
