
    
-- ###########################################
-- # Drop
-- ###########################################
-- Drop index


-- Drop many to many relations
    
-- Drop normal entities
    
DROP TABLE TB_M_DATASTRUCTURE_VERSIONS CASCADE CONSTRAINTS PURGE;

DROP TABLE TB_M_CONCEPT_SCHEMES_VERSIONS CASCADE CONSTRAINTS PURGE;

DROP TABLE TB_M_CONCEPTS CASCADE CONSTRAINTS PURGE;

DROP TABLE TB_M_LIS_CONCEPT_TYPES CASCADE CONSTRAINTS PURGE;


-- Drop pk sequence
DROP sequence SEQ_M_CONCEPT_SCHEMES_VERSIONS;
DROP sequence SEQ_M_CONCEPTS;
DROP sequence SEQ_LIS_CONCEPT_TYPES;
DROP sequence SEQ_M_DATASTRUCTURE_VERSIONS;   