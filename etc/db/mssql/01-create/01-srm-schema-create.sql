-- ###########################################
-- # Create
-- ###########################################

-- Create normal entities
    
CREATE TABLE TB_M_CATEGORIES (
  TB_CATEGORIES BIGINT NOT NULL

);


CREATE TABLE TB_M_CAT_SCHEMES_VERSIONS (
  PRODUCTION_VALIDATION_DATE_TZ VARCHAR(50),
  PRODUCTION_VALIDATION_DATE DATETIME,
  PRODUCTION_VALIDATION_USER VARCHAR(255),
  DIFFUSION_VALIDATION_DATE_TZ VARCHAR(50),
  DIFFUSION_VALIDATION_DATE DATETIME,
  DIFFUSION_VALIDATION_USER VARCHAR(255),
  INTERNAL_PUBLICATION_DATE_TZ VARCHAR(50),
  INTERNAL_PUBLICATION_DATE DATETIME,
  INTERNAL_PUBLICATION_USER VARCHAR(255),
  EXTERNAL_PUBLICATION_DATE_TZ VARCHAR(50),
  EXTERNAL_PUBLICATION_DATE DATETIME,
  EXTERNAL_PUBLICATION_USER VARCHAR(255),
  IS_EXT_PUBLICATION_FAILED BIT,
  EXT_PUBLICATION_FAILED_DATE_TZ VARCHAR(50),
  EXT_PUBLICATION_FAILED_DATE DATETIME,
  PROC_STATUS VARCHAR(40) NOT NULL,
  TB_CAT_SCHEMES_VERSIONS BIGINT NOT NULL

);


CREATE TABLE TB_M_CODES (
  TB_CODES BIGINT NOT NULL

);


CREATE TABLE TB_M_CODELIST_FAMILIES (
  ID BIGINT NOT NULL,
  IDENTIFIER VARCHAR(255),
  UUID VARCHAR(36) NOT NULL,
  CREATED_DATE_TZ VARCHAR(50),
  CREATED_DATE DATETIME,
  CREATED_BY VARCHAR(50),
  LAST_UPDATED_TZ VARCHAR(50),
  LAST_UPDATED DATETIME,
  LAST_UPDATED_BY VARCHAR(50),
  VERSION BIGINT NOT NULL,
  NAME_FK BIGINT
);


CREATE TABLE TB_M_CODELISTS_VERSIONS (
  IS_RECOMMENDED BIT,
  SHORT_NAME_FK BIGINT,
  CODELIST_FAMILY_FK BIGINT,
  PRODUCTION_VALIDATION_DATE_TZ VARCHAR(50),
  PRODUCTION_VALIDATION_DATE DATETIME,
  PRODUCTION_VALIDATION_USER VARCHAR(255),
  DIFFUSION_VALIDATION_DATE_TZ VARCHAR(50),
  DIFFUSION_VALIDATION_DATE DATETIME,
  DIFFUSION_VALIDATION_USER VARCHAR(255),
  INTERNAL_PUBLICATION_DATE_TZ VARCHAR(50),
  INTERNAL_PUBLICATION_DATE DATETIME,
  INTERNAL_PUBLICATION_USER VARCHAR(255),
  EXTERNAL_PUBLICATION_DATE_TZ VARCHAR(50),
  EXTERNAL_PUBLICATION_DATE DATETIME,
  EXTERNAL_PUBLICATION_USER VARCHAR(255),
  IS_EXT_PUBLICATION_FAILED BIT,
  EXT_PUBLICATION_FAILED_DATE_TZ VARCHAR(50),
  EXT_PUBLICATION_FAILED_DATE DATETIME,
  PROC_STATUS VARCHAR(40) NOT NULL,
  ACCESS_TYPE VARCHAR(40),
  TB_CODELISTS_VERSIONS BIGINT NOT NULL

);


CREATE TABLE TB_M_VARIABLE_FAMILIES (
  ID BIGINT NOT NULL,
  IDENTIFIER VARCHAR(255),
  UUID VARCHAR(36) NOT NULL,
  CREATED_DATE_TZ VARCHAR(50),
  CREATED_DATE DATETIME,
  CREATED_BY VARCHAR(50),
  LAST_UPDATED_TZ VARCHAR(50),
  LAST_UPDATED DATETIME,
  LAST_UPDATED_BY VARCHAR(50),
  VERSION BIGINT NOT NULL,
  NAME_FK BIGINT
);


