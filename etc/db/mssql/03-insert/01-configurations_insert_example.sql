-- ###########################################
-- # insert
-- ###########################################

insert into TB_CONFIGURATIONS (CONF_KEY,CONF_VALUE) values ('metamac.srm.variables.variable_world.urn','urn:siemac:org.siemac.metamac.infomodel.structuralresources.Variable=TERRITORIO_MUNDO');
insert into TB_CONFIGURATIONS (CONF_KEY,CONF_VALUE) values ('metamac.srm.variables.variable_element_world.urn','urn:siemac:org.siemac.metamac.infomodel.structuralresources.VariableElement=TERRITORIO_MUNDO.MUNDO');
insert into TB_CONFIGURATIONS (CONF_KEY,CONF_VALUE) values ('metamac.srm.jobs.delete_deprecated_entities.cron_expression','0 30 2 ? * *');
insert into TB_CONFIGURATIONS (CONF_KEY,CONF_VALUE) values ('metamac.srm.dsd.time_dimension_or_attribute.default_concept.urn',null);
insert into TB_CONFIGURATIONS (CONF_KEY,CONF_VALUE) values ('metamac.srm.dsd.primary_measure.default_concept.urn',null);
insert into TB_CONFIGURATIONS (CONF_KEY,CONF_VALUE) values ('metamac.srm.dsd.measure_dimension_or_attribute.default_concept.urn',null);

-- DATASOURCE: ORACLE
insert into TB_CONFIGURATIONS (CONF_KEY,CONF_VALUE) values ('metamac.srm.db.url','jdbc:sqlserver://FILL_ME_WITH_HOST:FILL_ME_WITH_PORT:XE;databaseName=FILL_ME_WITH_DATABASE_NAME');
insert into TB_CONFIGURATIONS (CONF_KEY,CONF_VALUE) values ('metamac.srm.db.username','FILL_ME_WITH_USERNAME');
insert into TB_CONFIGURATIONS (CONF_KEY,CONF_VALUE) values ('metamac.srm.db.password','FILL_ME_WITH_PASSWORD');
insert into TB_CONFIGURATIONS (CONF_KEY,CONF_VALUE) values ('metamac.srm.db.driver_name','com.microsoft.sqlserver.jdbc.SQLServerDriver');
insert into TB_CONFIGURATIONS (CONF_KEY,CONF_VALUE) values ('metamac.srm.db.dialect','org.hibernate.dialect.SQLServerDialect');

insert into TB_CONFIGURATIONS (CONF_KEY,CONF_VALUE) values ('metamac.srm.user_guide.file_name','Gestor_accesos-Manual_usuario.pdf');

