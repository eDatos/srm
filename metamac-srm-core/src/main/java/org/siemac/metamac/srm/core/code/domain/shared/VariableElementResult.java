package org.siemac.metamac.srm.core.code.domain.shared;

import java.io.Serializable;

public class VariableElementResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long              idDatabase;
    private String            code;
    private String            urn;
    private String            shortName;

    public VariableElementResult() {
    }

    public Long getIdDatabase() {
        return idDatabase;
    }

    public void setIdDatabase(Long idDatabase) {
        this.idDatabase = idDatabase;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUrn() {
        return urn;
    }

    public void setUrn(String urn) {
        this.urn = urn;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }
}