CREATE TABLE TB_M_VARIABLES (
  ID BIGINT NOT NULL,
  IDENTIFIER VARCHAR(255),
  VALID_FROM_TZ VARCHAR(50),
  VALID_FROM DATETIME,
  VALID_TO_TZ VARCHAR(50),
  VALID_TO DATETIME,
  UUID VARCHAR(36) NOT NULL,
  CREATED_DATE_TZ VARCHAR(50),
  CREATED_DATE DATETIME,
  CREATED_BY VARCHAR(50),
  LAST_UPDATED_TZ VARCHAR(50),
  LAST_UPDATED DATETIME,
  LAST_UPDATED_BY VARCHAR(50),
  VERSION BIGINT NOT NULL,
  NAME_FK BIGINT,
  SHORT_NAME_FK BIGINT,
  VARIABLE_FAMILY_FK BIGINT
);


CREATE TABLE TB_M_LIS_CONCEPT_TYPES (
  ID BIGINT NOT NULL,
  IDENTIFIER VARCHAR(255) NOT NULL,
  DESCRIPTION_FK BIGINT
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
  SDMX_RELATED_ARTEFACT VARCHAR(40),
  TB_CONCEPTS BIGINT NOT NULL

);


CREATE TABLE TB_M_CONCEPT_SCHEMES_VERSIONS (
  RELATED_OPERATION_FK BIGINT,
  PRODUCTION_VALIDATION_DATE_TZ VARCHAR(50),
  PRODUCTION_VALIDATION_DATE DATETIME,
  PRODUCTION_VALIDATION_USER VARCHAR(255),
  DIFFUSION_VALIDATION_DATE_TZ VARCHAR(50),
  DIFFUSION_VALIDATION_DATE DATETIME,
  DIFFUSION_VALIDATION_USER VARCHAR(255),
  INTERNAL_PUBLICATION_DATE_TZ VARCHAR(50),
  INTERNAL_PUBLICATION_DATE DATETIME,
  INTERNAL_PUBLICATION_USER VARCHAR(255),
  EXTERNAL_PUBLICATION_DATE_TZ VARCHAR(50),
  EXTERNAL_PUBLICATION_DATE DATETIME,
  EXTERNAL_PUBLICATION_USER VARCHAR(255),
  IS_EXT_PUBLICATION_FAILED BIT,
  EXT_PUBLICATION_FAILED_DATE_TZ VARCHAR(50),
  EXT_PUBLICATION_FAILED_DATE DATETIME,
  PROC_STATUS VARCHAR(40) NOT NULL,
  CONCEPT_SCHEME_TYPE VARCHAR(40) NOT NULL,
  TB_CONCEPT_SCHEMES_VERSIONS BIGINT NOT NULL

);


CREATE TABLE TB_M_DATASTRUCTURE_VERSIONS (
  PRODUCTION_VALIDATION_DATE_TZ VARCHAR(50),
  PRODUCTION_VALIDATION_DATE DATETIME,
  PRODUCTION_VALIDATION_USER VARCHAR(255),
  DIFFUSION_VALIDATION_DATE_TZ VARCHAR(50),
  DIFFUSION_VALIDATION_DATE DATETIME,
  DIFFUSION_VALIDATION_USER VARCHAR(255),
  INTERNAL_PUBLICATION_DATE_TZ VARCHAR(50),
  INTERNAL_PUBLICATION_DATE DATETIME,
  INTERNAL_PUBLICATION_USER VARCHAR(255),
  EXTERNAL_PUBLICATION_DATE_TZ VARCHAR(50),
  EXTERNAL_PUBLICATION_DATE DATETIME,
  EXTERNAL_PUBLICATION_USER VARCHAR(255),
  IS_EXT_PUBLICATION_FAILED BIT,
  EXT_PUBLICATION_FAILED_DATE_TZ VARCHAR(50),
  EXT_PUBLICATION_FAILED_DATE DATETIME,
  PROC_STATUS VARCHAR(40) NOT NULL,
  TB_DATASTRUCTURE_VERSIONS BIGINT NOT NULL

);


CREATE TABLE TB_M_ORGANISATIONS (
  TB_ORGANISATIONS BIGINT NOT NULL

);


