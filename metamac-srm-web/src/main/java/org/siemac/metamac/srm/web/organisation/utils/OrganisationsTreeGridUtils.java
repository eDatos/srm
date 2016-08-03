package org.siemac.metamac.srm.web.organisation.utils;

import org.siemac.metamac.srm.core.organisation.domain.shared.OrganisationMetamacVisualisationResult;
import org.siemac.metamac.srm.web.client.utils.ItemsTreeGridUtils;
import org.siemac.metamac.srm.web.organisation.model.ds.OrganisationDS;

import com.smartgwt.client.widgets.tree.TreeNode;

public class OrganisationsTreeGridUtils extends ItemsTreeGridUtils {

    public static TreeNode createOrganisationTreeNode(String schemeNodeName, OrganisationMetamacVisualisationResult organisation) {
        TreeNode node = createItemTreeNode(schemeNodeName, organisation);
        node.setAttribute(OrganisationDS.TYPE_NAME, CommonUtils.getOrganisationTypeName(organisation.getType()));
        return node;
    }
}
