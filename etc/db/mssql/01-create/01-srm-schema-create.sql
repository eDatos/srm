-- ###########################################
-- # Create
-- ###########################################

-- Create normal entities
    
CREATE TABLE TB_INTERNATIONAL_STRINGS (
  ID BIGINT NOT NULL,
  VERSION BIGINT NOT NULL
);


CREATE TABLE TB_EXTERNAL_ITEMS (
  ID BIGINT NOT NULL,
  CODE VARCHAR(255) NOT NULL,
  URI VARCHAR(4000) NOT NULL,
  URN VARCHAR(4000) NOT NULL,
  MANAGEMENT_APP_URL VARCHAR(4000),
  VERSION BIGINT NOT NULL,
  TITLE_FK BIGINT,
  TYPE VARCHAR(255) NOT NULL
);


CREATE TABLE TB_LOCALISED_STRINGS (
  ID BIGINT NOT NULL,
  LABEL VARCHAR(4000) NOT NULL,
  LOCALE VARCHAR(255) NOT NULL,
  IS_UNMODIFIABLE BIT,
  VERSION BIGINT NOT NULL,
  INTERNATIONAL_STRING_FK BIGINT NOT NULL
);


CREATE TABLE TB_ANNOTABLE_ARTEFACTS (
  ID BIGINT NOT NULL,
  UUID VARCHAR(36) NOT NULL,
  CREATED_DATE_TZ VARCHAR(50),
  CREATED_DATE DATETIME,
  CREATED_BY VARCHAR(50),
  LAST_UPDATED_TZ VARCHAR(50),
  LAST_UPDATED DATETIME,
  LAST_UPDATED_BY VARCHAR(50),
  VERSION BIGINT NOT NULL,
  ANNOTABLE_ARTEFACT_TYPE VARCHAR(31) NOT NULL,
  CODE VARCHAR(255),
  CODE_FULL VARCHAR(4000),
  URI_PROVIDER VARCHAR(255),
  URN VARCHAR(255),
  URN_PROVIDER VARCHAR(255),
  UPDATE_DATE_TZ VARCHAR(50),
  UPDATE_DATE DATETIME,
  CONCEPT_IDENTITY_FK BIGINT,
  REPRESENTATION_FK BIGINT,
  NAME_FK BIGINT,
  DESCRIPTION_FK BIGINT,
  COMMENT_FK BIGINT,
  RELATE_TO BIGINT,
  SPECIAL_ATTRIBUTE_TYPE VARCHAR(255),
  USAGE_STATUS VARCHAR(255),
  ORDER_LOGIC INT,
  SPECIAL_DIMENSION_TYPE VARCHAR(255),
  IS_ATTACHMENT_CONSTRAINT BIT,
  VERSION_LOGIC VARCHAR(255),
  VALID_FROM_TZ VARCHAR(50),
  VALID_FROM DATETIME,
  VALID_TO_TZ VARCHAR(50),
  VALID_TO DATETIME,
  FINAL_LOGIC BIT,
  FINAL_LOGIC_CLIENT BIT,
  LATEST_FINAL BIT,
  PUBLIC_LOGIC BIT,
  LATEST_PUBLIC BIT,
  IS_EXTERNAL_REFERENCE BIT,
  STRUCTURE_URL VARCHAR(255),
  SERVICE_URL VARCHAR(255),
  IS_LAST_VERSION BIT,
  REPLACED_BY_VERSION VARCHAR(255),
  REPLACE_TO_VERSION VARCHAR(255),
  IS_IMPORTED BIT,
  MAINTAINER_FK BIGINT
);


CREATE TABLE TB_ANNOTATIONS (
  ID BIGINT NOT NULL,
  CODE VARCHAR(255),
  TITLE VARCHAR(255),
  TYPE VARCHAR(255),
  URL VARCHAR(255),
  UUID VARCHAR(36) NOT NULL,
  VERSION BIGINT NOT NULL,
  TEXT_FK BIGINT,
  ANNOTABLE_ARTEFACT_FK BIGINT
);


CREATE TABLE TB_ITEM_SCHEMES (
  ID BIGINT NOT NULL,
  UUID VARCHAR(36) NOT NULL,
  VERSION BIGINT NOT NULL,
  VERSION_PATTERN VARCHAR(255) NOT NULL
);


CREATE TABLE TB_ORGANISATIONS (
  ID_AS_MAINTAINER VARCHAR(255),
  ORGANISATION_TYPE VARCHAR(255) NOT NULL,
  TB_ITEMS BIGINT NOT NULL

);


CREATE TABLE TB_ITEM_SCHEMES_VERSIONS (
  ID BIGINT NOT NULL,
  IS_PARTIAL BIT,
  UPDATE_DATE_TZ VARCHAR(50),
  UPDATE_DATE DATETIME,
  UUID VARCHAR(36) NOT NULL,
  CREATED_DATE_TZ VARCHAR(50),
  CREATED_DATE DATETIME,
  CREATED_BY VARCHAR(50),
  LAST_UPDATED_TZ VARCHAR(50),
  LAST_UPDATED DATETIME,
  LAST_UPDATED_BY VARCHAR(50),
  VERSION BIGINT NOT NULL,
  ITEM_SCHEME_FK BIGINT NOT NULL,
  MAINTANABLE_ARTEFACT_FK BIGINT
);


