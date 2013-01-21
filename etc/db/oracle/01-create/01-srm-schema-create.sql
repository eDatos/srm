-- ###########################################
-- # Create
-- ###########################################
-- Create pk sequence
    


-- Create normal entities
    
CREATE TABLE TB_M_CATEGORIES (
  TB_CATEGORIES NUMBER(19) NOT NULL

);


CREATE TABLE TB_M_CAT_SCHEMES_VERSIONS (
  PRODUCTION_VALIDATION_DATE_TZ VARCHAR2(50),
  PRODUCTION_VALIDATION_DATE TIMESTAMP,
  PRODUCTION_VALIDATION_USER VARCHAR2(255),
  DIFFUSION_VALIDATION_DATE_TZ VARCHAR2(50),
  DIFFUSION_VALIDATION_DATE TIMESTAMP,
  DIFFUSION_VALIDATION_USER VARCHAR2(255),
  INTERNAL_PUBLICATION_DATE_TZ VARCHAR2(50),
  INTERNAL_PUBLICATION_DATE TIMESTAMP,
  INTERNAL_PUBLICATION_USER VARCHAR2(255),
  EXTERNAL_PUBLICATION_DATE_TZ VARCHAR2(50),
  EXTERNAL_PUBLICATION_DATE TIMESTAMP,
  EXTERNAL_PUBLICATION_USER VARCHAR2(255),
  IS_EXT_PUBLICATION_FAILED NUMBER(1,0),
  EXT_PUBLICATION_FAILED_DATE_TZ VARCHAR2(50),
  EXT_PUBLICATION_FAILED_DATE TIMESTAMP,
  PROC_STATUS VARCHAR2(255) NOT NULL,
  TB_CAT_SCHEMES_VERSIONS NUMBER(19) NOT NULL

);


CREATE TABLE TB_M_VARIABLES (
  ID NUMBER(19) NOT NULL,
  VALID_FROM_TZ VARCHAR2(50),
  VALID_FROM TIMESTAMP,
  VALID_TO_TZ VARCHAR2(50),
  VALID_TO TIMESTAMP,
  UPDATE_DATE_TZ VARCHAR2(50),
  UPDATE_DATE TIMESTAMP,
  UUID VARCHAR2(36) NOT NULL,
  CREATED_DATE_TZ VARCHAR2(50),
  CREATED_DATE TIMESTAMP,
  CREATED_BY VARCHAR2(50),
  LAST_UPDATED_TZ VARCHAR2(50),
  LAST_UPDATED TIMESTAMP,
  LAST_UPDATED_BY VARCHAR2(50),
  VERSION NUMBER(19) NOT NULL,
  NAMEABLE_ARTEFACT_FK NUMBER(19) NOT NULL,
  SHORT_NAME_FK NUMBER(19) NOT NULL,
  REPLACED_BY_VARIABLE_FK NUMBER(19)
);


CREATE TABLE TB_M_VARIABLE_ELEMENTS (
  ID NUMBER(19) NOT NULL,
  VALID_FROM_TZ VARCHAR2(50),
  VALID_FROM TIMESTAMP,
  VALID_TO_TZ VARCHAR2(50),
  VALID_TO TIMESTAMP,
  UPDATE_DATE_TZ VARCHAR2(50),
  UPDATE_DATE TIMESTAMP,
  UUID VARCHAR2(36) NOT NULL,
  CREATED_DATE_TZ VARCHAR2(50),
  CREATED_DATE TIMESTAMP,
  CREATED_BY VARCHAR2(50),
  LAST_UPDATED_TZ VARCHAR2(50),
  LAST_UPDATED TIMESTAMP,
  LAST_UPDATED_BY VARCHAR2(50),
  VERSION NUMBER(19) NOT NULL,
  NAMEABLE_ARTEFACT_FK NUMBER(19) NOT NULL,
  SHORT_NAME_FK NUMBER(19) NOT NULL,
  VARIABLE_FK NUMBER(19) NOT NULL
);


CREATE TABLE TB_M_CODES (
  SHORT_NAME_FK NUMBER(19),
  VARIABLE_ELEMENT_FK NUMBER(19),
  TB_CODES NUMBER(19) NOT NULL

);


