-- --------------------------------------------------------------------------------------------------
-- EDATOS-3433 - Conector de kafka para la publicación de codelists en el SRM
-- --------------------------------------------------------------------------------------------------

-- Añade nueva columna a la tabla de versiones de data structures que determina el estado de transmisión a través de Kafka

ALTER TABLE TB_M_DATASTRUCTURE_VERSIONS
ADD COLUMN STREAM_MESSAGE_STATUS VARCHAR(255);

-- 'Update' statement without 'where' updates all table rows at once
UPDATE TB_M_DATASTRUCTURE_VERSIONS
SET STREAM_MESSAGE_STATUS = 'PENDING';

ALTER TABLE TB_M_DATASTRUCTURE_VERSIONS
ALTER COLUMN STREAM_MESSAGE_STATUS SET NOT NULL;

commit;
