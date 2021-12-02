-- ###########################################
-- # Create
-- ###########################################

-- Create normal entities
    
CREATE TABLE TB_MISC_VALUES (
  ID BIGINT NOT NULL,
  NAME NVARCHAR(255) NOT NULL,
  DATE_VALUE_TZ VARCHAR(50),
  DATE_VALUE DATETIME,
  STRING_VALUE NVARCHAR(255),
  UUID NVARCHAR(36) NOT NULL,
  CREATED_DATE_TZ VARCHAR(50),
  CREATED_DATE DATETIME,
  CREATED_BY NVARCHAR(50),
  LAST_UPDATED_TZ VARCHAR(50),
  LAST_UPDATED DATETIME,
  LAST_UPDATED_BY NVARCHAR(50),
  VERSION BIGINT NOT NULL
);


CREATE TABLE TB_M_CATEGORIES (
  TB_CATEGORIES BIGINT NOT NULL

);


CREATE TABLE TB_M_CAT_SCHEMES_VERSIONS (
  PRODUCTION_VALIDATION_DATE_TZ VARCHAR(50),
  PRODUCTION_VALIDATION_DATE DATETIME,
  PRODUCTION_VALIDATION_USER NVARCHAR(255),
  DIFFUSION_VALIDATION_DATE_TZ VARCHAR(50),
  DIFFUSION_VALIDATION_DATE DATETIME,
  DIFFUSION_VALIDATION_USER NVARCHAR(255),
  INTERNAL_PUBLICATION_DATE_TZ VARCHAR(50),
  INTERNAL_PUBLICATION_DATE DATETIME,
  INTERNAL_PUBLICATION_USER NVARCHAR(255),
  EXTERNAL_PUBLICATION_DATE_TZ VARCHAR(50),
  EXTERNAL_PUBLICATION_DATE DATETIME,
  EXTERNAL_PUBLICATION_USER NVARCHAR(255),
  PROC_STATUS NVARCHAR(255) NOT NULL,
  TB_CAT_SCHEMES_VERSIONS BIGINT NOT NULL,
  STREAM_MESSAGE_STATUS VARCHAR(255) NOT NULL

);


CREATE TABLE TB_M_VARIABLES (
  ID BIGINT NOT NULL,
  VALID_FROM_TZ VARCHAR(50),
  VALID_FROM DATETIME,
  VALID_TO_TZ VARCHAR(50),
  VALID_TO DATETIME,
  UPDATE_DATE_TZ VARCHAR(50),
  UPDATE_DATE DATETIME,
  UUID NVARCHAR(36) NOT NULL,
  CREATED_DATE_TZ VARCHAR(50),
  CREATED_DATE DATETIME,
  CREATED_BY NVARCHAR(50),
  LAST_UPDATED_TZ VARCHAR(50),
  LAST_UPDATED DATETIME,
  LAST_UPDATED_BY NVARCHAR(50),
  VERSION BIGINT NOT NULL,
  NAMEABLE_ARTEFACT_FK BIGINT NOT NULL,
  SHORT_NAME_FK BIGINT NOT NULL,
  REPLACED_BY_VARIABLE_FK BIGINT,
  VARIABLE_TYPE NVARCHAR(255)
);


CREATE TABLE TB_M_VARIABLE_ELEMENTS (
  ID BIGINT NOT NULL,
  VALID_FROM_TZ VARCHAR(50),
  VALID_FROM DATETIME,
  VALID_TO_TZ VARCHAR(50),
  VALID_TO DATETIME,
  LATITUDE FLOAT,
  LONGITUDE FLOAT,
  SHAPE_WKT TEXT,
  SHAPE_GEOJSON TEXT,
  UPDATE_DATE_TZ VARCHAR(50),
  UPDATE_DATE DATETIME,
  UUID NVARCHAR(36) NOT NULL,
  CREATED_DATE_TZ VARCHAR(50),
  CREATED_DATE DATETIME,
  CREATED_BY NVARCHAR(50),
  LAST_UPDATED_TZ VARCHAR(50),
  LAST_UPDATED DATETIME,
  LAST_UPDATED_BY NVARCHAR(50),
  VERSION BIGINT NOT NULL,
  IDENTIFIABLE_ARTEFACT_FK BIGINT NOT NULL,
  SHORT_NAME_FK BIGINT NOT NULL,
  COMMENT_FK BIGINT,
  REPLACED_BY_VAR_ELEMENT_FK BIGINT,
  GEOGRAPHICAL_GRANULARITY_FK BIGINT,
  VARIABLE_FK BIGINT NOT NULL
);


CREATE TABLE TB_M_CODES (
  ORDER1 INT,
  ORDER2 INT,
  ORDER3 INT,
  ORDER4 INT,
  ORDER5 INT,
  ORDER6 INT,
  ORDER7 INT,
  ORDER8 INT,
  ORDER9 INT,
  ORDER10 INT,
  ORDER11 INT,
  ORDER12 INT,
  ORDER13 INT,
  ORDER14 INT,
  ORDER15 INT,
  ORDER16 INT,
  ORDER17 INT,
  ORDER18 INT,
  ORDER19 INT,
  ORDER20 INT,
  OPENNESS1 BIT,
  OPENNESS2 BIT,
  OPENNESS3 BIT,
  OPENNESS4 BIT,
  OPENNESS5 BIT,
  OPENNESS6 BIT,
  OPENNESS7 BIT,
  OPENNESS8 BIT,
  OPENNESS9 BIT,
  OPENNESS10 BIT,
  OPENNESS11 BIT,
  OPENNESS12 BIT,
  OPENNESS13 BIT,
  OPENNESS14 BIT,
  OPENNESS15 BIT,
  OPENNESS16 BIT,
  OPENNESS17 BIT,
  OPENNESS18 BIT,
  OPENNESS19 BIT,
  OPENNESS20 BIT,
  SHORT_NAME_FK BIGINT,
  VARIABLE_ELEMENT_FK BIGINT,
  TB_CODES BIGINT NOT NULL

);


