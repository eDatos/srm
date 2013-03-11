package org.siemac.metamac.srm.core.base.mapper;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.siemac.metamac.core.common.ent.domain.InternationalString;
import org.siemac.metamac.core.common.ent.domain.LocalisedString;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import com.arte.statistic.sdmx.srm.core.base.domain.Annotation;
import com.arte.statistic.sdmx.srm.core.base.serviceimpl.utils.BaseJaxb2DoInheritUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/srm/applicationContext-test.xml"})
@TransactionConfiguration(transactionManager = "txManagerCore", defaultRollback = true)
@Transactional
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class BaseJaxb2DoInheritTest {

    @Test
    public void testInheritInternationString() {
        InternationalString internationalStringOld = new InternationalString();
        // Manual localised
        {
            LocalisedString localisedString = new LocalisedString("es", "text in 'es' old", false);
            internationalStringOld.addText(localisedString);
        }
        // Manual localised
        {
            LocalisedString localisedString = new LocalisedString("pt", "text in 'pt' old", false);
            internationalStringOld.addText(localisedString);
        }
        // Import previous localised
        {
            LocalisedString localisedString = new LocalisedString("en", "text in 'en' old", true);
            internationalStringOld.addText(localisedString);
        }

        InternationalString internationalStringNew = new InternationalString();
        // Import localised
        {
            LocalisedString localisedString = new LocalisedString("en", "text in 'en' new", true);
            internationalStringNew.addText(localisedString);
        }
        // Import localised
        {
            LocalisedString localisedString = new LocalisedString("ca", "text in 'ca' new", true);
            internationalStringNew.addText(localisedString);
        }
        // Import localised
        {
            LocalisedString localisedString = new LocalisedString("pt", "text in 'pt' new", true);
            internationalStringOld.addText(localisedString);
        }

        InternationalString internationalStringInherit = BaseJaxb2DoInheritUtils.inheritInternationString(internationalStringOld, internationalStringNew);

        assertEquals(4, internationalStringInherit.getTexts().size());
        assertEquals("text in 'pt' old", internationalStringInherit.getLocalisedLabel("pt"));
        assertEquals("text in 'es' old", internationalStringInherit.getLocalisedLabel("es"));
        assertEquals("text in 'en' new", internationalStringInherit.getLocalisedLabel("en"));
        assertEquals("text in 'ca' new", internationalStringInherit.getLocalisedLabel("ca"));
    }

    @Test
    public void inheritAnnotationsInternatialString() {
        Set<Annotation> annotationOlds = new HashSet<Annotation>();
        {
            Annotation annotation = new Annotation();
            annotation.setCode("code1");
            InternationalString internationalString = new InternationalString();
            // Manual localised
            {
                LocalisedString localisedString = new LocalisedString("es", "text in 'es' old", false);
                internationalString.addText(localisedString);
            }
            // Manual localised
            {
                LocalisedString localisedString = new LocalisedString("pt", "text in 'pt' old", false);
                internationalString.addText(localisedString);
            }
            // Import previous localised
            {
                LocalisedString localisedString = new LocalisedString("en", "text in 'en' old", true);
                internationalString.addText(localisedString);
            }
            annotation.setText(internationalString);
            annotationOlds.add(annotation);
        }
        {
            Annotation annotation = new Annotation();
            InternationalString internationalString = new InternationalString();
            // Manual localised
            {
                LocalisedString localisedString = new LocalisedString("es", "text in 'es' old", false);
                internationalString.addText(localisedString);
            }
            // Manual localised
            {
                LocalisedString localisedString = new LocalisedString("pt", "text in 'pt' old", false);
                internationalString.addText(localisedString);
            }
            // Import previous localised
            {
                LocalisedString localisedString = new LocalisedString("en", "text in 'en' old", true);
                internationalString.addText(localisedString);
            }
            annotation.setText(internationalString);
            annotationOlds.add(annotation);
        }

        Set<Annotation> annotationNews = new HashSet<Annotation>();
        {
            Annotation annotation = new Annotation();
            annotation.setCode("code1");
            InternationalString internationalString = new InternationalString();
            // Manual localised
            {
                LocalisedString localisedString = new LocalisedString("es", "text in 'es' new", true);
                internationalString.addText(localisedString);
            }
            // Manual localised
            {
                LocalisedString localisedString = new LocalisedString("pt", "text in 'pt' new", true);
                internationalString.addText(localisedString);
            }
            // Import previous localised
            {
                LocalisedString localisedString = new LocalisedString("en", "text in 'en' new", true);
                internationalString.addText(localisedString);
            }
            annotation.setText(internationalString);
            annotationNews.add(annotation);
        }

        BaseJaxb2DoInheritUtils.inheritAnnotations(annotationOlds, annotationNews);
        assertEquals(1, annotationNews.size());
        Annotation annotation = annotationNews.iterator().next();
        assertEquals(3, annotation.getText().getTexts().size());
        assertEquals("text in 'pt' old", annotation.getText().getLocalisedLabel("pt"));
        assertEquals("text in 'en' new", annotation.getText().getLocalisedLabel("en"));
        assertEquals("text in 'es' old", annotation.getText().getLocalisedLabel("es"));
    }

}