CREATE TABLE TB_M_CODELIST_FAMILIES (
  ID NUMBER(19) NOT NULL,
  UPDATE_DATE_TZ VARCHAR2(50),
  UPDATE_DATE TIMESTAMP,
  UUID VARCHAR2(36) NOT NULL,
  CREATED_DATE_TZ VARCHAR2(50),
  CREATED_DATE TIMESTAMP,
  CREATED_BY VARCHAR2(50),
  LAST_UPDATED_TZ VARCHAR2(50),
  LAST_UPDATED TIMESTAMP,
  LAST_UPDATED_BY VARCHAR2(50),
  VERSION NUMBER(19) NOT NULL,
  NAMEABLE_ARTEFACT_FK NUMBER(19) NOT NULL
);


CREATE TABLE TB_M_CODELISTS_VERSIONS (
  IS_RECOMMENDED NUMBER(1,0),
  SHORT_NAME_FK NUMBER(19),
  CODELIST_FAMILY_FK NUMBER(19),
  VARIABLE_FK NUMBER(19),
  REPLACED_BY_CODELIST_FK NUMBER(19),
  DEFAULT_ORDER_VISUALISATION_FK NUMBER(19),
  PRODUCTION_VALIDATION_DATE_TZ VARCHAR2(50),
  PRODUCTION_VALIDATION_DATE TIMESTAMP,
  PRODUCTION_VALIDATION_USER VARCHAR2(255),
  DIFFUSION_VALIDATION_DATE_TZ VARCHAR2(50),
  DIFFUSION_VALIDATION_DATE TIMESTAMP,
  DIFFUSION_VALIDATION_USER VARCHAR2(255),
  INTERNAL_PUBLICATION_DATE_TZ VARCHAR2(50),
  INTERNAL_PUBLICATION_DATE TIMESTAMP,
  INTERNAL_PUBLICATION_USER VARCHAR2(255),
  EXTERNAL_PUBLICATION_DATE_TZ VARCHAR2(50),
  EXTERNAL_PUBLICATION_DATE TIMESTAMP,
  EXTERNAL_PUBLICATION_USER VARCHAR2(255),
  IS_EXT_PUBLICATION_FAILED NUMBER(1,0),
  EXT_PUBLICATION_FAILED_DATE_TZ VARCHAR2(50),
  EXT_PUBLICATION_FAILED_DATE TIMESTAMP,
  PROC_STATUS VARCHAR2(255) NOT NULL,
  ACCESS_TYPE VARCHAR2(255),
  TB_CODELISTS_VERSIONS NUMBER(19) NOT NULL

);


CREATE TABLE TB_M_CODELIST_ORDER_VISUAL (
  ID NUMBER(19) NOT NULL,
  UPDATE_DATE_TZ VARCHAR2(50),
  UPDATE_DATE TIMESTAMP,
  UUID VARCHAR2(36) NOT NULL,
  CREATED_DATE_TZ VARCHAR2(50),
  CREATED_DATE TIMESTAMP,
  CREATED_BY VARCHAR2(50),
  LAST_UPDATED_TZ VARCHAR2(50),
  LAST_UPDATED TIMESTAMP,
  LAST_UPDATED_BY VARCHAR2(50),
  VERSION NUMBER(19) NOT NULL,
  NAMEABLE_ARTEFACT_FK NUMBER(19) NOT NULL,
  CODELIST_FK NUMBER(19) NOT NULL
);


CREATE TABLE TB_M_CODE_ORDER_VISUAL (
  ID NUMBER(19) NOT NULL,
  CODE_INDEX NUMBER(19) NOT NULL,
  UUID VARCHAR2(36) NOT NULL,
  CREATED_DATE_TZ VARCHAR2(50),
  CREATED_DATE TIMESTAMP,
  CREATED_BY VARCHAR2(50),
  LAST_UPDATED_TZ VARCHAR2(50),
  LAST_UPDATED TIMESTAMP,
  LAST_UPDATED_BY VARCHAR2(50),
  VERSION NUMBER(19) NOT NULL,
  CODE_FK NUMBER(19) NOT NULL,
  CODELIST_VISUALISATION_FK NUMBER(19) NOT NULL
);


CREATE TABLE TB_M_VARIABLE_FAMILIES (
  ID NUMBER(19) NOT NULL,
  UPDATE_DATE_TZ VARCHAR2(50),
  UPDATE_DATE TIMESTAMP,
  UUID VARCHAR2(36) NOT NULL,
  CREATED_DATE_TZ VARCHAR2(50),
  CREATED_DATE TIMESTAMP,
  CREATED_BY VARCHAR2(50),
  LAST_UPDATED_TZ VARCHAR2(50),
  LAST_UPDATED TIMESTAMP,
  LAST_UPDATED_BY VARCHAR2(50),
  VERSION NUMBER(19) NOT NULL,
  NAMEABLE_ARTEFACT_FK NUMBER(19) NOT NULL
);


