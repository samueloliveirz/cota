package com.samueloliverz.Cota.service;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import com.samueloliverz.Cota.config.LojaProperties;
import com.samueloliverz.Cota.dto.OrcamentoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
@RequiredArgsConstructor
public class PdfService {

    private final TemplateEngine templateEngine;
    private final LojaProperties loja;

    public byte[] gerarOrcamento(OrcamentoResponse orcamento) {
        // 1. Coloca os dados que o template vai usar
        Context context = new Context();
        context.setVariable("orcamento", orcamento);
        context.setVariable("loja", loja);

        // 2. Thymeleaf preenche o HTML
        String html = templateEngine.process("orcamento-formalizado", context);

        // 3. OpenHTMLtoPDF transforma o HTML em PDF
        try (ByteArrayOutputStream saida = new ByteArrayOutputStream()) {
            PdfRendererBuilder builder = new PdfRendererBuilder();
            builder.useFastMode();
            builder.withHtmlContent(html, null);
            builder.toStream(saida);
            builder.run();
            return saida.toByteArray();
        } catch (IOException e) {
            throw new IllegalStateException("Erro ao gerar o PDF", e);
        }
    }
}