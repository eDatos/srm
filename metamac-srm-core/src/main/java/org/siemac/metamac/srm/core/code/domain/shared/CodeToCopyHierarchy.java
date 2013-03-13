package org.siemac.metamac.srm.core.code.domain.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CodeToCopyHierarchy implements Serializable {

    private static final long         serialVersionUID = 1L;

    private String                    urn;
    private String                    code;
    private List<CodeToCopyHierarchy> children;

    public CodeToCopyHierarchy() {
    }

    public String getUrn() {
        return urn;
    }
    public void setUrn(String urn) {
        this.urn = urn;
    }

    /**
     * fill only if code must be changed
     */
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<CodeToCopyHierarchy> getChildren() {
        if (children == null) {
            children = new ArrayList<CodeToCopyHierarchy>();
        }
        return children;
    }
}