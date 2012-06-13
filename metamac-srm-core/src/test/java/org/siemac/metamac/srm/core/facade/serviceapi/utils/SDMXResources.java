package org.siemac.metamac.srm.core.facade.serviceapi.utils;

public class SDMXResources {

    // DSD ********************************************************************
    
    // Custom
    public static final String DSD_CUSTOM       = "src/test/resources/sdmx/2_1/dsd/custom.xml";
    
    // Contains the structural metadata that is used across the various data structures. Each of the subsequent structure message
    // provide external references to this file
    // public static final String DSD_ECB_EXR_COMMON = "src/test/resources/sdmx/2_1/dsd/exr_common.xml";

    // Contains an exchange rate data structure with no groups defined. All attribute relationships are to specific dimensions.
    // The file ecb_exr_ng_full.xml represents the same structure, but does not use external referencing to make the file easier
    // to load into tools which cannot resolve local file references
    public static final String DSD_ECB_EXR_NG_FULL       = "src/test/resources/sdmx/2_1/dsd/ecb_exr_ng_full.xml";

    // Contains an exchange rate data structure with a sibling group defined. In this sample, only on attribute references the
    // sibling group, and the rest maintain the relationships defined in the data structure without groups.
    // The file ecb_exr_sg_full.xml represents the same structure, but does not use external referencing to make the file easier
    // to load into tools which cannot resolve local file references
    public static final String DSD_ECB_EXR_SG_FULL       = "src/test/resources/sdmx/2_1/dsd/ecb_exr_sg_full.xml";

    // Contains an exchange rate data structure with an addition group defined. In this sample only one attribute specifies a
    // relationship with the new group, but the dimensions from the previous sample now declare the sibling group as an attachment
    // group. The file ecb_exr_rg_full.xml represents the same structure, but does not use external referencing to make the file
    // easier to load into tools which cannot resolve local file references
    public static final String DSD_ECB_EXR_RG_FULL       = "src/test/resources/sdmx/2_1/dsd/ecb_exr_rg_full.xml";

    // INE
    public static final String DSD_INE_DPOP              = "src/test/resources/sdmx/2_1/dsd/ine_dsd_dpop.xml";
    public static final String DSD_INE_EPOP              = "src/test/resources/sdmx/2_1/dsd/ine_dsd_epop.xml";
    public static final String DSD_INE_IDB               = "src/test/resources/sdmx/2_1/dsd/ine_dsd_idb.xml";
    public static final String DSD_INE_IPC               = "src/test/resources/sdmx/2_1/dsd/ine_dsd_ipc.xml";
    public static final String DSD_INE_IPCA              = "src/test/resources/sdmx/2_1/dsd/ine_dsd_ipca.xml";
    public static final String DSD_INE_MNP               = "src/test/resources/sdmx/2_1/dsd/ine_dsd_mnp.xml";
    
    // *************************************************************************

}
