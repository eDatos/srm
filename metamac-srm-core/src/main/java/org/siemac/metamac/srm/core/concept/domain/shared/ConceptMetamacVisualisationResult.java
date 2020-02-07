package org.siemac.metamac.srm.core.concept.domain.shared;

import org.siemac.metamac.srm.core.concept.enume.domain.ConceptRoleEnum;

import com.arte.statistic.sdmx.srm.core.common.domain.shared.ItemVisualisationResult;
import com.arte.statistic.sdmx.srm.core.common.domain.shared.RelatedResourceVisualisationResult;

public class ConceptMetamacVisualisationResult extends ItemVisualisationResult {

    private static final long                  serialVersionUID = 1L;

    private String                             acronym;
    private RelatedResourceVisualisationResult variable;
    private ConceptRoleEnum                    sdmxRelatedArtefact;
    private Integer                            order;

    public ConceptMetamacVisualisationResult() {
    }

    public String getAcronym() {
        return acronym;
    }

    public void setAcronym(String acronym) {
        this.acronym = acronym;
    }

    public RelatedResourceVisualisationResult getVariable() {
        return variable;
    }

    public void setVariable(RelatedResourceVisualisationResult variable) {
        this.variable = variable;
    }

    public ConceptRoleEnum getSdmxRelatedArtefact() {
        return sdmxRelatedArtefact;
    }

    public void setSdmxRelatedArtefact(ConceptRoleEnum sdmxRelatedArtefact) {
        this.sdmxRelatedArtefact = sdmxRelatedArtefact;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }
}