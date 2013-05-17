package org.siemac.metamac.srm.web.client.utils;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import org.siemac.metamac.srm.web.client.model.ds.ItemDS;
import org.siemac.metamac.srm.web.client.model.ds.VersionableResourceDS;
import org.siemac.metamac.srm.web.code.model.ds.CodelistDS;
import org.siemac.metamac.srm.web.concept.model.ds.ConceptDS;
import org.siemac.metamac.srm.web.concept.model.ds.ConceptSchemeDS;
import org.siemac.metamac.srm.web.dsd.model.ds.DataStructureDefinitionDS;
import org.siemac.metamac.srm.web.organisation.model.ds.OrganisationDS;
import org.siemac.metamac.srm.web.organisation.model.ds.OrganisationSchemeDS;
import org.siemac.metamac.web.common.client.utils.ListGridUtils;
import org.siemac.metamac.web.common.client.widgets.CustomLinkListGridField;
import org.siemac.metamac.web.common.client.widgets.CustomListGridField;

import com.smartgwt.client.widgets.grid.GroupNode;
import com.smartgwt.client.widgets.grid.GroupTitleRenderer;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.viewer.DetailViewerField;

public class ResourceFieldUtils {

    //
    // CATEGORIES
    //

    public static CustomListGridField[] getCategorySchemeListGridFields() {
        return getVersionableResourceListGridFields();
    }

    public static CustomListGridField[] getCategoryListGridFields() {
        return getItemListGridFields();
    }

    public static DetailViewerField[] getCategoryDetailViewerFields() {
        return getItemDetailViewerFields();
    }

    //
    // ORGANSIATIONS
    //

    public static CustomListGridField[] getOrganisationSchemeListGridFields() {
        CustomListGridField typeField = new CustomListGridField(OrganisationSchemeDS.TYPE, getConstants().organisationSchemeType());
        typeField.setHidden(true);

        return ListGridUtils.addFields(getVersionableResourceListGridFields(), typeField);
    }

    public static CustomListGridField[] getOrganisationListGridFields() {
        CustomListGridField typeField = new CustomListGridField(OrganisationDS.TYPE_NAME, getConstants().organisationType());
        typeField.setHidden(true);

        return ListGridUtils.addFields(getItemListGridFields(), typeField);
    }

    public static DetailViewerField[] getOrganisationDetailViewerFields() {
        DetailViewerField typeField = new DetailViewerField(OrganisationDS.TYPE_NAME, getConstants().organisationType());
        return ListGridUtils.addFields(getItemDetailViewerFields(), typeField);
    }

    //
    // CONCEPTS
    //

    public static CustomListGridField[] getConceptSchemeListGridFields() {
        CustomListGridField typeField = new CustomListGridField(ConceptSchemeDS.TYPE, getConstants().conceptSchemeType());

        CustomListGridField statisticalOperation = new CustomListGridField(ConceptSchemeDS.RELATED_OPERATION, getConstants().conceptSchemeOperation());
        statisticalOperation.setHidden(true);

        return ListGridUtils.addFields(getVersionableResourceListGridFields(), typeField, statisticalOperation);
    }

    public static CustomListGridField[] getConceptListGridFields() {
        CustomListGridField acronym = new CustomListGridField(ConceptDS.ACRONYM, getConstants().conceptAcronym());
        acronym.setHidden(true);

        CustomListGridField variable = new CustomListGridField(ConceptDS.VARIABLE, getConstants().variable());
        variable.setHidden(true);

        CustomListGridField sdmxRelatedArtefact = new CustomListGridField(ConceptDS.SDMX_RELATED_ARTEFACT, getConstants().conceptSdmxRelatedArtefact());
        sdmxRelatedArtefact.setHidden(true);

        return ListGridUtils.addFields(getItemListGridFields(), acronym, variable, sdmxRelatedArtefact);
    }

    public static DetailViewerField[] getConceptDetailViewerFields() {
        DetailViewerField acronym = new DetailViewerField(ConceptDS.ACRONYM, getConstants().conceptAcronym());

        DetailViewerField variable = new DetailViewerField(ConceptDS.VARIABLE, getConstants().variable());

        DetailViewerField sdmxRelatedArtefact = new DetailViewerField(ConceptDS.SDMX_RELATED_ARTEFACT, getConstants().conceptSdmxRelatedArtefact());

        return ListGridUtils.addFields(getItemDetailViewerFields(), acronym, variable, sdmxRelatedArtefact);
    }

    //
    // CODES
    //

    public static CustomListGridField[] getCodelistListGridFields() {
        CustomListGridField isRecommended = new CustomListGridField(CodelistDS.IS_RECOMMENDED, getConstants().codelistIsRecommended());

        CustomListGridField variable = new CustomListGridField(CodelistDS.VARIABLE, getConstants().variable());
        variable.setHidden(true);

        return ListGridUtils.addFields(getVersionableResourceListGridFields(), isRecommended, variable);
    }

