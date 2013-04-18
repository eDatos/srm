package org.siemac.metamac.srm.core.code.domain.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CodeToCopyHierarchy implements Serializable {

    private static final long         serialVersionUID = 1L;

    private String                    sourceUrn;
    private String                    newCodeIdentifier;
    private List<CodeToCopyHierarchy> children;

    public CodeToCopyHierarchy() {
    }

    public String getSourceUrn() {
        return sourceUrn;
    }

    public void setSourceUrn(String sourceUrn) {
        this.sourceUrn = sourceUrn;
    }

    /**
     * fill only if code must be changed
     */

    public String getNewCodeIdentifier() {
        return newCodeIdentifier;
    }

    public void setNewCodeIdentifier(String newCodeIdentifier) {
        this.newCodeIdentifier = newCodeIdentifier;
    }

    public List<CodeToCopyHierarchy> getChildren() {
        if (children == null) {
            children = new ArrayList<CodeToCopyHierarchy>();
        }
        return children;
    }
}