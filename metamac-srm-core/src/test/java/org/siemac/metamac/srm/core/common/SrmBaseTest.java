package org.siemac.metamac.srm.core.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.arte.statistic.sdmx.srm.core.common.SdmxSrmBaseTest;

public abstract class SrmBaseTest extends SdmxSrmBaseTest {

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
