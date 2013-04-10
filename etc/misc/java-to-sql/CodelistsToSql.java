package org.siemac.metamac.srm.core;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.math.RandomUtils;
import org.apache.commons.lang.mutable.MutableLong;

public class CodelistsToSql {

    /**
     * TENER EN CUENTA QUE HAY QUE ESTABLECER LOS VALORES DE LAS VARIABLES GLOBALES EN CADA EJECUCIÓN:
     * 1) Debe haberse insertado un maintainer por defecto, e indicar su id en la variable 'idMaintainer'
     * 2) 'firstId': setear al máximo + 1 de ids de todas las tablas.
     * 3) Indicar si debe o no publicarse 'publishCodelist'
     * 4) Número de códigos en "numCodes"
     * 5) Para muchos códigos, es mejor poner 'separateFiles' a true para que separe el resultado en varios ficheros.
     */
    private static long                   firstId                         = 500000000;
    private static int                    numCodes                        = 17000;
    private static Boolean                withAnnotations                 = Boolean.TRUE;
    private static Boolean                publishCodelist                 = Boolean.FALSE;

    private static MutableLong            idInternationalString           = new MutableLong(firstId);
    private static MutableLong            idLocalistedString              = new MutableLong(firstId);
    private static MutableLong            idCodes                         = new MutableLong(firstId);
    private static MutableLong            idCodelists                     = new MutableLong(firstId);
    private static MutableLong            idAnnotableArtefact             = new MutableLong(firstId);
    private static MutableLong            idAnnotations                   = new MutableLong(firstId);
    private static MutableLong            idVariables                     = new MutableLong(firstId);
    private static MutableLong            idVariableElements              = new MutableLong(firstId);
    private static MutableLong            idCodelistOrderVisualisation    = new MutableLong(firstId);
    private static MutableLong            idCodelistOpennessVisualisation = new MutableLong(firstId);

    private static List<String>           insertSentences                 = new ArrayList<String>();
    private static Long                   idMaintainer                    = Long.valueOf(2);

    private static List<Long>             parents                         = new ArrayList<Long>(numCodes);   // parents id to aleatory parent
    private static final boolean          separateFiles                   = false;

    private static Map<Long, Long>        parentsLevel                    = new HashMap<Long, Long>();
    private static Map<Long, MutableLong> parentsChildrenNumber           = new HashMap<Long, MutableLong>();
    private static int                    maxDepth                        = 15;

    public static void main(String[] args) throws Exception {
        String codeCodelist = "codelist" + idCodelists;
        long idCodelist = insertCodelist(codeCodelist, "Codelist de " + numCodes + " codes con InternationalString, anotaciones = " + withAnnotations);

        String codeVariable = "variable" + idVariables;
        long idVariable = insertVariableUpdatingCodelist(codeVariable, idCodelist);

        parents.add(null); // add null value to generate codes in first level
        parentsChildrenNumber.put(null, new MutableLong(0));
        int countFile = 1;
        for (int i = 0; i < numCodes; i++) {
            Long parentId = parents.get(RandomUtils.nextInt(parents.size()));
            Long codeId = insertCode(codeCodelist, idCodelist, parentId, codeVariable, idVariable);

            if (parentId != null) {
                parentsChildrenNumber.get(parentId).add(1);

                // Check actual item can be parent
                Long levelCode = parentsLevel.get(parentId) + 1;
                if (levelCode.intValue() <= maxDepth) {
                    parents.add(codeId);
                    parentsLevel.put(codeId, levelCode);
                } else {
                    // demasiado profundo
                }
            } else {
                // first level can be parent always
                parents.add(codeId);
                parentsLevel.put(codeId, Long.valueOf(1));
                parentsChildrenNumber.get(parentId).add(1);
            }
            if (parentsLevel.containsKey(codeId)) {
                parentsChildrenNumber.put(codeId, new MutableLong(0));
            }

            // Save into file
            if (separateFiles && insertSentences.size() > 50000) {
                saveFile(insertSentences, "target/codelist-out" + countFile + ".sql");
                insertSentences.clear();
                countFile++;
            }
        }

        // Update sequences
        insertSentences.add("UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = " + idInternationalString + " WHERE SEQUENCE_NAME = 'I18NSTRS';");
        insertSentences.add("UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = " + idLocalistedString + " WHERE SEQUENCE_NAME = 'L10NSTRS';");
        insertSentences.add("UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = " + idCodelists + " WHERE SEQUENCE_NAME = 'ITEM_SCHEMES';");
        insertSentences.add("UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = " + idCodelists + " WHERE SEQUENCE_NAME = 'ITEM_SCHEMES_VERSIONS';");
        insertSentences.add("UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = " + idCodelists + " WHERE SEQUENCE_NAME = 'CODELISTS_VERSIONS';");
        insertSentences.add("UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = " + idCodelists + " WHERE SEQUENCE_NAME = 'M_CODELISTS_VERSIONS';");
        insertSentences.add("UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = " + idCodes + " WHERE SEQUENCE_NAME = 'ITEMS';");
        insertSentences.add("UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = " + idCodes + " WHERE SEQUENCE_NAME = 'CODES';");
        insertSentences.add("UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = " + idCodes + " WHERE SEQUENCE_NAME = 'M_CODES';");
        insertSentences.add("UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = " + idAnnotableArtefact + " WHERE SEQUENCE_NAME = 'ANNOTABLE_ARTEFACTS';");
        insertSentences.add("UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = " + idAnnotations + " WHERE SEQUENCE_NAME = 'ANNOTATIONS';");
        insertSentences.add("UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = " + idVariables + " WHERE SEQUENCE_NAME = 'M_VARIABLES';");
        insertSentences.add("UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = " + idVariableElements + " WHERE SEQUENCE_NAME = 'M_VARIABLE_ELEMENTS';");
        insertSentences.add("UPDATE TB_SEQUENCES SET SEQUENCE_NEXT_VALUE = " + idCodelistOrderVisualisation + " WHERE SEQUENCE_NAME = 'M_CODELIST_ORDER_VISUAL';");

        if (insertSentences.size() > 0) {
            // Save into file
            saveFile(insertSentences, "target/codelist-out" + countFile + ".sql");
        }
    }

