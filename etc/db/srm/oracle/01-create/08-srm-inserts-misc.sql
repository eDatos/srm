INSERT INTO TB_MISC_VALUES
(ID, NAME, DATE_VALUE_TZ, DATE_VALUE, STRING_VALUE, 
 UUID, CREATED_DATE_TZ, CREATED_DATE, CREATED_BY, LAST_UPDATED_TZ, LAST_UPDATED, LAST_UPDATED_BY, VERSION)
VALUES
 (GET_NEXT_SEQUENCE_VALUE('MISC_VALUES'), 'variable_element.geographical_information.last_updated_date', 'Europe/London', to_timestamp(sysdate,'DD/MM/RR HH24:MI:SS,FF'), null,
 GET_NEXT_SEQUENCE_VALUE('MISC_VALUES'), 'Europe/London', to_timestamp(sysdate,'DD/MM/RR HH24:MI:SS,FF'), 'Metamac', 'Europe/London', to_timestamp(sysdate,'DD/MM/RR HH24:MI:SS,FF'), 'Metamac', 1);

UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'MISC_VALUES';

commit;