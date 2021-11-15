-- --------------------------------------------------------------------------------------------------
-- EDATOS-3433 - Conector de kafka para la publicación de codelists en el SRM
-- --------------------------------------------------------------------------------------------------

-- Añade nueva columna a la tabla de versiones de category schemes que determina el estado de transmisión a través de Kafka

ALTER TABLE TB_M_CAT_SCHEMES_VERSIONS
ADD STREAM_MESSAGE_STATUS VARCHAR2(255 CHAR);

-- 'Update' statement without 'where' updates all table rows at once
UPDATE TB_M_CAT_SCHEMES_VERSIONS
SET STREAM_MESSAGE_STATUS = 'PENDING';

ALTER TABLE TB_M_CAT_SCHEMES_VERSIONS
MODIFY STREAM_MESSAGE_STATUS VARCHAR2(255 CHAR) NOT NULL;

commit;