CREATE TABLE TB_M_LIS_CONCEPT_TYPES (
  ID NUMBER(19) NOT NULL,
  IDENTIFIER VARCHAR2(255) NOT NULL,
  DESCRIPTION_FK NUMBER(19)
);


CREATE TABLE TB_M_CONCEPTS (
  PLURAL_NAME_FK NUMBER(19),
  ACRONYM_FK NUMBER(19),
  DESCRIPTION_SOURCE_FK NUMBER(19),
  CONTEXT_FK NUMBER(19),
  DOC_METHOD_FK NUMBER(19),
  CONCEPT_TYPE_FK NUMBER(19),
  DERIVATION_FK NUMBER(19),
  LEGAL_ACTS_FK NUMBER(19),
  EXTENDS_FK NUMBER(19),
  VARIABLE_FK NUMBER(19),
  SDMX_RELATED_ARTEFACT VARCHAR2(255),
  TB_CONCEPTS NUMBER(19) NOT NULL

);


CREATE TABLE TB_M_CONCEPT_SCHEMES_VERSIONS (
  RELATED_OPERATION_FK NUMBER(19),
  PRODUCTION_VALIDATION_DATE_TZ VARCHAR2(50),
  PRODUCTION_VALIDATION_DATE TIMESTAMP,
  PRODUCTION_VALIDATION_USER VARCHAR2(255),
  DIFFUSION_VALIDATION_DATE_TZ VARCHAR2(50),
  DIFFUSION_VALIDATION_DATE TIMESTAMP,
  DIFFUSION_VALIDATION_USER VARCHAR2(255),
  INTERNAL_PUBLICATION_DATE_TZ VARCHAR2(50),
  INTERNAL_PUBLICATION_DATE TIMESTAMP,
  INTERNAL_PUBLICATION_USER VARCHAR2(255),
  EXTERNAL_PUBLICATION_DATE_TZ VARCHAR2(50),
  EXTERNAL_PUBLICATION_DATE TIMESTAMP,
  EXTERNAL_PUBLICATION_USER VARCHAR2(255),
  IS_EXT_PUBLICATION_FAILED NUMBER(1,0),
  EXT_PUBLICATION_FAILED_DATE_TZ VARCHAR2(50),
  EXT_PUBLICATION_FAILED_DATE TIMESTAMP,
  PROC_STATUS VARCHAR2(255) NOT NULL,
  CONCEPT_SCHEME_TYPE VARCHAR2(255) NOT NULL,
  TB_CONCEPT_SCHEMES_VERSIONS NUMBER(19) NOT NULL

);


CREATE TABLE TB_M_DATASTRUCTURE_VERSIONS (
  AUTO_OPEN NUMBER(1,0),
  SHOW_DECIMALS NUMBER(10),
  PRODUCTION_VALIDATION_DATE_TZ VARCHAR2(50),
  PRODUCTION_VALIDATION_DATE TIMESTAMP,
  PRODUCTION_VALIDATION_USER VARCHAR2(255),
  DIFFUSION_VALIDATION_DATE_TZ VARCHAR2(50),
  DIFFUSION_VALIDATION_DATE TIMESTAMP,
  DIFFUSION_VALIDATION_USER VARCHAR2(255),
  INTERNAL_PUBLICATION_DATE_TZ VARCHAR2(50),
  INTERNAL_PUBLICATION_DATE TIMESTAMP,
  INTERNAL_PUBLICATION_USER VARCHAR2(255),
  EXTERNAL_PUBLICATION_DATE_TZ VARCHAR2(50),
  EXTERNAL_PUBLICATION_DATE TIMESTAMP,
  EXTERNAL_PUBLICATION_USER VARCHAR2(255),
  IS_EXT_PUBLICATION_FAILED NUMBER(1,0),
  EXT_PUBLICATION_FAILED_DATE_TZ VARCHAR2(50),
  EXT_PUBLICATION_FAILED_DATE TIMESTAMP,
  PROC_STATUS VARCHAR2(255) NOT NULL,
  TB_DATASTRUCTURE_VERSIONS NUMBER(19) NOT NULL

);