CREATE TABLE TB_M_ORG_SCHEMES_VERSIONS (
  PRODUCTION_VALIDATION_DATE_TZ VARCHAR(50),
  PRODUCTION_VALIDATION_DATE DATETIME,
  PRODUCTION_VALIDATION_USER VARCHAR(255),
  DIFFUSION_VALIDATION_DATE_TZ VARCHAR(50),
  DIFFUSION_VALIDATION_DATE DATETIME,
  DIFFUSION_VALIDATION_USER VARCHAR(255),
  INTERNAL_PUBLICATION_DATE_TZ VARCHAR(50),
  INTERNAL_PUBLICATION_DATE DATETIME,
  INTERNAL_PUBLICATION_USER VARCHAR(255),
  EXTERNAL_PUBLICATION_DATE_TZ VARCHAR(50),
  EXTERNAL_PUBLICATION_DATE DATETIME,
  EXTERNAL_PUBLICATION_USER VARCHAR(255),
  IS_EXT_PUBLICATION_FAILED BIT,
  EXT_PUBLICATION_FAILED_DATE_TZ VARCHAR(50),
  EXT_PUBLICATION_FAILED_DATE DATETIME,
  PROC_STATUS VARCHAR(40) NOT NULL,
  TB_ORG_SCHEMES_VERSIONS BIGINT NOT NULL

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



-- Primary keys
    
ALTER TABLE TB_M_CODELIST_FAMILIES ADD CONSTRAINT PK_TB_M_CODELIST_FAMILIES
	PRIMARY KEY (ID)
;

ALTER TABLE TB_M_VARIABLE_FAMILIES ADD CONSTRAINT PK_TB_M_VARIABLE_FAMILIES
	PRIMARY KEY (ID)
;

ALTER TABLE TB_M_VARIABLES ADD CONSTRAINT PK_TB_M_VARIABLES
	PRIMARY KEY (ID)
;

ALTER TABLE TB_M_LIS_CONCEPT_TYPES ADD CONSTRAINT PK_TB_M_LIS_CONCEPT_TYPES
	PRIMARY KEY (ID)
;

    
ALTER TABLE TB_M_CONCEPT_RELATED ADD CONSTRAINT PK_TB_M_CONCEPT_RELATED
	PRIMARY KEY (CONCEPT_RELATED_FK, CONCEPT_FK)
;

ALTER TABLE TB_M_CONCEPT_ROLES ADD CONSTRAINT PK_TB_M_CONCEPT_ROLES
	PRIMARY KEY (CONCEPT_ROLE_FK, CONCEPT_FK)
;


-- Unique constraints




ALTER TABLE TB_M_CODELIST_FAMILIES
    ADD CONSTRAINT UQ_TB_M_CODELIST_FAMILIES UNIQUE (UUID)
;



ALTER TABLE TB_M_VARIABLE_FAMILIES
    ADD CONSTRAINT UQ_TB_M_VARIABLE_FAMILIES UNIQUE (UUID)
;


ALTER TABLE TB_M_VARIABLES
    ADD CONSTRAINT UQ_TB_M_VARIABLES UNIQUE (UUID)
;


ALTER TABLE TB_M_LIS_CONCEPT_TYPES
	ADD CONSTRAINT UQ_TB_M_LIS_CONCEPT_TYPES UNIQUE (ID)
;








-- Foreign key constraints
ALTER TABLE TB_M_CATEGORIES ADD CONSTRAINT FK_TB_M_CATEGORIES_TB_CATEGORIES
	FOREIGN KEY (TB_CATEGORIES) REFERENCES TB_ITEMS (ID)
;
ALTER TABLE TB_M_CAT_SCHEMES_VERSIONS ADD CONSTRAINT FK_TB_M_CAT_SCHEMES_VERSIONS_TB_CAT_SCHEMES_VERSIONS
	FOREIGN KEY (TB_CAT_SCHEMES_VERSIONS) REFERENCES TB_ITEM_SCHEMES_VERSIONS (ID)
;
ALTER TABLE TB_M_CODES ADD CONSTRAINT FK_TB_M_CODES_TB_CODES
	FOREIGN KEY (TB_CODES) REFERENCES TB_ITEMS (ID)
;
ALTER TABLE TB_M_CODELISTS_VERSIONS ADD CONSTRAINT FK_TB_M_CODELISTS_VERSIONS_TB_CODELISTS_VERSIONS
	FOREIGN KEY (TB_CODELISTS_VERSIONS) REFERENCES TB_ITEM_SCHEMES_VERSIONS (ID)
;
ALTER TABLE TB_M_CONCEPTS ADD CONSTRAINT FK_TB_M_CONCEPTS_TB_CONCEPTS
	FOREIGN KEY (TB_CONCEPTS) REFERENCES TB_ITEMS (ID)
;
ALTER TABLE TB_M_CONCEPT_SCHEMES_VERSIONS ADD CONSTRAINT FK_TB_M_CONCEPT_SCHEMES_VERSIONS_TB_CONCEPT_SCHEMES_VERSIONS
	FOREIGN KEY (TB_CONCEPT_SCHEMES_VERSIONS) REFERENCES TB_ITEM_SCHEMES_VERSIONS (ID)
;
ALTER TABLE TB_M_DATASTRUCTURE_VERSIONS ADD CONSTRAINT FK_TB_M_DATASTRUCTURE_VERSIONS_TB_DATASTRUCTURE_VERSIONS
	FOREIGN KEY (TB_DATASTRUCTURE_VERSIONS) REFERENCES TB_STRUCTURES_VERSIONS (ID)
;
ALTER TABLE TB_M_ORGANISATIONS ADD CONSTRAINT FK_TB_M_ORGANISATIONS_TB_ORGANISATIONS
	FOREIGN KEY (TB_ORGANISATIONS) REFERENCES TB_ITEMS (ID)
;
ALTER TABLE TB_M_ORG_SCHEMES_VERSIONS ADD CONSTRAINT FK_TB_M_ORG_SCHEMES_VERSIONS_TB_ORG_SCHEMES_VERSIONS
	FOREIGN KEY (TB_ORG_SCHEMES_VERSIONS) REFERENCES TB_ITEM_SCHEMES_VERSIONS (ID)
;


  
  
  
  
  
  
ALTER TABLE TB_M_CODELIST_FAMILIES ADD CONSTRAINT FK_TB_M_CODELIST_FAMILIES_NAME_FK
	FOREIGN KEY (NAME_FK) REFERENCES TB_INTERNATIONAL_STRINGS (ID)
;

  
ALTER TABLE TB_M_CODELISTS_VERSIONS ADD CONSTRAINT FK_TB_M_CODELISTS_VERSIONS_SHORT_NAME_FK
	FOREIGN KEY (SHORT_NAME_FK) REFERENCES TB_INTERNATIONAL_STRINGS (ID)
;
ALTER TABLE TB_M_CODELISTS_VERSIONS ADD CONSTRAINT FK_TB_M_CODELISTS_VERSIONS_CODELIST_FAMILY_FK
	FOREIGN KEY (CODELIST_FAMILY_FK) REFERENCES TB_M_CODELIST_FAMILIES (ID)
;

  
ALTER TABLE TB_M_VARIABLE_FAMILIES ADD CONSTRAINT FK_TB_M_VARIABLE_FAMILIES_NAME_FK
	FOREIGN KEY (NAME_FK) REFERENCES TB_INTERNATIONAL_STRINGS (ID)
;

  
ALTER TABLE TB_M_VARIABLES ADD CONSTRAINT FK_TB_M_VARIABLES_NAME_FK
	FOREIGN KEY (NAME_FK) REFERENCES TB_INTERNATIONAL_STRINGS (ID)
;
ALTER TABLE TB_M_VARIABLES ADD CONSTRAINT FK_TB_M_VARIABLES_SHORT_NAME_FK
	FOREIGN KEY (SHORT_NAME_FK) REFERENCES TB_INTERNATIONAL_STRINGS (ID)
;
ALTER TABLE TB_M_VARIABLES ADD CONSTRAINT FK_TB_M_VARIABLES_VARIABLE_FAMILY_FK
	FOREIGN KEY (VARIABLE_FAMILY_FK) REFERENCES TB_M_VARIABLE_FAMILIES (ID)
;

  
ALTER TABLE TB_M_LIS_CONCEPT_TYPES ADD CONSTRAINT FK_TB_M_LIS_CONCEPT_TYPES_DESCRIPTION_FK
	FOREIGN KEY (DESCRIPTION_FK) REFERENCES TB_INTERNATIONAL_STRINGS (ID)
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
	FOREIGN KEY (EXTENDS_FK) REFERENCES TB_ITEMS (ID)
;

  
ALTER TABLE TB_M_CONCEPT_SCHEMES_VERSIONS ADD CONSTRAINT FK_TB_M_CONCEPT_SCHEMES_VERSIONS_RELATED_OPERATION_FK
	FOREIGN KEY (RELATED_OPERATION_FK) REFERENCES TB_EXTERNAL_ITEMS (ID)
;

  
  
  
  
  
  
  

ALTER TABLE TB_M_CONCEPT_RELATED ADD CONSTRAINT FK_TB_M_CONCEPT_RELATED_CONCEPT_RELATED_FK
	FOREIGN KEY (CONCEPT_RELATED_FK) REFERENCES TB_ITEMS (ID)
;
ALTER TABLE TB_M_CONCEPT_RELATED ADD CONSTRAINT FK_TB_M_CONCEPT_RELATED_CONCEPT_FK
	FOREIGN KEY (CONCEPT_FK) REFERENCES TB_ITEMS (ID)
;

  
ALTER TABLE TB_M_CONCEPT_ROLES ADD CONSTRAINT FK_TB_M_CONCEPT_ROLES_CONCEPT_ROLE_FK
	FOREIGN KEY (CONCEPT_ROLE_FK) REFERENCES TB_ITEMS (ID)
;
ALTER TABLE TB_M_CONCEPT_ROLES ADD CONSTRAINT FK_TB_M_CONCEPT_ROLES_CONCEPT_FK
	FOREIGN KEY (CONCEPT_FK) REFERENCES TB_ITEMS (ID)
;

  