CREATE TABLE TB_ITEMS (
  ID BIGINT NOT NULL,
  UPDATE_DATE_TZ VARCHAR(50),
  UPDATE_DATE DATETIME,
  UUID VARCHAR(36) NOT NULL,
  CREATED_DATE_TZ VARCHAR(50),
  CREATED_DATE DATETIME,
  CREATED_BY VARCHAR(50),
  LAST_UPDATED_TZ VARCHAR(50),
  LAST_UPDATED DATETIME,
  LAST_UPDATED_BY VARCHAR(50),
  VERSION BIGINT NOT NULL,
  NAMEABLE_ARTEFACT_FK BIGINT NOT NULL,
  PARENT_FK BIGINT,
  ITEM_SCHEME_VERSION_FK BIGINT NOT NULL,
  ITEM_SCHEME_VERSION_FIRST_FK BIGINT
);


CREATE TABLE TB_REPRESENTATIONS (
  ID BIGINT NOT NULL,
  UUID VARCHAR(36) NOT NULL,
  VERSION BIGINT NOT NULL,
  REPRESENTATION_TYPE VARCHAR(31) NOT NULL,
  CONCEPTSCHEME_FK BIGINT,
  CODELIST_FK BIGINT,
  FACET_FK BIGINT
);


CREATE TABLE TB_CONCEPTS (
  CORE_REPRESENTATION_FK BIGINT,
  TB_ITEMS BIGINT NOT NULL

);


CREATE TABLE TB_CONCEPT_SCHEMES_VERSIONS (
  TB_ITEM_SCHEMES_VERSIONS BIGINT NOT NULL

);


CREATE TABLE TB_CODELISTS_VERSIONS (
  TB_ITEM_SCHEMES_VERSIONS BIGINT NOT NULL

);


CREATE TABLE TB_FACETS (
  ID BIGINT NOT NULL,
  IS_SEQUENCE_FT VARCHAR(255),
  INTERVAL_FT VARCHAR(255),
  START_VALUE_FT VARCHAR(255),
  END_VALUE_FT VARCHAR(255),
  TIME_INTERVAL_FT VARCHAR(14),
  START_TIME_FT VARCHAR(255),
  END_TIME_FT VARCHAR(255),
  MIN_LENGTH_FT VARCHAR(255),
  MAX_LENGTH_FT VARCHAR(255),
  MIN_VALUE_FT VARCHAR(255),
  MAX_VALUE_FT VARCHAR(255),
  DECIMALS_FT VARCHAR(255),
  PATTERN_FT VARCHAR(255),
  XHTML_EFT VARCHAR(255),
  IS_MULTI_LINGUAL VARCHAR(255),
  UUID VARCHAR(36) NOT NULL,
  VERSION BIGINT NOT NULL,
  FACET_VALUE VARCHAR(255) NOT NULL
);


CREATE TABLE TB_STRUCTURES (
  ID BIGINT NOT NULL,
  UUID VARCHAR(36) NOT NULL,
  VERSION BIGINT NOT NULL,
  VERSION_PATTERN VARCHAR(255) NOT NULL
);


CREATE TABLE TB_STRUCTURES_VERSIONS (
  ID BIGINT NOT NULL,
  UPDATE_DATE_TZ VARCHAR(50),
  UPDATE_DATE DATETIME,
  UUID VARCHAR(36) NOT NULL,
  CREATED_DATE_TZ VARCHAR(50),
  CREATED_DATE DATETIME,
  CREATED_BY VARCHAR(50),
  LAST_UPDATED_TZ VARCHAR(50),
  LAST_UPDATED DATETIME,
  LAST_UPDATED_BY VARCHAR(50),
  VERSION BIGINT NOT NULL,
  MAINTANABLE_ARTEFACT_FK BIGINT,
  STRUCTURE_FK BIGINT NOT NULL
);


CREATE TABLE TB_CATEGORIES (
  TB_ITEMS BIGINT NOT NULL

);


CREATE TABLE TB_CATEGORISATIONS (
  ID BIGINT NOT NULL,
  UUID VARCHAR(36) NOT NULL,
  CREATED_DATE_TZ VARCHAR(50),
  CREATED_DATE DATETIME,
  CREATED_BY VARCHAR(50),
  LAST_UPDATED_TZ VARCHAR(50),
  LAST_UPDATED DATETIME,
  LAST_UPDATED_BY VARCHAR(50),
  VERSION BIGINT NOT NULL,
  MAINTANABLE_ARTEFACT_FK BIGINT NOT NULL,
  CATEGORY_FK BIGINT NOT NULL,
  ARTEFACT_CATEGORISED_FK BIGINT NOT NULL
);


