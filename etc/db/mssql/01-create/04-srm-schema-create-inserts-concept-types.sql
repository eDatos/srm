INSERT INTO TB_M_LIS_CONCEPT_TYPES (ID, IDENTIFIER, DESCRIPTION_FK) values (FILL_WITH_SCHEMA_NAME.GetSequenceNextValue('M_LIS_CONCEPT_TYPES'), 'DIRECT', null);
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'M_CONCEPT_TYPES';

INSERT INTO TB_M_LIS_CONCEPT_TYPES (ID, IDENTIFIER, DESCRIPTION_FK) values (FILL_WITH_SCHEMA_NAME.GetSequenceNextValue('M_LIS_CONCEPT_TYPES'), 'DERIVED', null);
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'M_CONCEPT_TYPES';