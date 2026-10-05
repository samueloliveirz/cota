package com.samueloliverz.Cota.service;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import com.samueloliverz.Cota.config.LojaProperties;
import com.samueloliverz.Cota.dto.OrcamentoResponse;
import com.samueloliverz.Cota.dto.OrdemManutencaoResponse;
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

        // 2. Escolhe o template pelo tipo (ex: pdf/orcamento-normal, pdf/orcamento-vale)
        String template = "pdf/orcamento-" + orcamento.tipo().name().toLowerCase();

        // 3. Gera o PDF
        return renderizar(template, context);
    }

    public byte[] gerarManutencao(OrdemManutencaoResponse ordem) {
        // 1. Coloca os dados que o template vai usar
        Context context = new Context();
        context.setVariable("ordem", ordem);
        context.setVariable("loja", loja);

        // 2. A manutenção só tem um template, então o nome é fixo
        // 3. Gera o PDF
        return renderizar("pdf/ordem-manutencao", context);
    }
    // Parte comum dos dois PDFs: HTML -> PDF
    private byte[] renderizar(String template, Context context) {
        // Thymeleaf preenche o HTML com os dados
        String html = templateEngine.process(template, context);

        // OpenHTMLtoPDF transforma o HTML em PDF
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