package com.dms.kalari.admin.dto;

import com.dms.kalari.branch.entity.Node;
import com.dms.kalari.common.BaseDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DesignationDTO extends BaseDTO {

    private Long desigId;

    @NotBlank(message = "Designation code is required")
    @Size(max = 50, message = "Designation code must be less than 50 characters")
    private String desigCode;

    @NotBlank(message = "Designation name is required")
    @Size(max = 100, message = "Designation name must be less than 100 characters")
    private String desigName;

    @NotNull(message = "Designation level is required")
    private Node.Type desigLevel;

    /**
     * 1 => Official, 2 => Participants
     * For levels other than KALARI, always 1
     */
    @NotNull(message = "Designation type is required")
    private Short desigType;

    public DesignationDTO(Long desigId, String desigName, Node.Type desigLevel) {
        this.desigId = desigId;
        this.desigName = desigName;
        this.desigLevel = desigLevel;
    }
}