CREATE TABLE TB_CAT_SCHEMES_VERSIONS (
  TB_ITEM_SCHEMES_VERSIONS BIGINT NOT NULL

);


CREATE TABLE TB_CODES (
  TB_ITEMS BIGINT NOT NULL

);


CREATE TABLE TB_IMPORT_DATAS (
  ID BIGINT NOT NULL,
  JOB VARCHAR(255) NOT NULL,
  FILE_NAME VARCHAR(255) NOT NULL,
  CREATED_DATE_TZ VARCHAR(50),
  CREATED_DATE DATETIME,
  CREATED_BY VARCHAR(50),
  LAST_UPDATED_TZ VARCHAR(50),
  LAST_UPDATED DATETIME,
  LAST_UPDATED_BY VARCHAR(50),
  VERSION BIGINT NOT NULL,
  STATUS VARCHAR(255) NOT NULL
);


CREATE TABLE TB_EXCEPTION_IMPORT (
  ID BIGINT NOT NULL,
  ERROR_CODE VARCHAR(255),
  EXCEPTION_INFO VARCHAR(4000),
  PRINCIPAL BIT NOT NULL,
  UUID VARCHAR(36) NOT NULL,
  VERSION BIGINT NOT NULL,
  IMPORT_DATA_FK BIGINT NOT NULL
);


CREATE TABLE TB_CONTACTS (
  ID BIGINT NOT NULL,
  CODE VARCHAR(255),
  UUID VARCHAR(36) NOT NULL,
  CREATED_DATE_TZ VARCHAR(50),
  CREATED_DATE DATETIME,
  CREATED_BY VARCHAR(50),
  LAST_UPDATED_TZ VARCHAR(50),
  LAST_UPDATED DATETIME,
  LAST_UPDATED_BY VARCHAR(50),
  VERSION BIGINT NOT NULL,
  NAME_FK BIGINT,
  ORGANISATION_UNIT_FK BIGINT,
  RESPONSIBILITY_FK BIGINT,
  ORGANISATION_FK BIGINT NOT NULL
);


CREATE TABLE TB_CONTACT_ITEMS (
  ID BIGINT NOT NULL,
  ITEM_VALUE VARCHAR(255) NOT NULL,
  UUID VARCHAR(36) NOT NULL,
  VERSION BIGINT NOT NULL,
  CONTACT_FK BIGINT NOT NULL,
  ITEM_TYPE VARCHAR(255) NOT NULL
);


CREATE TABLE TB_ORG_SCHEMES_VERSIONS (
  ORGANISATION_SCHEME_TYPE VARCHAR(255) NOT NULL,
  TB_ITEM_SCHEMES_VERSIONS BIGINT NOT NULL

);


CREATE TABLE TB_ATTR_RELATIONSHIPS (
  ID BIGINT NOT NULL,
  UUID VARCHAR(36) NOT NULL,
  VERSION BIGINT NOT NULL,
  ATTR_RELATIONSHIPS_TYPE VARCHAR(31) NOT NULL,
  GROUP_KEY BIGINT
);


CREATE TABLE TB_DATASTRUCTURE_VERSIONS (
  TB_STRUCTURES_VERSIONS BIGINT NOT NULL

);



-- Create many to many relations
    
CREATE TABLE TB_COMPLIST_COMP (
  COMPONENT_FK BIGINT NOT NULL,
  COMPONENTLIST_FK BIGINT NOT NULL
);


CREATE TABLE TB_DATA_ATTRIBUTES_EI_ROLES (
  ROLE_FK BIGINT NOT NULL,
  TB_DATA_ATTRIBUTES BIGINT NOT NULL
);


CREATE TABLE TB_DIMENSIONS_EI_ROLES (
  ROLE_FK BIGINT NOT NULL,
  TB_DIMENSIONS BIGINT NOT NULL
);


CREATE TABLE TB_DIMREL_DIMCOM (
  DIMENSION_COMPONENT_FK BIGINT NOT NULL,
  DIMENSION_RELATIONSHIP_FK BIGINT NOT NULL
);


CREATE TABLE TB_DIMREL_GROUPDIM (
  GROUPDIMENSION_DESCRIPTOR_FK BIGINT NOT NULL,
  DIMENSION_RELATIONSHIP_FK BIGINT NOT NULL
);


CREATE TABLE TB_MEASURE_DIMEN_EI_ROLES (
  ROLE_FK BIGINT NOT NULL,
  TB_MEASURE_DIMENSIONS BIGINT NOT NULL
);


CREATE TABLE TB_STRVERSION_COMPLIST (
  STRUCTUREVERSION_FK BIGINT NOT NULL,
  COMPONENT_LIST_FK BIGINT NOT NULL
);



-- Primary keys
    
ALTER TABLE TB_INTERNATIONAL_STRINGS ADD CONSTRAINT PK_TB_INTERNATIONAL_STRINGS
	PRIMARY KEY (ID)
;

ALTER TABLE TB_EXTERNAL_ITEMS ADD CONSTRAINT PK_TB_EXTERNAL_ITEMS
	PRIMARY KEY (ID)
;

