package org.siemac.metamac.srm.core.code.domain;

import java.util.HashMap;
import java.util.Map;

import com.arte.statistic.sdmx.srm.core.common.domain.IdentifiableArtefactResult;

public class VariableElementResult extends IdentifiableArtefactResult {

    private Long                      idDatabase;
    private final Map<String, String> shortName = new HashMap<String, String>();

    public Long getIdDatabase() {
        return idDatabase;
    }

    public void setIdDatabase(Long idDatabase) {
        this.idDatabase = idDatabase;
    }

    public Map<String, String> getShortName() {
        return shortName;
    }
}
