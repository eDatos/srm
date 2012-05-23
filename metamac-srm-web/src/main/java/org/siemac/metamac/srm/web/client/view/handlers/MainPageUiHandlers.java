package org.siemac.metamac.internal.web.client.view.handlers;

import com.gwtplatform.mvp.client.UiHandlers;

public interface MainPageUiHandlers extends UiHandlers {

    void onNavigationPaneSectionHeaderClicked(String name);
    void onNavigationPaneSectionClicked(String name);
}
