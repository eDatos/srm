package org.siemac.metamac.srm.web.concept.widgets;

import java.util.LinkedHashMap;
import static org.siemac.metamac.srm.web.client.MetamacSrmWeb.getCoreMessages;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.rest.structural_resources_internal.v1_0.domain.QuantityUnitSymbolPosition;
import org.siemac.metamac.srm.core.concept.dto.QuantityDto;
import org.siemac.metamac.srm.core.concept.enume.domain.QuantityTypeEnum;
import org.siemac.metamac.srm.core.concept.enume.domain.QuantityUnitSymbolPositionEnum;
import org.siemac.metamac.srm.core.concept.serviceimpl.utils.shared.QuantityUtils;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.concept.enums.QuantityIndexBaseTypeEnum;
import org.siemac.metamac.srm.web.concept.model.ds.ConceptDS;
import org.siemac.metamac.srm.web.concept.view.handlers.ConceptUiHandlers;
import org.siemac.metamac.web.common.client.view.handlers.BaseUiHandlers;
import org.siemac.metamac.web.common.client.widgets.form.GroupDynamicForm;
import org.siemac.metamac.web.common.client.widgets.handlers.CustomLinkItemNavigationClickHandler;

import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.FormItemIfFunction;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.validator.CustomValidator;

public class BaseQuantityForm extends GroupDynamicForm {

    protected ConceptUiHandlers uiHandlers;

    public BaseQuantityForm(String groupTitle) {
        super(groupTitle);
    }

    public void setUiHandlers(ConceptUiHandlers uiHandlers) {
        this.uiHandlers = uiHandlers;
    }
    
    public ConceptUiHandlers getUiHandlers() {
        return uiHandlers;
    }

    protected QuantityIndexBaseTypeEnum getIndexBaseTypeEnum(QuantityDto quantityDto) {
        Integer baseValue = quantityDto.getBaseValue();
        String baseTime = quantityDto.getBaseTime();
        String baseLocation = quantityDto.getBaseLocation();
        if (!StringUtils.isBlank(baseTime)) {
            return QuantityIndexBaseTypeEnum.BASE_TIME;
        } else if (!StringUtils.isBlank(baseLocation)) {
            return QuantityIndexBaseTypeEnum.BASE_LOCATION;
        } else if (baseValue != null) {
            return QuantityIndexBaseTypeEnum.BASE_VALUE;
        }
        return null;
    }

    protected String getIndexBaseType(QuantityDto quantityDto) {
        QuantityIndexBaseTypeEnum basetype = getIndexBaseTypeEnum(quantityDto);
        if (basetype != null) {
            return MetamacSrmWeb.getCoreMessages().getString(MetamacSrmWeb.getCoreMessages().quantityIndexBaseTypeEnum() + basetype.getName());
        }
        return "";
    }

    protected LinkedHashMap<String, String> getQuantityIndexBaseTypeValueMap() {
        LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
        for (QuantityIndexBaseTypeEnum type : QuantityIndexBaseTypeEnum.values()) {
            //FIXME: ENABLED LOCATION when its done
            if (!QuantityIndexBaseTypeEnum.BASE_LOCATION.equals(type)) {
                valueMap.put(type.toString(), getCoreMessages().getString(getCoreMessages().quantityIndexBaseTypeEnum() + type.getName()));
            }
        }
        return valueMap;
    }
    
    protected LinkedHashMap<String, String> getQuantityUnitSymbolValueMap() {
        LinkedHashMap<String, String> valueMap = new LinkedHashMap<String, String>();
        for (QuantityUnitSymbolPositionEnum position : QuantityUnitSymbolPositionEnum.values()) {
            valueMap.put(position.toString(), getCoreMessages().getString(getCoreMessages().quantityUnitSymbolPositionEnum() + position.getName()));
        }
        return valueMap;
    }

