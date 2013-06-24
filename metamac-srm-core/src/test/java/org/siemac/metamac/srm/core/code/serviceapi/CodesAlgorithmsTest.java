package org.siemac.metamac.srm.core.code.serviceapi;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.NumberFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.siemac.metamac.srm.core.normalisation.DiceAproxStringMatch;
import org.siemac.metamac.srm.core.normalisation.DiceLuceneRamAproxStringMatch;
import org.siemac.metamac.srm.core.normalisation.MatchResult;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/srm/applicationContext-test.xml"})
public class CodesAlgorithmsTest {

    @Test
    public void testDiceAlgorithm() throws Exception {
        Map<String, String> dictionary = new HashMap<String, String>();

        dictionary.put("urn:1", "Tenerife");
        dictionary.put("urn:2", "La Palma");
        dictionary.put("urn:3", "La Gomera");
        dictionary.put("urn:4", "El Hierro");
        dictionary.put("urn:5", "HIERRO");

        DiceAproxStringMatch diceAproxStringMatch = new DiceAproxStringMatch(dictionary);

        {
            List<MatchResult> result = diceAproxStringMatch.getSuggestedWords("TENErifE", 1);
            assertEquals(1, result.size());
            assertEquals("urn:1", result.get(0).getDictionaryKey());
        }
        {
            List<MatchResult> result = diceAproxStringMatch.getSuggestedWords("Tenerife", 1);
            assertEquals(1, result.size());
            assertEquals("urn:1", result.get(0).getDictionaryKey());
        }
        {
            List<MatchResult> result = diceAproxStringMatch.getSuggestedWords("El Hierro", 1);
            assertEquals(1, result.size());
            assertEquals("urn:4", result.get(0).getDictionaryKey());
        }
        {
            List<MatchResult> result = diceAproxStringMatch.getSuggestedWords("Hierro El", 1);
            assertEquals(1, result.size());
            assertEquals("urn:5", result.get(0).getDictionaryKey());
        }
        {
            List<MatchResult> result = diceAproxStringMatch.getSuggestedWords("Hierro EL", 2);
            assertEquals(2, result.size());
            assertEquals("urn:5", result.get(0).getDictionaryKey());
            assertEquals("urn:4", result.get(1).getDictionaryKey());
        }
    }

    @Test
    public void testCountries() throws Exception {
        Map<String, String> dictionary = loadDictionary("/dictionaries/countries.txt");
        DiceAproxStringMatch diceAproxStringMatch = new DiceAproxStringMatch(dictionary);
        {
            List<MatchResult> result = diceAproxStringMatch.getSuggestedWords("España", 1);
            assertEquals(1, result.size());
            assertEquals("46", result.get(0).getDictionaryKey());
        }
        {
            List<MatchResult> result = diceAproxStringMatch.getSuggestedWords("Caiman de Islas", 1);
            assertEquals(1, result.size());
            assertEquals("45", result.get(0).getDictionaryKey());
        }
        {
            List<MatchResult> result = diceAproxStringMatch.getSuggestedWords("Arabia", 1);
            assertEquals(1, result.size());
            assertEquals("7", result.get(0).getDictionaryKey());
        }
    }

    @Ignore
    // it takes 3mins
    @Test
    public void testMunicipalities() throws Exception {
        Map<String, String> dictionary = loadDictionary("/dictionaries/municipalities.txt");

        DiceAproxStringMatch diceAproxStringMatch = new DiceAproxStringMatch(dictionary);
        {
            List<MatchResult> result = diceAproxStringMatch.getSuggestedWords("Santa ÚRSULA", 1);
            assertEquals(1, result.size());
            assertEquals("5733", result.get(0).getDictionaryKey());
        }

        for (String dictionaryValue : dictionary.values()) {
            List<MatchResult> result = diceAproxStringMatch.getSuggestedWords(dictionaryValue, 1);
            assertEquals(1, result.size());
        }
    }

    @Test
    public void testMunicipalitiesWithLucene() throws Exception {
        Map<String, String> dictionary = loadDictionary("/dictionaries/municipalities.txt");

        System.out.println("Init lucene: " + new Date());
        DiceLuceneRamAproxStringMatch luceneStringMatch = new DiceLuceneRamAproxStringMatch(dictionary);
        System.out.println("Lucene inicialized: " + new Date());
        {
            List<MatchResult> result = luceneStringMatch.getSuggestedWords("Santa ÚRSULA", 1);
            assertEquals(1, result.size());
            assertEquals("5733", result.get(0).getDictionaryKey());
        }
        {
            List<MatchResult> result = luceneStringMatch.getSuggestedWords("ÚRSULA sant", 1);
            assertEquals(1, result.size());
            assertEquals("5733", result.get(0).getDictionaryKey());
        }
        {
            List<MatchResult> result = luceneStringMatch.getSuggestedWords("Tenerife, Santa Cruz", 1);
            assertEquals(1, result.size());
            assertEquals("5732", result.get(0).getDictionaryKey());
        }

        // Search all
        System.out.println("Init search: " + new Date());
        for (String dictionaryKey : dictionary.keySet()) {
            String dictionaryValue = dictionary.get(dictionaryKey);
            List<MatchResult> result = luceneStringMatch.getSuggestedWords(dictionaryValue, 1);
            assertEquals(1, result.size());
            if (!dictionaryKey.equals(result.get(0).getDictionaryKey())) {
                System.out.println("not equals: " + dictionaryValue + " - " + dictionary.get(result.get(0).getDictionaryKey()));
            }
        }
        System.out.println("Finish search: " + new Date());

        luceneStringMatch.shutdown();
    }

