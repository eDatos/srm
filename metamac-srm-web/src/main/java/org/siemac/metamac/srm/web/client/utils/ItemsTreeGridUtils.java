package org.siemac.metamac.srm.web.client.utils;

import static org.siemac.metamac.web.common.client.utils.InternationalStringUtils.getLocalisedString;

import org.siemac.metamac.srm.web.client.model.ds.ItemDS;
import org.siemac.metamac.web.common.client.utils.DateUtils;

import com.arte.statistic.sdmx.srm.core.common.domain.shared.ItemVisualisationResult;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ItemSchemeDto;
import com.smartgwt.client.widgets.tree.TreeNode;

public class ItemsTreeGridUtils {

    public static TreeNode createItemSchemeTreeNode(String schemeNodeName, ItemSchemeDto itemSchemeDto) {
        TreeNode node = new TreeNode(schemeNodeName);
        node.setID(schemeNodeName);
        node.setAttribute(ItemDS.URN, itemSchemeDto.getUrn());
        node.setAttribute(ItemDS.CODE, itemSchemeDto.getCode());
        node.setAttribute(ItemDS.NAME, getLocalisedString(itemSchemeDto.getName()));
        node.setAttribute(ItemDS.DESCRIPTION, getLocalisedString(itemSchemeDto.getDescription()));
        node.setAttribute(ItemDS.CREATION_DATE, itemSchemeDto.getCreatedDate());
        return node;
    }

    public static TreeNode createItemTreeNode(String schemeNodeName, ItemVisualisationResult itemVisualisationResult) {
        String parentUrn = itemVisualisationResult.getParent() != null ? itemVisualisationResult.getParent().getUrn() : schemeNodeName;

        TreeNode node = new TreeNode(itemVisualisationResult.getItemIdDatabase().toString());
        node.setID(itemVisualisationResult.getUrn());
        node.setParentID(parentUrn);
        node.setAttribute(ItemDS.CODE, itemVisualisationResult.getCode());
        node.setAttribute(ItemDS.NAME, itemVisualisationResult.getName());
        node.setAttribute(ItemDS.URN, itemVisualisationResult.getUrn());
        node.setAttribute(ItemDS.DESCRIPTION, itemVisualisationResult.getDescription());
        node.setAttribute(ItemDS.CREATION_DATE, DateUtils.getFormattedDate(itemVisualisationResult.getCreatedDate()));
        node.setAttribute(ItemDS.ITEM_PARENT_URN, parentUrn);
        node.setAttribute(ItemDS.DTO, itemVisualisationResult);
        return node;
    }
}
