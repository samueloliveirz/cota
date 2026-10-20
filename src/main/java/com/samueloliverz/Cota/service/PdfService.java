package com.samueloliverz.Cota.service;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import com.samueloliverz.Cota.config.LojaProperties;
import com.samueloliverz.Cota.dto.OrcamentoResponse;
import com.samueloliverz.Cota.dto.OrdemManutencaoResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class PdfService {

    private final TemplateEngine templateEngine;
    private final LojaProperties loja;

    private String logo;

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
        context.setVariable("logo", logo());

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

    // O PDF é gerado sem URL base, então a imagem vai embutida no HTML (data URI).
    // Lê o arquivo uma vez só e guarda; sem logo configurada, o cabeçalho mostra o nome da loja.
    private String logo() {
        if (logo != null || loja.logo() == null || loja.logo().isBlank()) {
            return logo;
        }
        ClassPathResource arquivo = new ClassPathResource(loja.logo());
        if (!arquivo.exists()) {
            return null;
        }
        String tipo = loja.logo().toLowerCase().endsWith(".png") ? "image/png" : "image/jpeg";
        try (InputStream entrada = arquivo.getInputStream()) {
            logo = "data:" + tipo + ";base64," + Base64.getEncoder().encodeToString(entrada.readAllBytes());
            return logo;
        } catch (IOException e) {
            throw new IllegalStateException("Erro ao ler a logo da loja", e);
        }
    }
}
