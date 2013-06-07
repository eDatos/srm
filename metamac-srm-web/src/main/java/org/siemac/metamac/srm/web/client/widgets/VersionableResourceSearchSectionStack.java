package org.siemac.metamac.srm.web.client.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import java.util.Date;

import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.core.enume.domain.ProcStatusEnum;
import org.siemac.metamac.srm.web.client.model.ds.VersionableResourceDS;
import org.siemac.metamac.srm.web.client.utils.CommonUtils;
import org.siemac.metamac.srm.web.shared.criteria.VersionableResourceWebCriteria;
import org.siemac.metamac.web.common.client.MetamacWebCommon;
import org.siemac.metamac.web.common.client.widgets.form.GroupDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomButtonItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomCheckboxItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomDateItem;

import com.smartgwt.client.types.Visibility;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;

public abstract class VersionableResourceSearchSectionStack extends BaseAdvancedSearchSectionStack {

    public VersionableResourceSearchSectionStack() {
    }

    @Override
    protected void clearAdvancedSearchSection() {
        super.clearAdvancedSearchSection();
        // Search last versions by default
        ((CustomCheckboxItem) advancedSearchForm.getItem(VersionableResourceDS.IS_LAST_VERSION)).setValue(true);
    }

    @Override
    protected void createAdvancedSearchForm() {
        advancedSearchForm = new GroupDynamicForm(StringUtils.EMPTY);
        advancedSearchForm.setPadding(5);
        advancedSearchForm.setMargin(5);
        advancedSearchForm.setVisible(false);
        TextItem code = new TextItem(VersionableResourceDS.CODE, getConstants().identifiableArtefactCode());
        TextItem name = new TextItem(VersionableResourceDS.NAME, getConstants().nameableArtefactName());
        TextItem urn = new TextItem(VersionableResourceDS.URN, getConstants().identifiableArtefactUrn());
        TextItem description = new TextItem(VersionableResourceDS.DESCRIPTION, getConstants().nameableArtefactDescription());
        SelectItem procStatus = new SelectItem(VersionableResourceDS.PROC_STATUS, getConstants().lifeCycleProcStatus());
        procStatus.setValueMap(CommonUtils.getProcStatusHashMap());
        CustomDateItem internalPublicationDate = new CustomDateItem(VersionableResourceDS.INTERNAL_PUBLICATION_DATE, getConstants().lifeCycleInternalPublicationDate());
        TextItem internalPublicationUser = new TextItem(VersionableResourceDS.INTERNAL_PUBLICATION_USER, getConstants().lifeCycleInternalPublicationUser());
        CustomDateItem externalPublicationDate = new CustomDateItem(VersionableResourceDS.EXTERNAL_PUBLICATION_DATE, getConstants().lifeCycleExternalPublicationDate());
        TextItem externalPublicationUser = new TextItem(VersionableResourceDS.EXTERNAL_PUBLICATION_USER, getConstants().lifeCycleExternalPublicationUser());
        CustomCheckboxItem isLastVersion = new CustomCheckboxItem(VersionableResourceDS.IS_LAST_VERSION, getConstants().maintainableArtefactIsLastVersion());
        isLastVersion.setValue(true);
        CustomButtonItem searchItem = new CustomButtonItem(ADVANCED_SEARCH_ITEM_NAME, MetamacWebCommon.getConstants().search());
        searchItem.setColSpan(4);
        searchItem.addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                retrieveResources();
            }
        });

        FormItem[] advancedSearchFormItems = new FormItem[]{code, internalPublicationDate, name, internalPublicationUser, urn, externalPublicationDate, description, externalPublicationUser,
                procStatus, isLastVersion, searchItem};
        setFormItemsInAdvancedSearchForm(advancedSearchFormItems);
    }

    public VersionableResourceWebCriteria getVersionableResourceWebCriteria(VersionableResourceWebCriteria versionableResourceWebCriteria) {
        versionableResourceWebCriteria.setCriteria(searchForm.getValueAsString(SEARCH_ITEM_NAME));
        versionableResourceWebCriteria.setCode(advancedSearchForm.getValueAsString(VersionableResourceDS.CODE));
        versionableResourceWebCriteria.setName(advancedSearchForm.getValueAsString(VersionableResourceDS.NAME));
        versionableResourceWebCriteria.setUrn(advancedSearchForm.getValueAsString(VersionableResourceDS.URN));
        versionableResourceWebCriteria.setDescription(advancedSearchForm.getValueAsString(VersionableResourceDS.DESCRIPTION));
        versionableResourceWebCriteria.setProcStatus(!StringUtils.isBlank(advancedSearchForm.getValueAsString(VersionableResourceDS.PROC_STATUS)) ? ProcStatusEnum.valueOf(advancedSearchForm
                .getValueAsString(VersionableResourceDS.PROC_STATUS)) : null);
        versionableResourceWebCriteria.setInternalPublicationDate(advancedSearchForm.getValue(VersionableResourceDS.INTERNAL_PUBLICATION_DATE) != null ? (Date) advancedSearchForm
                .getValue(VersionableResourceDS.INTERNAL_PUBLICATION_DATE) : null);
        versionableResourceWebCriteria.setInternalPublicationUser(advancedSearchForm.getValueAsString(VersionableResourceDS.INTERNAL_PUBLICATION_USER));
        versionableResourceWebCriteria.setExternalPublicationDate(advancedSearchForm.getValue(VersionableResourceDS.EXTERNAL_PUBLICATION_DATE) != null ? (Date) advancedSearchForm
                .getValue(VersionableResourceDS.EXTERNAL_PUBLICATION_DATE) : null);
        versionableResourceWebCriteria.setExternalPublicationUser(advancedSearchForm.getValueAsString(VersionableResourceDS.EXTERNAL_PUBLICATION_USER));
        versionableResourceWebCriteria.setIsLastVersion(((CustomCheckboxItem) advancedSearchForm.getItem(VersionableResourceDS.IS_LAST_VERSION)).getValueAsBoolean());
        return versionableResourceWebCriteria;
    }
}
