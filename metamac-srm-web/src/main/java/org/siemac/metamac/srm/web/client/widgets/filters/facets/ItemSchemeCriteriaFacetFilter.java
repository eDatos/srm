package org.siemac.metamac.srm.web.client.widgets.filters.facets;

import java.util.List;

import org.siemac.metamac.core.common.dto.ExternalItemDto;
import org.siemac.metamac.core.common.enume.domain.TypeExternalArtefactsEnum;
import org.siemac.metamac.srm.web.client.widgets.filters.SimpleSrmRelatedResourceHiddenLastVersionFilterForm;
import org.siemac.metamac.srm.web.shared.criteria.RelatedResourceWebCriteria;
import org.siemac.metamac.web.common.client.MetamacWebCommon;
import org.siemac.metamac.web.common.client.constants.CommonWebConstants;
import org.siemac.metamac.web.common.client.utils.ExternalItemUtils;
import org.siemac.metamac.web.common.client.widgets.actions.search.SearchPaginatedAction;
import org.siemac.metamac.web.common.client.widgets.filters.base.FilterAction;
import org.siemac.metamac.web.common.client.widgets.filters.facets.FacetFilter;
import org.siemac.metamac.web.common.client.widgets.form.fields.external.SearchExternalItemLinkItem;
import org.siemac.metamac.web.common.client.widgets.windows.search.SearchRelatedResourceBasePaginatedWindow;
import org.siemac.metamac.web.common.shared.criteria.base.HasItemSchemeCriteria;

import com.arte.statistic.sdmx.v2_1.domain.dto.common.RelatedResourceDto;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;

public class ItemSchemeCriteriaFacetFilter implements FacetFilter {

    protected SearchExternalItemLinkItem     schemeFilter;
    protected SearchSrmSchemePaginatedWindow schemeWindow;
    protected FilterAction                   filterAction;

    public ItemSchemeCriteriaFacetFilter(final int maxResults, final SearchPaginatedAction<RelatedResourceWebCriteria> action) {
        super();
        schemeFilter = createSchemeFilterItem(action);
        schemeFilter.setShowTitle(true);
    }

    @Override
    public void setFilterAction(FilterAction action) {
        filterAction = action;
    }

    @Override
    public FormItem getFormItem() {
        return schemeFilter;
    }

    @Override
    public void setColSpan(int cols) {
        schemeFilter.setColSpan(cols);
    }

    public void populateCriteria(HasItemSchemeCriteria criteria) {
        ExternalItemDto selectedResource = schemeFilter.getExternalItemDto();
        criteria.setItemSchemeUrn(selectedResource != null ? selectedResource.getUrn() : null);
    }

    public void setFilterResources(List<RelatedResourceDto> resources) {
        if (schemeWindow != null) {
            schemeWindow.setResources(resources);
        }
    }

    public void refreshFilterSourcePaginationInfo(int firstResult, int elementsInPage, int totalResults) {
        if (schemeWindow != null) {
            schemeWindow.refreshSourcePaginationInfo(firstResult, elementsInPage, totalResults);
        }
    }

    public void setSelectedItemScheme(RelatedResourceDto selected) {
        schemeFilter.setExternalItem(selected != null ? ExternalItemUtils.getExternalItemDtoFromRelatedResourceDto(selected, getType(selected)) : null);
        filterAction.applyFilter();
    }

    private SearchExternalItemLinkItem createSchemeFilterItem(final SearchPaginatedAction<RelatedResourceWebCriteria> action) {

        SearchExternalItemLinkItem item = new SearchExternalItemLinkItem("SCHEME", MetamacWebCommon.getConstants().itemScheme()) {

            @Override
            public void onSearch() {

                schemeWindow = new SearchSrmSchemePaginatedWindow(MetamacWebCommon.getConstants().resourceSelection(), CommonWebConstants.FORM_LIST_MAX_RESULTS, action);

                schemeWindow.retrieveItems();
                schemeWindow.setSaveAction(new ClickHandler() {

                    @Override
                    public void onClick(ClickEvent event) {
                        setSelectedItemScheme(schemeWindow.getSelectedResource());
                        schemeWindow.markForDestroy();
                    }
                });
            }

            @Override
            protected void onClear() {
                super.onClear();
                setSelectedItemScheme(null);
            }
        };

        return item;
    }

    private class SearchSrmSchemePaginatedWindow extends SearchRelatedResourceBasePaginatedWindow<RelatedResourceDto, RelatedResourceWebCriteria> {

        public SearchSrmSchemePaginatedWindow(String title, int maxResults, SearchPaginatedAction<RelatedResourceWebCriteria> action) {
            super(title, maxResults, new SimpleSrmRelatedResourceHiddenLastVersionFilterForm(), action);
        }
    }

    private TypeExternalArtefactsEnum getType(RelatedResourceDto resource) {
        if (resource != null && resource.getType() != null) {
            for (TypeExternalArtefactsEnum type : TypeExternalArtefactsEnum.values()) {
                if (type.toString().equals(resource.getType().toString())) {
                    return type;
                }
            }
        }
        return null;
    }
}
