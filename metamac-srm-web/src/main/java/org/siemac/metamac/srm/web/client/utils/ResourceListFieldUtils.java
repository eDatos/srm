package org.siemac.metamac.srm.web.client.utils;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import org.siemac.metamac.srm.web.client.model.ds.VersionableResourceDS;
import org.siemac.metamac.srm.web.organisation.model.ds.OrganisationSchemeDS;
import org.siemac.metamac.web.common.client.utils.ListGridUtils;
import org.siemac.metamac.web.common.client.widgets.CustomListGridField;

public class ResourceListFieldUtils {

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

    public static CustomListGridField[] getCategorySchemeFields() {
        return getVersionableResourceFields();
    }

    public static CustomListGridField[] getOrganisationSchemeFields() {
        CustomListGridField typeField = new CustomListGridField(OrganisationSchemeDS.TYPE, getConstants().organisationSchemeType());
        typeField.setShowIfCondition(ListGridUtils.getFalseListGridFieldIfFunction());

        return ListGridUtils.addFields(getVersionableResourceFields(), typeField);
    }
}
