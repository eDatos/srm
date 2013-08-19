package org.siemac.metamac.srm.web.client.widgets;

import org.siemac.metamac.srm.core.code.domain.shared.VariableElementVisualisationResult;
import org.siemac.metamac.srm.web.code.model.ds.CodeDS;
import org.siemac.metamac.srm.web.shared.utils.RelatedResourceUtils;
import org.siemac.metamac.web.common.client.utils.CommonWebUtils;
import org.siemac.metamac.web.common.client.widgets.NavigableTreeNode;

import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;

public class CodeNavigableTreeNode extends NavigableTreeNode {

    public void setVariableElement(VariableElementVisualisationResult variableElement) {
        setAttribute(CodeDS.VARIABLE_ELEMENT, variableElement != null ? CommonWebUtils.getElementName(variableElement.getCode(), variableElement.getShortName()) : null);
        setAttribute(CodeDS.VARIABLE_ELEMENT_ID_DATABASE, variableElement != null ? variableElement.getIdDatabase() : null);
        setAttribute(CodeDS.VARIABLE_ELEMENT_URN, variableElement != null ? variableElement.getUrn() : null);
    }

    public void setVariableElement(RelatedResourceDto variableElement) {
        setAttribute(CodeDS.VARIABLE_ELEMENT, RelatedResourceUtils.getRelatedResourceName(variableElement));
        setAttribute(CodeDS.VARIABLE_ELEMENT_ID_DATABASE, variableElement != null ? variableElement.getId() : null);
        setAttribute(CodeDS.VARIABLE_ELEMENT_URN, variableElement != null ? variableElement.getUrn() : null);
    }

    public String getVariableElementUrn() {
        return getAttribute(CodeDS.VARIABLE_ELEMENT_URN);
    }
}
