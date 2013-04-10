
    
-- ###########################################
-- # Drop
-- ###########################################


-- Disable Foreign key constraints
ALTER TABLE TB_M_CATEGORIES DROP CONSTRAINT FK_TB_M_CATEGORIES_TB_CATEGORIES;
ALTER TABLE TB_M_CAT_SCHEMES_VERSIONS DROP CONSTRAINT FK_TB_M_CAT_SCHEMES_VERSIONS_TB_CAT_SCHEMES_VERSIONS;
ALTER TABLE TB_M_CODES DROP CONSTRAINT FK_TB_M_CODES_TB_CODES;
ALTER TABLE TB_M_CODELISTS_VERSIONS DROP CONSTRAINT FK_TB_M_CODELISTS_VERSIONS_TB_CODELISTS_VERSIONS;
ALTER TABLE TB_M_CONCEPTS DROP CONSTRAINT FK_TB_M_CONCEPTS_TB_CONCEPTS;
ALTER TABLE TB_M_CONCEPT_SCHEMES_VERSIONS DROP CONSTRAINT FK_TB_M_CONCEPT_SCHEMES_VERSIONS_TB_CONCEPT_SCHEMES_VERSIONS;
ALTER TABLE TB_M_DATASTRUCTURE_VERSIONS DROP CONSTRAINT FK_TB_M_DATASTRUCTURE_VERSIONS_TB_DATASTRUCTURE_VERSIONS;
ALTER TABLE TB_M_ORGANISATIONS DROP CONSTRAINT FK_TB_M_ORGANISATIONS_TB_ORGANISATIONS;
ALTER TABLE TB_M_ORG_SCHEMES_VERSIONS DROP CONSTRAINT FK_TB_M_ORG_SCHEMES_VERSIONS_TB_ORG_SCHEMES_VERSIONS;


  
  
  
  
