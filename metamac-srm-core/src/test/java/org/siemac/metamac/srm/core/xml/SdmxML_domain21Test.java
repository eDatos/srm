package org.siemac.metamac.srm.core.xml;

import org.custommonkey.xmlunit.XMLTestCase;
import org.siemac.metamac.srm.core.structure.domain.DataStructureDefinitionRepository;

/**
 * Spring based transactional test with XMLUnit support.
 */
public class SdmxML_domain21Test extends XMLTestCase {

    private DataStructureDefinitionRepository dataStructureDefinitionRepository;

    /**************************************************************************
     * TEST
     * 
     * @throws JAXBException
     **************************************************************************/

    /*
     * public void testUnmarshallDsd() throws Exception {
     * File f = new File("src/test/resources/sdmx/2_1/dsd/ecb_exr_ng_full.xml");
     * // InputStream is = Class.class.getResourceAsStream("/sdmx/2_1/dsd/ecb_exr_ng_full.xml");
     * JAXBContext jc = JAXBContext.newInstance(org.opensdmx.domain.v2_1.common.ObjectFactory.class, org.opensdmx.domain.v2_1.structure.ObjectFactory.class,
     * org.opensdmx.domain.v2_1.message.ObjectFactory.class);
     * SchemaFactory schemaFactory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema");
     * Schema schema = schemaFactory.newSchema(new File("../opensdmx-domain-21/sdmx21xsd/SDMXMessage.xsd"));
     * Unmarshaller u = jc.createUnmarshaller();
     * u.setSchema(schema);
     * Object doo = u.unmarshal(f);
     * Marshaller marshaller = jc.createMarshaller();
     * marshaller.marshal(doo, System.out);
     * // Structure doo = (Structure) u.unmarshal(f);
     * // List<Structure> yax = doo.getPersona();
     * // for (Structure foo : yax) {
     * // System.out.println(foo.getNombre());
     * // System.out.println(foo.getApellidos());
     * // System.out.println(foo.getDocumento());
     * // System.out.println("------------------");
     * // }
     * int a = 23;
     * }
     * // xmlToObject() {
     * // Unmarshaller u = null;
     * // try {
     * // Unmarshaller u = pool.take();
     * // ..
     * // ..
     * // } finally {
     * // if (u!= null) {
     * // pool.recicle(u);
     * // }
     * //
     * // }
     * // }
     */

//    public void testMessageFormat() {
//
//        Object[] param = new Object[2];
//        param[0] = "PAM-0";
//        param[1] = "PAM-1";
//        System.out.println("TEST-1 --------------------------");
//        System.out.println(MessageFormat.format("Parameter {0} was not expected in the type {1}.", param));
//
//        ApplicationException exp = new ApplicationException(MtmCoreExceptionType.MTM_CORE_E_PARAMETER_UNEXPECTED.getErrorCode(),
//                MtmCoreExceptionType.MTM_CORE_E_PARAMETER_UNEXPECTED.getMessageForReasonType(), "PAM-0", "PAM-1");
//
//        System.out.println("TEST-2 --------------------------");
//        System.out.println(exp.getMessage());
//
//        System.out.println("TEST-3 --------------------------");
//        System.out.println(exp.getLocalizedMessage());
//    }

}