    private static long insertCodelist(String codeCodelist, String title) {
        String urn = "urn:sdmx:org.sdmx.infomodel.codelist.Codelist=ISTAC:" + codeCodelist + "(1.0)";
        long idMaintainableArtefactInserted = insertMaintainableArtefact(codeCodelist, urn, title);
        insertItemScheme();
        insertItemSchemeVersion(idMaintainableArtefactInserted);
        insertCodelistVersion();
        insertCodelistVersionMetamac();
        long idCodelistInserted = idCodelists.longValue();
        Long idOrderVisualisationDefault = null;
        Long idOpennessVisualisationDefault = null;
        for (int i = 0; i < 20; i++) {
            Long idOrderVisualisation = insertCodelistOrderVisualisation(codeCodelist, idCodelistInserted, i + 1);
            if (idOrderVisualisationDefault == null) {
                idOrderVisualisationDefault = idOrderVisualisation;
            }
        }
        for (int i = 0; i < 1; i++) {
            Long idOpennessVisualisation = insertCodelistOpennessVisualisation(codeCodelist, idCodelistInserted, i + 1);
            if (idOpennessVisualisationDefault == null) {
                idOpennessVisualisationDefault = idOpennessVisualisation;
            }
        }
        insertSentences.add("UPDATE TB_M_CODELISTS_VERSIONS set DEFAULT_ORDER_VISUAL_FK = " + idOrderVisualisationDefault + ", DEFAULT_OPENNESS_VISUAL_FK = " + idOpennessVisualisationDefault
                + " where TB_CODELISTS_VERSIONS = " + idCodelistInserted + ";");

        // prepare next
        idCodelists.add(1);

        return idCodelistInserted;
    }

    private static long insertCode(String codeCodelist, long idCodelist, Long idParent, String codeVariable, long idVariable) {
        long idCode = idCodes.longValue();

        String code = "code" + idCode;
        String urn = "urn:sdmx:org.sdmx.infomodel.codelist.Code=ISTAC:" + codeCodelist + "(1.0)." + code;
        long idNameableArtefact = insertNameableArtefact(urn, code, withAnnotations);
        insertItem(idCodelist, idNameableArtefact, idCode, idParent);
        insertCode(idCode);
        long variableElement = insertVariableElement(codeVariable, idVariable);
        insertCodeMetamac(idParent, idCode, variableElement);

        // prepare next
        idCodes.add(1);
        idAnnotableArtefact.add(1);

        return idCode;
    }

