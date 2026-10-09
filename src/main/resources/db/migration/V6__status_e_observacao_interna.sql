ALTER TABLE ordem_manutencao ADD COLUMN status VARCHAR(20) NOT NULL DEFAULT 'ENVIADO';
ALTER TABLE ordem_manutencao ALTER COLUMN status DROP DEFAULT;

ALTER TABLE ordem_manutencao ADD COLUMN observacao_interna VARCHAR(1000);
ALTER TABLE orcamento ADD COLUMN observacao_interna VARCHAR(1000);