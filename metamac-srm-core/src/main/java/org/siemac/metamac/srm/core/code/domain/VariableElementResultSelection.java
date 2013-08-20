package org.siemac.metamac.srm.core.code.domain;

public class VariableElementResultSelection {

    private boolean returnOnlyGeographicalVariableElements = false;
    private boolean longitudeLatitude                      = false;
    private boolean shapeWkt                               = false;
    private boolean shapeGeojson                           = false;
    private boolean geographicalGranularity                = false;

    public boolean isReturnOnlyGeographicalVariableElements() {
        return returnOnlyGeographicalVariableElements;
    }

    public void setReturnOnlyGeographicalVariableElements(boolean returnOnlyGeographicalVariable) {
        this.returnOnlyGeographicalVariableElements = returnOnlyGeographicalVariable;
    }

    public boolean isLongitudeLatitude() {
        return longitudeLatitude;
    }

    public void setLongitudeLatitude(boolean longitudeLatitude) {
        this.longitudeLatitude = longitudeLatitude;
    }

    public boolean isShapeGeojson() {
        return shapeGeojson;
    }

    public void setShapeGeojson(boolean shapeGeojson) {
        this.shapeGeojson = shapeGeojson;
    }

    public boolean isShapeWkt() {
        return shapeWkt;
    }

    public void setShapeWkt(boolean shapeWkt) {
        this.shapeWkt = shapeWkt;
    }

    public boolean isGeographicalGranularity() {
        return geographicalGranularity;
    }

    public void setGeographicalGranularity(boolean geographicalGranularity) {
        this.geographicalGranularity = geographicalGranularity;
    }
}
