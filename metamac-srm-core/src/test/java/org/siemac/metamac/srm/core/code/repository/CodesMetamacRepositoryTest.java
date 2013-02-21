package org.siemac.metamac.srm.core.code.repository;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.siemac.metamac.srm.core.code.domain.CodeMetamacRepository;
import org.siemac.metamac.srm.core.code.domain.CodeMetamacResultExtensionPoint;
import org.siemac.metamac.srm.core.common.SrmBaseTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.arte.statistic.sdmx.srm.core.code.serviceapi.utils.CodesAsserts;
import com.arte.statistic.sdmx.srm.core.common.domain.AnnotationResult;
import com.arte.statistic.sdmx.srm.core.common.domain.ItemResult;

/**
 * Spring based transactional test with DbUnit support.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/srm/applicationContext-test.xml"})
@TransactionConfiguration(transactionManager = "txManagerCore", defaultRollback = true)
@Transactional
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class CodesMetamacRepositoryTest extends SrmBaseTest {

    @Autowired
    protected CodeMetamacRepository codeMetamacRepository;

    @Test
    public void testRetrieveCodesByCodelistUrn() throws Exception {

        // Retrieve
        List<ItemResult> codes = codeMetamacRepository.findCodesByCodelistByNativeSqlQuery(Long.valueOf(12));

        // Validate common metadata and SHORT_NAME
        assertEquals(9, codes.size());
        {
            // Code 01 (validate all metadata)
            ItemResult code = getItemResult(codes, CODELIST_1_V2_CODE_1);
            assertEquals(CODELIST_1_V2_CODE_1, code.getUrnProvider());
            assertEquals(CODELIST_1_V2_CODE_1, code.getUrn());
            assertEquals(null, code.getUriProvider());
            assertEquals("CODE01", code.getCode());
            assertEquals(null, code.getParent());
            assertEquals(2, code.getName().size());
            CodesAsserts.assertEqualsInternationalString(code.getName(), "es", "Nombre codelist-1-v2-code-1", "en", "Name codelist-1-v2-code-1");
            CodesAsserts.assertEqualsInternationalString(code.getDescription(), "es", "Descripción codelist-1-v2-code-1", null, null);
            CodesAsserts.assertEqualsInternationalString(((CodeMetamacResultExtensionPoint) code.getExtensionPoint()).getShortName(), "es", "Nombre corto variableElement 2-2", "en",
                    "Short name variableElement 2-2");

            assertEquals(2, code.getAnnotations().size());
            {
                AnnotationResult annotation = getAnnotationResult(code.getAnnotations(), "ANNOTATION211");
                assertEquals("ANNOTATION211", annotation.getCode());
                assertEquals("type-annotation211", annotation.getType());
                assertEquals("title-annotation211", annotation.getTitle());
                assertEquals("http://annotation211", annotation.getUrl());
                CodesAsserts.assertEqualsInternationalString(annotation.getText(), "es", "Anotación 1 code01", "en", "Annotation 1 code01");
            }
            {
                AnnotationResult annotation = getAnnotationResult(code.getAnnotations(), "ANNOTATION212");
                assertEquals("ANNOTATION212", annotation.getCode());
                assertEquals("type-annotation212", annotation.getType());
                assertEquals("title-annotation212", annotation.getTitle());
                assertEquals("http://annotation212", annotation.getUrl());
                CodesAsserts.assertEqualsInternationalString(annotation.getText(), "es", "Anotación 2 code01", null, null);
            }
        }
        {
            // Code 02
            ItemResult code = getItemResult(codes, CODELIST_1_V2_CODE_2);
            assertEquals(CODELIST_1_V2_CODE_2, code.getUrn());
            assertEquals("CODE02", code.getCode());
            assertEquals(1, code.getName().size());
            CodesAsserts.assertEqualsInternationalString(code.getName(), "es", "Nombre codelist-1-v2-code-2", null, null);
            CodesAsserts.assertEqualsInternationalString(code.getDescription(), null, null, null, null);
            CodesAsserts.assertEqualsInternationalString(((CodeMetamacResultExtensionPoint) code.getExtensionPoint()).getShortName(), "es", "nombre corto code2", "en", "short name code2");
            assertEquals(0, code.getAnnotations().size());
        }
        {
            // Code 02 01
            ItemResult code = getItemResult(codes, CODELIST_1_V2_CODE_2_1);
            assertEquals(CODELIST_1_V2_CODE_2_1, code.getUrn());
            assertEquals("CODE02", code.getParent().getCode());
            assertEquals(CODELIST_1_V2_CODE_2, code.getParent().getUrn());
            CodesAsserts.assertEqualsInternationalString(((CodeMetamacResultExtensionPoint) code.getExtensionPoint()).getShortName(), "es", "Nombre corto variableElement 2-1", "en",
                    "short name variableElement 2-1");
        }
        {
            // Code 02 01 01
            ItemResult code = getItemResult(codes, CODELIST_1_V2_CODE_2_1_1);
            assertEquals(CODELIST_1_V2_CODE_2_1_1, code.getUrn());
            assertEquals("CODE0201", code.getParent().getCode());
            CodesAsserts.assertEqualsInternationalString(((CodeMetamacResultExtensionPoint) code.getExtensionPoint()).getShortName(), "es", "nombre corto 2-1-1", null, null);
        }

        {
            // Code 02 02
            ItemResult code = getItemResult(codes, CODELIST_1_V2_CODE_2_2);
            assertEquals(CODELIST_1_V2_CODE_2_2, code.getUrn());
            CodesAsserts.assertEqualsInternationalString(((CodeMetamacResultExtensionPoint) code.getExtensionPoint()).getShortName(), null, null, null, null);
        }
        {
            // Code 03
            ItemResult code = getItemResult(codes, CODELIST_1_V2_CODE_3);
            assertEquals(CODELIST_1_V2_CODE_3, code.getUrn());
            CodesAsserts.assertEqualsInternationalString(((CodeMetamacResultExtensionPoint) code.getExtensionPoint()).getShortName(), "es", "Nombre corto variableElement 2-3", null, null);
        }
        {
            // Code 04
            ItemResult code = getItemResult(codes, CODELIST_1_V2_CODE_4);
            assertEquals(CODELIST_1_V2_CODE_4, code.getUrn());
            CodesAsserts.assertEqualsInternationalString(((CodeMetamacResultExtensionPoint) code.getExtensionPoint()).getShortName(), null, null, null, null);
        }
        {
            // Code 04 01
            ItemResult code = getItemResult(codes, CODELIST_1_V2_CODE_4_1);
            assertEquals(CODELIST_1_V2_CODE_4_1, code.getUrn());
            CodesAsserts.assertEqualsInternationalString(((CodeMetamacResultExtensionPoint) code.getExtensionPoint()).getShortName(), null, null, null, null);
        }
        {
            // Code 04 01 01
            ItemResult code = getItemResult(codes, CODELIST_1_V2_CODE_4_1_1);
            assertEquals(CODELIST_1_V2_CODE_4_1_1, code.getUrn());
            assertEquals("CODE0401", code.getParent().getCode());
            assertEquals(CODELIST_1_V2_CODE_4_1, code.getParent().getUrn());
            CodesAsserts.assertEqualsInternationalString(((CodeMetamacResultExtensionPoint) code.getExtensionPoint()).getShortName(), "es", "Nombre corto variableElement 2-1", "en",
                    "short name variableElement 2-1");
        }
    }

    @Override
    protected String getDataSetFile() {
        return "dbunit/SrmCodesTest.xml";
    }
}