CREATE TABLE TB_M_CODELIST_FAMILIES (
  ID BIGINT NOT NULL,
  UPDATE_DATE_TZ VARCHAR(50),
  UPDATE_DATE DATETIME,
  UUID NVARCHAR(36) NOT NULL,
  CREATED_DATE_TZ VARCHAR(50),
  CREATED_DATE DATETIME,
  CREATED_BY NVARCHAR(50),
  LAST_UPDATED_TZ VARCHAR(50),
  LAST_UPDATED DATETIME,
  LAST_UPDATED_BY NVARCHAR(50),
  VERSION BIGINT NOT NULL,
  NAMEABLE_ARTEFACT_FK BIGINT NOT NULL
);


CREATE TABLE TB_M_CODELIST_ORDER_VIS (
  ID BIGINT NOT NULL,
  UPDATE_DATE_TZ VARCHAR(50),
  UPDATE_DATE DATETIME,
  COLUMN_INDEX INT NOT NULL,
  UUID NVARCHAR(36) NOT NULL,
  CREATED_DATE_TZ VARCHAR(50),
  CREATED_DATE DATETIME,
  CREATED_BY NVARCHAR(50),
  LAST_UPDATED_TZ VARCHAR(50),
  LAST_UPDATED DATETIME,
  LAST_UPDATED_BY NVARCHAR(50),
  VERSION BIGINT NOT NULL,
  NAMEABLE_ARTEFACT_FK BIGINT NOT NULL,
  CODELIST_FK BIGINT NOT NULL
);


CREATE TABLE TB_M_CODELISTS_VERSIONS (
  IS_RECOMMENDED BIT,
  SHORT_NAME_FK BIGINT,
  DESCRIPTION_SOURCE_FK BIGINT,
  CODELIST_FAMILY_FK BIGINT,
  VARIABLE_FK BIGINT,
  REPLACED_BY_CODELIST_FK BIGINT,
  DEFAULT_ORDER_VISUAL_FK BIGINT,
  DEFAULT_OPENNESS_VISUAL_FK BIGINT,
  PRODUCTION_VALIDATION_DATE_TZ VARCHAR(50),
  PRODUCTION_VALIDATION_DATE DATETIME,
  PRODUCTION_VALIDATION_USER NVARCHAR(255),
  DIFFUSION_VALIDATION_DATE_TZ VARCHAR(50),
  DIFFUSION_VALIDATION_DATE DATETIME,
  DIFFUSION_VALIDATION_USER NVARCHAR(255),
  INTERNAL_PUBLICATION_DATE_TZ VARCHAR(50),
  INTERNAL_PUBLICATION_DATE DATETIME,
  INTERNAL_PUBLICATION_USER NVARCHAR(255),
  EXTERNAL_PUBLICATION_DATE_TZ VARCHAR(50),
  EXTERNAL_PUBLICATION_DATE DATETIME,
  EXTERNAL_PUBLICATION_USER NVARCHAR(255),
  PROC_STATUS NVARCHAR(255) NOT NULL,
  ACCESS_TYPE NVARCHAR(255),
  TB_CODELISTS_VERSIONS BIGINT NOT NULL,
  STREAM_MESSAGE_STATUS VARCHAR(255) NOT NULL

);


CREATE TABLE TB_M_CODELIST_OPENNESS_VIS (
  ID BIGINT NOT NULL,
  UPDATE_DATE_TZ VARCHAR(50),
  UPDATE_DATE DATETIME,
  COLUMN_INDEX INT NOT NULL,
  UUID NVARCHAR(36) NOT NULL,
  CREATED_DATE_TZ VARCHAR(50),
  CREATED_DATE DATETIME,
  CREATED_BY NVARCHAR(50),
  LAST_UPDATED_TZ VARCHAR(50),
  LAST_UPDATED DATETIME,
  LAST_UPDATED_BY NVARCHAR(50),
  VERSION BIGINT NOT NULL,
  NAMEABLE_ARTEFACT_FK BIGINT NOT NULL,
  CODELIST_FK BIGINT NOT NULL
);


CREATE TABLE TB_M_VAR_ELEM_OPERATIONS (
  ID BIGINT NOT NULL,
  CODE NVARCHAR(255) NOT NULL,
  UUID NVARCHAR(36) NOT NULL,
  CREATED_DATE_TZ VARCHAR(50),
  CREATED_DATE DATETIME,
  CREATED_BY NVARCHAR(50),
  LAST_UPDATED_TZ VARCHAR(50),
  LAST_UPDATED DATETIME,
  LAST_UPDATED_BY NVARCHAR(50),
  VERSION BIGINT NOT NULL,
  VARIABLE_FK BIGINT NOT NULL,
  OPERATION_TYPE NVARCHAR(255) NOT NULL
);


CREATE TABLE TB_M_VARIABLE_FAMILIES (
  ID BIGINT NOT NULL,
  UPDATE_DATE_TZ VARCHAR(50),
  UPDATE_DATE DATETIME,
  UUID NVARCHAR(36) NOT NULL,
  CREATED_DATE_TZ VARCHAR(50),
  CREATED_DATE DATETIME,
  CREATED_BY NVARCHAR(50),
  LAST_UPDATED_TZ VARCHAR(50),
  LAST_UPDATED DATETIME,
  LAST_UPDATED_BY NVARCHAR(50),
  VERSION BIGINT NOT NULL,
  NAMEABLE_ARTEFACT_FK BIGINT NOT NULL
);


CREATE TABLE TB_M_LIS_CONCEPT_TYPES (
  ID BIGINT NOT NULL,
  IDENTIFIER NVARCHAR(255) NOT NULL,
  DESCRIPTION_FK BIGINT
);