CREATE TABLE TB_M_DIMENSION_ORDERS (
  ID NUMBER(19) NOT NULL,
  DIM_ORDER NUMBER(10) NOT NULL,
  UUID VARCHAR2(36) NOT NULL,
  VERSION NUMBER(19) NOT NULL,
  DIM_COMPONENT_FK NUMBER(19) NOT NULL,
  DSD_VERSION_HEADING_FK NUMBER(19),
  DSD_VERSION_STUB_FK NUMBER(19)
);


CREATE TABLE TB_M_MEASURE_DIM_PRECISIONS (
  ID NUMBER(19) NOT NULL,
  SHOW_DECIMAL_PRECISION NUMBER(10) NOT NULL,
  UUID VARCHAR2(36) NOT NULL,
  VERSION NUMBER(19) NOT NULL,
  M_CODE_FK NUMBER(19) NOT NULL,
  DSD_VERSION_FK NUMBER(19) NOT NULL
);


CREATE TABLE TB_M_ORGANISATIONS (
  TB_ORGANISATIONS NUMBER(19) NOT NULL

);


CREATE TABLE TB_M_ORG_SCHEMES_VERSIONS (
  PRODUCTION_VALIDATION_DATE_TZ VARCHAR2(50),
  PRODUCTION_VALIDATION_DATE TIMESTAMP,
  PRODUCTION_VALIDATION_USER VARCHAR2(255),
  DIFFUSION_VALIDATION_DATE_TZ VARCHAR2(50),
  DIFFUSION_VALIDATION_DATE TIMESTAMP,
  DIFFUSION_VALIDATION_USER VARCHAR2(255),
  INTERNAL_PUBLICATION_DATE_TZ VARCHAR2(50),
  INTERNAL_PUBLICATION_DATE TIMESTAMP,
  INTERNAL_PUBLICATION_USER VARCHAR2(255),
  EXTERNAL_PUBLICATION_DATE_TZ VARCHAR2(50),
  EXTERNAL_PUBLICATION_DATE TIMESTAMP,
  EXTERNAL_PUBLICATION_USER VARCHAR2(255),
  IS_EXT_PUBLICATION_FAILED NUMBER(1,0),
  EXT_PUBLICATION_FAILED_DATE_TZ VARCHAR2(50),
  EXT_PUBLICATION_FAILED_DATE TIMESTAMP,
  PROC_STATUS VARCHAR2(255) NOT NULL,
  TB_ORG_SCHEMES_VERSIONS NUMBER(19) NOT NULL

);



-- Create many to many relations
    
CREATE TABLE TB_M_CONCEPT_RELATED (
  CONCEPT_RELATED_FK NUMBER(19) NOT NULL,
  CONCEPT_FK NUMBER(19) NOT NULL
);


CREATE TABLE TB_M_CONCEPT_ROLES (
  CONCEPT_ROLE_FK NUMBER(19) NOT NULL,
  CONCEPT_FK NUMBER(19) NOT NULL
);


CREATE TABLE TB_M_VAR_FAMILIES_VARIABLES (
  VARIABLE_FAMILY_FK NUMBER(19) NOT NULL,
  VARIABLE_FK NUMBER(19) NOT NULL
);



-- Primary keys
    
ALTER TABLE TB_M_VARIABLES ADD CONSTRAINT PK_TB_M_VARIABLES
	PRIMARY KEY (ID)
;

ALTER TABLE TB_M_VARIABLE_ELEMENTS ADD CONSTRAINT PK_TB_M_VARIABLE_ELEMENTS
	PRIMARY KEY (ID)
;

ALTER TABLE TB_M_CODELIST_FAMILIES ADD CONSTRAINT PK_TB_M_CODELIST_FAMILIES
	PRIMARY KEY (ID)
;

ALTER TABLE TB_M_CODELIST_ORDER_VISUAL ADD CONSTRAINT PK_TB_M_CODELIST_ORDER_VISUAL
	PRIMARY KEY (ID)
;

ALTER TABLE TB_M_CODE_ORDER_VISUAL ADD CONSTRAINT PK_TB_M_CODE_ORDER_VISUAL
	PRIMARY KEY (ID)
;

ALTER TABLE TB_M_VARIABLE_FAMILIES ADD CONSTRAINT PK_TB_M_VARIABLE_FAMILIES
	PRIMARY KEY (ID)
;

ALTER TABLE TB_M_LIS_CONCEPT_TYPES ADD CONSTRAINT PK_TB_M_LIS_CONCEPT_TYPES
	PRIMARY KEY (ID)
;