    /**
     * CREATE TABLE TB_M_VARIABLES (
     * ID NUMBER(19) NOT NULL,
     * VALID_FROM_TZ VARCHAR2(50),
     * VALID_FROM TIMESTAMP,
     * VALID_TO_TZ VARCHAR2(50),
     * VALID_TO TIMESTAMP,
     * UPDATE_DATE_TZ VARCHAR2(50),
     * UPDATE_DATE TIMESTAMP,
     * UUID VARCHAR2(36) NOT NULL,
     * CREATED_DATE_TZ VARCHAR2(50),
     * CREATED_DATE TIMESTAMP,
     * CREATED_BY VARCHAR2(50),
     * LAST_UPDATED_TZ VARCHAR2(50),
     * LAST_UPDATED TIMESTAMP,
     * LAST_UPDATED_BY VARCHAR2(50),
     * VERSION NUMBER(19) NOT NULL,
     * NAMEABLE_ARTEFACT_FK NUMBER(19) NOT NULL,
     * SHORT_NAME_FK NUMBER(19) NOT NULL,
     * REPLACED_BY_VARIABLE_FK NUMBER(19)
     * );
     */
    private static long insertVariableUpdatingCodelist(String codeVariable, long idCodelist) {
        long id = idVariables.longValue();
        String urn = "urn:siemac:org.siemac.metamac.infomodel.structuralresources.Variable=" + codeVariable;
        long idNameableArtefact = insertNameableArtefact(urn, codeVariable, false);
        long shortName = insertInternationalString("shortName_" + id);
        insertSentences.add("INSERT INTO TB_M_VARIABLES (ID, UUID, VERSION, NAMEABLE_ARTEFACT_FK, SHORT_NAME_FK) values (" + id + ", '" + id + "', 1, " + idNameableArtefact + ", " + shortName + ");");

        insertSentences.add("UPDATE TB_M_CODELISTS_VERSIONS set VARIABLE_FK = " + id + " where TB_CODELISTS_VERSIONS = " + idCodelist + ";");
        idVariables.add(1);
        return id;
    }

    /**
     * CREATE TABLE TB_M_VARIABLE_ELEMENTS (
     * ID NUMBER(19) NOT NULL,
     * VALID_FROM_TZ VARCHAR2(50),
     * VALID_FROM TIMESTAMP,
     * VALID_TO_TZ VARCHAR2(50),
     * VALID_TO TIMESTAMP,
     * UPDATE_DATE_TZ VARCHAR2(50),
     * UPDATE_DATE TIMESTAMP,
     * UUID VARCHAR2(36) NOT NULL,
     * CREATED_DATE_TZ VARCHAR2(50),
     * CREATED_DATE TIMESTAMP,
     * CREATED_BY VARCHAR2(50),
     * LAST_UPDATED_TZ VARCHAR2(50),
     * LAST_UPDATED TIMESTAMP,
     * LAST_UPDATED_BY VARCHAR2(50),
     * VERSION NUMBER(19) NOT NULL,
     * NAMEABLE_ARTEFACT_FK NUMBER(19) NOT NULL,
     * SHORT_NAME_FK NUMBER(19) NOT NULL,
     * REPLACED_BY_VAR_ELEMENT_FK NUMBER(19),
     * VARIABLE_FK NUMBER(19) NOT NULL
     * );
     */
    private static long insertVariableElement(String codeVariable, long idVariable) {
        long id = idVariableElements.longValue();
        String code = "code" + id;
        String urn = "urn:siemac:org.siemac.metamac.infomodel.structuralresources.VariableElement=" + codeVariable + "." + code;
        long idIdentifiableArtefact = insertIdentifiableArtefact(urn, code, false);
        long shortName = insertInternationalString("shortName_" + id);
        insertSentences.add("INSERT INTO TB_M_VARIABLE_ELEMENTS (ID, UUID, VERSION, IDENTIFIABLE_ARTEFACT_FK, SHORT_NAME_FK, VARIABLE_FK) values (" + id + ", '" + id + "', 1, "
                + idIdentifiableArtefact + ", " + shortName + ", " + idVariable + ");");
        idVariableElements.add(1);
        return id;
    }