CREATE TABLE TB_M_QUANTITIES (
  ID BIGINT NOT NULL,
  SIGNIFICANT_DIGITS INT,
  DECIMAL_PLACES INT,
  UNIT_MULTIPLIER INT,
  MINIMUM INT,
  MAXIMUM INT,
  IS_PERCENTAGE BIT,
  BASE_VALUE INT,
  BASE_TIME NVARCHAR(255),
  UUID NVARCHAR(36) NOT NULL,
  CREATED_DATE_TZ VARCHAR(50),
  CREATED_DATE DATETIME,
  CREATED_BY NVARCHAR(50),
  LAST_UPDATED_TZ VARCHAR(50),
  LAST_UPDATED DATETIME,
  LAST_UPDATED_BY NVARCHAR(50),
  VERSION BIGINT NOT NULL,
  UNIT_CODE_FK BIGINT,
  NUMERATOR_FK BIGINT,
  DENOMINATOR_FK BIGINT,
  PERCENTAGE_OF_FK BIGINT,
  BASE_LOCATION_FK BIGINT,
  BASE_QUANTITY_FK BIGINT,
  QUANTITY_TYPE NVARCHAR(255),
  UNIT_SYMBOL_POSITION NVARCHAR(255)
);


CREATE TABLE TB_M_CONCEPTS (
  PLURAL_NAME_FK BIGINT,
  ACRONYM_FK BIGINT,
  DESCRIPTION_SOURCE_FK BIGINT,
  CONTEXT_FK BIGINT,
  DOC_METHOD_FK BIGINT,
  CONCEPT_TYPE_FK BIGINT,
  DERIVATION_FK BIGINT,
  LEGAL_ACTS_FK BIGINT,
  EXTENDS_FK BIGINT,
  VARIABLE_FK BIGINT,
  QUANTITY_FK BIGINT,
  SDMX_RELATED_ARTEFACT NVARCHAR(255),
  TB_CONCEPTS BIGINT NOT NULL

);


CREATE TABLE TB_M_CONCEPT_SCHEMES_VERSIONS (
  RELATED_OPERATION_FK BIGINT,
  PRODUCTION_VALIDATION_DATE_TZ VARCHAR(50),
  PRODUCTION_VALIDATION_DATE DATETIME,
  PRODUCTION_VALIDATION_USER NVARCHAR(255),
  DIFFUSION_VALIDATION_DATE_TZ VARCHAR(50),
  DIFFUSION_VALIDATION_DATE DATETIME,
  DIFFUSION_VALIDATION_USER NVARCHAR(255),
  INTERNAL_PUBLICATION_DATE_TZ VARCHAR(50),
  INTERNAL_PUBLICATION_DATE DATETIME,
  INTERNAL_PUBLICATION_USER NVARCHAR(255),
  EXTERNAL_PUBLICATION_DATE_TZ VARCHAR(50),
  EXTERNAL_PUBLICATION_DATE DATETIME,
  EXTERNAL_PUBLICATION_USER NVARCHAR(255),
  PROC_STATUS NVARCHAR(255) NOT NULL,
  CONCEPT_SCHEME_TYPE NVARCHAR(255),
  TB_CONCEPT_SCHEMES_VERSIONS BIGINT NOT NULL,
  STREAM_MESSAGE_STATUS VARCHAR(255) NOT NULL

);


CREATE TABLE TB_M_DATASTRUCTURE_VERSIONS (
  AUTO_OPEN BIT,
  SHOW_DECIMALS INT,
  STATISTICAL_OPERATION_FK BIGINT,
  PRODUCTION_VALIDATION_DATE_TZ VARCHAR(50),
  PRODUCTION_VALIDATION_DATE DATETIME,
  PRODUCTION_VALIDATION_USER NVARCHAR(255),
  DIFFUSION_VALIDATION_DATE_TZ VARCHAR(50),
  DIFFUSION_VALIDATION_DATE DATETIME,
  DIFFUSION_VALIDATION_USER NVARCHAR(255),
  INTERNAL_PUBLICATION_DATE_TZ VARCHAR(50),
  INTERNAL_PUBLICATION_DATE DATETIME,
  INTERNAL_PUBLICATION_USER NVARCHAR(255),
  EXTERNAL_PUBLICATION_DATE_TZ VARCHAR(50),
  EXTERNAL_PUBLICATION_DATE DATETIME,
  EXTERNAL_PUBLICATION_USER NVARCHAR(255),
  PROC_STATUS NVARCHAR(255) NOT NULL,
  TB_DATASTRUCTURE_VERSIONS BIGINT NOT NULL,
  STREAM_MESSAGE_STATUS VARCHAR(255) NOT NULL

);


CREATE TABLE TB_M_DIMENSION_ORDERS (
  ID BIGINT NOT NULL,
  DIM_ORDER INT NOT NULL,
  UUID NVARCHAR(36) NOT NULL,
  VERSION BIGINT NOT NULL,
  DIM_COMPONENT_FK BIGINT NOT NULL,
  DSD_VERSION_HEADING_FK BIGINT,
  DSD_VERSION_STUB_FK BIGINT
);


CREATE TABLE TB_M_DIM_VIS_INFO (
  ID BIGINT NOT NULL,
  UUID NVARCHAR(36) NOT NULL,
  VERSION BIGINT NOT NULL,
  DIM_COMPONENT_FK BIGINT NOT NULL,
  DISPLAY_ORDER_FK BIGINT,
  HIERARCHY_LEVELS_OPEN_FK BIGINT,
  DSD_VERSION_FK BIGINT NOT NULL
);


CREATE TABLE TB_M_MEASURE_DIM_PRECISIONS (
  ID BIGINT NOT NULL,
  SHOW_DECIMAL_PRECISION INT NOT NULL,
  UUID NVARCHAR(36) NOT NULL,
  VERSION BIGINT NOT NULL,
  M_CONCEPT_FK BIGINT NOT NULL,
  DSD_VERSION_FK BIGINT NOT NULL
);


CREATE TABLE TB_M_ORGANISATIONS (
  SPECIAL_ORG_HAS_BEEN_PUBLISHED BIT,
  TB_ORGANISATIONS BIGINT NOT NULL

);


