package org.siemac.metamac.srm.core.util;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.jpa.persistenceunit.MutablePersistenceUnitInfo;
import org.springframework.orm.jpa.persistenceunit.PersistenceUnitPostProcessor;

/**
 * This merges all JPA entities from multiple jars. To use it, all entities must
 * be listed in their respective persistence.xml files using the <class> tag.
 * 
 * @see http://forum.springsource.org/showthread.php?t=61763
 * @see http://forum.springsource.org/showthread.php?90245-Multiple-Persistence-Units-and-DefaultPersistenceUnitManager-not-displaying
 */
public class MergingPersistenceUnitPostProcessor implements PersistenceUnitPostProcessor {

    private final static Logger log               = LoggerFactory.getLogger(MergingPersistenceUnitPostProcessor.class);
    private Set<String>         managedClassNames = new HashSet<String>();

    @Override
    public void postProcessPersistenceUnitInfo(MutablePersistenceUnitInfo pui) {
        logDetails(pui);
        mergePuiManagedClassesWithCached(pui.getManagedClassNames());
        mergeCachedWithPuiManagedClasses(pui.getManagedClassNames());
    }

    private void mergeCachedWithPuiManagedClasses(List<String> puiClassNames) {
        managedClassNames.addAll(puiClassNames);
    }

    private void mergePuiManagedClassesWithCached(List<String> puiClassNames) {
        puiClassNames.addAll(managedClassNames);
    }

    private void logDetails(MutablePersistenceUnitInfo pui) {
        log.info("pui=" + pui.getPersistenceUnitName());
        for (String className : pui.getManagedClassNames()) {
            log.info("Persistent class=" + className);
        }
    }

}