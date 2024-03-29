package org.siemac.metamac.srm.core.task.domain;

public class ImportationVariableElementsTsvHeader {

    private int                    columnsSize;
    private int                    codePosition;
    private InternationalStringTsv shortName;
    private int                    geographicalGranularityPosition = -1;
    private int                    renderingColorPosition          = -1;

    public int getColumnsSize() {
        return columnsSize;
    }

    public void setColumnsSize(int columnsSize) {
        this.columnsSize = columnsSize;
    }

    public int getCodePosition() {
        return codePosition;
    }

    public void setCodePosition(int codePosition) {
        this.codePosition = codePosition;
    }

    public InternationalStringTsv getShortName() {
        return shortName;
    }

    public void setShortName(InternationalStringTsv shortName) {
        this.shortName = shortName;
    }

    public int getGeographicalGranularityPosition() {
        return geographicalGranularityPosition;
    }

    public void setGeographicalGranularityPosition(int geographicalGranularityPosition) {
        this.geographicalGranularityPosition = geographicalGranularityPosition;
    }

    public boolean isGeographicalGranularitySetted() {
        return geographicalGranularityPosition != -1;
    }

    public int getRenderingColorPosition() {
        return renderingColorPosition;
    }

    public void setRenderingColorPosition(int renderingColorPosition) {
        this.renderingColorPosition = renderingColorPosition;
    }

    public boolean isRenderingColorSetted() {
        return renderingColorPosition != -1;
    }
}
