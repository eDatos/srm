package org.siemac.metamac.srm.core.task.domain;

import java.util.ArrayList;
import java.util.List;

public class InternationalStringTsv {

    private int                startPosition = -1;
    private int                endPosition   = -1;
    private final List<String> locales       = new ArrayList<String>();

    public int getStartPosition() {
        return startPosition;
    }

    public void setStartPosition(int startPosition) {
        this.startPosition = startPosition;
    }

    public int getEndPosition() {
        return endPosition;
    }

    public void setEndPosition(int endPosition) {
        this.endPosition = endPosition;
    }

    public void addLocale(String locale) {
        locales.add(locale);
    }

    public String getLocale(int position) {
        return locales.get(position);
    }

    public List<String> getLocales() {
        return locales;
    }

    public boolean isEndPositionSetted() {
        return endPosition != -1;
    }
}