    protected FormItemIfFunction getMinIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return showMinFunction(item, value, form);
            }
        };
    }

    protected boolean showMinFunction(FormItem item, Object value, DynamicForm form) {
        if (form.getValueAsString(ConceptDS.QUANTITY_TYPE) != null && !form.getValueAsString(ConceptDS.QUANTITY_TYPE).isEmpty()) {
            QuantityTypeEnum type = QuantityTypeEnum.valueOf(form.getValueAsString(ConceptDS.QUANTITY_TYPE));
            if (QuantityUtils.isMagnitudeOrExtension(type)) {
                return true;
            }
        }
        return false;
    }

    protected FormItemIfFunction getMaxIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return showMaxFunction(item, value, form);
            }
        };
    }

    protected boolean showMaxFunction(FormItem item, Object value, DynamicForm form) {
        if (form.getValueAsString(ConceptDS.QUANTITY_TYPE) != null && !form.getValueAsString(ConceptDS.QUANTITY_TYPE).isEmpty()) {
            QuantityTypeEnum type = QuantityTypeEnum.valueOf(form.getValueAsString(ConceptDS.QUANTITY_TYPE));
            if (QuantityUtils.isMagnitudeOrExtension(type)) {
                return true;
            }
        }
        return false;
    }


    
    protected FormItemIfFunction getDenominatorIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return showDenominatorFunction(item, value, form);
            }
        };
    }

    protected boolean showDenominatorFunction(FormItem item, Object value, DynamicForm form) {
        if (form.getValueAsString(ConceptDS.QUANTITY_TYPE) != null && !form.getValueAsString(ConceptDS.QUANTITY_TYPE).isEmpty()) {
            QuantityTypeEnum type = QuantityTypeEnum.valueOf(form.getValueAsString(ConceptDS.QUANTITY_TYPE));
            if (QuantityUtils.isFractionOrExtension(type)) {
                return true;
            }
        }
        return false;
    }

    protected FormItemIfFunction getNumeratorIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return showNumeratorFunction(item, value, form);
            }
        };
    }

    protected boolean showNumeratorFunction(FormItem item, Object value, DynamicForm form) {
        if (form.getValueAsString(ConceptDS.QUANTITY_TYPE) != null && !form.getValueAsString(ConceptDS.QUANTITY_TYPE).isEmpty()) {
            QuantityTypeEnum type = QuantityTypeEnum.valueOf(form.getValueAsString(ConceptDS.QUANTITY_TYPE));
            if (QuantityUtils.isFractionOrExtension(type)) {
                return true;
            }
        }
        return false;
    }

    protected FormItemIfFunction getIsPercentageIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return showIsPercentageFunction(item, value, form);
            }
        };
    }

    protected boolean showIsPercentageFunction(FormItem item, Object value, DynamicForm form) {
        if (form.getValueAsString(ConceptDS.QUANTITY_TYPE) != null && !form.getValueAsString(ConceptDS.QUANTITY_TYPE).isEmpty()) {
            QuantityTypeEnum type = QuantityTypeEnum.valueOf(form.getValueAsString(ConceptDS.QUANTITY_TYPE));
            if (QuantityUtils.isRatioOrExtension(type)) {
                return true;
            }
        }
        return false;
    }

    protected FormItemIfFunction getPercentageOfIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                return showPercentageOfFunction(item, value, form);
            }
        };
    }

    protected boolean showPercentageOfFunction(FormItem item, Object value, DynamicForm form) {
        if (form.getValueAsString(ConceptDS.QUANTITY_TYPE) != null && !form.getValueAsString(ConceptDS.QUANTITY_TYPE).isEmpty()) {
            QuantityTypeEnum type = QuantityTypeEnum.valueOf(form.getValueAsString(ConceptDS.QUANTITY_TYPE));
            if (QuantityUtils.isRatioOrExtension(type)) {
                Object isPercentageValue = form.getValue(ConceptDS.QUANTITY_IS_PERCENTAGE);
                if (isPercentageValue != null && (Boolean)isPercentageValue) {
                    return true;
                }
            }
        }
        return false;
    }

    protected FormItemIfFunction getIndexBaseTypeIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                if (form.getValueAsString(ConceptDS.QUANTITY_TYPE) != null && !form.getValueAsString(ConceptDS.QUANTITY_TYPE).isEmpty()) {
                    QuantityTypeEnum type = QuantityTypeEnum.valueOf(form.getValueAsString(ConceptDS.QUANTITY_TYPE));
                    if (QuantityUtils.isIndexOrExtension(type)) {
                        return true;
                    }
                }
                return false;
            }
        };
    }
    
    protected FormItemIfFunction getShowIfAnyQuantityType() {
        return new FormItemIfFunction() {
            
            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                if (form.getValueAsString(ConceptDS.QUANTITY_TYPE) != null && !form.getValueAsString(ConceptDS.QUANTITY_TYPE).isEmpty()) {
                    return true;
                }
                return false;
            }
        };
    }

    protected FormItemIfFunction getBaseValueIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                if (form.getValueAsString(ConceptDS.QUANTITY_TYPE) != null && !form.getValueAsString(ConceptDS.QUANTITY_TYPE).isEmpty()) {
                    QuantityTypeEnum type = QuantityTypeEnum.valueOf(form.getValueAsString(ConceptDS.QUANTITY_TYPE));
                    if (QuantityUtils.isIndexOrExtension(type)) {
                        if (QuantityIndexBaseTypeEnum.BASE_VALUE.toString().equals(BaseQuantityForm.this.getValueAsString(ConceptDS.QUANTITY_INDEX_BASE_TYPE))) {
                            return true;
                        }
                    }
                }
                return false;
            }
        };
    }

    protected FormItemIfFunction getBaseTimeIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                if (form.getValueAsString(ConceptDS.QUANTITY_TYPE) != null && !form.getValueAsString(ConceptDS.QUANTITY_TYPE).isEmpty()) {
                    QuantityTypeEnum type = QuantityTypeEnum.valueOf(form.getValueAsString(ConceptDS.QUANTITY_TYPE));
                    if (QuantityUtils.isIndexOrExtension(type)) {
                        if (QuantityIndexBaseTypeEnum.BASE_TIME.toString().equals(BaseQuantityForm.this.getValueAsString(ConceptDS.QUANTITY_INDEX_BASE_TYPE))) {
                            return true;
                        }
                    }
                }
                return false;
            }
        };
    }

    protected FormItemIfFunction getBaseLocationIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                if (form.getValueAsString(ConceptDS.QUANTITY_TYPE) != null && !form.getValueAsString(ConceptDS.QUANTITY_TYPE).isEmpty()) {
                    QuantityTypeEnum type = QuantityTypeEnum.valueOf(form.getValueAsString(ConceptDS.QUANTITY_TYPE));
                    if (QuantityUtils.isIndexOrExtension(type)) {
                        if (QuantityIndexBaseTypeEnum.BASE_LOCATION.toString().equals(BaseQuantityForm.this.getValueAsString(ConceptDS.QUANTITY_INDEX_BASE_TYPE))) {
                            return true;
                        }
                    }
                }
                return false;
            }
        };
    }

    protected FormItemIfFunction getBaseQuantityIfFunction() {
        return new FormItemIfFunction() {

            @Override
            public boolean execute(FormItem item, Object value, DynamicForm form) {
                if (form.getValueAsString(ConceptDS.QUANTITY_TYPE) != null && !form.getValueAsString(ConceptDS.QUANTITY_TYPE).isEmpty()) {
                    QuantityTypeEnum type = QuantityTypeEnum.valueOf(form.getValueAsString(ConceptDS.QUANTITY_TYPE));
                    if (QuantityUtils.isChangeRateOrExtension(type)) {
                        return true;
                    }
                }
                return false;
            }
        };
    }
    
    protected CustomLinkItemNavigationClickHandler getCustomLinkItemNavigationClickHandler() {
        return new CustomLinkItemNavigationClickHandler() {

            @Override
            public BaseUiHandlers getBaseUiHandlers() {
                return getUiHandlers();
            }

        };
    }

    // protected String getQuantityUnitTitle(String unitUuid) {
    // if (unitUuid != null) {
    // for (QuantityUnitDto unit : quantityUnitDtos) {
    // if (unitUuid.equals(unit.getUuid())) {
    // return InternationalStringUtils.getLocalisedString(unit.getTitle());
    // }
    // }
    // }
    // return new String();
    // }

    // private void sortQuantityUnits(List<QuantityUnitDto> units) {
    // Collections.sort(units, new Comparator<QuantityUnitDto>() {
    //
    // @Override
    // public int compare(QuantityUnitDto first, QuantityUnitDto second) {
    // String firstText = InternationalStringUtils.getLocalisedString(first.getTitle());
    // String secondText = InternationalStringUtils.getLocalisedString(second.getTitle());
    // return firstText.compareTo(secondText);
    // }
    // });
    // }
}
