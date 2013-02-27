package org.siemac.metamac.srm.web.client.utils;

import org.siemac.metamac.srm.web.client.model.ds.ItemDS;
import org.siemac.metamac.web.common.client.utils.InternationalStringUtils;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ItemSchemeDto;
import com.smartgwt.client.widgets.tree.TreeNode;

public class ItemsTreeGridUtils {

    public static TreeNode createItemSchemeTreeNode(String schemeNodeName, ItemSchemeDto itemSchemeDto) {
        TreeNode node = new TreeNode(schemeNodeName);
        node.setID(schemeNodeName);
        node.setAttribute(ItemDS.URN, itemSchemeDto.getUrn());
        node.setAttribute(ItemDS.CODE, itemSchemeDto.getCode());
        node.setAttribute(ItemDS.NAME, InternationalStringUtils.getLocalisedString(itemSchemeDto.getName()));
        return node;
    }
}
