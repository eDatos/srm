-- ###########################################
-- # Create
-- ###########################################
-- Create pk sequence
    


-- Create normal entities
    
CREATE TABLE TB_M_LIS_CONCEPT_TYPES (
  ID NUMBER(19) NOT NULL,
  IDENTIFIER VARCHAR2(255) NOT NULL,
  DESCRIPTION_FK NUMBER(19)
);


CREATE TABLE TB_M_CONCEPTS (
  UPDATE_DATE_METAMAC_TZ VARCHAR2(50),
  UPDATE_DATE_METAMAC TIMESTAMP,
  PLURAL_NAME_FK NUMBER(19),
  ACRONYM_FK NUMBER(19),
  DESCRIPTION_SOURCE_FK NUMBER(19),
  CONTEXT_FK NUMBER(19),
  DOC_METHOD_FK NUMBER(19),
  CONCEPT_TYPE_FK NUMBER(19),
  DERIVATION_FK NUMBER(19),
  LEGAL_ACTS_FK NUMBER(19),
  EXTENDS_FK NUMBER(19),
  SDMX_RELATED_ARTEFACT VARCHAR2(40),
  TB_CONCEPTS NUMBER(19) NOT NULL

);


CREATE TABLE TB_M_CONCEPT_SCHEMES_VERSIONS (
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
  UPDATE_DATE_METAMAC_TZ VARCHAR2(50),
  UPDATE_DATE_METAMAC TIMESTAMP,
  RELATED_OPERATION_FK NUMBER(19),
  PROC_STATUS VARCHAR2(40) NOT NULL,
  CONCEPT_SCHEME_TYPE VARCHAR2(40) NOT NULL,
  TB_CONCEPT_SCHEMES_VERSIONS NUMBER(19) NOT NULL

);



-- Create many to many relations
    

-- Primary keys
    
ALTER TABLE TB_M_LIS_CONCEPT_TYPES ADD CONSTRAINT PK_TB_M_LIS_CONCEPT_TYPES
	PRIMARY KEY (ID)
;

    

-- Unique constraints
    






-- Foreign key constraints
ALTER TABLE TB_M_CONCEPTS ADD CONSTRAINT FK_TB_M_CONCEPTS_TB_CONCEPTS
	FOREIGN KEY (TB_CONCEPTS) REFERENCES TB_ITEMS (ID)
;
ALTER TABLE TB_M_CONCEPT_SCHEMES_VERSIONS ADD CONSTRAINT FK_TB_M_CONCEPT_SCHEMES_VERS79
	FOREIGN KEY (TB_CONCEPT_SCHEMES_VERSIONS) REFERENCES TB_ITEM_SCHEMES_VERSIONS (ID)
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

  
ALTER TABLE TB_M_CONCEPT_SCHEMES_VERSIONS ADD CONSTRAINT FK_TB_M_CONCEPT_SCHEMES_VERS66
	FOREIGN KEY (RELATED_OPERATION_FK) REFERENCES TB_EXTERNAL_ITEMS (ID)
;

  

    

-- Index


