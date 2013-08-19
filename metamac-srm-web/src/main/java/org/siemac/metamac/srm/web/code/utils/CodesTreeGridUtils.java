package org.siemac.metamac.srm.web.code.utils;

import org.siemac.metamac.core.common.util.shared.BooleanUtils;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.code.domain.shared.CodeMetamacVisualisationResult;
import org.siemac.metamac.srm.core.code.domain.shared.CodeVariableElementNormalisationResult;
import org.siemac.metamac.srm.core.code.domain.shared.VariableElementVisualisationResult;
import org.siemac.metamac.srm.web.client.model.ds.ItemDS;
import org.siemac.metamac.srm.web.client.resources.GlobalResources;
import org.siemac.metamac.srm.web.client.utils.ItemsTreeGridUtils;
import org.siemac.metamac.srm.web.code.model.ds.CodeDS;
import org.siemac.metamac.srm.web.code.widgets.CodeTreeNode;
import org.siemac.metamac.web.common.client.utils.DateUtils;

import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.ItemSchemeDto;
import com.smartgwt.client.widgets.tree.TreeNode;

public class CodesTreeGridUtils extends ItemsTreeGridUtils {

    public static TreeNode createCodelistTreeNode(String schemeNodeName, ItemSchemeDto itemSchemeDto) {
        return createItemSchemeTreeNode(schemeNodeName, itemSchemeDto);
    }

    public static CodeTreeNode createCodeTreeNode(String schemeNodeName, CodeMetamacVisualisationResult code) {
        CodeTreeNode node = new CodeTreeNode(code.getName());
        node.setID(code.getUrn());
        node.setIsFolder(true);
        // If the code has no parent, the parent is the codelist
        node.setParentID(code.getParent() != null ? code.getParent().getUrn() : schemeNodeName);
        node.setAttribute(CodeDS.ID_DATABASE, code.getItemIdDatabase());
        node.setAttribute(ItemDS.CODE, code.getCode());
        node.setAttribute(ItemDS.NAME, code.getName() != null ? code.getName() : StringUtils.EMPTY);
        node.setAttribute(ItemDS.URN, code.getUrn());
        node.setAttribute(ItemDS.DESCRIPTION, code.getDescription());
        node.setAttribute(ItemDS.CREATION_DATE, DateUtils.getFormattedDate(code.getCreatedDate()));
        node.setAttribute(ItemDS.ITEM_PARENT_URN, code.getParent() != null ? code.getParent().getUrn() : schemeNodeName);
        node.setAttribute(ItemDS.DTO, code);
        // Specify the order of the node in its level.
        // Avoid null pointer in CodeTreeNode comparator setting 0 when there is no order defined.
        node.setOrder(code.getOrder() == null ? 0 : code.getOrder());
        // Specify the openness level
        node.setAttribute(CodeDS.OPENNESS_LEVEL, code.getOpenness());
        node.setAttribute(CodeDS.OPENNESS_LEVEL_INITIAL, code.getOpenness()); // The value is unmodifiable by the user
        String iconUrl = BooleanUtils.isTrue(code.getOpenness()) ? GlobalResources.RESOURCE.folderOpened().getURL() : GlobalResources.RESOURCE.folderClosed().getURL();
        node.setAttribute(CodeDS.OPENNESS_LEVEL_ICON, iconUrl);

        // Only for variable elements tree grid
        node.setVariableElement(code.getVariableElement());

        // Info icon
        node.setAttribute(CodeDS.INFO, org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE.info().getURL());
        // Icons for the variable elements assignment tree)
        node.setAttribute(CodeDS.VARIABLE_ELEMENT_EDITION, org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE.search().getURL());
        node.setAttribute(CodeDS.VARIABLE_ELEMENT_CLEAR, org.siemac.metamac.web.common.client.resources.GlobalResources.RESOURCE.deleteListGrid().getURL());

        return node;
    }

    public static CodeTreeNode createCodeTreeNode(String schemeNodeName, CodeVariableElementNormalisationResult result) {
        CodeMetamacVisualisationResult code = result.getCode();
        VariableElementVisualisationResult variableElement = result.getVariableElementProposed();
        CodeTreeNode node = createCodeTreeNode(schemeNodeName, code);
        node.setVariableElement(variableElement);
        return node;
    }

    public static void setVariableElementInRecord(CodeTreeNode record, RelatedResourceDto variableElement) {
        record.setVariableElement(variableElement);
    }

    public static void clearVariableElementFromRecord(CodeTreeNode record) {
        setVariableElementInRecord(record, null);
    }
}
