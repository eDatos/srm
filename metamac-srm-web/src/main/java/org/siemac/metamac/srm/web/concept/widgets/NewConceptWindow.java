package org.siemac.metamac.srm.web.concept.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import org.siemac.metamac.core.common.dto.InternationalStringDto;
import org.siemac.metamac.srm.core.concept.dto.ConceptMetamacDto;
import org.siemac.metamac.srm.core.concept.enume.domain.ConceptRoleEnum;
import org.siemac.metamac.srm.core.concept.enume.domain.ConceptSchemeTypeEnum;
import org.siemac.metamac.srm.web.concept.model.ds.ConceptDS;
import org.siemac.metamac.srm.web.concept.utils.CommonUtils;
import org.siemac.metamac.web.common.client.utils.CommonWebUtils;
import org.siemac.metamac.web.common.client.utils.InternationalStringUtils;
import org.siemac.metamac.web.common.client.widgets.CustomWindow;
import org.siemac.metamac.web.common.client.widgets.form.CustomDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomButtonItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.RequiredSelectItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.RequiredTextItem;

import com.smartgwt.client.widgets.form.fields.events.HasClickHandlers;

public class NewConceptWindow extends CustomWindow {

    private static final int    FORM_ITEM_CUSTOM_WIDTH = 300;

    private static final String FIELD_SAVE             = "save-con";

    private CustomDynamicForm   form;

    public NewConceptWindow(String title, ConceptSchemeTypeEnum type) {
        super(title);
        setAutoSize(true);

        RequiredTextItem codeItem = new RequiredTextItem(ConceptDS.CODE, getConstants().identifiableArtefactCode());
        codeItem.setValidators(CommonWebUtils.getSemanticIdentifierCustomValidator());
        codeItem.setWidth(FORM_ITEM_CUSTOM_WIDTH);

        RequiredTextItem nameItem = new RequiredTextItem(ConceptDS.NAME, getConstants().nameableArtefactName());
        nameItem.setWidth(FORM_ITEM_CUSTOM_WIDTH);

        RequiredSelectItem sdmxRelatedArtefact = new RequiredSelectItem(ConceptDS.SDMX_RELATED_ARTEFACT, getConstants().conceptSdmxRelatedArtefact());
        sdmxRelatedArtefact.setValueMap(CommonUtils.getConceptRoleHashMap());
        sdmxRelatedArtefact.setWidth(FORM_ITEM_CUSTOM_WIDTH);
        sdmxRelatedArtefact.setVisible(CommonUtils.isMetadataSdmxRelatedArtefactVisible(type));

        CustomButtonItem saveItem = new CustomButtonItem(FIELD_SAVE, getConstants().conceptCreate());

        form = new CustomDynamicForm();
        form.setMargin(5);
        form.setFields(codeItem, nameItem, sdmxRelatedArtefact, saveItem);

        addItem(form);
        show();
    }

    public HasClickHandlers getSave() {
        return form.getItem(FIELD_SAVE);
    }

    public ConceptMetamacDto getNewConceptDto() {
        ConceptMetamacDto conceptDto = new ConceptMetamacDto();
        conceptDto.setCode(form.getValueAsString(ConceptDS.CODE));
        conceptDto.setName(InternationalStringUtils.updateInternationalString(new InternationalStringDto(), form.getValueAsString(ConceptDS.NAME)));
        conceptDto.setSdmxRelatedArtefact(form.getItem(ConceptDS.SDMX_RELATED_ARTEFACT).isVisible() ? ConceptRoleEnum.valueOf(form.getValueAsString(ConceptDS.SDMX_RELATED_ARTEFACT)) : null);
        return conceptDto;
    }

    public boolean validateForm() {
        return form.validate(false);
    }

}
