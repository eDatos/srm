package org.siemac.metamac.srm.web.client.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import org.siemac.metamac.srm.web.client.model.ds.VersionableResourceDS;
import org.siemac.metamac.web.common.client.utils.ListGridUtils;
import org.siemac.metamac.web.common.client.widgets.BaseCustomListGrid;
import org.siemac.metamac.web.common.client.widgets.CustomListGridField;

public class VersionableResourceListGrid extends BaseCustomListGrid {

    public VersionableResourceListGrid() {
        super();

        this.setShowAllRecords(true);

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

        this.setFields(code, urn, version, name, procStatus, maintainer, creationDate, resourceCreationDate, externalPublicationDate);
    }
}
