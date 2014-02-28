package org.siemac.metamac.srm.web.dsd.presenter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.siemac.metamac.core.common.constants.shared.UrnConstants;
import org.siemac.metamac.core.common.util.shared.StringUtils;
import org.siemac.metamac.core.common.util.shared.UrnUtils;
import org.siemac.metamac.srm.core.dsd.dto.DataStructureDefinitionMetamacDto;
import org.siemac.metamac.srm.navigation.shared.NameTokens;
import org.siemac.metamac.srm.web.client.LoggedInGatekeeper;
import org.siemac.metamac.srm.web.client.MetamacSrmWeb;
import org.siemac.metamac.srm.web.client.utils.PlaceRequestUtils;
import org.siemac.metamac.srm.web.dsd.enums.DsdTabTypeEnum;
import org.siemac.metamac.srm.web.dsd.events.SelectViewDsdDescriptorEvent;
import org.siemac.metamac.srm.web.dsd.utils.CommonUtils;
import org.siemac.metamac.srm.web.dsd.view.handlers.DsdGroupKeysTabUiHandlers;
import org.siemac.metamac.srm.web.shared.dsd.DeleteDescriptorListForDsdAction;
import org.siemac.metamac.srm.web.shared.dsd.DeleteDescriptorListForDsdResult;
import org.siemac.metamac.srm.web.shared.dsd.GetDsdAndDescriptorsAction;
import org.siemac.metamac.srm.web.shared.dsd.GetDsdAndDescriptorsResult;
import org.siemac.metamac.srm.web.shared.dsd.SaveDescriptorForDsdAction;
import org.siemac.metamac.srm.web.shared.dsd.SaveDescriptorForDsdResult;
import org.siemac.metamac.web.common.client.events.ShowMessageEvent;
import org.siemac.metamac.web.common.client.utils.WaitingAsyncCallbackHandlingError;

import com.arte.statistic.sdmx.v2_1.domain.dto.srm.DescriptorDto;
import com.arte.statistic.sdmx.v2_1.domain.dto.srm.DimensionComponentDto;
import com.arte.statistic.sdmx.v2_1.domain.enume.srm.domain.TypeComponentList;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.HasUiHandlers;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.TitleFunction;
import com.gwtplatform.mvp.client.annotations.UseGatekeeper;
import com.gwtplatform.mvp.client.proxy.Place;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.HasClickHandlers;

public class DsdGroupKeysTabPresenter extends Presenter<DsdGroupKeysTabPresenter.DsdGroupKeysTabView, DsdGroupKeysTabPresenter.DsdGroupKeysTabProxy> implements DsdGroupKeysTabUiHandlers {

    private final DispatchAsync               dispatcher;
    private final PlaceManager                placeManager;

    private DataStructureDefinitionMetamacDto dataStructureDefinitionDto;
    private List<DescriptorDto>               groupKeys;
    private List<DimensionComponentDto>       dimensionComponentDtos;

    @ProxyCodeSplit
    @NameToken(NameTokens.dsdGroupKeysPage)
    @UseGatekeeper(LoggedInGatekeeper.class)
    public interface DsdGroupKeysTabProxy extends Proxy<DsdGroupKeysTabPresenter>, Place {

    }

    @TitleFunction
    public static String getTranslatedTitle() {
        return MetamacSrmWeb.getConstants().breadcrumbGroupKeys();
    }

    public interface DsdGroupKeysTabView extends View, HasUiHandlers<DsdGroupKeysTabUiHandlers> {

        void setDsdGroupKeys(DataStructureDefinitionMetamacDto dsd, List<DimensionComponentDto> dimensionComponentDtos, List<DescriptorDto> descriptorDtos);
        DescriptorDto getGroupKeys();
        List<DescriptorDto> getSelectedGroupKeys();
        boolean validate();
        HasClickHandlers getSave();
        HasClickHandlers getDelete();
        void onGroupKeysSaved(DescriptorDto descriptorDto);
    }

    @Inject
    public DsdGroupKeysTabPresenter(EventBus eventBus, DsdGroupKeysTabView dsdGroupKeysTabView, DsdGroupKeysTabProxy dsdGroupKeysTabProxy, DispatchAsync dispatcher, PlaceManager placeManager) {
        super(eventBus, dsdGroupKeysTabView, dsdGroupKeysTabProxy);
        this.dispatcher = dispatcher;
        this.placeManager = placeManager;
        getView().setUiHandlers(this);
    }