    /**
     * CREATE TABLE TB_ANNOTATIONS (
     * ID NUMBER(19) NOT NULL,
     * CODE VARCHAR2(255),
     * TITLE VARCHAR2(255),
     * TYPE VARCHAR2(255),
     * URL VARCHAR2(255),
     * UUID VARCHAR2(36) NOT NULL,
     * VERSION NUMBER(19) NOT NULL,
     * TEXT_FK NUMBER(19),
     * ANNOTABLE_ARTEFACT_FK NUMBER(19)
     * );
     */
    private static void insertAnnotation(long idAnnotableArtefact) {
        if (!withAnnotations) {
            return;
        }

        long id = idAnnotations.longValue();
        String code = "codeAnnotation" + id;
        String title = "title_" + code;
        String type = "type_" + code;
        String url = "http://url" + code;

        // TODO con internationalString
        long textId = insertInternationalString("text_" + code);
        insertSentences.add("INSERT INTO TB_ANNOTATIONS (ID, CODE, TITLE, TYPE, URL, UUID, VERSION, TEXT_FK, ANNOTABLE_ARTEFACT_FK) values (" + id + ", '" + code + "', '" + title + "', '" + type
                + "', '" + url + "', " + id + ", 1, " + textId + ", " + idAnnotableArtefact + ");");

        // TODO con dos etiquetas
        // insertSentences.add("INSERT INTO TB_ANNOTATIONS (ID, CODE, TITLE, TYPE, URL, UUID, VERSION, LOCALE1, LABEL1, LOCALE2, LABEL2, ANNOTABLE_ARTEFACT_FK) values (" + id + ", '" + code + "', '"
        // + title + "', '" + type + "', '" + url + "', " + id + ", 1, 'es', '" + title + " es', 'en', '" + title + " en', " + idAnnotableArtefact + ");");

        idAnnotations.add(1);
    }

    /**
     * 
     CREATE TABLE TB_ANNOTABLE_ARTEFACTS (
     * ID NUMBER(19) NOT NULL,
     * UUID VARCHAR2(36) NOT NULL,
     * CREATED_DATE_TZ VARCHAR2(50),
     * CREATED_DATE TIMESTAMP,
     * CREATED_BY VARCHAR2(50),
     * LAST_UPDATED_TZ VARCHAR2(50),
     * LAST_UPDATED TIMESTAMP,
     * LAST_UPDATED_BY VARCHAR2(50),
     * VERSION NUMBER(19) NOT NULL,
     * ANNOTABLE_ARTEFACT_TYPE VARCHAR2(31) NOT NULL,
     * CODE VARCHAR2(255),
     * CODE_FULL VARCHAR2(4000),
     * URI_PROVIDER VARCHAR2(255),
     * URN VARCHAR2(255),
     * URN_PROVIDER VARCHAR2(255),
     * UPDATE_DATE_TZ VARCHAR2(50),
     * UPDATE_DATE TIMESTAMP,
     * CONCEPT_IDENTITY_FK NUMBER(19),
     * REPRESENTATION_FK NUMBER(19),
     * NAME_FK NUMBER(19),
     * DESCRIPTION_FK NUMBER(19),
     * COMMENT_FK NUMBER(19),
     * RELATE_TO NUMBER(19),
     * USAGE_STATUS VARCHAR2(255),
     * ORDER_LOGIC NUMBER(10),
     * IS_ATTACHMENT_CONSTRAINT NUMBER(1,0),
     * VERSION_LOGIC VARCHAR2(255),
     * VALID_FROM_TZ VARCHAR2(50),
     * VALID_FROM TIMESTAMP,
     * VALID_TO_TZ VARCHAR2(50),
     * VALID_TO TIMESTAMP,
     * FINAL_LOGIC NUMBER(1,0),
     * FINAL_LOGIC_CLIENT NUMBER(1,0),
     * LATEST_FINAL NUMBER(1,0),
     * PUBLIC_LOGIC NUMBER(1,0),
     * LATEST_PUBLIC NUMBER(1,0),
     * IS_EXTERNAL_REFERENCE NUMBER(1,0),
     * STRUCTURE_URL VARCHAR2(255),
     * SERVICE_URL VARCHAR2(255),
     * IS_LAST_VERSION NUMBER(1,0),
     * REPLACED_BY_VERSION VARCHAR2(255),
     * REPLACE_TO_VERSION VARCHAR2(255),
     * IS_IMPORTED NUMBER(1,0),
     * MAINTAINER_FK NUMBER(19)
     * );
     */
    private static long insertMaintainableArtefact(String code, String urn, String title) {
        long id = idAnnotableArtefact.toLong();
        long name = insertInternationalString(code + " " + title);
        long description = insertInternationalString("description_" + code);
        long comment = insertInternationalString("comment_" + code);
        String finalLogic = publishCodelist ? "1" : "0";

        insertSentences
                .add("INSERT INTO TB_ANNOTABLE_ARTEFACTS (ID, UUID, VERSION, ANNOTABLE_ARTEFACT_TYPE, CODE, URN, URN_PROVIDER, NAME_FK, DESCRIPTION_FK, COMMENT_FK, VERSION_LOGIC, FINAL_LOGIC, FINAL_LOGIC_CLIENT, LATEST_FINAL, IS_LAST_VERSION, IS_IMPORTED, MAINTAINER_FK) values ("
                        + id
                        + ", "
                        + id
                        + ", 1, 'MAINTAINABLE_ARTEFACT', '"
                        + code
                        + "', '"
                        + urn
                        + "', '"
                        + urn
                        + "', "
                        + name
                        + ", "
                        + description
                        + ", "
                        + comment
                        + ", '01.000', "
                        + finalLogic
                        + ", " + finalLogic + ", " + finalLogic + ", 1, 0, " + idMaintainer + ");");

        insertAnnotation(id);
        insertAnnotation(id);
        insertAnnotation(id);

        idAnnotableArtefact.add(1);
        return id;
    }
    /**
     * 
     CREATE TABLE TB_ITEM_SCHEMES (
     * ID NUMBER(19) NOT NULL,
     * UUID VARCHAR2(36) NOT NULL,
     * VERSION NUMBER(19) NOT NULL,
     * VERSION_PATTERN VARCHAR2(255) NOT NULL
     * );
     */
    private static void insertItemScheme() {
        insertSentences.add("INSERT INTO TB_ITEM_SCHEMES (ID, UUID, VERSION, VERSION_PATTERN) values (" + idCodelists + ", " + idCodelists + ", 1, 'XX_YYY');");
    }

