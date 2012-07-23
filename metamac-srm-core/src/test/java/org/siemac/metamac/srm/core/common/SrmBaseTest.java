package org.siemac.metamac.srm.core.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.fornax.cartridges.sculptor.framework.errorhandling.ServiceContext;
import org.siemac.metamac.common.test.MetamacBaseTests;

public abstract class SrmBaseTest extends MetamacBaseTests {

    // --------------------------------------------------------------------------------------------------------------
    // SERVICE CONTEXT
    // --------------------------------------------------------------------------------------------------------------

    @Override
    protected ServiceContext getServiceContextAdministrador() {
        ServiceContext serviceContext = super.getServiceContextWithoutPrincipal();

        // TODO: Descomentar cuando esten los roles
        // putMetamacPrincipalInServiceContext(serviceContext, AccessControlRoleEnum.ADMINISTRADOR);
        return serviceContext;
    }

    // TODO: Descomentar cuando ya esten definidos los roles
    // private void putMetamacPrincipalInServiceContext(ServiceContext serviceContext, AccessControlRoleEnum role) {
    // MetamacPrincipal metamacPrincipal = new MetamacPrincipal();
    // metamacPrincipal.setUserId(serviceContext.getUserId());
    // metamacPrincipal.getAccesses().add(new MetamacPrincipalAccess(role.getName(), SrmConstants.SECURITY_APPLICATION_ID, null));
    // serviceContext.setProperty(SsoClientConstants.PRINCIPAL_ATTRIBUTE, metamacPrincipal);
    // }

    // --------------------------------------------------------------------------------------------------------------
    // DBUNIT CONFIGURATION
    // --------------------------------------------------------------------------------------------------------------

    @Override
    protected String getDataSetFile() {
        return "dbunit/SrmCoreTest.xml";
    }

    @Override
    protected List<String> getTablesToRemoveContent() {
        List<String> tables = new ArrayList<String>();

        tables.add("TB_MEASURE_DIMEN_EI_ROLES");
        tables.add("TB_DIMREL_GROUPDIM");
        tables.add("TB_DIMREL_DIMCOM");
        tables.add("TB_DIMENSIONS_EI_ROLES");
        tables.add("TB_DATA_ATTRIBUTES_EI_ROLES");
        tables.add("TB_COMPLIST_COMP");
        tables.add("TB_ATTR_RELATIONSHIPS");
        tables.add("TB_LOCALISED_STRINGS");
        tables.add("TB_FACETS");
        tables.add("TB_ANNOTATIONS");
        tables.add("TB_CONCEPT_SCHEMES_VERSIONS");
        tables.add("TB_CONCEPTS");
        tables.add("TB_REPRESENTATIONS");
        tables.add("TB_ITEMS");
        tables.add("TB_ITEM_SCHEMES_VERSIONS");
        tables.add("TB_EXTERNAL_ITEMS");
        tables.add("TB_ITEM_SCHEMES");
        tables.add("TB_LOCALISED_STRINGS");
        tables.add("TB_INTERNATIONAL_STRINGS");
        tables.add("TB_ANNOTABLE_ARTEFACTS");

        return tables;
    }

    @Override
    protected List<String> getSequencesToRestart() {
        List<String> sequences = new ArrayList<String>();
        sequences.add("SEQ_I18NSTRS");
        sequences.add("SEQ_L10NSTRS");
        sequences.add("SEQ_DIMREL_GROUPDIM");
        sequences.add("SEQ_DIMREL_DIMCOM");
        sequences.add("SEQ_DIMENSIONS_EI_ROLES");
        sequences.add("SEQ_DATA_ATTRIBUTES_EI_ROLES");
        sequences.add("SEQ_COMPLIST_COMP");
        sequences.add("SEQ_ATTR_RELATIONSHIPS");
        sequences.add("SEQ_LOCALISED_STRINGS");
        sequences.add("SEQ_FACETS");
        sequences.add("SEQ_ANNOTATIONS");
        sequences.add("SEQ_CONCEPT_SCHEMES_VERSIONS");
        sequences.add("SEQ_CONCEPTS");
        sequences.add("SEQ_REPRESENTATIONS");
        sequences.add("SEQ_ITEMS");
        sequences.add("SEQ_ITEM_SCHEMES_VERSIONS");
        sequences.add("SEQ_ITEM_SCHEMES");
        sequences.add("SEQ_ITEM_SCHEMES_VERSIONS");
        sequences.add("SEQ_EXTERNAL_ITEMS");
        sequences.add("SEQ_ITEM_SCHEMES");
        sequences.add("SEQ_INTERNATIONAL_STRINGS");
        sequences.add("SEQ_ANNOTABLE_ARTEFACTS");
        return sequences;
    }

    @Override
    protected Map<String, String> getTablePrimaryKeys() {
        Map<String, String> tablePrimaryKeys = new HashMap<String, String>();
        tablePrimaryKeys.put("TB_CONCEPT_SCHEMES_VERSIONS", "TB_ITEM_SCHEMES_VERSIONS");
        return tablePrimaryKeys;
    }
}
