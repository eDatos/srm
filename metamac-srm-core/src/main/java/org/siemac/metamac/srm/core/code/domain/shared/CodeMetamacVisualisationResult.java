package org.siemac.metamac.srm.core.code.domain.shared;

import java.io.Serializable;

public class CodeMetamacVisualisationResult implements Serializable {

    private static final long              serialVersionUID = 1L;

    private Long                           itemIdDatabase;
    private String                         code;
    private String                         urn;
    private Long                           parentIdDatabase;
    private CodeMetamacVisualisationResult parent;
    private String                         name;
    private Long                           order;

    public CodeMetamacVisualisationResult() {
    }

    public Long getItemIdDatabase() {
        return itemIdDatabase;
    }

    public void setItemIdDatabase(Long itemIdDatabase) {
        this.itemIdDatabase = itemIdDatabase;
    }

    public Long getParentIdDatabase() {
        return parentIdDatabase;
    }

    public void setParentIdDatabase(Long parentIdDatabase) {
        this.parentIdDatabase = parentIdDatabase;
    }

    public CodeMetamacVisualisationResult getParent() {
        return parent;
    }

    public void setParent(CodeMetamacVisualisationResult parent) {
        this.parent = parent;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUrn() {
        return urn;
    }

    public void setUrn(String urn) {
        this.urn = urn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getOrder() {
        return order;
    }

    public void setOrder(Long order) {
        this.order = order;
    }

}