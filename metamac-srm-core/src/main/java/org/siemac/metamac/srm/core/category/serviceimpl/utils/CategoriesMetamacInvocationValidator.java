package org.siemac.metamac.srm.core.category.serviceimpl.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.BooleanUtils;
import org.siemac.metamac.core.common.exception.MetamacException;
import org.siemac.metamac.core.common.exception.MetamacExceptionItem;
import org.siemac.metamac.core.common.exception.utils.ExceptionUtils;
import org.siemac.metamac.srm.core.category.domain.CategoryMetamac;
import org.siemac.metamac.srm.core.category.domain.CategorySchemeVersionMetamac;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionParameters;
import org.siemac.metamac.srm.core.common.error.ServiceExceptionType;

import com.arte.statistic.sdmx.srm.core.base.domain.ItemSchemeVersion;
import com.arte.statistic.sdmx.srm.core.base.serviceimpl.utils.ValidationUtils;
import com.arte.statistic.sdmx.srm.core.category.serviceimpl.utils.CategoriesInvocationValidator;
import com.arte.statistic.sdmx.srm.core.common.domain.ItemResultSelection;

public class CategoriesMetamacInvocationValidator extends CategoriesInvocationValidator {

    public static void checkCreateCategoryScheme(CategorySchemeVersionMetamac categorySchemeVersion, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }
        ValidationUtils.checkParameterRequired(categorySchemeVersion, ServiceExceptionParameters.CATEGORY_SCHEME, exceptions);
        if (categorySchemeVersion != null) {
            checkCategoryScheme(categorySchemeVersion, exceptions);
        }

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkUpdateCategoryScheme(CategorySchemeVersionMetamac categorySchemeVersion, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }
        ValidationUtils.checkParameterRequired(categorySchemeVersion, ServiceExceptionParameters.CATEGORY_SCHEME, exceptions);
        if (categorySchemeVersion != null) {
            checkCategoryScheme(categorySchemeVersion, exceptions);
        }

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkCreateCategory(CategorySchemeVersionMetamac categorySchemeVersion, CategoryMetamac category, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(categorySchemeVersion, ServiceExceptionParameters.CATEGORY_SCHEME, exceptions);
        ValidationUtils.checkParameterRequired(category, ServiceExceptionParameters.CATEGORY, exceptions);
        if (category != null && categorySchemeVersion != null) {
            checkCategory(categorySchemeVersion, category, exceptions);
        }

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkUpdateCategory(CategoryMetamac category, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(category, ServiceExceptionParameters.CATEGORY, exceptions);
        if (category != null) {
            checkCategory(category.getItemSchemeVersion(), category, exceptions);
        }

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveCategoriesByCategorySchemeUrn(String categorySchemeUrn, String locale, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(categorySchemeUrn, ServiceExceptionParameters.URN, exceptions);
        ValidationUtils.checkParameterRequired(locale, ServiceExceptionParameters.LOCALE, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveCategoriesByCategorySchemeUrnUnordered(String categorySchemeUrn, ItemResultSelection itemResultSelection, List<MetamacExceptionItem> exceptions)
            throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(categorySchemeUrn, ServiceExceptionParameters.URN, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkImportCategoriesTsv(String categorySchemeUrn, File file, String fileName, boolean updateAlreadyExisting, Boolean canBeBackground, List<MetamacExceptionItem> exceptions)
            throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(categorySchemeUrn, ServiceExceptionParameters.URN, exceptions);
        ValidationUtils.checkParameterRequired(file, ServiceExceptionParameters.STREAM, exceptions);
        ValidationUtils.checkParameterRequired(fileName, ServiceExceptionParameters.FILE_NAME, exceptions);
        ValidationUtils.checkParameterRequired(canBeBackground, ServiceExceptionParameters.CAN_BE_BACKGROUND, exceptions);
        ValidationUtils.checkParameterRequired(updateAlreadyExisting, ServiceExceptionParameters.IMPORTATION_TSV_UPDATE_ALREADY_EXISTING, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkExportCategoriesTsv(String categorySchemeUrn, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(categorySchemeUrn, ServiceExceptionParameters.URN, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    public static void checkRetrieveCategorySchemeByCategoryUrn(String categoryUrn, List<MetamacExceptionItem> exceptions) throws MetamacException {
        if (exceptions == null) {
            exceptions = new ArrayList<MetamacExceptionItem>();
        }

        ValidationUtils.checkParameterRequired(categoryUrn, ServiceExceptionParameters.URN, exceptions);

        ExceptionUtils.throwIfException(exceptions);
    }

    private static void checkCategoryScheme(CategorySchemeVersionMetamac categorySchemeVersion, List<MetamacExceptionItem> exceptions) {
        if (categorySchemeVersion.getMaintainableArtefact() != null && BooleanUtils.isTrue(categorySchemeVersion.getMaintainableArtefact().getIsExternalReference())) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, ServiceExceptionParameters.MAINTAINABLE_ARTEFACT_IS_EXTERNAL_REFERENCE));
        }
        if (BooleanUtils.isTrue(categorySchemeVersion.getIsPartial())) {
            exceptions.add(new MetamacExceptionItem(ServiceExceptionType.METADATA_INCORRECT, ServiceExceptionParameters.ITEM_SCHEME_IS_PARTIAL));
        }
    }

    private static void checkCategory(ItemSchemeVersion categorySchemeVersion, CategoryMetamac category, List<MetamacExceptionItem> exceptions) {

        // common metadata in sdmx are checked in Sdmx module
    }
}
