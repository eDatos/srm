INSERT INTO TB_MISC_VALUES
(ID, NAME, DATE_VALUE_TZ, DATE_VALUE, STRING_VALUE, 
 UUID, CREATED_DATE_TZ, CREATED_DATE, CREATED_BY, LAST_UPDATED_TZ, LAST_UPDATED, LAST_UPDATED_BY, VERSION)
VALUES
 (FILL_WITH_SCHEMA_NAME.GetSequenceNextValue('MISC_VALUES'), 'variable_element.geographical_information.last_updated_date', 'Europe/London', convert(datetime, GETDATE(), 120), null,
 FILL_WITH_SCHEMA_NAME.GetSequenceNextValue('MISC_VALUES'), 'Europe/London', convert(datetime, GETDATE(), 120), 'Metamac', 'Europe/London', convert(datetime, GETDATE(), 120), 'Metamac', 1);

UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'MISC_VALUES';

commit;