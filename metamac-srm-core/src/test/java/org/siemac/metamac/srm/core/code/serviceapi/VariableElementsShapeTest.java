package org.siemac.metamac.srm.core.code.serviceapi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.Serializable;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.ArrayUtils;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.FeatureSource;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.siemac.metamac.srm.core.code.domain.VariableElement;

import com.arte.statistic.sdmx.srm.core.base.domain.IdentifiableArtefact;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.WKTReader;

public class VariableElementsShapeTest {

    @Test
    public void testParseShapeComarcas() throws Exception {
        URL shapeFileUrl = this.getClass().getResource("/geojson/comarcas_n1/polygons");
        List<VariableElement> variableElements = parseShapeFile(shapeFileUrl, "MULTIPOLYGON", "POLYGON");
        assertEquals(11, variableElements.size());
        int i = 0;
        {
            VariableElement variableElement = variableElements.get(i++);
            assertEquals("HIERRO", variableElement.getIdentifiableArtefact().getCode());
            assertEquals(
                    "MULTIPOLYGON (((-17.91683252750836 27.84729983929485, -17.893904180037357 27.83153207983529, -17.895640080161346 27.82350354176185, -17.885513996104752 27.816270624578568, -17.883344120949765 27.802962056961327, -17.91343305643222 27.77048625880839, -17.909454951981417 27.764916912577263, -17.92724792825229 27.73157316436233, -17.948657363114805 27.72846300997352, -17.957047547047413 27.719638851009915, -17.958638788827734 27.69208143654161, -17.972019685616807 27.672263243459415, -17.970500773008318 27.64897325012925, -17.9777336901916 27.639136482759984, -17.990969928637007 27.63834086186982, -18.009630854969874 27.649407225160246, -18.028581097990074 27.678338893893375, -18.04999053285259 27.69208143654161, -18.145392710500083 27.70365410403486, -18.16014786155398 27.71573307573094, -18.15891826563282 27.728896985004518, -18.15168534844954 27.731862481049664, -18.15327659022986 27.756454399472823, -18.132084142882846 27.770920233839387, -18.11378486240914 27.75898592048697, -18.082321672661863 27.75276561170935, -18.03639264854802 27.761662099844784, -17.98880005348202 27.79963491505702, -17.99154856201167 27.822129287497027, -17.971947356444975 27.826975342009824, -17.957698509593907 27.84144117637639, -17.91683252750836 27.84729983929485)))",
                    variableElement.getShape());
        }
        {
            VariableElement variableElement = variableElements.get(i++);
            assertEquals("FUERTEVENTURA", variableElement.getIdentifiableArtefact().getCode());
            assertEquals(
                    "MULTIPOLYGON (((-13.878043901535358 28.751993120579797, -13.872980859507061 28.742156353210536, -13.86502465060545 28.74533883677118, -13.86654356321394 28.733187535903266, -13.842964253196438 28.72609927706365, -13.837684223652643 28.713514001164736, -13.822567426739583 28.59265195503209, -13.830234318953861 28.57522062462038, -13.825605251956562 28.554896127335358, -13.838841490401968 28.538477405329306, -13.83500804429483 28.5250241793684, -13.843542886571102 28.50325309864672, -13.862637787934966 28.493705647964788, -13.867773159135098 28.480397080347547, -13.856996112532007 28.45992792471886, -13.861552850357475 28.43099625598573, -13.849256891145895 28.41378191308952, -13.8532349955967 28.392010832367838, -13.859961608577153 28.392661794914336, -13.886217097952468 28.324961690078812, -13.895402902775237 28.323587435813987, -13.893811660994913 28.313967655960223, -13.90343144084868 28.300369771655653, -13.897789765445719 28.285686949773588, -13.910519699688296 28.273029344702845, -13.917463300184247 28.251547580668497, -13.945888664714547 28.228185258166494, -13.987405609346586 28.227606624791832, -14.012937807003572 28.207426785850473, -14.073983628030476 28.201061818729187, -14.219075946727118 28.16475257446911, -14.260665220530992 28.112458583233977, -14.256470128564688 28.1140498250143, -14.330390542177833 28.04461382005479, -14.36105811103495 28.047073011897105, -14.368652674077397 28.054016612393056, -14.409446326991109 28.058284033531194, -14.477508077685796 28.077523593238723, -14.507958659027414 28.065589279886307, -14.501666021077959 28.079548810050042, -14.492407887083356 28.083526914500847, -14.499206829235643 28.10066892822523, -14.49146760784953 28.10913144132967, -14.481052207105604 28.099583990647734, -14.455520009448618 28.104647032676034, -14.450095321561156 28.099583990647734, -14.368508015733731 28.118027929465104, -14.30825781559699 28.14804453577573, -14.22319870952159 28.21422572800276, -14.208298900124028 28.259648447913772, -14.213361942152325 28.287133533210245, -14.20395914981406 28.327782527780293, -14.176474064517587 28.34644345411316, -14.161501925948192 28.371469347567317, -14.16273152186935 28.389262323838192, -14.155643263029733 28.394542353381986, -14.15839177155938 28.40741694596823, -14.146674445722462 28.43866314820001, -14.100383775749457 28.47555102583475, -14.063785214802047 28.542817155639273, -14.066606052503527 28.550411718681723, -14.033479291804095 28.592507296688424, -14.038397675488726 28.6149293399566, -14.00874271503727 28.676770781873664, -14.017566874000874 28.693406491395212, -14.016047961392385 28.71488825542956, -14.005053927273796 28.710693163463258, -13.998978276839837 28.723712414393166, -13.989213838642407 28.723567756049498, -13.976917879430827 28.734417131824422, -13.956882698833136 28.734778777683587, -13.942055218607406 28.745700482630344, -13.938511089187598 28.738539894618892, -13.926721434178848 28.75119749968964, -13.916161375091257 28.746496103520503, -13.891931102527261 28.75654985840527, -13.878043901535358 28.751993120579797)), ((-13.81403258446331 28.741143744804877, -13.819240284835272 28.735646727745582, -13.830812952328525 28.739263186337222, -13.835875994356822 28.752137778923466, -13.812947646885817 28.765301688197038, -13.81403258446331 28.741143744804877)))",
                    variableElement.getShape());
        }
        {
            VariableElement variableElement = variableElements.get(i++);
            assertEquals("GRAN_CANARIA_METROP", variableElement.getIdentifiableArtefact().getCode());
        }
        {
            VariableElement variableElement = variableElements.get(i++);
            assertEquals("GRAN_CANARIA_NORTE", variableElement.getIdentifiableArtefact().getCode());
        }
        {
            VariableElement variableElement = variableElements.get(i++);
            assertEquals("GRAN_CANARIA_SUR", variableElement.getIdentifiableArtefact().getCode());
        }
        {
            VariableElement variableElement = variableElements.get(i++);
            assertEquals("GOMERA", variableElement.getIdentifiableArtefact().getCode());
        }
        {
            VariableElement variableElement = variableElements.get(i++);
            assertEquals("PALMA", variableElement.getIdentifiableArtefact().getCode());
        }
        {
            VariableElement variableElement = variableElements.get(i++);
            assertEquals("LANZAROTE", variableElement.getIdentifiableArtefact().getCode());
        }
        {
            VariableElement variableElement = variableElements.get(i++);
            assertEquals("TENERIFE_METROP", variableElement.getIdentifiableArtefact().getCode());
        }
        {
            VariableElement variableElement = variableElements.get(i++);
            assertEquals("TENERIFE_NORTE", variableElement.getIdentifiableArtefact().getCode());
        }
        {
            VariableElement variableElement = variableElements.get(i++);
            assertEquals("TENERIFE_SUR", variableElement.getIdentifiableArtefact().getCode());
        }
        assertEquals(variableElements.size(), i);
    }

