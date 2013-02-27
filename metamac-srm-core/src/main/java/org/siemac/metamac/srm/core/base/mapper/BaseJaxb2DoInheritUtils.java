package org.siemac.metamac.srm.core.base.mapper;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.siemac.metamac.core.common.ent.domain.InternationalString;
import org.siemac.metamac.core.common.ent.domain.LocalisedString;
import org.siemac.metamac.core.common.util.shared.StringUtils;

import com.arte.statistic.sdmx.srm.core.base.domain.Annotation;
import com.arte.statistic.sdmx.srm.core.base.serviceimpl.utils.BaseVersioningCopyUtils;

public class BaseJaxb2DoInheritUtils {

    /**
     * Inherit localised text.
     * If the new version contains translations that have already been specified manually for the previous version, then manually specified will be holden, ie the new translations are omitted from the
     * new version.
     * 
     * @param iStrOld
     * @param iStrNew
     */
    public static void inheritInternationString(InternationalString iStrOld, InternationalString iStrNew) {
        if (iStrOld == null) {
            return;
        }

        // For all old locales
        Iterator<LocalisedString> iter = iStrOld.getTexts().iterator();
        while (iter.hasNext()) {
            LocalisedString localisedStr = iter.next();

            // If the old locale don't was created in the importation.
            if (!localisedStr.getIsUnmodifiable()) {
                // If the old locale exist in the new version
                LocalisedString discardLocalised = null;
                if ((discardLocalised = iStrNew.getLocalisedLabelEntity(localisedStr.getLocale())) != null) {
                    // Discard the new
                    iStrNew.removeText(discardLocalised);
                }
                // Inherit
                iStrNew.addText(BaseVersioningCopyUtils.copy(localisedStr));
            }
        }
    }

    /**
     * Inherits localised strings for annotations
     * 
     * @param annotationsOld
     * @param annotationsNew
     */
    public static void inheritAnnotationsInternatialString(Set<Annotation> annotationsOld, Set<Annotation> annotationsNew) {
        if (annotationsOld.isEmpty()) {
            return;
        }

        // Make map for annotationsNew with "code" key if exist.
        Map<String, Annotation> annotationMap = new HashMap<String, Annotation>();
        for (Annotation annotationNew : annotationsNew) {
            if (StringUtils.isNotEmpty(annotationNew.getCode())) {
                annotationMap.put(annotationNew.getCode(), annotationNew);
            }
        }

        // Inherits international string for the matched annotations
        for (Annotation annotationOld : annotationsOld) {
            if (StringUtils.isNotEmpty(annotationOld.getCode()) && annotationMap.containsKey(annotationOld.getCode())) {
                Annotation annotationNew = annotationMap.get(annotationOld.getCode());
                inheritInternationString(annotationOld.getText(), annotationNew.getText());
            }
        }
    }
}