ALTER TABLE TB_M_VARIABLES DROP CONSTRAINT FK_TB_M_VARIABLES_NAMEABLE_ARTEFACT_FK;
ALTER TABLE TB_M_VARIABLES DROP CONSTRAINT FK_TB_M_VARIABLES_SHORT_NAME_FK;
ALTER TABLE TB_M_VARIABLES DROP CONSTRAINT FK_TB_M_VARIABLES_REPLACED_BY_VARIABLE_FK;

  
ALTER TABLE TB_M_VARIABLE_ELEMENTS DROP CONSTRAINT FK_TB_M_VARIABLE_ELEMENTS_IDENTIFIABLE_ARTEFACT_FK;
ALTER TABLE TB_M_VARIABLE_ELEMENTS DROP CONSTRAINT FK_TB_M_VARIABLE_ELEMENTS_SHORT_NAME_FK;
ALTER TABLE TB_M_VARIABLE_ELEMENTS DROP CONSTRAINT FK_TB_M_VARIABLE_ELEMENTS_REPLACED_BY_VAR_ELEMENT_FK;
ALTER TABLE TB_M_VARIABLE_ELEMENTS DROP CONSTRAINT FK_TB_M_VARIABLE_ELEMENTS_VARIABLE_FK;

  
ALTER TABLE TB_M_CODES DROP CONSTRAINT FK_TB_M_CODES_SHORT_NAME_FK;
ALTER TABLE TB_M_CODES DROP CONSTRAINT FK_TB_M_CODES_VARIABLE_ELEMENT_FK;

  
ALTER TABLE TB_M_CODELIST_FAMILIES DROP CONSTRAINT FK_TB_M_CODELIST_FAMILIES_NAMEABLE_ARTEFACT_FK;

  
ALTER TABLE TB_M_CODELIST_ORDER_VIS DROP CONSTRAINT FK_TB_M_CODELIST_ORDER_VIS_NAMEABLE_ARTEFACT_FK;
ALTER TABLE TB_M_CODELIST_ORDER_VIS DROP CONSTRAINT FK_TB_M_CODELIST_ORDER_VIS_CODELIST_FK;

  
ALTER TABLE TB_M_CODELISTS_VERSIONS DROP CONSTRAINT FK_TB_M_CODELISTS_VERSIONS_SHORT_NAME_FK;
ALTER TABLE TB_M_CODELISTS_VERSIONS DROP CONSTRAINT FK_TB_M_CODELISTS_VERSIONS_DESCRIPTION_SOURCE_FK;
ALTER TABLE TB_M_CODELISTS_VERSIONS DROP CONSTRAINT FK_TB_M_CODELISTS_VERSIONS_CODELIST_FAMILY_FK;
ALTER TABLE TB_M_CODELISTS_VERSIONS DROP CONSTRAINT FK_TB_M_CODELISTS_VERSIONS_VARIABLE_FK;
ALTER TABLE TB_M_CODELISTS_VERSIONS DROP CONSTRAINT FK_TB_M_CODELISTS_VERSIONS_REPLACED_BY_CODELIST_FK;
ALTER TABLE TB_M_CODELISTS_VERSIONS DROP CONSTRAINT FK_TB_M_CODELISTS_VERSIONS_DEFAULT_ORDER_VISUAL_FK;
ALTER TABLE TB_M_CODELISTS_VERSIONS DROP CONSTRAINT FK_TB_M_CODELISTS_VERSIONS_DEFAULT_OPENNESS_VISUAL_FK;

  
ALTER TABLE TB_M_CODELIST_OPENNESS_VIS DROP CONSTRAINT FK_TB_M_CODELIST_OPENNESS_VIS_NAMEABLE_ARTEFACT_FK;
ALTER TABLE TB_M_CODELIST_OPENNESS_VIS DROP CONSTRAINT FK_TB_M_CODELIST_OPENNESS_VIS_CODELIST_FK;

  
ALTER TABLE TB_M_VAR_ELEM_OPERATIONS DROP CONSTRAINT FK_TB_M_VAR_ELEM_OPERATIONS_VARIABLE_FK;

  
ALTER TABLE TB_M_VARIABLE_FAMILIES DROP CONSTRAINT FK_TB_M_VARIABLE_FAMILIES_NAMEABLE_ARTEFACT_FK;

  
ALTER TABLE TB_M_LIS_CONCEPT_TYPES DROP CONSTRAINT FK_TB_M_LIS_CONCEPT_TYPES_DESCRIPTION_FK;

  
ALTER TABLE TB_M_CONCEPTS DROP CONSTRAINT FK_TB_M_CONCEPTS_PLURAL_NAME_FK;
ALTER TABLE TB_M_CONCEPTS DROP CONSTRAINT FK_TB_M_CONCEPTS_ACRONYM_FK;
ALTER TABLE TB_M_CONCEPTS DROP CONSTRAINT FK_TB_M_CONCEPTS_DESCRIPTION_SOURCE_FK;
ALTER TABLE TB_M_CONCEPTS DROP CONSTRAINT FK_TB_M_CONCEPTS_CONTEXT_FK;
ALTER TABLE TB_M_CONCEPTS DROP CONSTRAINT FK_TB_M_CONCEPTS_DOC_METHOD_FK;
ALTER TABLE TB_M_CONCEPTS DROP CONSTRAINT FK_TB_M_CONCEPTS_CONCEPT_TYPE_FK;
ALTER TABLE TB_M_CONCEPTS DROP CONSTRAINT FK_TB_M_CONCEPTS_DERIVATION_FK;
ALTER TABLE TB_M_CONCEPTS DROP CONSTRAINT FK_TB_M_CONCEPTS_LEGAL_ACTS_FK;
ALTER TABLE TB_M_CONCEPTS DROP CONSTRAINT FK_TB_M_CONCEPTS_EXTENDS_FK;
ALTER TABLE TB_M_CONCEPTS DROP CONSTRAINT FK_TB_M_CONCEPTS_VARIABLE_FK;

  
ALTER TABLE TB_M_CONCEPT_SCHEMES_VERSIONS DROP CONSTRAINT FK_TB_M_CONCEPT_SCHEMES_VERSIONS_RELATED_OPERATION_FK;

  
ALTER TABLE TB_M_DATASTRUCTURE_VERSIONS DROP CONSTRAINT FK_TB_M_DATASTRUCTURE_VERSIONS_STATISTICAL_OPERATION_FK;

  
ALTER TABLE TB_M_DIMENSION_ORDERS DROP CONSTRAINT FK_TB_M_DIMENSION_ORDERS_DIM_COMPONENT_FK;
ALTER TABLE TB_M_DIMENSION_ORDERS DROP CONSTRAINT FK_TB_M_DIMENSION_ORDERS_DSD_VERSION_HEADING_FK;
ALTER TABLE TB_M_DIMENSION_ORDERS DROP CONSTRAINT FK_TB_M_DIMENSION_ORDERS_DSD_VERSION_STUB_FK;

  
ALTER TABLE TB_M_MEASURE_DIM_PRECISIONS DROP CONSTRAINT FK_TB_M_MEASURE_DIM_PRECISIONS_M_CONCEPT_FK;
ALTER TABLE TB_M_MEASURE_DIM_PRECISIONS DROP CONSTRAINT FK_TB_M_MEASURE_DIM_PRECISIONS_DSD_VERSION_FK;

  
  
  
  
  