ALTER TABLE TB_M_DIMENSION_ORDERS ADD CONSTRAINT PK_TB_M_DIMENSION_ORDERS
	PRIMARY KEY (ID)
;

ALTER TABLE TB_M_MEASURE_DIM_PRECISIONS ADD CONSTRAINT PK_TB_M_MEASURE_DIM_PRECISIONS
	PRIMARY KEY (ID)
;

    
ALTER TABLE TB_M_CONCEPT_RELATED ADD CONSTRAINT PK_TB_M_CONCEPT_RELATED
	PRIMARY KEY (CONCEPT_RELATED_FK, CONCEPT_FK)
;

ALTER TABLE TB_M_CONCEPT_ROLES ADD CONSTRAINT PK_TB_M_CONCEPT_ROLES
	PRIMARY KEY (CONCEPT_ROLE_FK, CONCEPT_FK)
;

ALTER TABLE TB_M_VAR_FAMILIES_VARIABLES ADD CONSTRAINT PK_TB_M_VAR_FAMILIES_VARIABLES
	PRIMARY KEY (VARIABLE_FAMILY_FK, VARIABLE_FK)
;


-- Unique constraints
    





ALTER TABLE TB_M_VARIABLES
    ADD CONSTRAINT UQ_TB_M_VARIABLES UNIQUE (UUID)
;



ALTER TABLE TB_M_VARIABLE_ELEMENTS
    ADD CONSTRAINT UQ_TB_M_VARIABLE_ELEMENTS UNIQUE (UUID)
;





ALTER TABLE TB_M_CODELIST_FAMILIES
    ADD CONSTRAINT UQ_TB_M_CODELIST_FAMILIES UNIQUE (UUID)
;





ALTER TABLE TB_M_CODELIST_ORDER_VISUAL
    ADD CONSTRAINT UQ_TB_M_CODELIST_ORDER_VISUAL UNIQUE (UUID)
;



ALTER TABLE TB_M_CODE_ORDER_VISUAL
    ADD CONSTRAINT UQ_TB_M_CODE_ORDER_VISUAL UNIQUE (UUID)
;



ALTER TABLE TB_M_VARIABLE_FAMILIES
    ADD CONSTRAINT UQ_TB_M_VARIABLE_FAMILIES UNIQUE (UUID)
;










ALTER TABLE TB_M_DIMENSION_ORDERS
    ADD CONSTRAINT UQ_TB_M_DIMENSION_ORDERS UNIQUE (UUID)
;



ALTER TABLE TB_M_MEASURE_DIM_PRECISIONS
    ADD CONSTRAINT UQ_TB_M_MEASURE_DIM_PRECISIONS UNIQUE (UUID)
;







-- Foreign key constraints
ALTER TABLE TB_M_CATEGORIES ADD CONSTRAINT FK_TB_M_CATEGORIES_TB_CATEGO01
	FOREIGN KEY (TB_CATEGORIES) REFERENCES TB_ITEMS (ID)
;
ALTER TABLE TB_M_CAT_SCHEMES_VERSIONS ADD CONSTRAINT FK_TB_M_CAT_SCHEMES_VERSIONS03
	FOREIGN KEY (TB_CAT_SCHEMES_VERSIONS) REFERENCES TB_ITEM_SCHEMES_VERSIONS (ID)
;
ALTER TABLE TB_M_CODES ADD CONSTRAINT FK_TB_M_CODES_TB_CODES
	FOREIGN KEY (TB_CODES) REFERENCES TB_ITEMS (ID)
;
ALTER TABLE TB_M_CODELISTS_VERSIONS ADD CONSTRAINT FK_TB_M_CODELISTS_VERSIONS_T45
	FOREIGN KEY (TB_CODELISTS_VERSIONS) REFERENCES TB_ITEM_SCHEMES_VERSIONS (ID)
;
ALTER TABLE TB_M_CONCEPTS ADD CONSTRAINT FK_TB_M_CONCEPTS_TB_CONCEPTS
	FOREIGN KEY (TB_CONCEPTS) REFERENCES TB_ITEMS (ID)
;
ALTER TABLE TB_M_CONCEPT_SCHEMES_VERSIONS ADD CONSTRAINT FK_TB_M_CONCEPT_SCHEMES_VERS79
	FOREIGN KEY (TB_CONCEPT_SCHEMES_VERSIONS) REFERENCES TB_ITEM_SCHEMES_VERSIONS (ID)