    public static CustomListGridField[] getCodeListGridFields() {
        return getItemListGridFields();
    }

    public static DetailViewerField[] getCodeDetailViewerFields() {
        return getItemDetailViewerFields();
    }

    //
    // DSD
    //

    public static CustomListGridField[] getDsdListGridFields() {
        CustomListGridField statisticalOperation = new CustomListGridField(DataStructureDefinitionDS.STATISTICAL_OPERATION, getConstants().dsdOperation());
        return ListGridUtils.addFields(getVersionableResourceListGridFields(), statisticalOperation);
    }

    //
    // VERSIONABLE RESOURCE
    //

    public static CustomListGridField[] getVersionableResourceListGridFields() {

        CustomListGridField code = new CustomListGridField(VersionableResourceDS.CODE, getConstants().identifiableArtefactCode());

        CustomListGridField urn = new CustomListGridField(VersionableResourceDS.URN, getConstants().identifiableArtefactUrn());
        urn.setHidden(true);

        CustomListGridField version = new CustomListGridField(VersionableResourceDS.VERSION_LOGIC, getConstants().maintainableArtefactVersionLogic());

        CustomListGridField name = new CustomListGridField(VersionableResourceDS.NAME, getConstants().nameableArtefactName());

        CustomListGridField maintainer = new CustomListGridField(VersionableResourceDS.MAINTAINER, getConstants().maintainableArtefactMaintainer());
        maintainer.setHidden(true);

        CustomListGridField procStatus = new CustomListGridField(VersionableResourceDS.PROC_STATUS, getConstants().lifeCycleProcStatus());

        CustomListGridField creationDate = new CustomListGridField(VersionableResourceDS.VERSION_CREATION_DATE, getConstants().maintainableArtefactVersionCreationDate());
        creationDate.setHidden(true);

        CustomListGridField resourceCreationDate = new CustomListGridField(VersionableResourceDS.RESOURCE_CREATION_DATE, getConstants().maintainableArtefactResourceCreationDate());
        resourceCreationDate.setHidden(true);

        CustomListGridField externalPublicationDate = new CustomListGridField(VersionableResourceDS.EXTERNAL_PUBLICATION_DATE, getConstants().lifeCycleExternalPublicationDate());
        externalPublicationDate.setHidden(true);

        CustomListGridField resourceId = new CustomListGridField(VersionableResourceDS.RESOURCE_ID, getConstants().versionableResourceId());
        resourceId.setHidden(true);
        resourceId.setGroupTitleRenderer(new GroupTitleRenderer() {

            @Override
            public String getGroupTitle(Object groupValue, GroupNode groupNode, ListGridField field, String fieldName, ListGrid grid) {
                return groupNode.getGroupMembers()[0].getAttribute(VersionableResourceDS.CODE);
            }
        });

        return new CustomListGridField[]{code, urn, version, name, maintainer, procStatus, creationDate, resourceCreationDate, externalPublicationDate, resourceId};
    }

    //
    // ITEM
    //

    public static CustomListGridField[] getItemListGridFields() {

        CustomListGridField code = new CustomListGridField(ItemDS.CODE, getConstants().identifiableArtefactCode());

        CustomListGridField urn = new CustomListGridField(ItemDS.URN, getConstants().identifiableArtefactUrn());
        urn.setHidden(true);

        CustomListGridField name = new CustomListGridField(ItemDS.NAME, getConstants().nameableArtefactName());

        CustomLinkListGridField itemScheme = new CustomLinkListGridField(ItemDS.ITEM_SCHEME, getConstants().itemScheme());

        CustomListGridField creationDate = new CustomListGridField(ItemDS.CREATION_DATE, getConstants().identifiableArtefactCreationDate());
        creationDate.setHidden(true);

        return new CustomListGridField[]{code, urn, name, itemScheme, creationDate};
    }

    public static DetailViewerField[] getItemDetailViewerFields() {

        DetailViewerField code = new DetailViewerField(ItemDS.CODE, getConstants().identifiableArtefactCode());

        DetailViewerField urn = new DetailViewerField(ItemDS.URN, getConstants().identifiableArtefactUrn());

        DetailViewerField name = new DetailViewerField(ItemDS.NAME, getConstants().nameableArtefactName());

        DetailViewerField description = new DetailViewerField(ItemDS.DESCRIPTION, getConstants().nameableArtefactDescription());

        DetailViewerField creationDate = new DetailViewerField(ItemDS.CREATION_DATE, getConstants().identifiableArtefactCreationDate());

        return new DetailViewerField[]{code, urn, name, description, creationDate};
    }
}
