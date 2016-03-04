-- --------------------------------------------------------------------------------------------------
-- METAMAC-2408 - Cambiar el acceso a la ayuda para que sea una URL
-- --------------------------------------------------------------------------------------------------

-- Añadir propiedad con la URL de la ayuda

insert into TB_DATA_CONFIGURATIONS (ID,VERSION,SYSTEM_PROPERTY,CONF_KEY,CONF_VALUE) values(GET_NEXT_SEQUENCE_VALUE('DATA_CONFIGURATIONS'),1,1,'metamac.srm.help.url','FILL_ME_WITH_HELP_URL');
UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = SEQUENCE_NEXT_VALUE + 1 WHERE SEQUENCE_NAME = 'DATA_CONFIGURATIONS';


-- Eliminar propiedad con el nombre del manual de usuario

delete from TB_DATA_CONFIGURATIONS
where CONF_KEY = 'metamac.srm.user_guide.file_name';


commit;