ALTER TABLE TB_LOCALISED_STRINGS ADD CONSTRAINT PK_TB_LOCALISED_STRINGS
	PRIMARY KEY (ID)
;

ALTER TABLE TB_ANNOTABLE_ARTEFACTS ADD CONSTRAINT PK_TB_ANNOTABLE_ARTEFACTS
	PRIMARY KEY (ID)
;

ALTER TABLE TB_ANNOTATIONS ADD CONSTRAINT PK_TB_ANNOTATIONS
	PRIMARY KEY (ID)
;

ALTER TABLE TB_ITEM_SCHEMES ADD CONSTRAINT PK_TB_ITEM_SCHEMES
	PRIMARY KEY (ID)
;

ALTER TABLE TB_ITEM_SCHEMES_VERSIONS ADD CONSTRAINT PK_TB_ITEM_SCHEMES_VERSIONS
	PRIMARY KEY (ID)
;

ALTER TABLE TB_ITEMS ADD CONSTRAINT PK_TB_ITEMS
	PRIMARY KEY (ID)
;

ALTER TABLE TB_REPRESENTATIONS ADD CONSTRAINT PK_TB_REPRESENTATIONS
	PRIMARY KEY (ID)
;

ALTER TABLE TB_FACETS ADD CONSTRAINT PK_TB_FACETS
	PRIMARY KEY (ID)
;

ALTER TABLE TB_STRUCTURES ADD CONSTRAINT PK_TB_STRUCTURES
	PRIMARY KEY (ID)
;

ALTER TABLE TB_STRUCTURES_VERSIONS ADD CONSTRAINT PK_TB_STRUCTURES_VERSIONS
	PRIMARY KEY (ID)
;

ALTER TABLE TB_CATEGORISATIONS ADD CONSTRAINT PK_TB_CATEGORISATIONS
	PRIMARY KEY (ID)
;

ALTER TABLE TB_IMPORT_DATAS ADD CONSTRAINT PK_TB_IMPORT_DATAS
	PRIMARY KEY (ID)
;

ALTER TABLE TB_EXCEPTION_IMPORT ADD CONSTRAINT PK_TB_EXCEPTION_IMPORT
	PRIMARY KEY (ID)
;

ALTER TABLE TB_CONTACTS ADD CONSTRAINT PK_TB_CONTACTS
	PRIMARY KEY (ID)
;

ALTER TABLE TB_CONTACT_ITEMS ADD CONSTRAINT PK_TB_CONTACT_ITEMS
	PRIMARY KEY (ID)
;

ALTER TABLE TB_ATTR_RELATIONSHIPS ADD CONSTRAINT PK_TB_ATTR_RELATIONSHIPS
	PRIMARY KEY (ID)
;

    
ALTER TABLE TB_COMPLIST_COMP ADD CONSTRAINT PK_TB_COMPLIST_COMP
	PRIMARY KEY (COMPONENT_FK, COMPONENTLIST_FK)
;

ALTER TABLE TB_DATA_ATTRIBUTES_EI_ROLES ADD CONSTRAINT PK_TB_DATA_ATTRIBUTES_EI_ROLES
	PRIMARY KEY (ROLE_FK, TB_DATA_ATTRIBUTES)
;

ALTER TABLE TB_DIMENSIONS_EI_ROLES ADD CONSTRAINT PK_TB_DIMENSIONS_EI_ROLES
	PRIMARY KEY (ROLE_FK, TB_DIMENSIONS)
;

ALTER TABLE TB_DIMREL_DIMCOM ADD CONSTRAINT PK_TB_DIMREL_DIMCOM
	PRIMARY KEY (DIMENSION_COMPONENT_FK, DIMENSION_RELATIONSHIP_FK)
;

ALTER TABLE TB_DIMREL_GROUPDIM ADD CONSTRAINT PK_TB_DIMREL_GROUPDIM
	PRIMARY KEY (GROUPDIMENSION_DESCRIPTOR_FK, DIMENSION_RELATIONSHIP_FK)
;

ALTER TABLE TB_MEASURE_DIMEN_EI_ROLES ADD CONSTRAINT PK_TB_MEASURE_DIMEN_EI_ROLES
	PRIMARY KEY (ROLE_FK, TB_MEASURE_DIMENSIONS)
;

ALTER TABLE TB_STRVERSION_COMPLIST ADD CONSTRAINT PK_TB_STRVERSION_COMPLIST
	PRIMARY KEY (STRUCTUREVERSION_FK, COMPONENT_LIST_FK)
;


-- Unique constraints

ALTER TABLE TB_INTERNATIONAL_STRINGS
	ADD CONSTRAINT UQ_TB_INTERNATIONAL_STRINGS UNIQUE (ID)
;


ALTER TABLE TB_EXTERNAL_ITEMS
	ADD CONSTRAINT UQ_TB_EXTERNAL_ITEMS UNIQUE (ID)
;


ALTER TABLE TB_LOCALISED_STRINGS
	ADD CONSTRAINT UQ_TB_LOCALISED_STRINGS UNIQUE (ID)
