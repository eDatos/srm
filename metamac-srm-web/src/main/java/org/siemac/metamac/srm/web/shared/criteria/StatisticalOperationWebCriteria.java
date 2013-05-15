package org.siemac.metamac.srm.web.shared.criteria;

import org.siemac.metamac.web.common.shared.criteria.MetamacWebCriteria;

public class StatisticalOperationWebCriteria extends MetamacWebCriteria {

    private static final long serialVersionUID = 1L;

    /**
     * If it is TRUE, all the operations will be included in the search (not only the user operations)
     */
    private boolean           noFilterByUserPrincipal;

    public StatisticalOperationWebCriteria() {
    }

    public StatisticalOperationWebCriteria(String criteria) {
        super(criteria);
    }

    public boolean isNoFilterByUserPrincipal() {
        return noFilterByUserPrincipal;
    }

    public void setNoFilterByUserPrincipal(boolean noFilterByUserPrincipal) {
        this.noFilterByUserPrincipal = noFilterByUserPrincipal;
    }
}
