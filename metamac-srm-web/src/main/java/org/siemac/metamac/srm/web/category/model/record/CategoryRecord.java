package org.siemac.metamac.srm.web.category.model.record;

import org.siemac.metamac.srm.core.category.dto.CategoryMetamacBasicDto;
import org.siemac.metamac.srm.core.category.dto.CategoryMetamacDto;
import org.siemac.metamac.srm.web.category.model.ds.CategoryDS;

import com.smartgwt.client.widgets.grid.ListGridRecord;

public class CategoryRecord extends ListGridRecord {

    public CategoryRecord() {
    }

    public CategoryRecord(Long id, String code, String name, String urn, String categorySchemeUrn, String description, CategoryMetamacDto categoryDto) {
        setId(id);
        setCode(code);
        setName(name);
        setUrn(urn);
        setCategorySchemeUrn(categorySchemeUrn);
        setDescription(description);
        setCategoryDto(categoryDto);
    }

    public void setId(Long id) {
        setAttribute(CategoryDS.ID, id);
    }

    public void setName(String name) {
        setAttribute(CategoryDS.NAME, name);
    }

    public void setUrn(String value) {
        setAttribute(CategoryDS.URN, value);
    }

    public void setCategorySchemeUrn(String value) {
        setAttribute(CategoryDS.ITEM_SCHEME_URN, value);
    }

    public void setDescription(String desc) {
        setAttribute(CategoryDS.DESCRIPTION, desc);
    }

    public void setCode(String code) {
        setAttribute(CategoryDS.CODE, code);
    }

    public void setCategoryDto(CategoryMetamacDto categoryDto) {
        setAttribute(CategoryDS.DTO, categoryDto);
    }

    public void setCategoryBasicDto(CategoryMetamacBasicDto categoryDto) {
        setAttribute(CategoryDS.DTO, categoryDto);
    }

    public Long getId() {
        return getAttributeAsLong(CategoryDS.ID);
    }

    public String getCode() {
        return getAttribute(CategoryDS.CODE);
    }

    public String getName() {
        return getAttribute(CategoryDS.NAME);
    }

    public String getUrn() {
        return getAttribute(CategoryDS.URN);
    }

    public String getCategorySchemeUrn() {
        return getAttribute(CategoryDS.ITEM_SCHEME_URN);
    }

    public String getDescription() {
        return getAttribute(CategoryDS.DESCRIPTION);
    }

    public CategoryMetamacDto getCategoryDto() {
        return (CategoryMetamacDto) getAttributeAsObject(CategoryDS.DTO);
    }

    public CategoryMetamacBasicDto getCategoryBasicDto() {
        return (CategoryMetamacBasicDto) getAttributeAsObject(CategoryDS.DTO);
    }
}
