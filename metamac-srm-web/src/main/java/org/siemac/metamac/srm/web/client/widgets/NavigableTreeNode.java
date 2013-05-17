package org.siemac.metamac.srm.web.client.widgets;

import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.core.common.dto.RelatedResourceBaseDto;
import org.siemac.metamac.web.common.client.utils.ExternalItemUtils;
import org.siemac.metamac.web.common.client.utils.NavigationUtils;
import org.siemac.metamac.web.common.shared.RelatedResourceBaseUtils;

import com.smartgwt.client.widgets.tree.TreeNode;

public class NavigableTreeNode extends TreeNode {

    public void setRelatedResource(String property, RelatedResourceBaseDto relatedResourceBaseDto) {
        setAttribute(property, RelatedResourceBaseUtils.getRelatedResourceName(relatedResourceBaseDto));
        setAttribute(NavigationUtils.getDtoPropertyName(property), relatedResourceBaseDto);
    }

    public void setExternalItem(String property, ExternalItemDto externalItemDto) {
        setAttribute(property, ExternalItemUtils.getExternalItemName(externalItemDto));
        setAttribute(NavigationUtils.getDtoPropertyName(property), externalItemDto);
    }
}