    private static void insertItemSchemeVersion(long idMaintainableArtefact) {

        /**
         * * CREATE TABLE TB_ITEM_SCHEMES_VERSIONS (
         * ID NUMBER(19) NOT NULL,
         * IS_PARTIAL NUMBER(1,0),
         * UPDATE_DATE_TZ VARCHAR2(50),
         * UPDATE_DATE TIMESTAMP,
         * UUID VARCHAR2(36) NOT NULL,
         * CREATED_DATE_TZ VARCHAR2(50),
         * CREATED_DATE TIMESTAMP,
         * CREATED_BY VARCHAR2(50),
         * LAST_UPDATED_TZ VARCHAR2(50),
         * LAST_UPDATED TIMESTAMP,
         * LAST_UPDATED_BY VARCHAR2(50),
         * VERSION NUMBER(19) NOT NULL,
         * MAINTAINABLE_ARTEFACT_FK NUMBER(19),
         * ITEM_SCHEME_FK NUMBER(19) NOT NULL
         * );
         */
        insertSentences.add("INSERT INTO TB_ITEM_SCHEMES_VERSIONS (ID, UUID, VERSION, IS_PARTIAL, MAINTAINABLE_ARTEFACT_FK, ITEM_SCHEME_FK) values (" + idCodelists + ", " + idCodelists + ", 1, 0, "
                + idMaintainableArtefact + ", " + idCodelists + ");");
    }

    private static void insertCodelistVersion() {
        /**
         * CREATE TABLE TB_CODELISTS_VERSIONS (
         * TB_ITEM_SCHEMES_VERSIONS NUMBER(19) NOT NULL
         * );
         */
        insertSentences.add("INSERT INTO TB_CODELISTS_VERSIONS (TB_ITEM_SCHEMES_VERSIONS) values (" + idCodelists + ");");
    }

    private static void insertCodelistVersionMetamac() {
        /**
         * 
         CREATE TABLE TB_M_CODELISTS_VERSIONS (
         * IS_RECOMMENDED NUMBER(1,0),
         * SHORT_NAME_FK NUMBER(19),
         * CODELIST_FAMILY_FK NUMBER(19),
         * VARIABLE_FK NUMBER(19),
         * REPLACED_BY_CODELIST_FK NUMBER(19),
         * DEFAULT_ORDER_VISUALISATION_FK NUMBER(19),
         * PRODUCTION_VALIDATION_DATE_TZ VARCHAR2(50),
         * PRODUCTION_VALIDATION_DATE TIMESTAMP,
         * PRODUCTION_VALIDATION_USER VARCHAR2(255),
         * DIFFUSION_VALIDATION_DATE_TZ VARCHAR2(50),
         * DIFFUSION_VALIDATION_DATE TIMESTAMP,
         * DIFFUSION_VALIDATION_USER VARCHAR2(255),
         * INTERNAL_PUBLICATION_DATE_TZ VARCHAR2(50),
         * INTERNAL_PUBLICATION_DATE TIMESTAMP,
         * INTERNAL_PUBLICATION_USER VARCHAR2(255),
         * EXTERNAL_PUBLICATION_DATE_TZ VARCHAR2(50),
         * EXTERNAL_PUBLICATION_DATE TIMESTAMP,
         * EXTERNAL_PUBLICATION_USER VARCHAR2(255),
         * IS_EXT_PUBLICATION_FAILED NUMBER(1,0),
         * EXT_PUBLICATION_FAILED_DATE_TZ VARCHAR2(50),
         * EXT_PUBLICATION_FAILED_DATE TIMESTAMP,
         * PROC_STATUS VARCHAR2(255) NOT NULL,
         * ACCESS_TYPE VARCHAR2(255),
         * TB_CODELISTS_VERSIONS NUMBER(19) NOT NULL
         * );
         */
        String subcode = idCodelists.toString();
        long shortName = insertInternationalString("shortName_" + subcode);
        String status = publishCodelist ? "EXTERNALLY_PUBLISHED" : "DRAFT";
        insertSentences.add("INSERT INTO TB_M_CODELISTS_VERSIONS (SHORT_NAME_FK, PROC_STATUS, ACCESS_TYPE, TB_CODELISTS_VERSIONS) values (" + shortName + ", '" + status + "', 'PUBLIC', "
                + idCodelists + ");");
    }