;


ALTER TABLE TB_ANNOTABLE_ARTEFACTS
    ADD CONSTRAINT UQ_TB_ANNOTABLE_ARTEFACTS UNIQUE (UUID)
;


ALTER TABLE TB_ANNOTATIONS
    ADD CONSTRAINT UQ_TB_ANNOTATIONS UNIQUE (UUID)
;


ALTER TABLE TB_ITEM_SCHEMES
    ADD CONSTRAINT UQ_TB_ITEM_SCHEMES UNIQUE (UUID)
;



ALTER TABLE TB_ITEM_SCHEMES_VERSIONS
    ADD CONSTRAINT UQ_TB_ITEM_SCHEMES_VERSIONS UNIQUE (UUID)
;


ALTER TABLE TB_ITEMS
    ADD CONSTRAINT UQ_TB_ITEMS UNIQUE (UUID)
;


ALTER TABLE TB_REPRESENTATIONS
    ADD CONSTRAINT UQ_TB_REPRESENTATIONS UNIQUE (UUID)
;





ALTER TABLE TB_FACETS
    ADD CONSTRAINT UQ_TB_FACETS UNIQUE (UUID)
;


ALTER TABLE TB_STRUCTURES
    ADD CONSTRAINT UQ_TB_STRUCTURES UNIQUE (UUID)
;


ALTER TABLE TB_STRUCTURES_VERSIONS
    ADD CONSTRAINT UQ_TB_STRUCTURES_VERSIONS UNIQUE (UUID)
;



ALTER TABLE TB_CATEGORISATIONS
    ADD CONSTRAINT UQ_TB_CATEGORISATIONS UNIQUE (UUID)
;




ALTER TABLE TB_IMPORT_DATAS
	ADD CONSTRAINT UQ_TB_IMPORT_DATAS UNIQUE (JOB)
;


ALTER TABLE TB_EXCEPTION_IMPORT
    ADD CONSTRAINT UQ_TB_EXCEPTION_IMPORT UNIQUE (UUID)
;


ALTER TABLE TB_CONTACTS
    ADD CONSTRAINT UQ_TB_CONTACTS UNIQUE (UUID)
;


ALTER TABLE TB_CONTACT_ITEMS
    ADD CONSTRAINT UQ_TB_CONTACT_ITEMS UNIQUE (UUID)
;



ALTER TABLE TB_ATTR_RELATIONSHIPS
    ADD CONSTRAINT UQ_TB_ATTR_RELATIONSHIPS UNIQUE (UUID)
;




-- Foreign key constraints
ALTER TABLE TB_ORGANISATIONS ADD CONSTRAINT FK_TB_ORGANISATIONS_TB_ITEMS
	FOREIGN KEY (TB_ITEMS) REFERENCES TB_ITEMS (ID)
;
ALTER TABLE TB_CONCEPTS ADD CONSTRAINT FK_TB_CONCEPTS_TB_ITEMS
	FOREIGN KEY (TB_ITEMS) REFERENCES TB_ITEMS (ID)
;
ALTER TABLE TB_CONCEPT_SCHEMES_VERSIONS ADD CONSTRAINT FK_TB_CONCEPT_SCHEMES_VERSIONS_TB_ITEM_SCHEMES_VERSIONS
	FOREIGN KEY (TB_ITEM_SCHEMES_VERSIONS) REFERENCES TB_ITEM_SCHEMES_VERSIONS (ID)
;
ALTER TABLE TB_CODELISTS_VERSIONS ADD CONSTRAINT FK_TB_CODELISTS_VERSIONS_TB_ITEM_SCHEMES_VERSIONS
	FOREIGN KEY (TB_ITEM_SCHEMES_VERSIONS) REFERENCES TB_ITEM_SCHEMES_VERSIONS (ID)
;
ALTER TABLE TB_CATEGORIES ADD CONSTRAINT FK_TB_CATEGORIES_TB_ITEMS
	FOREIGN KEY (TB_ITEMS) REFERENCES TB_ITEMS (ID)
;
ALTER TABLE TB_CAT_SCHEMES_VERSIONS ADD CONSTRAINT FK_TB_CAT_SCHEMES_VERSIONS_TB_ITEM_SCHEMES_VERSIONS
	FOREIGN KEY (TB_ITEM_SCHEMES_VERSIONS) REFERENCES TB_ITEM_SCHEMES_VERSIONS (ID)
;
ALTER TABLE TB_CODES ADD CONSTRAINT FK_TB_CODES_TB_ITEMS
	FOREIGN KEY (TB_ITEMS) REFERENCES TB_ITEMS (ID)
;
ALTER TABLE TB_ORG_SCHEMES_VERSIONS ADD CONSTRAINT FK_TB_ORG_SCHEMES_VERSIONS_TB_ITEM_SCHEMES_VERSIONS
	FOREIGN KEY (TB_ITEM_SCHEMES_VERSIONS) REFERENCES TB_ITEM_SCHEMES_VERSIONS (ID)