CREATE TABLE TB_M_ORG_SCHEMES_VERSIONS (
  PRODUCTION_VALIDATION_DATE_TZ VARCHAR(50),
  PRODUCTION_VALIDATION_DATE DATETIME,
  PRODUCTION_VALIDATION_USER NVARCHAR(255),
  DIFFUSION_VALIDATION_DATE_TZ VARCHAR(50),
  DIFFUSION_VALIDATION_DATE DATETIME,
  DIFFUSION_VALIDATION_USER NVARCHAR(255),
  INTERNAL_PUBLICATION_DATE_TZ VARCHAR(50),
  INTERNAL_PUBLICATION_DATE DATETIME,
  INTERNAL_PUBLICATION_USER NVARCHAR(255),
  EXTERNAL_PUBLICATION_DATE_TZ VARCHAR(50),
  EXTERNAL_PUBLICATION_DATE DATETIME,
  EXTERNAL_PUBLICATION_USER NVARCHAR(255),
  PROC_STATUS NVARCHAR(255) NOT NULL,
  TB_ORG_SCHEMES_VERSIONS BIGINT NOT NULL,
  STREAM_MESSAGE_STATUS VARCHAR(255) NOT NULL

);



-- Create many to many relations
    
CREATE TABLE TB_M_CONCEPT_RELATED (
  CONCEPT_RELATED_FK BIGINT NOT NULL,
  CONCEPT_FK BIGINT NOT NULL
);


CREATE TABLE TB_M_CONCEPT_ROLES (
  CONCEPT_ROLE_FK BIGINT NOT NULL,
  CONCEPT_FK BIGINT NOT NULL
);


CREATE TABLE TB_M_VAR_ELEM_OP_SOURCES (
  SOURCE_FK BIGINT NOT NULL,
  OPERATION_FK BIGINT NOT NULL
);


CREATE TABLE TB_M_VAR_ELEM_OP_TARGETS (
  TARGET_FK BIGINT NOT NULL,
  OPERATION_FK BIGINT NOT NULL
);


CREATE TABLE TB_M_VAR_FAMILIES_VARIABLES (
  VARIABLE_FAMILY_FK BIGINT NOT NULL,
  VARIABLE_FK BIGINT NOT NULL
);



-- Primary keys
    
ALTER TABLE TB_MISC_VALUES ADD CONSTRAINT PK_TB_MISC_VALUES
	PRIMARY KEY (ID)
;

ALTER TABLE TB_M_VARIABLES ADD CONSTRAINT PK_TB_M_VARIABLES
	PRIMARY KEY (ID)
;

ALTER TABLE TB_M_VARIABLE_ELEMENTS ADD CONSTRAINT PK_TB_M_VARIABLE_ELEMENTS
	PRIMARY KEY (ID)
;

ALTER TABLE TB_M_CODELIST_FAMILIES ADD CONSTRAINT PK_TB_M_CODELIST_FAMILIES
	PRIMARY KEY (ID)
;

ALTER TABLE TB_M_CODELIST_ORDER_VIS ADD CONSTRAINT PK_TB_M_CODELIST_ORDER_VIS
	PRIMARY KEY (ID)
;

ALTER TABLE TB_M_CODELIST_OPENNESS_VIS ADD CONSTRAINT PK_TB_M_CODELIST_OPENNESS_VIS
	PRIMARY KEY (ID)
;

ALTER TABLE TB_M_VAR_ELEM_OPERATIONS ADD CONSTRAINT PK_TB_M_VAR_ELEM_OPERATIONS
	PRIMARY KEY (ID)
;

ALTER TABLE TB_M_VARIABLE_FAMILIES ADD CONSTRAINT PK_TB_M_VARIABLE_FAMILIES
	PRIMARY KEY (ID)
;

ALTER TABLE TB_M_LIS_CONCEPT_TYPES ADD CONSTRAINT PK_TB_M_LIS_CONCEPT_TYPES
	PRIMARY KEY (ID)
;

ALTER TABLE TB_M_QUANTITIES ADD CONSTRAINT PK_TB_M_QUANTITIES
	PRIMARY KEY (ID)
;

ALTER TABLE TB_M_DIMENSION_ORDERS ADD CONSTRAINT PK_TB_M_DIMENSION_ORDERS
	PRIMARY KEY (ID)
;

ALTER TABLE TB_M_DIM_VIS_INFO ADD CONSTRAINT PK_TB_M_DIM_VIS_INFO
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

ALTER TABLE TB_M_VAR_ELEM_OP_SOURCES ADD CONSTRAINT PK_TB_M_VAR_ELEM_OP_SOURCES
	PRIMARY KEY (SOURCE_FK, OPERATION_FK)
;

ALTER TABLE TB_M_VAR_ELEM_OP_TARGETS ADD CONSTRAINT PK_TB_M_VAR_ELEM_OP_TARGETS
	PRIMARY KEY (TARGET_FK, OPERATION_FK)
;

ALTER TABLE TB_M_VAR_FAMILIES_VARIABLES ADD CONSTRAINT PK_TB_M_VAR_FAMILIES_VARIABLES
	PRIMARY KEY (VARIABLE_FAMILY_FK, VARIABLE_FK)
;


-- Unique constraints

ALTER TABLE TB_MISC_VALUES
    ADD CONSTRAINT UQ_TB_MISC_VALUES UNIQUE (UUID)
;




ALTER TABLE TB_M_VARIABLES
    ADD CONSTRAINT UQ_TB_M_VARIABLES UNIQUE (UUID)
;


ALTER TABLE TB_M_VARIABLE_ELEMENTS
    ADD CONSTRAINT UQ_TB_M_VARIABLE_ELEMENTS UNIQUE (UUID)
;



ALTER TABLE TB_M_CODELIST_FAMILIES
    ADD CONSTRAINT UQ_TB_M_CODELIST_FAMILIES UNIQUE (UUID)
;


ALTER TABLE TB_M_CODELIST_ORDER_VIS
    ADD CONSTRAINT UQ_TB_M_CODELIST_ORDER_VIS UNIQUE (UUID)
;



ALTER TABLE TB_M_CODELIST_OPENNESS_VIS
    ADD CONSTRAINT UQ_TB_M_CODELIST_OPENNESS_VIS UNIQUE (UUID)
;


