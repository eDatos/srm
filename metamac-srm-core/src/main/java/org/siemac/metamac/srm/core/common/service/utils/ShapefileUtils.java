package org.siemac.metamac.srm.core.common.service.utils;

import java.io.Serializable;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.FeatureSource;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.siemac.metamac.core.common.exception.MetamacExceptionBuilder;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;

public class ShapefileUtils {

    private static final String DATASTORE_PARAMETER_URL = "url";

    public static FeatureIterator<SimpleFeature> getShapefileIterator(URL shapeFileUrl) throws Exception {
        // Connection parameters
        Map<String, Serializable> datastoreParameters = new HashMap<String, Serializable>();
        datastoreParameters.put(DATASTORE_PARAMETER_URL, shapeFileUrl);
        DataStore dataStore = DataStoreFinder.getDataStore(datastoreParameters);

        String[] typeNames = dataStore.getTypeNames();
        if (typeNames == null || typeNames.length == 0) {
            throw MetamacExceptionBuilder.builder().withExceptionItems(ServiceExceptionType.IMPORTATION_SHAPE_ERROR_FILE_PARSING).withMessageParameters(shapeFileUrl).build();
        }
        String typeName = typeNames[0];

        FeatureSource<SimpleFeatureType, SimpleFeature> featureSource = dataStore.getFeatureSource(typeName);
        FeatureCollection<SimpleFeatureType, SimpleFeature> collection = featureSource.getFeatures();
        FeatureIterator<SimpleFeature> iterator = collection.features();
        return iterator;
    }
}
