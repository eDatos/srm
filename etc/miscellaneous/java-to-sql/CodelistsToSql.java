package org.siemac.metamac;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.mutable.MutableLong;

public class CodelistsToSql {

    // TODO setear al máximo actual de ids
    // TODO ojo! no se setea el valor actual en la tabla de sentencias
    private static long         maxId                 = 10000000;
    private static long         numCodes              = 15000;
    private static Boolean      withAnnotations       = Boolean.TRUE;

    private static MutableLong  idInternationalString = new MutableLong(maxId);
    private static MutableLong  idLocalistedString    = new MutableLong(maxId);
    private static MutableLong  idCodes               = new MutableLong(maxId);
    private static MutableLong  idCodelists           = new MutableLong(maxId);
    private static MutableLong  idAnnotableArtefact   = new MutableLong(maxId);
    private static MutableLong  idAnnotations         = new MutableLong(maxId);
    private static MutableLong  idVariables           = new MutableLong(maxId);
    private static MutableLong  idVariableElements    = new MutableLong(maxId);

    private static List<String> insertSentences       = new ArrayList<String>();
    private static Long         idMaintainer          = Long.valueOf(2);        // TODO ojo! debe haberse insertado un maintainer por defecto, e indicar su id en la variable global

    public static void main(String[] args) throws Exception {
        long idCodelist = insertCodelist("Codelist de " + numCodes + " codes con InternationalString, anotaciones = " + withAnnotations);
        long idVariable = insertVariableUpdatingCodelist(idCodelist);
        for (int i = 0; i < numCodes; i++) {
            insertCode(idCodelist, idVariable);
        }
        // Save into file
        saveFile(insertSentences, "target/codelist-out.sql");
    }

    private static long insertCodelist(String title) {
        long idMaintainableArtefactInserted = insertMaintainableArtefact(title);
        insertItemScheme();
        insertItemSchemeVersion(idMaintainableArtefactInserted);
        insertCodelistVersion();
        insertCodelistVersionMetamac();
        long idCodelistInserted = idCodelists.longValue();
        idCodelists.add(1);

        return idCodelistInserted;
    }

    private static void insertCode(long idCodelist, long idVariable) {
        long idNameableArtefact = insertNameableArtefact("code" + idCodes);
        insertItem(idCodelist, idNameableArtefact);
        insertCode();
        long variableElement = insertVariableElement(idVariable);
        insertCodeMetamac(variableElement);
        idCodes.add(1);
        idAnnotableArtefact.add(1);
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
    private static long insertVariableUpdatingCodelist(long idCodelist) {
        long id = idVariables.longValue();
        long idNameableArtefact = insertNameableArtefact("variable" + id);
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
    private static long insertVariableElement(long idVariable) {
        long id = idVariableElements.longValue();
        long idNameableArtefact = insertNameableArtefact("variableElement" + id);
        long shortName = insertInternationalString("shortName_" + id);
        insertSentences.add("INSERT INTO TB_M_VARIABLE_ELEMENTS (ID, UUID, VERSION, NAMEABLE_ARTEFACT_FK, SHORT_NAME_FK, VARIABLE_FK) values (" + id + ", '" + id + "', 1, " + idNameableArtefact
                + ", " + shortName + ", " + idVariable + ");");
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
    private static long insertMaintainableArtefact(String title) {
        long id = idAnnotableArtefact.toLong();
        String code = "codelist" + idCodelists;
        String urn = "urn:codelist:" + code + ":01.000";
        long name = insertInternationalString(code + " " + title);
        long description = insertInternationalString("description_" + code);
        long comment = insertInternationalString("comment_" + code);

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
                        + ", '01.000', 1, 1, 1, 1, 0, "
                        + idMaintainer + ");");

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
         * MAINTANABLE_ARTEFACT_FK NUMBER(19),
         * ITEM_SCHEME_FK NUMBER(19) NOT NULL
         * );
         */
        insertSentences.add("INSERT INTO TB_ITEM_SCHEMES_VERSIONS (ID, UUID, VERSION, MAINTANABLE_ARTEFACT_FK, ITEM_SCHEME_FK) values (" + idCodelists + ", " + idCodelists + ", 1, "
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
        insertSentences.add("INSERT INTO TB_M_CODELISTS_VERSIONS (SHORT_NAME_FK, PROC_STATUS, ACCESS_TYPE, TB_CODELISTS_VERSIONS) values (" + shortName + ", 'EXTERNALLY_PUBLISHED', 'PUBLIC', "
                + idCodelists + ");");
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
    private static long insertNameableArtefact(String subcode) {
        long id = idAnnotableArtefact.toLong();
        String code = "code" + subcode;
        String urn = "urn:code:" + code;
        long name = insertInternationalString("name_" + code);
        long description = insertInternationalString("description_" + code);
        long comment = insertInternationalString("comment_" + code);

        insertSentences.add("INSERT INTO TB_ANNOTABLE_ARTEFACTS (ID, UUID, VERSION, ANNOTABLE_ARTEFACT_TYPE, CODE, URN, URN_PROVIDER, NAME_FK, DESCRIPTION_FK, COMMENT_FK) values (" + id + ", " + id
                + ", 1, 'NAMEABLE_ARTEFACT', '" + code + "', '" + urn + "', '" + urn + "', " + name + ", " + description + ", " + comment + ");");

        insertAnnotation(id);
        insertAnnotation(id);
        insertAnnotation(id);

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
    private static void insertItem(long idCodelist, long idNameableArtefact) {
        insertSentences.add("INSERT INTO TB_ITEMS (ID, UUID, VERSION, NAMEABLE_ARTEFACT_FK, ITEM_SCHEME_VERSION_FK, ITEM_SCHEME_VERSION_FIRST_FK) values (" + idCodes + ", " + idCodes + ", 1,"
                + idNameableArtefact + ", " + idCodelist + ", " + idCodelist + ");");
    }

    /**
     * CREATE TABLE TB_CODES (
     * TB_ITEMS NUMBER(19) NOT NULL
     * );
     */
    private static void insertCode() {
        insertSentences.add("INSERT INTO TB_CODES (TB_ITEMS) values (" + idCodes + ");");
    }

    /**
     * CREATE TABLE TB_M_CODES (
     * SHORT_NAME_FK NUMBER(19),
     * VARIABLE_ELEMENT_FK NUMBER(19),
     * TB_CODES NUMBER(19) NOT NULL
     * );
     */
    private static void insertCodeMetamac(long variableElement) {
        String subcode = idCodes.toString();
        long shortName = insertInternationalString("shortName_" + subcode);
        insertSentences.add("INSERT INTO TB_M_CODES (SHORT_NAME_FK, VARIABLE_ELEMENT_FK, TB_CODES) values (" + shortName + ", " + variableElement + ", " + idCodes + ");");
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