ALTER TABLE TB_M_VAR_ELEM_OPERATIONS
    ADD CONSTRAINT UQ_TB_M_VAR_ELEM_OPERATIONS UNIQUE (UUID)
;


ALTER TABLE TB_M_VARIABLE_FAMILIES
    ADD CONSTRAINT UQ_TB_M_VARIABLE_FAMILIES UNIQUE (UUID)
;


ALTER TABLE TB_M_LIS_CONCEPT_TYPES
	ADD CONSTRAINT UQ_TB_M_LIS_CONCEPT_TYPES UNIQUE (ID)
;


ALTER TABLE TB_M_QUANTITIES
    ADD CONSTRAINT UQ_TB_M_QUANTITIES UNIQUE (UUID)
;





ALTER TABLE TB_M_DIMENSION_ORDERS
    ADD CONSTRAINT UQ_TB_M_DIMENSION_ORDERS UNIQUE (UUID)
;


ALTER TABLE TB_M_DIM_VIS_INFO
    ADD CONSTRAINT UQ_TB_M_DIM_VIS_INFO UNIQUE (UUID)
;


ALTER TABLE TB_M_MEASURE_DIM_PRECISIONS
    ADD CONSTRAINT UQ_TB_M_MEASURE_DIM_PRECISIONS UNIQUE (UUID)
;





-- Foreign key constraints
ALTER TABLE TB_M_CATEGORIES ADD CONSTRAINT FK_TB_M_CATEGORIES_TB_CATEGORIES
	FOREIGN KEY (TB_CATEGORIES) REFERENCES TB_CATEGORIES (ID)
;
ALTER TABLE TB_M_CAT_SCHEMES_VERSIONS ADD CONSTRAINT FK_TB_M_CAT_SCHEMES_VERSIONS_TB_CAT_SCHEMES_VERSIONS
	FOREIGN KEY (TB_CAT_SCHEMES_VERSIONS) REFERENCES TB_ITEM_SCHEMES_VERSIONS (ID)
;
ALTER TABLE TB_M_CODES ADD CONSTRAINT FK_TB_M_CODES_TB_CODES
	FOREIGN KEY (TB_CODES) REFERENCES TB_CODES (ID)
;
ALTER TABLE TB_M_CODELISTS_VERSIONS ADD CONSTRAINT FK_TB_M_CODELISTS_VERSIONS_TB_CODELISTS_VERSIONS
	FOREIGN KEY (TB_CODELISTS_VERSIONS) REFERENCES TB_ITEM_SCHEMES_VERSIONS (ID)
;
ALTER TABLE TB_M_CONCEPTS ADD CONSTRAINT FK_TB_M_CONCEPTS_TB_CONCEPTS
	FOREIGN KEY (TB_CONCEPTS) REFERENCES TB_CONCEPTS (ID)
;
ALTER TABLE TB_M_CONCEPT_SCHEMES_VERSIONS ADD CONSTRAINT FK_TB_M_CONCEPT_SCHEMES_VERSIONS_TB_CONCEPT_SCHEMES_VERSIONS
	FOREIGN KEY (TB_CONCEPT_SCHEMES_VERSIONS) REFERENCES TB_ITEM_SCHEMES_VERSIONS (ID)
;
ALTER TABLE TB_M_DATASTRUCTURE_VERSIONS ADD CONSTRAINT FK_TB_M_DATASTRUCTURE_VERSIONS_TB_DATASTRUCTURE_VERSIONS
	FOREIGN KEY (TB_DATASTRUCTURE_VERSIONS) REFERENCES TB_STRUCTURES_VERSIONS (ID)
;
ALTER TABLE TB_M_ORGANISATIONS ADD CONSTRAINT FK_TB_M_ORGANISATIONS_TB_ORGANISATIONS
	FOREIGN KEY (TB_ORGANISATIONS) REFERENCES TB_ORGANISATIONS (ID)
;
ALTER TABLE TB_M_ORG_SCHEMES_VERSIONS ADD CONSTRAINT FK_TB_M_ORG_SCHEMES_VERSIONS_TB_ORG_SCHEMES_VERSIONS
	FOREIGN KEY (TB_ORG_SCHEMES_VERSIONS) REFERENCES TB_ITEM_SCHEMES_VERSIONS (ID)
;


  
  
  
  
  
  
ALTER TABLE TB_M_VARIABLES ADD CONSTRAINT FK_TB_M_VARIABLES_NAMEABLE_ARTEFACT_FK
	FOREIGN KEY (NAMEABLE_ARTEFACT_FK) REFERENCES TB_ANNOTABLE_ARTEFACTS (ID)
;
ALTER TABLE TB_M_VARIABLES ADD CONSTRAINT FK_TB_M_VARIABLES_SHORT_NAME_FK
	FOREIGN KEY (SHORT_NAME_FK) REFERENCES TB_INTERNATIONAL_STRINGS (ID)
;
ALTER TABLE TB_M_VARIABLES ADD CONSTRAINT FK_TB_M_VARIABLES_REPLACED_BY_VARIABLE_FK
	FOREIGN KEY (REPLACED_BY_VARIABLE_FK) REFERENCES TB_M_VARIABLES (ID)
;

  
ALTER TABLE TB_M_VARIABLE_ELEMENTS ADD CONSTRAINT FK_TB_M_VARIABLE_ELEMENTS_IDENTIFIABLE_ARTEFACT_FK
	FOREIGN KEY (IDENTIFIABLE_ARTEFACT_FK) REFERENCES TB_ANNOTABLE_ARTEFACTS (ID)
;
ALTER TABLE TB_M_VARIABLE_ELEMENTS ADD CONSTRAINT FK_TB_M_VARIABLE_ELEMENTS_SHORT_NAME_FK
	FOREIGN KEY (SHORT_NAME_FK) REFERENCES TB_INTERNATIONAL_STRINGS (ID)
;
ALTER TABLE TB_M_VARIABLE_ELEMENTS ADD CONSTRAINT FK_TB_M_VARIABLE_ELEMENTS_COMMENT_FK
	FOREIGN KEY (COMMENT_FK) REFERENCES TB_INTERNATIONAL_STRINGS (ID)
