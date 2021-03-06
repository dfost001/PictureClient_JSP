//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.04.18 at 11:42:40 AM PDT 
//


package pictureClient;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;




/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the pictureClient package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _PicSample_QNAME = new QName("", "picSample");
    private final static QName _ClientResponse_QNAME = new QName("", "clientResponse");
    private final static QName _Customer_QNAME = new QName("", "customer");
    private final static QName _PicSampleDtUpdated_QNAME = new QName("", "dtUpdated");
    private final static QName _PicSamplePhoto_QNAME = new QName("", "photo");
    private final static QName _ClientResponsePictures_QNAME = new QName("", "pictures");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: pictureClient
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ClientResponse }
     * 
     */
    public ClientResponse createClientResponse() {
        return new ClientResponse();
    }

    /**
     * Create an instance of {@link PicSample }
     * 
     */
    public PicSample createPicSample() {
        return new PicSample();
    }

    /**
     * Create an instance of {@link Customer }
     * 
     */
    public Customer createCustomer() {
        return new Customer();
    }

    /**
     * Create an instance of {@link ClientResponse.Pictures }
     * 
     */
    public ClientResponse.Pictures createClientResponsePictures() {
        return new ClientResponse.Pictures();
    }   
   

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link PicSample }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "picSample")
    public JAXBElement<PicSample> createPicSample(PicSample value) {
        return new JAXBElement<PicSample>(_PicSample_QNAME, PicSample.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ClientResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "clientResponse")
    public JAXBElement<ClientResponse> createClientResponse(ClientResponse value) {
        return new JAXBElement<ClientResponse>(_ClientResponse_QNAME, ClientResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Customer }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "customer")
    public JAXBElement<Customer> createCustomer(Customer value) {
        return new JAXBElement<Customer>(_Customer_QNAME, Customer.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link XMLGregorianCalendar }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "dtUpdated", scope = PicSample.class)
    public JAXBElement<XMLGregorianCalendar> createPicSampleDtUpdated(XMLGregorianCalendar value) {
        return new JAXBElement<XMLGregorianCalendar>(_PicSampleDtUpdated_QNAME, XMLGregorianCalendar.class, PicSample.class, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link byte[]}{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "photo", scope = PicSample.class)
    public JAXBElement<byte[]> createPicSamplePhoto(byte[] value) {
        return new JAXBElement<byte[]>(_PicSamplePhoto_QNAME, byte[].class, PicSample.class, ((byte[]) value));
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ClientResponse.Pictures }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "pictures", scope = ClientResponse.class)
    public JAXBElement<ClientResponse.Pictures> createClientResponsePictures(ClientResponse.Pictures value) {
        return new JAXBElement<ClientResponse.Pictures>(_ClientResponsePictures_QNAME, ClientResponse.Pictures.class, ClientResponse.class, value);
    }

}