    @Test
    public void testBigDictionaryWithLucene() throws Exception {
        Map<String, String> dictionary = loadDictionary("/dictionaries/big-dictionary.txt");
        assertEquals(18765, dictionary.keySet().size());
        System.out.println("Init lucene: " + new Date());

        DiceLuceneRamAproxStringMatch luceneStringMatch = new DiceLuceneRamAproxStringMatch(dictionary);
        System.out.println("Lucene inicialized: " + new Date());
        {
            // 0102902920##----- Terneras de las razas de montaña gris, parda, amarilla y manchada de
            List<MatchResult> result = luceneStringMatch.getSuggestedWords("Terneras de montaña de color amarillo", 1);
            assertEquals(1, result.size());
            assertEquals("0102902920", result.get(0).getDictionaryKey());
        }
        {
            // 03##PESCADOS Y CRUSTÁCEOS; MOLUSCOS Y OTROS INVERTEBRADOS ACUÁTICOS:
            List<MatchResult> result = luceneStringMatch.getSuggestedWords("Crustáceos y pescados", 1);
            assertEquals(1, result.size());
            assertEquals("03", result.get(0).getDictionaryKey());
        }
        {
            // 6907##Placas y baldosas, de cerámica, sin barnizar ni esmaltar, para pavimentación o
            List<MatchResult> result = luceneStringMatch.getSuggestedWords("Baldosas para el pavimento pero sin barniz", 1);
            assertEquals(1, result.size());
            assertEquals("6907", result.get(0).getDictionaryKey());
        }

        luceneStringMatch.shutdown();
    }

    @Test
    public void testBigDictionaryWithLuceneSearchAllDictionary() throws Exception {
        Map<String, String> dictionary = loadDictionary("/dictionaries/big-dictionary.txt");
        assertEquals(18765, dictionary.keySet().size());
        System.out.println("Init lucene: " + new Date());

        logMemory();
        DiceLuceneRamAproxStringMatch luceneStringMatch = new DiceLuceneRamAproxStringMatch(dictionary);
        logMemory();

        System.out.println("Lucene inicialized: " + new Date());

        // Search all
        System.out.println("Init search: " + new Date());
        for (String dictionaryKey : dictionary.keySet()) {
            String dictionaryValue = dictionary.get(dictionaryKey);
            List<MatchResult> result = luceneStringMatch.getSuggestedWords(dictionaryValue, 1);
            assertEquals(1, result.size());
            // if (!dictionaryKey.equals(result.get(0).getDictionaryKey())) {
            // System.out.println("not equals: " + dictionaryValue + " - " + dictionary.get(result.get(0).getDictionaryKey()));
            // }
        }
        System.out.println("Finish search: " + new Date());

        luceneStringMatch.shutdown();
    }

    private Map<String, String> loadDictionary(String filePath) throws IOException {
        Map<String, String> dictionary = new HashMap<String, String>();
        InputStreamReader inputStreamReader = new InputStreamReader(this.getClass().getResourceAsStream(filePath));
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
        String line = null;
        int i = 0;
        while ((line = bufferedReader.readLine()) != null) {
            i++;
            if (StringUtils.isBlank(line)) {
                continue;
            }
            String[] lineSplited = line.split("##");
            if (lineSplited.length > 2) {
                fail("Incorrect line " + i);
            }
            if (lineSplited.length != 2) {
                continue;
            }
            String key = lineSplited[0];
            String value = lineSplited[1];
            if (StringUtils.isBlank(key) || StringUtils.isBlank(value)) {
                continue;
            }
            if (dictionary.containsKey(key)) {
                dictionary.put(key, value);
            }
            dictionary.put(key, value);
        }
        return dictionary;
    }

    private void logMemory() {
        Runtime runtime = Runtime.getRuntime();
        NumberFormat format = NumberFormat.getInstance();

        long maxMemory = runtime.maxMemory();
        long allocatedMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        System.out.println("------------------------------------------");
        System.out.println("free memory: " + format.format(freeMemory / 1024));
        System.out.println("allocated memory: " + format.format(allocatedMemory / 1024));
        System.out.println("max memory: " + format.format(maxMemory / 1024));
        System.out.println("total free memory: " + format.format((freeMemory + (maxMemory - allocatedMemory)) / 1024));
        System.out.println("------------------------------------------");
    }
}
