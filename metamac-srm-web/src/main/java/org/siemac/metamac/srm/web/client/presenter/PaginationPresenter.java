package org.siemac.metamac.internal.web.client.presenter;

import com.google.gwt.event.shared.EventBus;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.proxy.Proxy;

public abstract class PaginationPresenter<V extends View, Proxy_ extends Proxy<?>> extends Presenter<V, Proxy_> {

    protected int     maxResults;
    protected int     firstResult;
    protected int     pageNumber;
    protected int     numberOfElements;

    protected Integer totalResults;

    private int       defultMaxResults;

    @Inject
    public PaginationPresenter(EventBus eventBus, V view, Proxy_ proxy, DispatchAsync dispatcher, int defaultMaxResults) {
        super(eventBus, view, proxy);

        this.defultMaxResults = defaultMaxResults;
    }

    protected void retrieveResultSet() {
        // OVERRIDE!!!!
    }

    // PAGINATION

    protected void resultSetFirstButtonClicked() {
        firstResult = 0;
        pageNumber = 1;

        retrieveResultSet();
    }

    protected void resultSetPreviousButtonClicked() {
        firstResult -= maxResults;
        pageNumber--;

        retrieveResultSet();
    }

    protected void resultSetNextButtonClicked() {
        firstResult += numberOfElements;
        pageNumber++;

        retrieveResultSet();
    }

    protected void resultSetLastButtonClicked() {
        int numPages = totalResults / defultMaxResults;
        if ((numPages * defultMaxResults) < totalResults) {
            numPages++;
        }
        firstResult = (numPages * defultMaxResults) - defultMaxResults;
        pageNumber = numPages;
        retrieveResultSet();
    }

    protected int getMaxResults() {
        return maxResults;
    }

    protected void setMaxResults(int maxResults) {
        this.maxResults = maxResults;
    }

    protected int getFirstResult() {
        return firstResult;
    }

    protected void setFirstResult(int firstResult) {
        this.firstResult = firstResult;
    }

    protected int getPageNumber() {
        return pageNumber;
    }

    protected void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    protected int getNumberOfElements() {
        return numberOfElements;
    }

    protected void setNumberOfElements(int numberOfElements) {
        this.numberOfElements = numberOfElements;
    }

    public void onResultSetFirstButtonClicked() {
        resultSetFirstButtonClicked();
    }

    public void onResultSetLastButtonClicked() {
        resultSetLastButtonClicked();
    }

    public void onResultSetPreviousButtonClicked() {
        resultSetPreviousButtonClicked();
    }

    public void onResultSetNextButtonClicked() {
        resultSetNextButtonClicked();
    }

    protected void initializePaginationSettings() {
        maxResults = defultMaxResults;
        firstResult = 0;
        pageNumber = 1;
        numberOfElements = maxResults;
    }

}