    /**
     * 
     CREATE TABLE TB_M_CODELIST_ORDER_VIS (
     * ID NUMBER(19) NOT NULL,
     * UPDATE_DATE_TZ VARCHAR2(50),
     * UPDATE_DATE TIMESTAMP,
     * COLUMN_INDEX NUMBER(10) NOT NULL,
     * UUID VARCHAR2(36) NOT NULL,
     * CREATED_DATE_TZ VARCHAR2(50),
     * CREATED_DATE TIMESTAMP,
     * CREATED_BY VARCHAR2(50),
     * LAST_UPDATED_TZ VARCHAR2(50),
     * LAST_UPDATED TIMESTAMP,
     * LAST_UPDATED_BY VARCHAR2(50),
     * VERSION NUMBER(19) NOT NULL,
     * NAMEABLE_ARTEFACT_FK NUMBER(19) NOT NULL,
     * CODELIST_FK NUMBER(19) NOT NULL
     * );
     * );
     */
    private static Long insertCodelistOrderVisualisation(String codeCodelist, long idCodelist, Integer columnIndex) {

        Long id = idCodelistOrderVisualisation.toLong();
        String code = null;
        if (columnIndex.intValue() == 1) {
            code = "ALPHABETICAL";
        } else {
            code = "orden" + columnIndex;;
        }
        String urn = "urn:siemac:org.siemac.metamac.infomodel.structuralresources.CodelistOrder=ISTAC:" + codeCodelist + "(1.0)." + code;
        long idNameableArtefact = insertNameableArtefact(urn, code, false);
        insertSentences.add("INSERT INTO TB_M_CODELIST_ORDER_VIS (ID, UUID, VERSION, COLUMN_INDEX, NAMEABLE_ARTEFACT_FK, CODELIST_FK) values (" + id + ", " + id + ", 0, " + columnIndex + ", "
                + idNameableArtefact + "," + idCodelists + ");");
        idCodelistOrderVisualisation.add(1);

        return id;
    }

    private static Long insertCodelistOpennessVisualisation(String codeCodelist, long idCodelist, Integer columnIndex) {

        Long id = idCodelistOpennessVisualisation.toLong();
        String code = null;
        if (columnIndex.intValue() == 1) {
            code = "ALL_EXPANDED";
        } else {
            code = "openness" + columnIndex;;
        }
        String urn = "urn:siemac:org.siemac.metamac.infomodel.structuralresources.CodelistOpennessLevels=ISTAC:" + codeCodelist + "(1.0)." + code;
        long idNameableArtefact = insertNameableArtefact(urn, code, false);
        insertSentences.add("INSERT INTO TB_M_CODELIST_OPENNESS_VIS (ID, UUID, VERSION, COLUMN_INDEX, NAMEABLE_ARTEFACT_FK, CODELIST_FK) values (" + id + ", " + id + ", 0, " + columnIndex + ", "
                + idNameableArtefact + "," + idCodelists + ");");
        idCodelistOpennessVisualisation.add(1);

        return id;
    }

