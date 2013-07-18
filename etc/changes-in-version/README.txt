Cuando se cree la RELEASE, añadir estos pasos al manual de instalación:

1. Parar Tomcat

2. Cambios en Base de datos

	ALTER TABLE TB_M_VARIABLE_ELEMENTS ADD COMMENT_FK NUMBER(19);
	ALTER TABLE TB_M_VARIABLE_ELEMENTS ADD CONSTRAINT FK_TB_M_VARIABLE_ELEMENTS_CO19 FOREIGN KEY (COMMENT_FK) REFERENCES TB_INTERNATIONAL_STRINGS (ID);
	ALTER TABLE TB_M_VARIABLES ADD VARIABLE_TYPE VARCHAR2(255 CHAR);
 	
	ALTER TABLE TB_M_VARIABLE_ELEMENTS ADD LATITUDE FLOAT(126);
	ALTER TABLE TB_M_VARIABLE_ELEMENTS ADD LONGITUDE FLOAT(126);
	ALTER TABLE TB_M_VARIABLE_ELEMENTS ADD SHAPE CLOB;
 	ALTER TABLE TB_M_VARIABLE_ELEMENTS ADD GEOGRAPHICAL_GRANULARITY_FK NUMBER(19);
 	ALTER TABLE TB_M_VARIABLE_ELEMENTS ADD CONSTRAINT FK_TB_M_VARIABLE_ELEMENTS_GE77 FOREIGN KEY (GEOGRAPHICAL_GRANULARITY_FK) REFERENCES TB_CODES (ID);

	ALTER TABLE TB_M_QUANTITIES DROP COLUMN BASE_LOCATION_FK;
	ALTER TABLE TB_M_QUANTITIES ADD BASE_LOCATION_FK NUMBER(19);
	ALTER TABLE TB_M_QUANTITIES ADD CONSTRAINT FK_TB_M_QUANTITIES_BASE_LOCA48 FOREIGN KEY (BASE_LOCATION_FK) REFERENCES TB_CODES (ID);

	CREATE TABLE TB_ENTITIES_TO_DELETE (
	     TABLE_NAME VARCHAR2(255 CHAR) NOT NULL,
		 ID_TO_DELETE NUMBER(19) NOT NULL
	);
	
	CREATE INDEX IX_TB_ENTITIES_TO_D_TABLE_NAME ON TB_ENTITIES_TO_DELETE (TABLE_NAME);
	
	ALTER TABLE TB_EXTERNAL_ITEMS ADD CODE_NESTED VARCHAR2(255 CHAR);
	
3. Cambios en DATA

	- Añadida propiedad: 'metamac.srm.codelist.variable_element.geographical_granularity.urn' (ver descripción en manual de instalación)
	- Añadida propiedad: 'metamac.srm.jobs.delete_deprecated_entities.cron_expression' (ver descripción en manual de instalación)
	- Renombrar propiedad: la propiedad 'metamac.srm.dsd.time_dimension.default_concept.urn' se ha renombrado a 'metamac.srm.dsd.time_dimension_or_attribute.default_concept.urn'
	- Renombrar propiedad: la propiedad 'metamac.srm.dsd.measure_dimension.default_concept.urn' se ha renombrado a 'metamac.srm.dsd.measure_dimension_or_attribute.default_concept.urn'
	- Renombrar propiedad driverName
	- Renombrar propiedad: metamac.srm.user.guide.file.name por metamac.srm.user_guide.file_name
	 	
99. Reiniciar Tomcat