;
ALTER TABLE TB_M_DATASTRUCTURE_VERSIONS ADD CONSTRAINT FK_TB_M_DATASTRUCTURE_VERSIO51
	FOREIGN KEY (TB_DATASTRUCTURE_VERSIONS) REFERENCES TB_STRUCTURES_VERSIONS (ID)
;
ALTER TABLE TB_M_ORGANISATIONS ADD CONSTRAINT FK_TB_M_ORGANISATIONS_TB_ORG83
	FOREIGN KEY (TB_ORGANISATIONS) REFERENCES TB_ITEMS (ID)
;
ALTER TABLE TB_M_ORG_SCHEMES_VERSIONS ADD CONSTRAINT FK_TB_M_ORG_SCHEMES_VERSIONS65
	FOREIGN KEY (TB_ORG_SCHEMES_VERSIONS) REFERENCES TB_ITEM_SCHEMES_VERSIONS (ID)
;


  
  
  
  
ALTER TABLE TB_M_VARIABLES ADD CONSTRAINT FK_TB_M_VARIABLES_NAMEABLE_A57
	FOREIGN KEY (NAMEABLE_ARTEFACT_FK) REFERENCES TB_ANNOTABLE_ARTEFACTS (ID)
;
ALTER TABLE TB_M_VARIABLES ADD CONSTRAINT FK_TB_M_VARIABLES_SHORT_NAME89
	FOREIGN KEY (SHORT_NAME_FK) REFERENCES TB_INTERNATIONAL_STRINGS (ID)
;
ALTER TABLE TB_M_VARIABLES ADD CONSTRAINT FK_TB_M_VARIABLES_REPLACED_B76
	FOREIGN KEY (REPLACED_BY_VARIABLE_FK) REFERENCES TB_M_VARIABLES (ID)
;

  
ALTER TABLE TB_M_VARIABLE_ELEMENTS ADD CONSTRAINT FK_TB_M_VARIABLE_ELEMENTS_NA92
	FOREIGN KEY (NAMEABLE_ARTEFACT_FK) REFERENCES TB_ANNOTABLE_ARTEFACTS (ID)
;
ALTER TABLE TB_M_VARIABLE_ELEMENTS ADD CONSTRAINT FK_TB_M_VARIABLE_ELEMENTS_SH38
	FOREIGN KEY (SHORT_NAME_FK) REFERENCES TB_INTERNATIONAL_STRINGS (ID)
;
ALTER TABLE TB_M_VARIABLE_ELEMENTS ADD CONSTRAINT FK_TB_M_VARIABLE_ELEMENTS_VA80
	FOREIGN KEY (VARIABLE_FK) REFERENCES TB_M_VARIABLES (ID)
;

  
ALTER TABLE TB_M_CODES ADD CONSTRAINT FK_TB_M_CODES_SHORT_NAME_FK
	FOREIGN KEY (SHORT_NAME_FK) REFERENCES TB_INTERNATIONAL_STRINGS (ID)
;
ALTER TABLE TB_M_CODES ADD CONSTRAINT FK_TB_M_CODES_VARIABLE_ELEME75
	FOREIGN KEY (VARIABLE_ELEMENT_FK) REFERENCES TB_M_VARIABLE_ELEMENTS (ID)
;

  
ALTER TABLE TB_M_CODELIST_FAMILIES ADD CONSTRAINT FK_TB_M_CODELIST_FAMILIES_NA36
	FOREIGN KEY (NAMEABLE_ARTEFACT_FK) REFERENCES TB_ANNOTABLE_ARTEFACTS (ID)
;

  
ALTER TABLE TB_M_CODELISTS_VERSIONS ADD CONSTRAINT FK_TB_M_CODELISTS_VERSIONS_S48
	FOREIGN KEY (SHORT_NAME_FK) REFERENCES TB_INTERNATIONAL_STRINGS (ID)
;
ALTER TABLE TB_M_CODELISTS_VERSIONS ADD CONSTRAINT FK_TB_M_CODELISTS_VERSIONS_C42
	FOREIGN KEY (CODELIST_FAMILY_FK) REFERENCES TB_M_CODELIST_FAMILIES (ID)
;
ALTER TABLE TB_M_CODELISTS_VERSIONS ADD CONSTRAINT FK_TB_M_CODELISTS_VERSIONS_V14
	FOREIGN KEY (VARIABLE_FK) REFERENCES TB_M_VARIABLES (ID)
