package com.dms.kalari.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.PastOrPresent;
import java.math.BigDecimal;
import java.time.LocalDate;

import com.dms.kalari.common.BaseDTO;

import lombok.Data;

@Data
public class PropertyPayOptionDTO extends BaseDTO {

    private Long poptId;
    private Long poptPropId;
    private Long poptDocId;

    @NotNull(message = "Payment type is required.")
    private Short poptType;

    //@PastOrPresent(message = "Payment date cannot be in the future.")
    @NotNull(message = "Payment date is required.")
    private LocalDate poptDate;

    @NotNull(message = "Payment amount is required.")
    @DecimalMin(value = "0.01", message = "Payment amount must be greater than zero.")
    private BigDecimal poptAmount;

    private Short poptBank;

    @Size(max = 20, message = "Cheque number cannot exceed 20 characters.")
    private String poptChqno;

    private Short poptStatus = 1;
    private LocalDate poptStatusDate;
}
