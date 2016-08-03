package org.siemac.metamac.srm.core.code.domain;

import java.util.HashMap;
import java.util.Map;

import com.arte.statistic.sdmx.srm.core.common.domain.IdentifiableArtefactResult;
import com.arte.statistic.sdmx.srm.core.common.domain.ItemResult;

public class VariableElementResult extends IdentifiableArtefactResult {

    private Long                      idDatabase;
    private final Map<String, String> shortName = new HashMap<String, String>();
    private String                    shapeWkt;
    private String                    shapeGeojson;
    private Double                    latitude;
    private Double                    longitude;
    private ItemResult                geographicalGranularity;

    public Long getIdDatabase() {
        return idDatabase;
    }

    public void setIdDatabase(Long idDatabase) {
        this.idDatabase = idDatabase;
    }

    public Map<String, String> getShortName() {
        return shortName;
    }

    public String getShapeWkt() {
        return shapeWkt;
    }

    public void setShapeWkt(String shapeWkt) {
        this.shapeWkt = shapeWkt;
    }

    public String getShapeGeojson() {
        return shapeGeojson;
    }

    public void setShapeGeojson(String shapeGeojson) {
        this.shapeGeojson = shapeGeojson;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public ItemResult getGeographicalGranularity() {
        return geographicalGranularity;
    }

    public void setGeographicalGranularity(ItemResult geographicalGranularity) {
        this.geographicalGranularity = geographicalGranularity;
    }
}
