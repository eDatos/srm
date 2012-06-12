package org.siemac.metamac.srm.web.dsd.widgets;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import org.siemac.metamac.core.common.dto.ExternalItemBtDto;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.resources.GlobalResources;
import org.siemac.metamac.srm.web.dsd.model.record.ConceptRecord;
import org.siemac.metamac.srm.web.dsd.utils.RecordUtils;
import org.siemac.metamac.web.common.client.utils.FormItemUtils;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomCanvasItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomSelectItem;

import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.TransferImgButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.grid.HoverCustomizer;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.layout.VStack;

public class RoleSelectItem extends CustomCanvasItem {

    private CustomSelectItem conceptSchemeItem;
    private ListGrid         conceptsList;
    private ListGrid         roleList;

    public RoleSelectItem(String name, String title) {
        super(name, title);
        setTitleVAlign(VerticalAlignment.TOP);
        conceptSchemeItem = new CustomSelectItem("scheme-items", MetamacSrmWeb.getConstants().dsdConceptScheme());
        conceptSchemeItem.setWidth(FormItemUtils.FORM_ITEM_WIDTH);
        conceptSchemeItem.setShowTitle(false);

        conceptsList = new ListGrid();
        conceptsList.setWidth(FormItemUtils.FORM_ITEM_WIDTH);
        conceptsList.setCellHeight(24);
        conceptsList.setBorder("1px solid #aaacb2");
        conceptsList.setShowHeader(false);
        conceptsList.setLeaveScrollbarGap(false);
        conceptsList.setStyleName("conceptsListStyle");

        roleList = new ListGrid();
        roleList.setWidth(FormItemUtils.FORM_ITEM_WIDTH);
        roleList.setCellHeight(24);
        roleList.setBorder("1px solid #aaacb2");
        roleList.setShowHeader(false);
        roleList.setLeaveScrollbarGap(false);
        roleList.setCanRemoveRecords(true);
        roleList.setRemoveFieldTitle(MetamacSrmWeb.getConstants().actionDelete());
        roleList.setRemoveIcon(GlobalResources.RESOURCE.remove().getURL());

        ListGridField id = new ListGridField(ConceptRecord.IDENTIFIER, "nameId");
        id.setShowHover(true);
        id.setHoverCustomizer(new HoverCustomizer() {

            @Override
            public String hoverHTML(Object value, ListGridRecord record, int rowNum, int colNum) {
                ConceptRecord conceptRecord = (ConceptRecord) record;
                return conceptRecord.getCodeId();
            }
        });

        conceptsList.setFields(id);
        roleList.setFields(id);

        VStack buttonStack = new VStack(10);
        buttonStack.setWidth(32);
        buttonStack.setHeight(74);
        buttonStack.setLayoutAlign(Alignment.CENTER);

        TransferImgButton rightImg = new TransferImgButton(TransferImgButton.RIGHT);
        rightImg.addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                // Do not transfer data if already exists in conceptsList
                ListGridRecord[] selectedConcepts = conceptsList.getSelectedRecords();
                for (int i = 0; i < selectedConcepts.length; i++) {
                    ConceptRecord concept = (ConceptRecord) selectedConcepts[i];
                    if (existsRecordInList(concept, roleList.getRecords())) {
                        conceptsList.deselectRecord(concept);
                    }
                }
                // Copy the selected records from concepstList to roleList
                selectedConcepts = conceptsList.getSelectedRecords();
                for (int i = 0; i < selectedConcepts.length; i++) {
                    roleList.addData(selectedConcepts[i]);
                }
            }
        });

        buttonStack.addMember(rightImg);

        DynamicForm form = new DynamicForm();
        form.setFields(conceptSchemeItem);

        HLayout hLayout = new HLayout(1);
        hLayout.addMember(conceptsList);
        hLayout.addMember(buttonStack);
        hLayout.addMember(roleList);

        VLayout vLayout = new VLayout();
        vLayout.addMember(form);
        vLayout.addMember(hLayout);

        setCanvas(vLayout);
        setCellStyle("roleItemSytle");
    }

    public void setRequired(boolean required) {
        setTitleStyle("requiredFormLabel");
    }

    public void setConceptSchemesValueMap(LinkedHashMap<String, String> map) {
        conceptSchemeItem.setValueMap(map);
    }

    public void setConcepts(List<ExternalItemBtDto> concepts) {
        conceptsList.selectAllRecords();
        conceptsList.removeSelectedData();
        for (ExternalItemBtDto concept : concepts) {
            ConceptRecord record = RecordUtils.getConceptRecord(concept);
            conceptsList.addData(record);
        }
    }

    public SelectItem getConceptSchemeItem() {
        return conceptSchemeItem;
    }

    public List<ExternalItemBtDto> getSelectedConcepts() {
        List<ExternalItemBtDto> selectedConcepts = new ArrayList<ExternalItemBtDto>();
        ListGridRecord[] records = roleList.getRecords();
        for (int i = 0; i < records.length; i++) {
            ConceptRecord record = (ConceptRecord) records[i];
            selectedConcepts.add(record.getConcept());
        }
        return selectedConcepts;
    }

    public void setRoleConcepts(Set<ExternalItemBtDto> roleConcepts) {
        for (ExternalItemBtDto concept : roleConcepts) {
            ConceptRecord record = RecordUtils.getConceptRecord(concept);
            roleList.addData(record);
        }
    }

    public void clearValue() {
        conceptSchemeItem.clearValue();
        conceptsList.selectAllRecords();
        conceptsList.removeSelectedData();
        roleList.selectAllRecords();
        roleList.removeSelectedData();
    }

    private boolean existsRecordInList(ConceptRecord record, ListGridRecord[] list) {
        for (int i = 0; i < list.length; i++) {
            ConceptRecord conceptRecord = (ConceptRecord) list[i];
            if (record.getCodeId().equals(conceptRecord.getCodeId())) {
                return true;
            }
        }
        return false;
    }

}
