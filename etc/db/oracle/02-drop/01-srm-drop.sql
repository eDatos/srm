-- ###########################################
-- # Drop
-- ###########################################
-- Drop index
  
  
  
  
  


-- Drop many to many relations
    
DROP TABLE TB_M_CONCEPT_ROLES CASCADE CONSTRAINTS PURGE;

DROP TABLE TB_M_CONCEPT_RELATED CASCADE CONSTRAINTS PURGE;

-- Drop normal entities
    
DROP TABLE TB_M_ORG_SCHEMES_VERSIONS CASCADE CONSTRAINTS PURGE;

DROP TABLE TB_M_ORGANISATIONS CASCADE CONSTRAINTS PURGE;

DROP TABLE TB_M_DATASTRUCTURE_VERSIONS CASCADE CONSTRAINTS PURGE;

DROP TABLE TB_M_CONCEPT_SCHEMES_VERSIONS CASCADE CONSTRAINTS PURGE;

DROP TABLE TB_M_CONCEPTS CASCADE CONSTRAINTS PURGE;

DROP TABLE TB_M_LIS_CONCEPT_TYPES CASCADE CONSTRAINTS PURGE;

DROP TABLE TB_M_CODELISTS_VERSIONS CASCADE CONSTRAINTS PURGE;

DROP TABLE TB_M_CODES CASCADE CONSTRAINTS PURGE;

DROP TABLE TB_M_CAT_SCHEMES_VERSIONS CASCADE CONSTRAINTS PURGE;

DROP TABLE TB_M_CATEGORIES CASCADE CONSTRAINTS PURGE;


-- Drop pk sequence
    