;
ALTER TABLE TB_M_CODELISTS_VERSIONS ADD CONSTRAINT FK_TB_M_CODELISTS_VERSIONS_R30
	FOREIGN KEY (REPLACED_BY_CODELIST_FK) REFERENCES TB_ITEM_SCHEMES_VERSIONS (ID)
;
ALTER TABLE TB_M_CODELISTS_VERSIONS ADD CONSTRAINT FK_TB_M_CODELISTS_VERSIONS_D30
	FOREIGN KEY (DEFAULT_ORDER_VISUALISATION_FK) REFERENCES TB_M_CODELIST_ORDER_VISUAL (ID)
;

  
ALTER TABLE TB_M_CODELIST_ORDER_VISUAL ADD CONSTRAINT FK_TB_M_CODELIST_ORDER_VISUA37
	FOREIGN KEY (NAMEABLE_ARTEFACT_FK) REFERENCES TB_ANNOTABLE_ARTEFACTS (ID)
;
ALTER TABLE TB_M_CODELIST_ORDER_VISUAL ADD CONSTRAINT FK_TB_M_CODELIST_ORDER_VISUA32
	FOREIGN KEY (CODELIST_FK) REFERENCES TB_ITEM_SCHEMES_VERSIONS (ID) ON DELETE CASCADE
;

  
ALTER TABLE TB_M_CODE_ORDER_VISUAL ADD CONSTRAINT FK_TB_M_CODE_ORDER_VISUAL_CO20
	FOREIGN KEY (CODE_FK) REFERENCES TB_ITEMS (ID) ON DELETE CASCADE
;
ALTER TABLE TB_M_CODE_ORDER_VISUAL ADD CONSTRAINT FK_TB_M_CODE_ORDER_VISUAL_CO42
	FOREIGN KEY (CODELIST_VISUALISATION_FK) REFERENCES TB_M_CODELIST_ORDER_VISUAL (ID) ON DELETE CASCADE
;

  
ALTER TABLE TB_M_VARIABLE_FAMILIES ADD CONSTRAINT FK_TB_M_VARIABLE_FAMILIES_NA21
	FOREIGN KEY (NAMEABLE_ARTEFACT_FK) REFERENCES TB_ANNOTABLE_ARTEFACTS (ID)
;

  
ALTER TABLE TB_M_LIS_CONCEPT_TYPES ADD CONSTRAINT FK_TB_M_LIS_CONCEPT_TYPES_DE09
	FOREIGN KEY (DESCRIPTION_FK) REFERENCES TB_INTERNATIONAL_STRINGS (ID)
;

  
ALTER TABLE TB_M_CONCEPTS ADD CONSTRAINT FK_TB_M_CONCEPTS_PLURAL_NAME33
	FOREIGN KEY (PLURAL_NAME_FK) REFERENCES TB_INTERNATIONAL_STRINGS (ID)
;
ALTER TABLE TB_M_CONCEPTS ADD CONSTRAINT FK_TB_M_CONCEPTS_ACRONYM_FK
	FOREIGN KEY (ACRONYM_FK) REFERENCES TB_INTERNATIONAL_STRINGS (ID)
;
ALTER TABLE TB_M_CONCEPTS ADD CONSTRAINT FK_TB_M_CONCEPTS_DESCRIPTION07
	FOREIGN KEY (DESCRIPTION_SOURCE_FK) REFERENCES TB_INTERNATIONAL_STRINGS (ID)
;
ALTER TABLE TB_M_CONCEPTS ADD CONSTRAINT FK_TB_M_CONCEPTS_CONTEXT_FK
	FOREIGN KEY (CONTEXT_FK) REFERENCES TB_INTERNATIONAL_STRINGS (ID)
;
ALTER TABLE TB_M_CONCEPTS ADD CONSTRAINT FK_TB_M_CONCEPTS_DOC_METHOD_FK
	FOREIGN KEY (DOC_METHOD_FK) REFERENCES TB_INTERNATIONAL_STRINGS (ID)
;
ALTER TABLE TB_M_CONCEPTS ADD CONSTRAINT FK_TB_M_CONCEPTS_CONCEPT_TYP46
	FOREIGN KEY (CONCEPT_TYPE_FK) REFERENCES TB_M_LIS_CONCEPT_TYPES (ID)
;
ALTER TABLE TB_M_CONCEPTS ADD CONSTRAINT FK_TB_M_CONCEPTS_DERIVATION_FK
	FOREIGN KEY (DERIVATION_FK) REFERENCES TB_INTERNATIONAL_STRINGS (ID)