    @Test
    public void testParseShapeComarcasPoints() throws Exception {
        URL shapeFileUrl = this.getClass().getResource("/geojson/comarcas_n1/points");
        List<VariableElement> variableElements = parseShapeFile(shapeFileUrl, "POINT");
        assertEquals(2, variableElements.size());
        int i = 0;
        {
            VariableElement variableElement = variableElements.get(i++);
            assertEquals("TENERIFE_NORTE", variableElement.getIdentifiableArtefact().getCode());
            assertNull(variableElement.getShape());
            assertEquals("-16.61153654488294", variableElement.getLongitude());
            assertEquals("28.26462033711757", variableElement.getLatitude());
        }
        {
            VariableElement variableElement = variableElements.get(i++);
            assertNull(variableElement.getShape());
            assertEquals("GOMERA", variableElement.getIdentifiableArtefact().getCode());
            assertNull(variableElement.getShape());
            assertEquals("-17.23229859783003", variableElement.getLongitude());
            assertEquals("28.120675513245782", variableElement.getLatitude());
        }
        assertEquals(variableElements.size(), i);
    }

    private List<VariableElement> parseShapeFile(URL shapeFileUrl, String... geometryType) throws Exception {

        List<VariableElement> variableElements = new ArrayList<VariableElement>();

        // Connection parameters
        Map<String, Serializable> datastoreParameters = new HashMap<String, Serializable>();
        datastoreParameters.put("url", shapeFileUrl);
        DataStore dataStore = DataStoreFinder.getDataStore(datastoreParameters);

        String[] typeNames = dataStore.getTypeNames();
        String typeName = typeNames[0];

        FeatureSource<SimpleFeatureType, SimpleFeature> featureSource = dataStore.getFeatureSource(typeName);
        FeatureCollection<SimpleFeatureType, SimpleFeature> collection = featureSource.getFeatures();
        FeatureIterator<SimpleFeature> iterator = collection.features();

        while (iterator.hasNext()) {
            SimpleFeature feature = iterator.next();
            Geometry geometry = (Geometry) feature.getDefaultGeometry();

            if (!ArrayUtils.contains(geometryType, geometry.getGeometryType().toUpperCase())) {
                continue;
            }

            Point centroid = geometry.getCentroid();
            assertNotNull(centroid);

            // Transform Geometry to well-known text
            String text = geometry.toText();
            assertNotNull(text);

            // Transform well-known text to Geometry to validate
            WKTReader wktReader = new WKTReader();
            Geometry geometryBuilded = wktReader.read(text);
            assertTrue(geometry.equals(geometryBuilded));

            // Attributes
            Object code = feature.getAttribute("VAR_ELEM");
            assertNotNull(code);
            VariableElement variableElement = new VariableElement();
            variableElement.setIdentifiableArtefact(new IdentifiableArtefact());
            variableElement.getIdentifiableArtefact().setCode(code.toString());
            if (geometry instanceof MultiPolygon) {
                MultiPolygon multiPolygon = (MultiPolygon) geometry;
                assertNotNull(multiPolygon);
                variableElement.setShape(text);
            } else if (geometry instanceof Point) {
                Point point = (Point) geometry;
                assertNotNull(point);
                variableElement.setLongitude(String.valueOf(point.getX()));
                variableElement.setLatitude(String.valueOf(point.getY()));
            }
            variableElements.add(variableElement);
        }
        return variableElements;
    }
}
