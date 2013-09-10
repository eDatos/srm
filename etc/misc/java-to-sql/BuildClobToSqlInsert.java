package org.siemac.metamac.srm.core.code.serviceapi;

/**
 * ORACLE has restriction in line lenght. So, we must split in n lines
 * Sample use:
 * DECLARE
 * shapeWkt CLOB := '';
 * BEGIN
 * shapeWkt := shapeWkt || 'veryLargeS'; -----------> In this point, put result of System.out.print()
 * shapeWkt := shapeWkt || 'tringToSpl';
 * shapeWkt := shapeWkt || 'it';
 * INSERT INTO TABLE_NAME (VALUE) VALUES (shapeWkt);
 */
public class BuildClobToSqlInsert {

    public static void main(String[] args) {
        String field = "shapeGeoJson";
        int maximumLineLength = 10;
        String largeString = "veryLargeStringToSplit";
        StringBuilder largeStringInBlocks = new StringBuilder();
        String[] largeStringSplited = largeString.split("(?<=\\G.{" + maximumLineLength + "})");
        for (int i = 0; i < largeStringSplited.length; i++) {
            String string = largeStringSplited[i];
            largeStringInBlocks.append("\t" + field + " := " + field + " || '" + string + "';\n");
        }
        System.out.print(largeStringInBlocks.toString());

        /**
         * RESULT:
         * shapeGeoJson := shapeGeoJson || 'veryLargeS';
         * shapeGeoJson := shapeGeoJson || 'tringToSpl';
         * shapeGeoJson := shapeGeoJson || 'it';
         */
    }
}
