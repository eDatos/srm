package org.siemac.metamac.srm.core.task.domain;

public class ImportationConceptsTsvHeader extends ImportationItemsTsvHeader {

    private InternationalStringTsv pluralName;
    private InternationalStringTsv acronym;
    private InternationalStringTsv descriptionSource;
    private InternationalStringTsv context;
    private InternationalStringTsv docMethod;
    private InternationalStringTsv derivation;
    private InternationalStringTsv legalActs;

    public InternationalStringTsv getPluralName() {
        return pluralName;
    }

    public void setPluralName(InternationalStringTsv pluralName) {
        this.pluralName = pluralName;
    }

    public InternationalStringTsv getAcronym() {
        return acronym;
    }

    public void setAcronym(InternationalStringTsv acronym) {
        this.acronym = acronym;
    }

    public InternationalStringTsv getDescriptionSource() {
        return descriptionSource;
    }

    public void setDescriptionSource(InternationalStringTsv descriptionSource) {
        this.descriptionSource = descriptionSource;
    }

    public InternationalStringTsv getContext() {
        return context;
    }

    public void setContext(InternationalStringTsv context) {
        this.context = context;
    }

    public InternationalStringTsv getDocMethod() {
        return docMethod;
    }

    public void setDocMethod(InternationalStringTsv docMethod) {
        this.docMethod = docMethod;
    }

    public InternationalStringTsv getDerivation() {
        return derivation;
    }

    public void setDerivation(InternationalStringTsv derivation) {
        this.derivation = derivation;
    }

    public InternationalStringTsv getLegalActs() {
        return legalActs;
    }

    public void setLegalActs(InternationalStringTsv legalActs) {
        this.legalActs = legalActs;
    }

}
