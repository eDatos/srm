-- AGENCY

INSERT INTO TB_M_ORGANISATIONS (TB_ORGANISATIONS) VALUES (GET_NEXT_SEQUENCE_VALUE('ITEMS') - 2);
INSERT INTO TB_M_ORGANISATIONS (TB_ORGANISATIONS) VALUES (GET_NEXT_SEQUENCE_VALUE('ITEMS') - 1);

UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 2 WHERE SEQUENCE_NAME = 'M_ORGANISATIONS';