;
ALTER TABLE TB_DATASTRUCTURE_VERSIONS ADD CONSTRAINT FK_TB_DATASTRUCTURE_VERSIONS_TB_STRUCTURES_VERSIONS
	FOREIGN KEY (TB_STRUCTURES_VERSIONS) REFERENCES TB_STRUCTURES_VERSIONS (ID)
;


  
  
ALTER TABLE TB_EXTERNAL_ITEMS ADD CONSTRAINT FK_TB_EXTERNAL_ITEMS_TITLE_FK
	FOREIGN KEY (TITLE_FK) REFERENCES TB_INTERNATIONAL_STRINGS (ID)
;

  
ALTER TABLE TB_LOCALISED_STRINGS ADD CONSTRAINT FK_TB_LOCALISED_STRINGS_INTERNATIONAL_STRING_FK
	FOREIGN KEY (INTERNATIONAL_STRING_FK) REFERENCES TB_INTERNATIONAL_STRINGS (ID)
;

  
  
ALTER TABLE TB_ANNOTATIONS ADD CONSTRAINT FK_TB_ANNOTATIONS_TB_ANNOTABLE_ARTEFACTS
	FOREIGN KEY (ANNOTABLE_ARTEFACT_FK) REFERENCES TB_ANNOTABLE_ARTEFACTS (ID)
;

ALTER TABLE TB_ANNOTATIONS ADD CONSTRAINT FK_TB_ANNOTATIONS_TEXT_FK
	FOREIGN KEY (TEXT_FK) REFERENCES TB_INTERNATIONAL_STRINGS (ID)
;

  
  
  
  
  
ALTER TABLE TB_ITEM_SCHEMES_VERSIONS ADD CONSTRAINT FK_TB_ITEM_SCHEMES_VERSIONS_ITEM_SCHEME_FK
	FOREIGN KEY (ITEM_SCHEME_FK) REFERENCES TB_ITEM_SCHEMES (ID)
;
ALTER TABLE TB_ITEM_SCHEMES_VERSIONS ADD CONSTRAINT FK_TB_ITEM_SCHEMES_VERSIONS_MAINTANABLE_ARTEFACT_FK
	FOREIGN KEY (MAINTANABLE_ARTEFACT_FK) REFERENCES TB_ANNOTABLE_ARTEFACTS (ID)
;

  
ALTER TABLE TB_ITEMS ADD CONSTRAINT FK_TB_ITEMS_NAMEABLE_ARTEFACT_FK
	FOREIGN KEY (NAMEABLE_ARTEFACT_FK) REFERENCES TB_ANNOTABLE_ARTEFACTS (ID)
;
ALTER TABLE TB_ITEMS ADD CONSTRAINT FK_TB_ITEMS_PARENT_FK
	FOREIGN KEY (PARENT_FK) REFERENCES TB_ITEMS (ID)
;
ALTER TABLE TB_ITEMS ADD CONSTRAINT FK_TB_ITEMS_ITEM_SCHEME_VERSION_FK
	FOREIGN KEY (ITEM_SCHEME_VERSION_FK) REFERENCES TB_ITEM_SCHEMES_VERSIONS (ID)
;
ALTER TABLE TB_ITEMS ADD CONSTRAINT FK_TB_ITEMS_ITEM_SCHEME_VERSION_FIRST_FK
	FOREIGN KEY (ITEM_SCHEME_VERSION_FIRST_FK) REFERENCES TB_ITEM_SCHEMES_VERSIONS (ID)
;

  
  
  
ALTER TABLE TB_CONCEPTS ADD CONSTRAINT FK_TB_CONCEPTS_CORE_REPRESENTATION_FK
	FOREIGN KEY (CORE_REPRESENTATION_FK) REFERENCES TB_REPRESENTATIONS (ID)
;

  
  
  
  
  
  
  
  
  
ALTER TABLE TB_STRUCTURES_VERSIONS ADD CONSTRAINT FK_TB_STRUCTURES_VERSIONS_MAINTANABLE_ARTEFACT_FK
	FOREIGN KEY (MAINTANABLE_ARTEFACT_FK) REFERENCES TB_ANNOTABLE_ARTEFACTS (ID)
;
ALTER TABLE TB_STRUCTURES_VERSIONS ADD CONSTRAINT FK_TB_STRUCTURES_VERSIONS_STRUCTURE_FK
	FOREIGN KEY (STRUCTURE_FK) REFERENCES TB_STRUCTURES (ID)
;

  
  
  
ALTER TABLE TB_CATEGORISATIONS ADD CONSTRAINT FK_TB_CATEGORISATIONS_MAINTANABLE_ARTEFACT_FK
	FOREIGN KEY (MAINTANABLE_ARTEFACT_FK) REFERENCES TB_ANNOTABLE_ARTEFACTS (ID)
;
ALTER TABLE TB_CATEGORISATIONS ADD CONSTRAINT FK_TB_CATEGORISATIONS_CATEGORY_FK
	FOREIGN KEY (CATEGORY_FK) REFERENCES TB_ITEMS (ID)