    /**
     * 
     CREATE TABLE TB_ANNOTABLE_ARTEFACTS (
     * ID NUMBER(19) NOT NULL,
     * UUID VARCHAR2(36) NOT NULL,
     * CREATED_DATE_TZ VARCHAR2(50),
     * CREATED_DATE TIMESTAMP,
     * CREATED_BY VARCHAR2(50),
     * LAST_UPDATED_TZ VARCHAR2(50),
     * LAST_UPDATED TIMESTAMP,
     * LAST_UPDATED_BY VARCHAR2(50),
     * VERSION NUMBER(19) NOT NULL,
     * ANNOTABLE_ARTEFACT_TYPE VARCHAR2(31) NOT NULL,
     * CODE VARCHAR2(255),
     * CODE_FULL VARCHAR2(4000),
     * URI_PROVIDER VARCHAR2(255),
     * URN VARCHAR2(255),
     * URN_PROVIDER VARCHAR2(255),
     * UPDATE_DATE_TZ VARCHAR2(50),
     * UPDATE_DATE TIMESTAMP,
     * CONCEPT_IDENTITY_FK NUMBER(19),
     * REPRESENTATION_FK NUMBER(19),
     * NAME_FK NUMBER(19),
     * DESCRIPTION_FK NUMBER(19),
     * COMMENT_FK NUMBER(19),
     * RELATE_TO NUMBER(19),
     * USAGE_STATUS VARCHAR2(255),
     * ORDER_LOGIC NUMBER(10),
     * IS_ATTACHMENT_CONSTRAINT NUMBER(1,0),
     * VERSION_LOGIC VARCHAR2(255),
     * VALID_FROM_TZ VARCHAR2(50),
     * VALID_FROM TIMESTAMP,
     * VALID_TO_TZ VARCHAR2(50),
     * VALID_TO TIMESTAMP,
     * FINAL_LOGIC NUMBER(1,0),
     * FINAL_LOGIC_CLIENT NUMBER(1,0),
     * LATEST_FINAL NUMBER(1,0),
     * PUBLIC_LOGIC NUMBER(1,0),
     * LATEST_PUBLIC NUMBER(1,0),
     * IS_EXTERNAL_REFERENCE NUMBER(1,0),
     * STRUCTURE_URL VARCHAR2(255),
     * SERVICE_URL VARCHAR2(255),
     * IS_LAST_VERSION NUMBER(1,0),
     * REPLACED_BY_VERSION VARCHAR2(255),
     * REPLACE_TO_VERSION VARCHAR2(255),
     * IS_IMPORTED NUMBER(1,0),
     * MAINTAINER_FK NUMBER(19)
     * );
     */
    private static long insertNameableArtefact(String urn, String code, boolean withAnnotations) {
        long id = idAnnotableArtefact.toLong();
        long name = insertInternationalString("name_" + code);
        long description = insertInternationalString("description_" + code);
        long comment = insertInternationalString("comment_" + code);

        insertSentences.add("INSERT INTO TB_ANNOTABLE_ARTEFACTS (ID, UUID, VERSION, ANNOTABLE_ARTEFACT_TYPE, CODE, URN, URN_PROVIDER, NAME_FK, DESCRIPTION_FK, COMMENT_FK) values (" + id + ", " + id
                + ", 1, 'NAMEABLE_ARTEFACT', '" + code + "', '" + urn + "', '" + urn + "', " + name + ", " + description + ", " + comment + ");");

        if (withAnnotations) {
            insertAnnotation(id);
            insertAnnotation(id);
            insertAnnotation(id);
        }

        idAnnotableArtefact.add(1);
        return id;
    }

    private static long insertIdentifiableArtefact(String urn, String code, boolean withAnnotations) {
        long id = idAnnotableArtefact.toLong();

        insertSentences.add("INSERT INTO TB_ANNOTABLE_ARTEFACTS (ID, UUID, VERSION, ANNOTABLE_ARTEFACT_TYPE, CODE, URN, URN_PROVIDER) values (" + id + ", " + id + ", 1, 'IDENTIFIABLE_ARTEFACT', '"
                + code + "', '" + urn + "', '" + urn + "');");

        if (withAnnotations) {
            insertAnnotation(id);
            insertAnnotation(id);
            insertAnnotation(id);
        }

        idAnnotableArtefact.add(1);
        return id;
    }