;
ALTER TABLE TB_M_VARIABLE_ELEMENTS ADD CONSTRAINT FK_TB_M_VARIABLE_ELEMENTS_REPLACED_BY_VAR_ELEMENT_FK
	FOREIGN KEY (REPLACED_BY_VAR_ELEMENT_FK) REFERENCES TB_M_VARIABLE_ELEMENTS (ID)
;
ALTER TABLE TB_M_VARIABLE_ELEMENTS ADD CONSTRAINT FK_TB_M_VARIABLE_ELEMENTS_GEOGRAPHICAL_GRANULARITY_FK
	FOREIGN KEY (GEOGRAPHICAL_GRANULARITY_FK) REFERENCES TB_CODES (ID)
;
ALTER TABLE TB_M_VARIABLE_ELEMENTS ADD CONSTRAINT FK_TB_M_VARIABLE_ELEMENTS_VARIABLE_FK
	FOREIGN KEY (VARIABLE_FK) REFERENCES TB_M_VARIABLES (ID)
;

  
ALTER TABLE TB_M_CODES ADD CONSTRAINT FK_TB_M_CODES_SHORT_NAME_FK
	FOREIGN KEY (SHORT_NAME_FK) REFERENCES TB_INTERNATIONAL_STRINGS (ID)
;
ALTER TABLE TB_M_CODES ADD CONSTRAINT FK_TB_M_CODES_VARIABLE_ELEMENT_FK
	FOREIGN KEY (VARIABLE_ELEMENT_FK) REFERENCES TB_M_VARIABLE_ELEMENTS (ID)
;

  
ALTER TABLE TB_M_CODELIST_FAMILIES ADD CONSTRAINT FK_TB_M_CODELIST_FAMILIES_NAMEABLE_ARTEFACT_FK
	FOREIGN KEY (NAMEABLE_ARTEFACT_FK) REFERENCES TB_ANNOTABLE_ARTEFACTS (ID)
;

  
ALTER TABLE TB_M_CODELIST_ORDER_VIS ADD CONSTRAINT FK_TB_M_CODELIST_ORDER_VIS_NAMEABLE_ARTEFACT_FK
	FOREIGN KEY (NAMEABLE_ARTEFACT_FK) REFERENCES TB_ANNOTABLE_ARTEFACTS (ID)
;
ALTER TABLE TB_M_CODELIST_ORDER_VIS ADD CONSTRAINT FK_TB_M_CODELIST_ORDER_VIS_CODELIST_FK
	FOREIGN KEY (CODELIST_FK) REFERENCES TB_ITEM_SCHEMES_VERSIONS (ID)
;

  
ALTER TABLE TB_M_CODELISTS_VERSIONS ADD CONSTRAINT FK_TB_M_CODELISTS_VERSIONS_SHORT_NAME_FK
	FOREIGN KEY (SHORT_NAME_FK) REFERENCES TB_INTERNATIONAL_STRINGS (ID)
;
ALTER TABLE TB_M_CODELISTS_VERSIONS ADD CONSTRAINT FK_TB_M_CODELISTS_VERSIONS_DESCRIPTION_SOURCE_FK
	FOREIGN KEY (DESCRIPTION_SOURCE_FK) REFERENCES TB_INTERNATIONAL_STRINGS (ID)
;
ALTER TABLE TB_M_CODELISTS_VERSIONS ADD CONSTRAINT FK_TB_M_CODELISTS_VERSIONS_CODELIST_FAMILY_FK
	FOREIGN KEY (CODELIST_FAMILY_FK) REFERENCES TB_M_CODELIST_FAMILIES (ID)
;
ALTER TABLE TB_M_CODELISTS_VERSIONS ADD CONSTRAINT FK_TB_M_CODELISTS_VERSIONS_VARIABLE_FK
	FOREIGN KEY (VARIABLE_FK) REFERENCES TB_M_VARIABLES (ID)
;
ALTER TABLE TB_M_CODELISTS_VERSIONS ADD CONSTRAINT FK_TB_M_CODELISTS_VERSIONS_REPLACED_BY_CODELIST_FK
	FOREIGN KEY (REPLACED_BY_CODELIST_FK) REFERENCES TB_ITEM_SCHEMES_VERSIONS (ID)
;
ALTER TABLE TB_M_CODELISTS_VERSIONS ADD CONSTRAINT FK_TB_M_CODELISTS_VERSIONS_DEFAULT_ORDER_VISUAL_FK
	FOREIGN KEY (DEFAULT_ORDER_VISUAL_FK) REFERENCES TB_M_CODELIST_ORDER_VIS (ID)
;
ALTER TABLE TB_M_CODELISTS_VERSIONS ADD CONSTRAINT FK_TB_M_CODELISTS_VERSIONS_DEFAULT_OPENNESS_VISUAL_FK
	FOREIGN KEY (DEFAULT_OPENNESS_VISUAL_FK) REFERENCES TB_M_CODELIST_OPENNESS_VIS (ID)
;

  
ALTER TABLE TB_M_CODELIST_OPENNESS_VIS ADD CONSTRAINT FK_TB_M_CODELIST_OPENNESS_VIS_NAMEABLE_ARTEFACT_FK
	FOREIGN KEY (NAMEABLE_ARTEFACT_FK) REFERENCES TB_ANNOTABLE_ARTEFACTS (ID)
;
ALTER TABLE TB_M_CODELIST_OPENNESS_VIS ADD CONSTRAINT FK_TB_M_CODELIST_OPENNESS_VIS_CODELIST_FK
	FOREIGN KEY (CODELIST_FK) REFERENCES TB_ITEM_SCHEMES_VERSIONS (ID)
;

  
ALTER TABLE TB_M_VAR_ELEM_OPERATIONS ADD CONSTRAINT FK_TB_M_VAR_ELEM_OPERATIONS_VARIABLE_FK
	FOREIGN KEY (VARIABLE_FK) REFERENCES TB_M_VARIABLES (ID)
