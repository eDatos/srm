package org.siemac.metamac.srm.core.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.arte.statistic.sdmx.srm.core.common.SdmxSrmBaseTest;

public abstract class SrmBaseTest extends SdmxSrmBaseTest {

    protected static final String CONCEPT_SCHEME_1_V1 = "urn:sdmx:org.sdmx.infomodel.concepscheme.ConceptScheme=ISTAC:CONCEPTSCHEME01(01.000)";
    protected static final String CONCEPT_SCHEME_1_V2 = "urn:sdmx:org.sdmx.infomodel.concepscheme.ConceptScheme=ISTAC:CONCEPTSCHEME01(02.000)";
    protected static final String CONCEPT_SCHEME_2_V1 = "urn:sdmx:org.sdmx.infomodel.concepscheme.ConceptScheme=ISTAC:CONCEPTSCHEME02(01.000)";
    protected static final String CONCEPT_SCHEME_3_V1 = "urn:sdmx:org.sdmx.infomodel.concepscheme.ConceptScheme=ISTAC:CONCEPTSCHEME03(01.000)";
    protected static final String CONCEPT_SCHEME_4_V1 = "urn:sdmx:org.sdmx.infomodel.concepscheme.ConceptScheme=ISTAC:CONCEPTSCHEME04(01.000)";
    protected static final String CONCEPT_SCHEME_5_V1 = "urn:sdmx:org.sdmx.infomodel.concepscheme.ConceptScheme=ISTAC:CONCEPTSCHEME05(01.000)";
    protected static final String CONCEPT_SCHEME_6_V1 = "urn:sdmx:org.sdmx.infomodel.concepscheme.ConceptScheme=ISTAC:CONCEPTSCHEME06(01.000)";
    protected static final String CONCEPT_SCHEME_7_V1 = "urn:sdmx:org.sdmx.infomodel.concepscheme.ConceptScheme=ISTAC:CONCEPTSCHEME07(01.000)";
    protected static final String CONCEPT_SCHEME_7_V2 = "urn:sdmx:org.sdmx.infomodel.concepscheme.ConceptScheme=ISTAC:CONCEPTSCHEME07(02.000)";
    protected static final String CONCEPT_SCHEME_8_V1 = "urn:sdmx:org.sdmx.infomodel.concepscheme.ConceptScheme=ISTAC:CONCEPTSCHEME08(01.000)";
    protected static final String CONCEPT_SCHEME_9_V1 = "urn:sdmx:org.sdmx.infomodel.concepscheme.ConceptScheme=ISTAC:CONCEPTSCHEME09(01.000)";

    protected static final String NOT_EXISTS          = "not-exists";

    // --------------------------------------------------------------------------------------------------------------
    // DBUNIT CONFIGURATION
    // --------------------------------------------------------------------------------------------------------------

    @Override
    protected List<String> getTablesToRemoveContent() {
        List<String> tables = new ArrayList<String>();
        tables.add("TB_M_CONCEPT_SCHEMES_VERSIONS");
        tables.addAll(super.getTablesToRemoveContent());
        return tables;
    }

    @Override
    protected List<String> getSequencesToRestart() {
        List<String> sequences = super.getSequencesToRestart();
        sequences.addAll(super.getSequencesToRestart());
        sequences.add("SEQ_M_CONCEPT_SCHEMES_VERSIONS");
        return sequences;
    }

    @Override
    protected Map<String, String> getTablePrimaryKeys() {
        Map<String, String> tablePrimaryKeys = super.getTablePrimaryKeys();
        tablePrimaryKeys.put("TB_M_CONCEPT_SCHEMES_VERSIONS", "TB_CONCEPT_SCHEMES_VERSIONS");
        tablePrimaryKeys.putAll(super.getTablePrimaryKeys());
        return tablePrimaryKeys;
    }

    @Override
    protected String getDataSetFile() {
        // throw exception because this method is already overrided by SdmxSrmBaseTest class, and in SRM must be override by children classes
        throw new IllegalArgumentException("Dbunit file must be override by children");
    }
}
