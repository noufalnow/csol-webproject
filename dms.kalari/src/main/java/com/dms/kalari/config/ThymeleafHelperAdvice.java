package com.dms.kalari.config;

import com.dms.kalari.util.XorMaskHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class ThymeleafHelperAdvice {

    @Autowired
    private XorMaskHelper xorMaskHelper;

    // This will be available in ALL Thymeleaf templates as 'xorMaskHelper'
    @ModelAttribute("xorMaskHelper")
    public XorMaskHelper getXorMaskHelper() {
        return xorMaskHelper;
    }
}
