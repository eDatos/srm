package org.siemac.metamac.srm.web.concept.model.record;

import org.siemac.metamac.srm.core.concept.dto.ConceptMetamacDto;
import org.siemac.metamac.srm.web.concept.model.ds.ConceptDS;

import com.smartgwt.client.widgets.tree.TreeNode;

public class ConceptTreeNode extends TreeNode {

    public ConceptTreeNode(Long id, String code, String name, String urn, ConceptMetamacDto conceptDto) {
        setId(id);
        setCode(code);
        setName(name);
        setUrn(urn);
        setConceptDto(conceptDto);
    }

    public void setId(Long id) {
        setAttribute(ConceptDS.ID, id);
    }

    public void setName(String name) {
        setAttribute(ConceptDS.NAME, name);
    }

    public void setUrn(String value) {
        setAttribute(ConceptDS.URN, value);
    }

    public void setCode(String code) {
        setAttribute(ConceptDS.CODE, code);
    }

    public void setConceptDto(ConceptMetamacDto conceptDto) {
        setAttribute(ConceptDS.DTO, conceptDto);
    }

    public Long getId() {
        return getAttributeAsLong(ConceptDS.ID);
    }

    public String getCode() {
        return getAttribute(ConceptDS.CODE);
    }

    public String getName() {
        return getAttribute(ConceptDS.NAME);
    }

    public String getUrn() {
        return getAttribute(ConceptDS.URN);
    }

    public ConceptMetamacDto getConceptDto() {
        return (ConceptMetamacDto) getAttributeAsObject(ConceptDS.DTO);
    }

}
