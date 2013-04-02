package org.siemac.metamac.srm.core.importation.domain;

import java.util.ArrayList;
import java.util.List;

public class InternationalStringCsv {

    private int                startPosition;
    private int                endPosition;
    private final List<String> locales = new ArrayList<String>();

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
}
