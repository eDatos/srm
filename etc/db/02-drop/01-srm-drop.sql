
    
-- ###########################################
-- # Drop
-- ###########################################
-- Drop index
DROP INDEX IX_TB_ATTR_RELATIONSHIPS_DTYPE;
DROP INDEX IX_TB_REPRESENTATIONS_DTYPE;
DROP INDEX IX_TB_ANNOTABLE_ARTEFACTS_DT96;


-- Drop many to many relations
    
DROP TABLE TB_MEASURE_DIMEN_EI_ROLES CASCADE CONSTRAINTS PURGE;

DROP TABLE TB_DIMREL_GROUPDIM CASCADE CONSTRAINTS PURGE;

DROP TABLE TB_DIMREL_DIMCOM CASCADE CONSTRAINTS PURGE;

DROP TABLE TB_DIMENSIONS_EI_ROLES CASCADE CONSTRAINTS PURGE;

DROP TABLE TB_DATA_ATTRIBUTES_EI_ROLES CASCADE CONSTRAINTS PURGE;

DROP TABLE TB_COMPLIST_COMP CASCADE CONSTRAINTS PURGE;

-- Drop normal entities
    
DROP TABLE TB_ATTR_RELATIONSHIPS CASCADE CONSTRAINTS PURGE;

DROP TABLE TB_LOCALISED_STRINGS CASCADE CONSTRAINTS PURGE;

DROP TABLE TB_FACETS CASCADE CONSTRAINTS PURGE;

DROP TABLE TB_ANNOTATIONS CASCADE CONSTRAINTS PURGE;

DROP TABLE TB_CONCEPT_SCHEMES CASCADE CONSTRAINTS PURGE;

DROP TABLE TB_EXTERNAL_ITEMS CASCADE CONSTRAINTS PURGE;

DROP TABLE TB_CONCEPTS CASCADE CONSTRAINTS PURGE;

DROP TABLE TB_REPRESENTATIONS CASCADE CONSTRAINTS PURGE;

DROP TABLE TB_INTERNATIONAL_STRINGS CASCADE CONSTRAINTS PURGE;

DROP TABLE TB_ANNOTABLE_ARTEFACTS CASCADE CONSTRAINTS PURGE;


-- Drop pk sequence
    
drop sequence SEQ_ANNOTABLE;
drop sequence SEQ_ANNOTATION;
drop sequence SEQ_I18NSTRS;
drop sequence SEQ_L10NSTRS;
drop sequence SEQ_CONTACT;
drop sequence SEQ_REPREN;
drop sequence SEQ_FACET;
drop sequence SEQ_ATTR_RELAT;
drop sequence SEQ_EXTERNAL_ITEMS;
drop sequence SEQ_CONCEPT_SCHEMES;
drop sequence SEQ_CONCEPTS;