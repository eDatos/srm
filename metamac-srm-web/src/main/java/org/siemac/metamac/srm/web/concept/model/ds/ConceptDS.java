package org.siemac.metamac.srm.web.concept.model.ds;

import org.siemac.metamac.srm.web.client.MetamacSrmWeb;

import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.fields.DataSourceIntegerField;

public class ConceptDS extends DataSource {

    // IDENTIFIERS
    public static final String ID                    = "con-id";
    public static final String ID_LOGIC              = "con-id-logic";
    public static final String URI                   = "con-uri";
    public static final String URN                   = "con-urn";
    public static final String NAME                  = "con-name";
    public static final String PLURAL_NAME           = "con-plu-name";
    public static final String PLURAL_ACRONYM        = "con-acron";
    // CONTENT DESCRIPTORS
    public static final String DESCRIPTION           = "con-desc";
    public static final String DESCRIPTION_SOURCE    = "con-desc-source";
    public static final String CONTEXT               = "con-context";
    public static final String DOC_METHOD            = "con-doc-method";
    // CLASS DESCRIPTORS
    public static final String SDMX_RELATED_ARTEFACT = "con-sdmx-art";
    public static final String TYPE                  = "con-type";
    public static final String ROLES                 = "con-roles";
    // PRODUCTION DESCRIPTORS
    public static final String DERIVATION            = "con-derivation";
    // RELATION WITH CONCEPS
    public static final String EXTENDS               = "con-extends";
    public static final String RELATED_CONCEPTS      = "con-related-concepts";
    // LEGAL ACTS
    public static final String LEGAL_ACTS            = "con-legal-acts";

    public static String       DTO                   = "con-dto";

    public ConceptDS() {
        DataSourceIntegerField idLogic = new DataSourceIntegerField(ID_LOGIC, MetamacSrmWeb.getConstants().conceptSchemeIdLogic());
        idLogic.setPrimaryKey(true);
        addField(idLogic);
    }

}