;

  
ALTER TABLE TB_M_VARIABLE_FAMILIES ADD CONSTRAINT FK_TB_M_VARIABLE_FAMILIES_NAMEABLE_ARTEFACT_FK
	FOREIGN KEY (NAMEABLE_ARTEFACT_FK) REFERENCES TB_ANNOTABLE_ARTEFACTS (ID)
;

  
ALTER TABLE TB_M_LIS_CONCEPT_TYPES ADD CONSTRAINT FK_TB_M_LIS_CONCEPT_TYPES_DESCRIPTION_FK
	FOREIGN KEY (DESCRIPTION_FK) REFERENCES TB_INTERNATIONAL_STRINGS (ID)
;

  
ALTER TABLE TB_M_QUANTITIES ADD CONSTRAINT FK_TB_M_QUANTITIES_UNIT_CODE_FK
	FOREIGN KEY (UNIT_CODE_FK) REFERENCES TB_CODES (ID)
;
ALTER TABLE TB_M_QUANTITIES ADD CONSTRAINT FK_TB_M_QUANTITIES_NUMERATOR_FK
	FOREIGN KEY (NUMERATOR_FK) REFERENCES TB_CONCEPTS (ID)
;
ALTER TABLE TB_M_QUANTITIES ADD CONSTRAINT FK_TB_M_QUANTITIES_DENOMINATOR_FK
	FOREIGN KEY (DENOMINATOR_FK) REFERENCES TB_CONCEPTS (ID)
;
ALTER TABLE TB_M_QUANTITIES ADD CONSTRAINT FK_TB_M_QUANTITIES_PERCENTAGE_OF_FK
	FOREIGN KEY (PERCENTAGE_OF_FK) REFERENCES TB_INTERNATIONAL_STRINGS (ID)
;
ALTER TABLE TB_M_QUANTITIES ADD CONSTRAINT FK_TB_M_QUANTITIES_BASE_LOCATION_FK
	FOREIGN KEY (BASE_LOCATION_FK) REFERENCES TB_CODES (ID)
;
ALTER TABLE TB_M_QUANTITIES ADD CONSTRAINT FK_TB_M_QUANTITIES_BASE_QUANTITY_FK
	FOREIGN KEY (BASE_QUANTITY_FK) REFERENCES TB_CONCEPTS (ID)
;

  
ALTER TABLE TB_M_CONCEPTS ADD CONSTRAINT FK_TB_M_CONCEPTS_PLURAL_NAME_FK
	FOREIGN KEY (PLURAL_NAME_FK) REFERENCES TB_INTERNATIONAL_STRINGS (ID)
;
ALTER TABLE TB_M_CONCEPTS ADD CONSTRAINT FK_TB_M_CONCEPTS_ACRONYM_FK
	FOREIGN KEY (ACRONYM_FK) REFERENCES TB_INTERNATIONAL_STRINGS (ID)
;
ALTER TABLE TB_M_CONCEPTS ADD CONSTRAINT FK_TB_M_CONCEPTS_DESCRIPTION_SOURCE_FK
	FOREIGN KEY (DESCRIPTION_SOURCE_FK) REFERENCES TB_INTERNATIONAL_STRINGS (ID)
;
ALTER TABLE TB_M_CONCEPTS ADD CONSTRAINT FK_TB_M_CONCEPTS_CONTEXT_FK
	FOREIGN KEY (CONTEXT_FK) REFERENCES TB_INTERNATIONAL_STRINGS (ID)
;
ALTER TABLE TB_M_CONCEPTS ADD CONSTRAINT FK_TB_M_CONCEPTS_DOC_METHOD_FK
	FOREIGN KEY (DOC_METHOD_FK) REFERENCES TB_INTERNATIONAL_STRINGS (ID)
;
ALTER TABLE TB_M_CONCEPTS ADD CONSTRAINT FK_TB_M_CONCEPTS_CONCEPT_TYPE_FK
	FOREIGN KEY (CONCEPT_TYPE_FK) REFERENCES TB_M_LIS_CONCEPT_TYPES (ID)
;
ALTER TABLE TB_M_CONCEPTS ADD CONSTRAINT FK_TB_M_CONCEPTS_DERIVATION_FK
	FOREIGN KEY (DERIVATION_FK) REFERENCES TB_INTERNATIONAL_STRINGS (ID)
;
ALTER TABLE TB_M_CONCEPTS ADD CONSTRAINT FK_TB_M_CONCEPTS_LEGAL_ACTS_FK
	FOREIGN KEY (LEGAL_ACTS_FK) REFERENCES TB_INTERNATIONAL_STRINGS (ID)
;
ALTER TABLE TB_M_CONCEPTS ADD CONSTRAINT FK_TB_M_CONCEPTS_EXTENDS_FK
	FOREIGN KEY (EXTENDS_FK) REFERENCES TB_CONCEPTS (ID)
;
ALTER TABLE TB_M_CONCEPTS ADD CONSTRAINT FK_TB_M_CONCEPTS_VARIABLE_FK
	FOREIGN KEY (VARIABLE_FK) REFERENCES TB_M_VARIABLES (ID)
;
ALTER TABLE TB_M_CONCEPTS ADD CONSTRAINT FK_TB_M_CONCEPTS_QUANTITY_FK
	FOREIGN KEY (QUANTITY_FK) REFERENCES TB_M_QUANTITIES (ID)
;

  
ALTER TABLE TB_M_CONCEPT_SCHEMES_VERSIONS ADD CONSTRAINT FK_TB_M_CONCEPT_SCHEMES_VERSIONS_RELATED_OPERATION_FK
	FOREIGN KEY (RELATED_OPERATION_FK) REFERENCES TB_EXTERNAL_ITEMS (ID)
;

  
ALTER TABLE TB_M_DATASTRUCTURE_VERSIONS ADD CONSTRAINT FK_TB_M_DATASTRUCTURE_VERSIONS_STATISTICAL_OPERATION_FK
	FOREIGN KEY (STATISTICAL_OPERATION_FK) REFERENCES TB_EXTERNAL_ITEMS (ID)