;
ALTER TABLE TB_M_CONCEPTS ADD CONSTRAINT FK_TB_M_CONCEPTS_LEGAL_ACTS_FK
	FOREIGN KEY (LEGAL_ACTS_FK) REFERENCES TB_INTERNATIONAL_STRINGS (ID)
;
ALTER TABLE TB_M_CONCEPTS ADD CONSTRAINT FK_TB_M_CONCEPTS_EXTENDS_FK
	FOREIGN KEY (EXTENDS_FK) REFERENCES TB_ITEMS (ID)
;
ALTER TABLE TB_M_CONCEPTS ADD CONSTRAINT FK_TB_M_CONCEPTS_VARIABLE_FK
	FOREIGN KEY (VARIABLE_FK) REFERENCES TB_M_VARIABLES (ID)
;

  
ALTER TABLE TB_M_CONCEPT_SCHEMES_VERSIONS ADD CONSTRAINT FK_TB_M_CONCEPT_SCHEMES_VERS66
	FOREIGN KEY (RELATED_OPERATION_FK) REFERENCES TB_EXTERNAL_ITEMS (ID)
;

  
  
  
ALTER TABLE TB_M_DIMENSION_ORDERS ADD CONSTRAINT FK_TB_M_DIMENSION_ORDERS_DIM84
	FOREIGN KEY (DIM_COMPONENT_FK) REFERENCES TB_ANNOTABLE_ARTEFACTS (ID)
;
ALTER TABLE TB_M_DIMENSION_ORDERS ADD CONSTRAINT FK_TB_M_DIMENSION_ORDERS_DSD27
	FOREIGN KEY (DSD_VERSION_HEADING_FK) REFERENCES TB_STRUCTURES_VERSIONS (ID) ON DELETE CASCADE
;
ALTER TABLE TB_M_DIMENSION_ORDERS ADD CONSTRAINT FK_TB_M_DIMENSION_ORDERS_DSD13
	FOREIGN KEY (DSD_VERSION_STUB_FK) REFERENCES TB_STRUCTURES_VERSIONS (ID) ON DELETE CASCADE
;

  
ALTER TABLE TB_M_MEASURE_DIM_PRECISIONS ADD CONSTRAINT FK_TB_M_MEASURE_DIM_PRECISIO34
	FOREIGN KEY (M_CODE_FK) REFERENCES TB_ITEMS (ID)
;
ALTER TABLE TB_M_MEASURE_DIM_PRECISIONS ADD CONSTRAINT FK_TB_M_MEASURE_DIM_PRECISIO29
	FOREIGN KEY (DSD_VERSION_FK) REFERENCES TB_STRUCTURES_VERSIONS (ID) ON DELETE CASCADE
;

  
  
  
  
  

ALTER TABLE TB_M_CONCEPT_RELATED ADD CONSTRAINT FK_TB_M_CONCEPT_RELATED_CONC10
	FOREIGN KEY (CONCEPT_RELATED_FK) REFERENCES TB_ITEMS (ID)
;
ALTER TABLE TB_M_CONCEPT_RELATED ADD CONSTRAINT FK_TB_M_CONCEPT_RELATED_CONC62
	FOREIGN KEY (CONCEPT_FK) REFERENCES TB_ITEMS (ID)
;

  
ALTER TABLE TB_M_CONCEPT_ROLES ADD CONSTRAINT FK_TB_M_CONCEPT_ROLES_CONCEP01
	FOREIGN KEY (CONCEPT_ROLE_FK) REFERENCES TB_ITEMS (ID)
;
ALTER TABLE TB_M_CONCEPT_ROLES ADD CONSTRAINT FK_TB_M_CONCEPT_ROLES_CONCEP48
	FOREIGN KEY (CONCEPT_FK) REFERENCES TB_ITEMS (ID)
;

  
ALTER TABLE TB_M_VAR_FAMILIES_VARIABLES ADD CONSTRAINT FK_TB_M_VAR_FAMILIES_VARIABL11
	FOREIGN KEY (VARIABLE_FAMILY_FK) REFERENCES TB_M_VARIABLE_FAMILIES (ID)
;
ALTER TABLE TB_M_VAR_FAMILIES_VARIABLES ADD CONSTRAINT FK_TB_M_VAR_FAMILIES_VARIABL30
	FOREIGN KEY (VARIABLE_FK) REFERENCES TB_M_VARIABLES (ID)
;

  


-- Index
  
  
  
  
  


