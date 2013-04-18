package org.siemac.metamac.srm.core.normalisation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.KeywordAnalyzer;
import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Based on Dice's coefficient used as a similarity measure
 */

public class DiceLuceneRamAproxStringMatch extends AproxStringMatch {

    private final Logger         logger         = LoggerFactory.getLogger(DiceLuceneRamAproxStringMatch.class);

    private IndexReader          reader;
    private IndexSearcher        searcher;
    private RAMDirectory         ramDirectory;

    private static final Version LUCENE_VERSION = Version.LUCENE_36;
    private static final String  FIELD_NGRAMS   = "ngrams";
    private static final String  FIELD_KEYWORD  = "keyword";

    public DiceLuceneRamAproxStringMatch(Map<String, String> dictionary) throws MetamacException {
        super(dictionary);
        indexDictionary();
    }

    @Override
    public List<MatchResult> getSuggestedWords(String word, int maxResults) throws MetamacException {
        try {
            return compareToDictionary(word, maxResults);
        } catch (Exception e) {
            logger.error("Error normalising variable elements", e);
            throw new MetamacException(ServiceExceptionType.CODES_VARIABLE_ELEMENTS_NORMALISATION_ERROR, e);
        }
    }

    public void shutdown() {
        try {
            if (reader != null) {
                reader.close();
                reader = null;
            }
        } catch (IOException e) {
            logger.error("Error closing index reader", e);
        }
        try {
            if (searcher != null) {
                searcher.close();
                searcher = null;
            }
        } catch (IOException e) {
            logger.error("Error closing index searcher", e);
        }
        if (ramDirectory != null) {
            ramDirectory.close();
            ramDirectory = null;
        }
    }

    private List<MatchResult> compareToDictionary(String word, int maxResults) throws Exception {
        String wordNormalized = normaliseWord(word);
        Query query = buildQuery(wordNormalized);

        int numHint = maxResults * 2; // to improve matching with more results
        TopScoreDocCollector collector = TopScoreDocCollector.create(numHint, false);
        IndexSearcher searcher = getIndexSearcherRam();
        searcher.search(query, collector);

        List<MatchResult> results = new ArrayList<MatchResult>();
        for (ScoreDoc docInfo : collector.topDocs().scoreDocs) {
            Document document = searcher.doc(docInfo.doc);
            String dictionaryKey = document.get(FIELD_KEYWORD);
            results.add(new MatchResult(dictionaryKey, docInfo.score));
        }
        if (results.size() <= 1) {
            return results;
        }
        Collections.sort(results);
        improveScore(word, results);
        return results.subList(0, maxResults);

    }

    /**
     * Improve score when first and second results has similar scores, thanks to Dice Algorithm
     */
    private void improveScore(String word, List<MatchResult> matches) {
        if (matches.size() > 1) {
            double score1 = matches.get(0).getScore();
            double score2 = matches.get(1).getScore();
            // if The score proportion is not significant, then we try new
            if (score1 / score2 < 1.5) {
                // Recalculate
                List<MatchResult> transformedResults = new ArrayList<MatchResult>();
                double maxScore = 0;
                for (MatchResult match : matches) {
                    List<String> baseNgrams = getDictionaryNgrams(match.getDictionaryKey());
                    double score = computeDiceCoefficient(word, baseNgrams);
                    transformedResults.add(new MatchResult(match.getDictionaryKey(), score));
                    if (score > maxScore) {
                        maxScore = score;
                    }
                }
                // We change old scores after being affected by the new coefficient
                for (int i = 0; i < matches.size(); i++) {
                    MatchResult original = matches.get(i);
                    MatchResult transformed = transformedResults.get(i);

                    double newScore = (transformed.getScore() / maxScore) * original.getScore();
                    original.setScore(newScore);
                }
                Collections.sort(matches);
            }
        }
    }

    private Query buildQuery(String word) throws ParseException {
        Analyzer analyzer = new KeywordAnalyzer();
        QueryParser queryparser = new QueryParser(LUCENE_VERSION, FIELD_NGRAMS, analyzer);
        List<String> ngrams = wordMultiNgrams(word.toLowerCase());
        StringBuilder queryBuilder = new StringBuilder();
        for (String ngram : ngrams) {
            String piece = ngram.trim();
            if (!piece.isEmpty()) {
                queryBuilder.append("\"" + QueryParser.escape(piece) + "\" ");
            }
        }

        String ngramsStr = queryBuilder.toString();
        Query query = queryparser.parse(ngramsStr);
        return query;
    }

    private IndexSearcher getIndexSearcherRam() throws IOException {
        if (reader == null) {
            reader = IndexReader.open(ramDirectory);
        }
        if (searcher == null) {
            searcher = new IndexSearcher(reader);
        }
        return searcher;
    }

    private void indexDictionary() throws MetamacException {
        IndexWriter indexWriter = null;
        try {
            ramDirectory = new RAMDirectory();
            indexWriter = createIndexWriter(ramDirectory);

            for (Entry<String, String> entry : dictionary.entrySet()) {
                Document doc = wordToDictionaryDocument(entry);
                indexWriter.addDocument(doc);
            }
            indexWriter.commit();
        } catch (Exception e) {
            logger.error("Error indexing dictionary", e);
            throw new MetamacException(ServiceExceptionType.CODES_VARIABLE_ELEMENTS_NORMALISATION_ERROR, e);
        } finally {
            if (indexWriter != null) {
                try {
                    indexWriter.close();
                } catch (Exception e) {
                    logger.error("Error closing indexWriter", e);
                }
            }
        }
    }

    private Document wordToDictionaryDocument(Entry<String, String> dictionaryWord) {
        Document doc = new Document();
        doc.add(new Field(FIELD_KEYWORD, dictionaryWord.getKey(), Store.YES, Index.NO));
        List<String> ngrams = getDictionaryNgrams(dictionaryWord.getKey());
        for (String ngram : ngrams) {
            doc.add(new Field(FIELD_NGRAMS, ngram, Store.YES, Index.NOT_ANALYZED));
        }
        return doc;
    }

    private double computeDiceCoefficient(String word, List<String> ngrams) {
        List<String> inputNgrams = wordMultiNgrams(word);
        List<String> baseNgrams = new ArrayList<String>(ngrams);
        int intersection = 0;
        int union = inputNgrams.size() + baseNgrams.size();
        for (String pair1 : inputNgrams) {
            if (baseNgrams.contains(pair1)) {
                intersection++;
                baseNgrams.remove(pair1);
            }
        }
        return ((2.0 * intersection) / union);
    }

    private IndexWriter createIndexWriter(RAMDirectory ramDirectory) throws IOException {
        Analyzer analyzer = new WhitespaceAnalyzer(LUCENE_VERSION);
        IndexWriterConfig config = new IndexWriterConfig(LUCENE_VERSION, analyzer);
        return new IndexWriter(ramDirectory, config);
    }
}
