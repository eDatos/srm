package org.siemac.metamac.srm.web.concept.utils;

import org.siemac.metamac.srm.core.concept.domain.shared.ConceptMetamacVisualisationResult;
import org.siemac.metamac.srm.web.client.utils.ItemsTreeGridUtils;
import org.siemac.metamac.srm.web.concept.model.ds.ConceptDS;
import org.siemac.metamac.srm.web.shared.utils.RelatedResourceUtils;

import com.smartgwt.client.widgets.tree.TreeNode;

public class ConceptsTreeGridUtils extends ItemsTreeGridUtils {

    public static TreeNode createConceptTreeNode(String schemeNodeName, ConceptMetamacVisualisationResult concept) {
        TreeNode node = createItemTreeNode(schemeNodeName, concept);
        node.setAttribute(ConceptDS.ACRONYM, concept.getAcronym());
        node.setAttribute(ConceptDS.VARIABLE, RelatedResourceUtils.getRelatedResourceName(concept.getVariable()));
        node.setAttribute(ConceptDS.SDMX_RELATED_ARTEFACT, CommonUtils.getConceptRoleName(concept.getSdmxRelatedArtefact()));
        return node;
    }
}
