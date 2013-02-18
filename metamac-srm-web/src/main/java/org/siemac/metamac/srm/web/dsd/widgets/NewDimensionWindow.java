package org.siemac.metamac.srm.web.dsd.widgets;

import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getConstants;

import org.siemac.metamac.srm.web.dsd.model.ds.DimensionDS;
import org.siemac.metamac.srm.web.dsd.utils.CommonUtils;
import org.siemac.metamac.web.common.client.MetamacWebCommon;
import org.siemac.metamac.web.common.client.widgets.CustomWindow;
import org.siemac.metamac.web.common.client.widgets.form.CustomDynamicForm;
import org.siemac.metamac.web.common.client.widgets.form.fields.CustomButtonItem;
import org.siemac.metamac.web.common.client.widgets.form.fields.RequiredSelectItem;

import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.SpecialDimensionTypeEnum;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.TypeDimensionComponent;
import com.smartgwt.client.widgets.form.fields.events.HasClickHandlers;

public class NewDimensionWindow extends CustomWindow {

    private static final int    FORM_ITEM_CUSTOM_WIDTH = 300;

    private static final String FIELD_ACCEPT           = "save-con";

    private CustomDynamicForm   form;

    public NewDimensionWindow(String title) {
        super(title);
        setAutoSize(true);

        RequiredSelectItem dimensionType = new RequiredSelectItem(DimensionDS.TYPE_VIEW, getConstants().dsdDimensionsType());
        dimensionType.setValueMap(CommonUtils.getTypeDimensionComponentHashMap());
        dimensionType.setWidth(FORM_ITEM_CUSTOM_WIDTH);

        CustomButtonItem saveItem = new CustomButtonItem(FIELD_ACCEPT, MetamacWebCommon.getConstants().accept());

        form = new CustomDynamicForm();
        form.setMargin(5);
        form.setFields(dimensionType, saveItem);

        addItem(form);
        show();
    }

    public HasClickHandlers getAccept() {
        return form.getItem(FIELD_ACCEPT);
    }

    public TypeDimensionComponent getDimensionType() {
        String dimensionType = form.getValueAsString(DimensionDS.TYPE_VIEW);
        if (SpecialDimensionTypeEnum.SPATIAL.name().equals(dimensionType)) {
            return TypeDimensionComponent.DIMENSION;
        } else {
            return TypeDimensionComponent.valueOf(dimensionType);
        }
    }

    public SpecialDimensionTypeEnum getSpecialDimensionType() {
        String dimensionType = form.getValueAsString(DimensionDS.TYPE_VIEW);
        if (SpecialDimensionTypeEnum.SPATIAL.name().equals(dimensionType)) {
            return SpecialDimensionTypeEnum.SPATIAL;
        }
        return null;
    }

    public boolean validateForm() {
        return form.validate(false);
    }
}