;
ALTER TABLE TB_CATEGORISATIONS ADD CONSTRAINT FK_TB_CATEGORISATIONS_ARTEFACT_CATEGORISED_FK
	FOREIGN KEY (ARTEFACT_CATEGORISED_FK) REFERENCES TB_ANNOTABLE_ARTEFACTS (ID)
;

  
  
  
  
  
  
  
ALTER TABLE TB_EXCEPTION_IMPORT ADD CONSTRAINT FK_TB_EXCEPTION_IMPORT_IMPORT_DATA_FK
	FOREIGN KEY (IMPORT_DATA_FK) REFERENCES TB_IMPORT_DATAS (ID)
;

  
ALTER TABLE TB_CONTACTS ADD CONSTRAINT FK_TB_CONTACTS_NAME_FK
	FOREIGN KEY (NAME_FK) REFERENCES TB_INTERNATIONAL_STRINGS (ID)
;
ALTER TABLE TB_CONTACTS ADD CONSTRAINT FK_TB_CONTACTS_ORGANISATION_UNIT_FK
	FOREIGN KEY (ORGANISATION_UNIT_FK) REFERENCES TB_INTERNATIONAL_STRINGS (ID)
;
ALTER TABLE TB_CONTACTS ADD CONSTRAINT FK_TB_CONTACTS_RESPONSIBILITY_FK
	FOREIGN KEY (RESPONSIBILITY_FK) REFERENCES TB_INTERNATIONAL_STRINGS (ID)
;
ALTER TABLE TB_CONTACTS ADD CONSTRAINT FK_TB_CONTACTS_ORGANISATION_FK
	FOREIGN KEY (ORGANISATION_FK) REFERENCES TB_ITEMS (ID)
;

  
ALTER TABLE TB_CONTACT_ITEMS ADD CONSTRAINT FK_TB_CONTACT_ITEMS_CONTACT_FK
	FOREIGN KEY (CONTACT_FK) REFERENCES TB_CONTACTS (ID)
;

  
  
  
  
  
  
  

ALTER TABLE TB_COMPLIST_COMP ADD CONSTRAINT FK_TB_COMPLIST_COMP_COMPONENT_FK
	FOREIGN KEY (COMPONENT_FK) REFERENCES TB_ANNOTABLE_ARTEFACTS (ID)
;
ALTER TABLE TB_COMPLIST_COMP ADD CONSTRAINT FK_TB_COMPLIST_COMP_COMPONENTLIST_FK
	FOREIGN KEY (COMPONENTLIST_FK) REFERENCES TB_ANNOTABLE_ARTEFACTS (ID)
;

  
ALTER TABLE TB_DATA_ATTRIBUTES_EI_ROLES ADD CONSTRAINT FK_TB_DATA_ATTRIBUTES_EI_ROLES_ROLE_FK
	FOREIGN KEY (ROLE_FK) REFERENCES TB_ITEMS (ID)
;
ALTER TABLE TB_DATA_ATTRIBUTES_EI_ROLES ADD CONSTRAINT FK_TB_DATA_ATTRIBUTES_EI_ROLES_TB_DATA_ATTRIBUTES
	FOREIGN KEY (TB_DATA_ATTRIBUTES) REFERENCES TB_ANNOTABLE_ARTEFACTS (ID)
;

  
ALTER TABLE TB_DIMENSIONS_EI_ROLES ADD CONSTRAINT FK_TB_DIMENSIONS_EI_ROLES_ROLE_FK
	FOREIGN KEY (ROLE_FK) REFERENCES TB_ITEMS (ID)
;
ALTER TABLE TB_DIMENSIONS_EI_ROLES ADD CONSTRAINT FK_TB_DIMENSIONS_EI_ROLES_TB_DIMENSIONS
	FOREIGN KEY (TB_DIMENSIONS) REFERENCES TB_ANNOTABLE_ARTEFACTS (ID)
;

  
ALTER TABLE TB_DIMREL_DIMCOM ADD CONSTRAINT FK_TB_DIMREL_DIMCOM_DIMENSION_COMPONENT_FK
	FOREIGN KEY (DIMENSION_COMPONENT_FK) REFERENCES TB_ANNOTABLE_ARTEFACTS (ID)
;
ALTER TABLE TB_DIMREL_DIMCOM ADD CONSTRAINT FK_TB_DIMREL_DIMCOM_DIMENSION_RELATIONSHIP_FK
	FOREIGN KEY (DIMENSION_RELATIONSHIP_FK) REFERENCES TB_ATTR_RELATIONSHIPS (ID)
;

  
ALTER TABLE TB_DIMREL_GROUPDIM ADD CONSTRAINT FK_TB_DIMREL_GROUPDIM_GROUPDIMENSION_DESCRIPTOR_FK
	FOREIGN KEY (GROUPDIMENSION_DESCRIPTOR_FK) REFERENCES TB_ANNOTABLE_ARTEFACTS (ID)
