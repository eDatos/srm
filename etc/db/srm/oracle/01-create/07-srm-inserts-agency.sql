-- AGENCY

INSERT INTO TB_M_ORGANISATIONS (TB_ORGANISATIONS, SPECIAL_ORG_HAS_BEEN_PUBLISHED) VALUES (GET_NEXT_SEQUENCE_VALUE('ORGANISATIONS') - 2, 1);
INSERT INTO TB_M_ORGANISATIONS (TB_ORGANISATIONS, SPECIAL_ORG_HAS_BEEN_PUBLISHED) VALUES (GET_NEXT_SEQUENCE_VALUE('ORGANISATIONS') - 1, 1);

UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 2 WHERE SEQUENCE_NAME = 'M_ORGANISATIONS';


commit;