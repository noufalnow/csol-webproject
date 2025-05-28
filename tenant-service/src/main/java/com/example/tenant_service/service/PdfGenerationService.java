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

    public byte[] generatePdf(String templateName, Map<String, Object> data) throws Exception {
        Context context = new Context();
        context.setVariables(data);
        
        // Add base URL for resource resolution
        String baseUrl = ResourceUtils.getURL("classpath:static/").toString();
        
        // Process template
        String htmlContent = templateEngine.process(templateName, context);

        // Configure PDF renderer
        ITextRenderer renderer = new ITextRenderer();
        
        // For image resolution from classpath
        renderer.getSharedContext().setBaseURL(baseUrl);
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        renderer.setDocumentFromString(htmlContent, baseUrl);
        renderer.layout();
        renderer.createPDF(outputStream);
        renderer.finishPDF();

        return outputStream.toByteArray();
    }
}