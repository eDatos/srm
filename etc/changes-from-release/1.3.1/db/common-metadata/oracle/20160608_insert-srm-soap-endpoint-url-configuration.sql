-- --------------------------------------------------------------------------------------------------------------
-- METAMAC-2452 - Los archivos urlrewrite de las aplicaciones externas no mantienen el contexto de la aplicación
-- --------------------------------------------------------------------------------------------------------------

-- Añadir propiedad con la URL base del servicio web SOAP del SRM

insert into TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE) values(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'),1,1,'metamac.srm.soap.external','http://FILL_ME_WITH_HOST_AND_PORT/FILL_ME_WITH_SRM_EXTERNAL_APP/apis/soap');
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';

commit;