;

  
ALTER TABLE TB_M_DIMENSION_ORDERS ADD CONSTRAINT FK_TB_M_DIMENSION_ORDERS_DIM_COMPONENT_FK
	FOREIGN KEY (DIM_COMPONENT_FK) REFERENCES TB_ANNOTABLE_ARTEFACTS (ID)
;
ALTER TABLE TB_M_DIMENSION_ORDERS ADD CONSTRAINT FK_TB_M_DIMENSION_ORDERS_DSD_VERSION_HEADING_FK
	FOREIGN KEY (DSD_VERSION_HEADING_FK) REFERENCES TB_STRUCTURES_VERSIONS (ID)
;
ALTER TABLE TB_M_DIMENSION_ORDERS ADD CONSTRAINT FK_TB_M_DIMENSION_ORDERS_DSD_VERSION_STUB_FK
	FOREIGN KEY (DSD_VERSION_STUB_FK) REFERENCES TB_STRUCTURES_VERSIONS (ID)
;

  
ALTER TABLE TB_M_DIM_VIS_INFO ADD CONSTRAINT FK_TB_M_DIM_VIS_INFO_DIM_COMPONENT_FK
	FOREIGN KEY (DIM_COMPONENT_FK) REFERENCES TB_ANNOTABLE_ARTEFACTS (ID)
;
ALTER TABLE TB_M_DIM_VIS_INFO ADD CONSTRAINT FK_TB_M_DIM_VIS_INFO_DISPLAY_ORDER_FK
	FOREIGN KEY (DISPLAY_ORDER_FK) REFERENCES TB_M_CODELIST_ORDER_VIS (ID)
;
ALTER TABLE TB_M_DIM_VIS_INFO ADD CONSTRAINT FK_TB_M_DIM_VIS_INFO_HIERARCHY_LEVELS_OPEN_FK
	FOREIGN KEY (HIERARCHY_LEVELS_OPEN_FK) REFERENCES TB_M_CODELIST_OPENNESS_VIS (ID)
;
ALTER TABLE TB_M_DIM_VIS_INFO ADD CONSTRAINT FK_TB_M_DIM_VIS_INFO_DSD_VERSION_FK
	FOREIGN KEY (DSD_VERSION_FK) REFERENCES TB_STRUCTURES_VERSIONS (ID)
;

  
ALTER TABLE TB_M_MEASURE_DIM_PRECISIONS ADD CONSTRAINT FK_TB_M_MEASURE_DIM_PRECISIONS_M_CONCEPT_FK
	FOREIGN KEY (M_CONCEPT_FK) REFERENCES TB_CONCEPTS (ID)
;
ALTER TABLE TB_M_MEASURE_DIM_PRECISIONS ADD CONSTRAINT FK_TB_M_MEASURE_DIM_PRECISIONS_DSD_VERSION_FK
	FOREIGN KEY (DSD_VERSION_FK) REFERENCES TB_STRUCTURES_VERSIONS (ID)
;

  
  
  
  
  

ALTER TABLE TB_M_CONCEPT_RELATED ADD CONSTRAINT FK_TB_M_CONCEPT_RELATED_CONCEPT_RELATED_FK
	FOREIGN KEY (CONCEPT_RELATED_FK) REFERENCES TB_CONCEPTS (ID)
;
ALTER TABLE TB_M_CONCEPT_RELATED ADD CONSTRAINT FK_TB_M_CONCEPT_RELATED_CONCEPT_FK
	FOREIGN KEY (CONCEPT_FK) REFERENCES TB_CONCEPTS (ID)
;

  
ALTER TABLE TB_M_CONCEPT_ROLES ADD CONSTRAINT FK_TB_M_CONCEPT_ROLES_CONCEPT_ROLE_FK
	FOREIGN KEY (CONCEPT_ROLE_FK) REFERENCES TB_CONCEPTS (ID)
;
ALTER TABLE TB_M_CONCEPT_ROLES ADD CONSTRAINT FK_TB_M_CONCEPT_ROLES_CONCEPT_FK
	FOREIGN KEY (CONCEPT_FK) REFERENCES TB_CONCEPTS (ID)
;

  
ALTER TABLE TB_M_VAR_ELEM_OP_SOURCES ADD CONSTRAINT FK_TB_M_VAR_ELEM_OP_SOURCES_SOURCE_FK
	FOREIGN KEY (SOURCE_FK) REFERENCES TB_M_VARIABLE_ELEMENTS (ID)
;
ALTER TABLE TB_M_VAR_ELEM_OP_SOURCES ADD CONSTRAINT FK_TB_M_VAR_ELEM_OP_SOURCES_OPERATION_FK
	FOREIGN KEY (OPERATION_FK) REFERENCES TB_M_VAR_ELEM_OPERATIONS (ID)
;

  
ALTER TABLE TB_M_VAR_ELEM_OP_TARGETS ADD CONSTRAINT FK_TB_M_VAR_ELEM_OP_TARGETS_TARGET_FK
	FOREIGN KEY (TARGET_FK) REFERENCES TB_M_VARIABLE_ELEMENTS (ID)
;
ALTER TABLE TB_M_VAR_ELEM_OP_TARGETS ADD CONSTRAINT FK_TB_M_VAR_ELEM_OP_TARGETS_OPERATION_FK
	FOREIGN KEY (OPERATION_FK) REFERENCES TB_M_VAR_ELEM_OPERATIONS (ID)
;

  
ALTER TABLE TB_M_VAR_FAMILIES_VARIABLES ADD CONSTRAINT FK_TB_M_VAR_FAMILIES_VARIABLES_VARIABLE_FAMILY_FK
	FOREIGN KEY (VARIABLE_FAMILY_FK) REFERENCES TB_M_VARIABLE_FAMILIES (ID)
;
ALTER TABLE TB_M_VAR_FAMILIES_VARIABLES ADD CONSTRAINT FK_TB_M_VAR_FAMILIES_VARIABLES_VARIABLE_FK
	FOREIGN KEY (VARIABLE_FK) REFERENCES TB_M_VARIABLES (ID)
;

  

    


    