    @Override
    protected void onBind() {
        super.onBind();

        registerHandler(getView().getSave().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if (getView().validate()) {
                    saveGroupKeys(getView().getGroupKeys());
                }
            }
        }));

        registerHandler(getView().getDelete().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                List<DescriptorDto> descriptorToDelete = getView().getSelectedGroupKeys();
                deleteGroupKeys(descriptorToDelete);
            }
        }));
    }

    @Override
    protected void revealInParent() {
        RevealContentEvent.fire(this, DsdPresenter.TYPE_SetContextAreaContentDsd, this);
    }

    @Override
    public void prepareFromRequest(PlaceRequest request) {
        super.prepareFromRequest(request);
        String dsdIdentifier = PlaceRequestUtils.getDsdParamFromUrl(placeManager);// DSD identifier is the URN without the prefix
        if (!StringUtils.isBlank(dsdIdentifier)) {
            retrieveGroupKeys(UrnUtils.generateUrn(UrnConstants.URN_SDMX_CLASS_DATASTRUCTURE_PREFIX, dsdIdentifier));
        }
    }

    @Override
    protected void onReveal() {
        super.onReveal();
        SelectViewDsdDescriptorEvent.fire(this, DsdTabTypeEnum.GROUP_KEYS);
    }

    @Override
    public void saveGroupKeys(DescriptorDto descriptorDto) {
        dispatcher.execute(new SaveDescriptorForDsdAction(dataStructureDefinitionDto.getUrn(), descriptorDto), new WaitingAsyncCallbackHandlingError<SaveDescriptorForDsdResult>(this) {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fireErrorMessage(DsdGroupKeysTabPresenter.this, caught);
            }
            @Override
            public void onWaitSuccess(SaveDescriptorForDsdResult result) {
                fireSuccessMessage(MetamacSrmWeb.getMessages().dsdGroupKeysSaved());
                dataStructureDefinitionDto = result.getDataStructureDefinitionMetamacDto();
                updateGroupKeysList(false); // Do no update the view!! The method onDimensionSaved updates the dimension list in the view
                getView().onGroupKeysSaved(result.getDescriptorDtoSaved());
            }
        });
    }

    @Override
    public void deleteGroupKeys(List<DescriptorDto> descriptorsToDelete) {
        dispatcher.execute(new DeleteDescriptorListForDsdAction(dataStructureDefinitionDto.getUrn(), descriptorsToDelete),
                new WaitingAsyncCallbackHandlingError<DeleteDescriptorListForDsdResult>(this) {

                    @Override
                    public void onWaitFailure(Throwable caught) {
                        updateGroupKeysList(true);
                        ShowMessageEvent.fireErrorMessage(DsdGroupKeysTabPresenter.this, caught);
                    }
                    @Override
                    public void onWaitSuccess(DeleteDescriptorListForDsdResult result) {
                        updateGroupKeysList(true);
                        fireSuccessMessage(MetamacSrmWeb.getMessages().dsdGroupKeysDeleted());
                    }
                });
    }

    public void retrieveGroupKeys(String urn) {
        Set<TypeComponentList> descriptorsToRetrieve = new HashSet<TypeComponentList>();
        descriptorsToRetrieve.add(TypeComponentList.DIMENSION_DESCRIPTOR);
        descriptorsToRetrieve.add(TypeComponentList.GROUP_DIMENSION_DESCRIPTOR);
        dispatcher.execute(new GetDsdAndDescriptorsAction(urn, descriptorsToRetrieve), new WaitingAsyncCallbackHandlingError<GetDsdAndDescriptorsResult>(this) {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fireErrorMessage(DsdGroupKeysTabPresenter.this, caught);
            }
            @Override
            public void onWaitSuccess(GetDsdAndDescriptorsResult result) {
                dataStructureDefinitionDto = result.getDsd();
                dimensionComponentDtos = CommonUtils.getDimensionComponents(result.getDimensions());
                groupKeys = result.getGroupKeys();
                getView().setDsdGroupKeys(dataStructureDefinitionDto, dimensionComponentDtos, groupKeys);
            }
        });
    }

    private void updateGroupKeysList(final boolean updateView) {
        groupKeys = new ArrayList<DescriptorDto>();

        Set<TypeComponentList> descriptorsToRetrieve = new HashSet<TypeComponentList>();
        descriptorsToRetrieve.add(TypeComponentList.GROUP_DIMENSION_DESCRIPTOR);
        dispatcher.execute(new GetDsdAndDescriptorsAction(dataStructureDefinitionDto.getUrn(), descriptorsToRetrieve), new WaitingAsyncCallbackHandlingError<GetDsdAndDescriptorsResult>(this) {

            @Override
            public void onWaitFailure(Throwable caught) {
                ShowMessageEvent.fireErrorMessage(DsdGroupKeysTabPresenter.this, caught);
            }
            @Override
            public void onWaitSuccess(GetDsdAndDescriptorsResult result) {
                dataStructureDefinitionDto = result.getDsd();
                groupKeys = result.getGroupKeys();
                if (updateView) {
                    getView().setDsdGroupKeys(dataStructureDefinitionDto, dimensionComponentDtos, groupKeys);
                }
            }
        });
    }
}
