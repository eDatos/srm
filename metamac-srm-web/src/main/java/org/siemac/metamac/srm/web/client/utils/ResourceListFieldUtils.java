package org.siemac.metamac.srm.web.client.utils;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import org.siemac.metamac.srm.web.client.model.ds.VersionableResourceDS;
import org.siemac.metamac.srm.web.code.model.ds.CodelistDS;
import org.siemac.metamac.srm.web.concept.model.ds.ConceptSchemeDS;
import org.siemac.metamac.srm.web.dsd.model.ds.DataStructureDefinitionDS;
import org.siemac.metamac.srm.web.organisation.model.ds.OrganisationSchemeDS;
import org.siemac.metamac.web.common.client.utils.ListGridUtils;
import org.siemac.metamac.web.common.client.widgets.CustomListGridField;

public class ResourceListFieldUtils {

    //
    // CATEGORIES
    //

    public static CustomListGridField[] getCategorySchemeFields() {
        return getVersionableResourceFields();
    }

    //
    // ORGANSIATIONS
    //

    public static CustomListGridField[] getOrganisationSchemeFields() {
        CustomListGridField typeField = new CustomListGridField(OrganisationSchemeDS.TYPE, getConstants().organisationSchemeType());
        typeField.setShowIfCondition(ListGridUtils.getFalseListGridFieldIfFunction());

        return ListGridUtils.addFields(getVersionableResourceFields(), typeField);
    }

    //
    // CONCEPTS
    //

    public static CustomListGridField[] getConceptSchemeFields() {
        CustomListGridField typeField = new CustomListGridField(ConceptSchemeDS.TYPE, getConstants().conceptSchemeType());
        typeField.setShowIfCondition(ListGridUtils.getFalseListGridFieldIfFunction());

        CustomListGridField statisticalOperation = new CustomListGridField(ConceptSchemeDS.RELATED_OPERATION, getConstants().conceptSchemeOperation());
        statisticalOperation.setShowIfCondition(ListGridUtils.getFalseListGridFieldIfFunction());

        return ListGridUtils.addFields(getVersionableResourceFields(), typeField, statisticalOperation);
    }

    //
    // CODES
    //

    public static CustomListGridField[] getCodelistFields() {
        CustomListGridField isRecommended = new CustomListGridField(CodelistDS.IS_RECOMMENDED, getConstants().codelistIsRecommended());

        CustomListGridField variable = new CustomListGridField(CodelistDS.VARIABLE, getConstants().variable());
        variable.setShowIfCondition(ListGridUtils.getFalseListGridFieldIfFunction());

        return ListGridUtils.addFields(getVersionableResourceFields(), isRecommended, variable);
    }

    //
    // DSD
    //

    public static CustomListGridField[] getDsdFields() {
        CustomListGridField statisticalOperation = new CustomListGridField(DataStructureDefinitionDS.STATISTICAL_OPERATION, getConstants().dsdOperation());
        return ListGridUtils.addFields(getVersionableResourceFields(), statisticalOperation);
    }

    //
    // VERSIONABLE RESOURCE
    //

    public static CustomListGridField[] getVersionableResourceFields() {

        CustomListGridField code = new CustomListGridField(VersionableResourceDS.CODE, getConstants().identifiableArtefactCode());

        CustomListGridField urn = new CustomListGridField(VersionableResourceDS.URN, getConstants().identifiableArtefactUrn());
        urn.setShowIfCondition(ListGridUtils.getFalseListGridFieldIfFunction());

        CustomListGridField version = new CustomListGridField(VersionableResourceDS.VERSION_LOGIC, getConstants().maintainableArtefactVersionLogic());

        CustomListGridField name = new CustomListGridField(VersionableResourceDS.NAME, getConstants().nameableArtefactName());

        CustomListGridField maintainer = new CustomListGridField(VersionableResourceDS.MAINTAINER, getConstants().maintainableArtefactMaintainer());
        maintainer.setShowIfCondition(ListGridUtils.getFalseListGridFieldIfFunction());

        CustomListGridField procStatus = new CustomListGridField(VersionableResourceDS.PROC_STATUS, getConstants().lifeCycleProcStatus());

        CustomListGridField creationDate = new CustomListGridField(VersionableResourceDS.VERSION_CREATION_DATE, getConstants().maintainableArtefactVersionCreationDate());
        creationDate.setShowIfCondition(ListGridUtils.getFalseListGridFieldIfFunction());

        CustomListGridField resourceCreationDate = new CustomListGridField(VersionableResourceDS.RESOURCE_CREATION_DATE, getConstants().maintainableArtefactResourceCreationDate());
        resourceCreationDate.setShowIfCondition(ListGridUtils.getFalseListGridFieldIfFunction());

        CustomListGridField externalPublicationDate = new CustomListGridField(VersionableResourceDS.EXTERNAL_PUBLICATION_DATE, getConstants().lifeCycleExternalPublicationDate());
        externalPublicationDate.setShowIfCondition(ListGridUtils.getFalseListGridFieldIfFunction());

        return new CustomListGridField[]{code, urn, version, name, maintainer, procStatus, creationDate, resourceCreationDate, externalPublicationDate};
    }
}
