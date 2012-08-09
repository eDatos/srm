-- ###########################################
-- # Create
-- ###########################################
-- Create pk sequence
    


-- Create normal entities
    
CREATE TABLE TB_M_CONCEPT_SCHEMES_VERSIONS (
  RELATED_OPERATION_FK NUMBER(19),
  PROC_STATUS VARCHAR2(40) NOT NULL,
  CONCEPT_SCHEME_TYPE VARCHAR2(40) NOT NULL,
  TB_CONCEPT_SCHEMES_VERSIONS NUMBER(19) NOT NULL

);



-- Create many to many relations
    

-- Primary keys
    
    

-- Unique constraints
    



-- Foreign key constraints
ALTER TABLE TB_M_CONCEPT_SCHEMES_VERSIONS ADD CONSTRAINT FK_TB_M_CONCEPT_SCHEMES_VERS79
	FOREIGN KEY (TB_CONCEPT_SCHEMES_VERSIONS) REFERENCES TB_ITEM_SCHEMES_VERSIONS (ID)
;


ALTER TABLE TB_M_CONCEPT_SCHEMES_VERSIONS ADD CONSTRAINT FK_TB_M_CONCEPT_SCHEMES_VERS66
	FOREIGN KEY (RELATED_OPERATION_FK) REFERENCES TB_EXTERNAL_ITEMS (ID)
;

  

    

-- Index


