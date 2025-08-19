package com.dms.kalari.dto;

import java.sql.Date;

import com.dms.kalari.common.BaseDTO;

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
