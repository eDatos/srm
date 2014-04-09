package org.siemac.metamac.srm.web.client.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import java.util.ArrayList;
import java.util.List;

import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.core.common.dto.LocalisedStringDto;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.widgets.form.utils.SrmFormUtils;
import org.siemac.metamac.srm.web.shared.ImportableResourceTypeEnum;
import org.siemac.metamac.srm.web.shared.utils.SrmSharedTokens;
import org.siemac.metamac.web.common.client.MetamacWebCommon;
import org.siemac.metamac.web.common.client.model.ds.RelatedResourceBaseDS;
import org.siemac.metamac.web.common.client.widgets.UploadResourceWithPreviewWindow;
import org.siemac.metamac.web.common.client.widgets.form.CustomDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomButtonItem;
import org.siemac.metamac.web.common.client.widgets.form.utils.FormUtils;

import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.json.client.JSONArray;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.json.client.JSONValue;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.HiddenItem;
import com.smartgwt.client.widgets.form.fields.UploadItem;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;

public abstract class ImportSDMXStructureWindow extends UploadResourceWithPreviewWindow {

    private static final int         FORM_ITEM_WIDTH = 800;
    private final String             FILE_TYPE_ID    = SrmSharedTokens.UPLOAD_PARAM_FILE_TYPE;
    private final String             URNS_ID         = "urns";
    private final String             RESOURCES_ID    = "resources";
    private List<RelatedResourceDto> allResources    = new ArrayList<RelatedResourceDto>();

    public ImportSDMXStructureWindow() {
        super(getConstants().actionImportSDMXResource());
        setAutoSize(true);
        setWidth(FORM_ITEM_WIDTH);
    }

    @Override
    protected UploadForm buildMainUploadForm() {
        return new UploadSDMXResourceForm();
    }

    @Override
    protected CustomDynamicForm buildExtraForm() {
        SearchRelatedResourceListItem listItem = buildRelatedResourceListItem(RESOURCES_ID, MetamacWebCommon.getConstants().resourceSelection());
        listItem.setHeight(250);
        listItem.setRequired(true);

        CustomButtonItem uploadButton = new CustomButtonItem("button-import", MetamacWebCommon.getConstants().accept());

        uploadButton.addClickHandler(new com.smartgwt.client.widgets.form.fields.events.ClickHandler() {

            @Override
            public void onClick(com.smartgwt.client.widgets.form.fields.events.ClickEvent event) {
                submitIfValid();
            }

        });

        CustomDynamicForm form = new CustomDynamicForm();
        form.setItems(listItem, uploadButton);
        form.setVisible(false);
        return form;
    }

    @Override
    protected void copyHiddenValuesToMainForm(UploadForm mainForm, DynamicForm extraForm) {
        List<RelatedResourceDto> selectedResources = SrmFormUtils.getValueAsRelatedResourcesList(extraForm, RESOURCES_ID);
        List<String> urns = new ArrayList<String>();
        for (RelatedResourceDto resource : selectedResources) {
            urns.add(resource.getUrn());
        }
        mainForm.setValue(URNS_ID, StringUtils.join(urns.toArray(), '#'));
    }

    private SearchRelatedResourceListItem buildRelatedResourceListItem(final String name, final String title) {
        final SearchRelatedResourceListItem listItem = new SearchRelatedResourceListItem(name, title, null) {

            @Override
            protected void onSearch() {
                final SelectMultipleRelatedResourceWindow window = new SelectMultipleRelatedResourceWindow(title);
                window.setSaveAction(new ClickHandler() {

                    @Override
                    public void onClick(ClickEvent event) {
                        if (getForm() != null) {
                            FormUtils.setValue(getForm(), name, window.getSelectedResources());
                            window.hide();
                        }
                    }
                });
                window.setResources(allResources);
                List<RelatedResourceDto> selectedResources = SrmFormUtils.getValueAsRelatedResourcesList(extraForm, RESOURCES_ID);
                window.setSelectedResources(selectedResources);
            };

        };
        return listItem;
    }

    @Override
    public String getRelativeURL(String url) {
        return MetamacSrmWeb.getRelativeURL(url);
    }

    @Override
    protected void onPreviewComplete(String response) {
        JSONValue json = JSONParser.parseStrict(response);
        JSONArray array = json.isArray();
        List<RelatedResourceDto> resources = new ArrayList<RelatedResourceDto>();
        for (int i = 0; i < array.size(); i++) {
            resources.add(buildRelatedResource(array.get(i).isObject()));
        }
        allResources = new ArrayList<RelatedResourceDto>(resources);
        extraForm.clearValue(RESOURCES_ID);
        extraForm.setVisible(true);
    }

    private RelatedResourceDto buildRelatedResource(JSONObject json) {
        RelatedResourceDto dto = new RelatedResourceDto();
        dto.setUrn(getJsonStringValue(json, RelatedResourceBaseDS.URN));
        dto.setUrnProvider(getJsonStringValue(json, RelatedResourceBaseDS.URN_PROVIDER));
        dto.setCode(getJsonStringValue(json, RelatedResourceBaseDS.CODE));
        dto.setTitle(getJsonInternationalStringValue(json, RelatedResourceBaseDS.TITLE));
        if (dto.getUrn() == null) {
            dto.setUrn(dto.getUrnProvider());
        }
        return dto;
    }

    private String getJsonStringValue(JSONObject json, String field) {
        if (json.get(field) != null && json.get(field).isString() != null) {
            return json.get(field).isString().stringValue();
        }
        return null;
    }

    private InternationalStringDto getJsonInternationalStringValue(JSONObject json, String field) {
        if (json.get(field) != null && json.get(field).isObject() != null) {
            JSONObject intStringJson = json.get(field).isObject();
            InternationalStringDto dto = new InternationalStringDto();
            for (String locale : intStringJson.keySet()) {
                LocalisedStringDto localised = new LocalisedStringDto();
                localised.setLocale(locale);
                localised.setLabel(getJsonStringValue(intStringJson, locale));
                dto.addText(localised);
            }
            return dto;
        }
        return null;
    }

    @Override
    protected void onPreviewFailed(String errorMessage) {
        uploadFailed(errorMessage);
    }

    @Override
    protected void onSubmitComplete(String response) {
        uploadSuccess(response);
    }

    @Override
    protected void onSubmitFailed(String errorMessage) {
        uploadFailed(errorMessage);
    }

    protected abstract void uploadFailed(String error);
    protected abstract void uploadSuccess(String message);

    private class UploadSDMXResourceForm extends UploadForm {

        private UploadItem uploadItem;

        public UploadSDMXResourceForm() {
            super();

            uploadItem = new UploadItem("file-name");
            uploadItem.setTitle(getConstants().sDMXResource());
            uploadItem.setWidth(300);
            uploadItem.setRequired(true);
            uploadItem.setTitleStyle("requiredFormLabel");

            uploadItem.addChangeHandler(new com.smartgwt.client.widgets.form.fields.events.ChangeHandler() {

                @Override
                public void onChange(com.smartgwt.client.widgets.form.fields.events.ChangeEvent event) {
                    Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {

                        @Override
                        public void execute() {
                            submitPreviewIfValid();
                        }
                    });
                }
            });

            HiddenItem fileTypeItem = new HiddenItem(FILE_TYPE_ID);
            fileTypeItem.setDefaultValue(ImportableResourceTypeEnum.SDMX_STRUCTURE.name());

            HiddenItem urnsItem = new HiddenItem(URNS_ID);

            setFields(uploadItem, fileTypeItem, urnsItem);
        }

        @Override
        public UploadItem getUploadItem() {
            return uploadItem;
        }
    }

}