;
ALTER TABLE TB_DIMREL_GROUPDIM ADD CONSTRAINT FK_TB_DIMREL_GROUPDIM_DIMENSION_RELATIONSHIP_FK
	FOREIGN KEY (DIMENSION_RELATIONSHIP_FK) REFERENCES TB_ATTR_RELATIONSHIPS (ID)
;

  
ALTER TABLE TB_MEASURE_DIMEN_EI_ROLES ADD CONSTRAINT FK_TB_MEASURE_DIMEN_EI_ROLES_ROLE_FK
	FOREIGN KEY (ROLE_FK) REFERENCES TB_ITEMS (ID)
;
ALTER TABLE TB_MEASURE_DIMEN_EI_ROLES ADD CONSTRAINT FK_TB_MEASURE_DIMEN_EI_ROLES_TB_MEASURE_DIMENSIONS
	FOREIGN KEY (TB_MEASURE_DIMENSIONS) REFERENCES TB_ANNOTABLE_ARTEFACTS (ID)
;

  
ALTER TABLE TB_STRVERSION_COMPLIST ADD CONSTRAINT FK_TB_STRVERSION_COMPLIST_STRUCTUREVERSION_FK
	FOREIGN KEY (STRUCTUREVERSION_FK) REFERENCES TB_ANNOTABLE_ARTEFACTS (ID)
;
ALTER TABLE TB_STRVERSION_COMPLIST ADD CONSTRAINT FK_TB_STRVERSION_COMPLIST_COMPONENT_LIST_FK
	FOREIGN KEY (COMPONENT_LIST_FK) REFERENCES TB_STRUCTURES_VERSIONS (ID)
;

  

  
  
ALTER TABLE TB_ANNOTABLE_ARTEFACTS ADD CONSTRAINT FK_TB_ANNOTABLE_ARTEFACTS_NAME_FK
	FOREIGN KEY (NAME_FK) REFERENCES TB_INTERNATIONAL_STRINGS (ID)
;
ALTER TABLE TB_ANNOTABLE_ARTEFACTS ADD CONSTRAINT FK_TB_ANNOTABLE_ARTEFACTS_DESCRIPTION_FK
	FOREIGN KEY (DESCRIPTION_FK) REFERENCES TB_INTERNATIONAL_STRINGS (ID)
;
ALTER TABLE TB_ANNOTABLE_ARTEFACTS ADD CONSTRAINT FK_TB_ANNOTABLE_ARTEFACTS_COMMENT_FK
	FOREIGN KEY (COMMENT_FK) REFERENCES TB_INTERNATIONAL_STRINGS (ID)
;

  
ALTER TABLE TB_ANNOTABLE_ARTEFACTS ADD CONSTRAINT FK_TB_ANNOTABLE_ARTEFACTS_MAINTAINER_FK
	FOREIGN KEY (MAINTAINER_FK) REFERENCES TB_ITEMS (ID)
;

  
ALTER TABLE TB_ANNOTABLE_ARTEFACTS ADD CONSTRAINT FK_TB_ANNOTABLE_ARTEFACTS_CONCEPT_IDENTITY_FK
	FOREIGN KEY (CONCEPT_IDENTITY_FK) REFERENCES TB_ITEMS (ID)
;
ALTER TABLE TB_ANNOTABLE_ARTEFACTS ADD CONSTRAINT FK_TB_ANNOTABLE_ARTEFACTS_REPRESENTATION_FK
	FOREIGN KEY (REPRESENTATION_FK) REFERENCES TB_REPRESENTATIONS (ID)
;

  
  
  
ALTER TABLE TB_REPRESENTATIONS ADD CONSTRAINT FK_TB_REPRESENTATIONS_CONCEPTSCHEME_FK
	FOREIGN KEY (CONCEPTSCHEME_FK) REFERENCES TB_ITEM_SCHEMES_VERSIONS (ID)
;
ALTER TABLE TB_REPRESENTATIONS ADD CONSTRAINT FK_TB_REPRESENTATIONS_CODELIST_FK
	FOREIGN KEY (CODELIST_FK) REFERENCES TB_ITEM_SCHEMES_VERSIONS (ID)
;

  
ALTER TABLE TB_REPRESENTATIONS ADD CONSTRAINT FK_TB_REPRESENTATIONS_FACET_FK
	FOREIGN KEY (FACET_FK) REFERENCES TB_FACETS (ID)
;

  
  
  
ALTER TABLE TB_ANNOTABLE_ARTEFACTS ADD CONSTRAINT FK_TB_ANNOTABLE_ARTEFACTS_RELATE_TO
	FOREIGN KEY (RELATE_TO) REFERENCES TB_ATTR_RELATIONSHIPS (ID)
;

  
  
  
  
  
  
  
  
  
  
  
ALTER TABLE TB_ATTR_RELATIONSHIPS ADD CONSTRAINT FK_TB_ATTR_RELATIONSHIPS_GROUP_KEY
	FOREIGN KEY (GROUP_KEY) REFERENCES TB_ANNOTABLE_ARTEFACTS (ID)
;

  
  
  
  
  
  
  
  
  
  
  
  
  
  
  



