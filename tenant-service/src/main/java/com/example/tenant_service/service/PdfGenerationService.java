package com.example.tenant_service.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;
import java.io.ByteArrayOutputStream;
import java.util.Map;

@Service
public class PdfGenerationService {

    private final TemplateEngine templateEngine;
    private final ResourceLoader resourceLoader;

    @Autowired
    public PdfGenerationService(TemplateEngine templateEngine, ResourceLoader resourceLoader) {
        this.templateEngine = templateEngine;
        this.resourceLoader = resourceLoader;
    }

    public byte[] generateMultiPageCertificate(Map<String, Object> data) throws Exception {
        Context context = new Context();
        context.setVariables(data);
        String baseUrl = ResourceUtils.getURL("classpath:static/").toString();
        
        // Process template with page breaks
        String htmlContent = templateEngine.process("fragments/events/certificate-multi", context);
        
        ITextRenderer renderer = new ITextRenderer();
        renderer.getSharedContext().setBaseURL(baseUrl);
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        renderer.setDocumentFromString(htmlContent, baseUrl);
        renderer.layout();
        renderer.createPDF(outputStream);
        renderer.finishPDF();

        return outputStream.toByteArray();
    }
}