ALTER TABLE TB_M_CONCEPT_RELATED DROP CONSTRAINT FK_TB_M_CONCEPT_RELATED_CONCEPT_RELATED_FK;
ALTER TABLE TB_M_CONCEPT_RELATED DROP CONSTRAINT FK_TB_M_CONCEPT_RELATED_CONCEPT_FK;

  
ALTER TABLE TB_M_CONCEPT_ROLES DROP CONSTRAINT FK_TB_M_CONCEPT_ROLES_CONCEPT_ROLE_FK;
ALTER TABLE TB_M_CONCEPT_ROLES DROP CONSTRAINT FK_TB_M_CONCEPT_ROLES_CONCEPT_FK;

  
ALTER TABLE TB_M_VAR_ELEM_OP_SOURCES DROP CONSTRAINT FK_TB_M_VAR_ELEM_OP_SOURCES_SOURCE_FK;
ALTER TABLE TB_M_VAR_ELEM_OP_SOURCES DROP CONSTRAINT FK_TB_M_VAR_ELEM_OP_SOURCES_OPERATION_FK;

  
ALTER TABLE TB_M_VAR_ELEM_OP_TARGETS DROP CONSTRAINT FK_TB_M_VAR_ELEM_OP_TARGETS_TARGET_FK;
ALTER TABLE TB_M_VAR_ELEM_OP_TARGETS DROP CONSTRAINT FK_TB_M_VAR_ELEM_OP_TARGETS_OPERATION_FK;

  
ALTER TABLE TB_M_VAR_FAMILIES_VARIABLES DROP CONSTRAINT FK_TB_M_VAR_FAMILIES_VARIABLES_VARIABLE_FAMILY_FK;
ALTER TABLE TB_M_VAR_FAMILIES_VARIABLES DROP CONSTRAINT FK_TB_M_VAR_FAMILIES_VARIABLES_VARIABLE_FK;

  

    
-- Drop many to many relations
    
DROP TABLE TB_M_VAR_FAMILIES_VARIABLES;

DROP TABLE TB_M_VAR_ELEM_OP_TARGETS;

DROP TABLE TB_M_VAR_ELEM_OP_SOURCES;

DROP TABLE TB_M_CONCEPT_ROLES;

DROP TABLE TB_M_CONCEPT_RELATED;

-- Drop normal entities
    
DROP TABLE TB_M_ORG_SCHEMES_VERSIONS;

DROP TABLE TB_M_ORGANISATIONS;

DROP TABLE TB_M_MEASURE_DIM_PRECISIONS;

DROP TABLE TB_M_DIMENSION_ORDERS;

DROP TABLE TB_M_DATASTRUCTURE_VERSIONS;

DROP TABLE TB_M_CONCEPT_SCHEMES_VERSIONS;

DROP TABLE TB_M_CONCEPTS;

DROP TABLE TB_M_LIS_CONCEPT_TYPES;

DROP TABLE TB_M_VARIABLE_FAMILIES;

DROP TABLE TB_M_VAR_ELEM_OPERATIONS;

DROP TABLE TB_M_CODELIST_OPENNESS_VIS;

DROP TABLE TB_M_CODELISTS_VERSIONS;

DROP TABLE TB_M_CODELIST_ORDER_VIS;

DROP TABLE TB_M_CODELIST_FAMILIES;

DROP TABLE TB_M_CODES;

DROP TABLE TB_M_VARIABLE_ELEMENTS;

DROP TABLE TB_M_VARIABLES;

DROP TABLE TB_M_CAT_SCHEMES_VERSIONS;

DROP TABLE TB_M_CATEGORIES;