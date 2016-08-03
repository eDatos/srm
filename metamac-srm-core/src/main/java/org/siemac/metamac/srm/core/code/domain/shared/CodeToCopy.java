package org.siemac.metamac.srm.core.code.domain.shared;

import java.io.Serializable;

public class CodeToCopy implements Serializable {

    private static final long serialVersionUID = 1L;

    private String            sourceUrn;
    private String            newCodeIdentifier;
    private String            parentNewCodeIdentifier;

    public CodeToCopy() {
    }

    public String getSourceUrn() {
        return sourceUrn;
    }

    public void setSourceUrn(String sourceUrn) {
        this.sourceUrn = sourceUrn;
    }

    /**
     * fill with new code of parent
     */

    public String getParentNewCodeIdentifier() {
        return parentNewCodeIdentifier;
    }

    public void setParentNewCodeIdentifier(String parentNewCodeIdentifier) {
        this.parentNewCodeIdentifier = parentNewCodeIdentifier;
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
}