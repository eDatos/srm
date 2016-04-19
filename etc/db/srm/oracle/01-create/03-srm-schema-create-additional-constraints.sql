ALTER TABLE TB_MISC_VALUES ADD CONSTRAINT TB_MISC_VALUES_NAME UNIQUE(NAME);

ALTER TABLE TB_M_CAT_SCHEMES_VERSIONS ADD PRIMARY KEY (TB_CAT_SCHEMES_VERSIONS);
ALTER TABLE TB_M_CODELISTS_VERSIONS ADD PRIMARY KEY (TB_CODELISTS_VERSIONS);
ALTER TABLE TB_M_CONCEPT_SCHEMES_VERSIONS ADD PRIMARY KEY (TB_CONCEPT_SCHEMES_VERSIONS);
ALTER TABLE TB_M_ORGANISATIONS ADD PRIMARY KEY (TB_ORGANISATIONS);