package org.siemac.metamac.srm.web.code.utils;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.code.domain.shared.CodeMetamacVisualisationResult;
import org.siemac.metamac.srm.web.client.model.ds.ItemDS;
import org.siemac.metamac.srm.web.client.utils.ItemsTreeGridUtils;
import org.siemac.metamac.srm.web.code.widgets.CodeTreeNode;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ItemSchemeDto;
import com.smartgwt.client.widgets.tree.TreeNode;

public class CodesTreeGridUtils extends ItemsTreeGridUtils {

    public static TreeNode createCodelistTreeNode(String schemeNodeName, ItemSchemeDto itemSchemeDto) {
        return createItemSchemeTreeNode(schemeNodeName, itemSchemeDto);
    }

    public static CodeTreeNode createCodeTreeNode(String schemeNodeName, CodeMetamacVisualisationResult item) {
        CodeTreeNode node = new CodeTreeNode(item.getName());
        node.setID(item.getUrn());
        node.setIsFolder(true);
        // If the code has no parent, the parent is the codelist
        node.setParentID(item.getParent() != null ? item.getParent().getUrn() : schemeNodeName);
        node.setAttribute(ItemDS.CODE, item.getCode());
        node.setAttribute(ItemDS.NAME, item.getName() != null ? item.getName() : StringUtils.EMPTY);
        node.setAttribute(ItemDS.URN, item.getUrn());
        node.setAttribute(ItemDS.ITEM_PARENT_URN, item.getParent() != null ? item.getParent().getUrn() : null);
        node.setAttribute(ItemDS.DTO, item);
        // Specify the order of the node in its level.
        // Avoid null pointer in CodeTreeNode comparator setting 0 when there is no order defined.
        node.setOrder(item.getOrder() == null ? 0 : item.getOrder());
        return node;
    }
}