    /**
     * CREATE TABLE TB_ITEMS (
     * ID NUMBER(19) NOT NULL,
     * UPDATE_DATE_TZ VARCHAR2(50),
     * UPDATE_DATE TIMESTAMP, TODO
     * UUID VARCHAR2(36) NOT NULL,
     * CREATED_DATE_TZ VARCHAR2(50), TODO
     * CREATED_DATE TIMESTAMP, TODO
     * CREATED_BY VARCHAR2(50),
     * LAST_UPDATED_TZ VARCHAR2(50),
     * LAST_UPDATED TIMESTAMP,
     * LAST_UPDATED_BY VARCHAR2(50),
     * VERSION NUMBER(19) NOT NULL,
     * NAMEABLE_ARTEFACT_FK NUMBER(19) NOT NULL,
     * PARENT_FK NUMBER(19), TODO
     * ITEM_SCHEME_VERSION_FK NUMBER(19) NOT NULL,
     * ITEM_SCHEME_VERSION_FIRST_FK NUMBER(19)
     * );
     */
    private static void insertItem(long idCodelist, long idNameableArtefact, long idCode, Long idParent) {
        if (idParent == null) {
            insertSentences.add("INSERT INTO TB_ITEMS (ID, UUID, VERSION, NAMEABLE_ARTEFACT_FK, ITEM_SCHEME_VERSION_FK, ITEM_SCHEME_VERSION_FIRST_FK) values (" + idCode + ", " + idCode + ", 1,"
                    + idNameableArtefact + ", " + idCodelist + ", " + idCodelist + ");");
        } else {
            insertSentences.add("INSERT INTO TB_ITEMS (ID, UUID, VERSION, NAMEABLE_ARTEFACT_FK, ITEM_SCHEME_VERSION_FK, PARENT_FK) values (" + idCode + ", " + idCode + ", 1," + idNameableArtefact
                    + ", " + idCodelist + ", " + idParent + ");");
        }
    }

    /**
     * CREATE TABLE TB_CODES (
     * TB_ITEMS NUMBER(19) NOT NULL
     * );
     */
    private static void insertCode(Long idCode) {
        insertSentences.add("INSERT INTO TB_CODES (TB_ITEMS) values (" + idCode + ");");
    }

    /**
     * CREATE TABLE TB_M_CODES (
     * SHORT_NAME_FK NUMBER(19),
     * VARIABLE_ELEMENT_FK NUMBER(19),
     * TB_CODES NUMBER(19) NOT NULL
     * );
     */
    private static void insertCodeMetamac(Long idParent, long idCode, long variableElement) {
        String subcode = String.valueOf(idCode);
        long shortName = insertInternationalString("shortName_" + subcode);
        MutableLong order1 = parentsChildrenNumber.get(idParent);
        StringBuilder sentenceOrders = new StringBuilder();
        for (int i = 0; i < 20; i++) {
            long order = order1.longValue() + i;
            sentenceOrders.append(", " + order);
        }
        insertSentences
                .add("INSERT INTO TB_M_CODES (SHORT_NAME_FK, VARIABLE_ELEMENT_FK, TB_CODES, OPENNESS1, ORDER1, ORDER2, ORDER3, ORDER4, ORDER5, ORDER6, ORDER7, ORDER8, ORDER9, ORDER10, ORDER11, ORDER12, ORDER13, ORDER14, ORDER15, ORDER16, ORDER17, ORDER18, ORDER19, ORDER20) "
                        + "values (" + shortName + ", " + variableElement + ", " + idCode + ", 1" + sentenceOrders + ");");
    }

    /**
     * CREATE TABLE TB_INTERNATIONAL_STRINGS (
     * ID NUMBER(19) NOT NULL,
     * VERSION NUMBER(19) NOT NULL
     * );
     */
    private static long insertInternationalString(String label) {
        insertSentences.add("INSERT INTO TB_INTERNATIONAL_STRINGS (ID, VERSION) values (" + idInternationalString + ", 1);");
        addInsertSentenceLocalisedString(label);
        long id = idInternationalString.longValue();
        idInternationalString.add(1);
        return id;
    }

    /**
     * CREATE TABLE TB_LOCALISED_STRINGS (
     * ID NUMBER(19) NOT NULL,
     * LABEL VARCHAR2(4000) NOT NULL,
     * LOCALE VARCHAR2(255) NOT NULL,
     * IS_UNMODIFIABLE NUMBER(1,0),
     * VERSION NUMBER(19) NOT NULL,
     * INTERNATIONAL_STRING_FK NUMBER(19) NOT NULL
     * );
     */
    private static void addInsertSentenceLocalisedString(String label) {
        addInsertSentenceLocalisedString(label, "es");
        addInsertSentenceLocalisedString(label, "en");
    }

    private static void addInsertSentenceLocalisedString(String label, String locale) {
        label += "_" + locale;
        insertSentences.add("INSERT INTO TB_LOCALISED_STRINGS (ID, LABEL, LOCALE, VERSION, INTERNATIONAL_STRING_FK) values (" + idLocalistedString + ", \'" + label + "\', \'" + locale + "\', 1, "
                + idInternationalString + ");");
        idLocalistedString.add(1);
    }

    private static void saveFile(List<String> sentences, String path) throws Exception {
        FileWriter outFile = new FileWriter(path);
        PrintWriter out = new PrintWriter(outFile);
        for (String sentence : sentences) {
            out.println(sentence);
        }
        out.close();
    }

}
