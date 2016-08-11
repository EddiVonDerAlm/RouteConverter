//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.0.5-b02-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2008.10.12 at 02:39:09 PM CEST 
//


package slash.navigation.kml.binding22beta;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementRefs;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for NetworkLinkType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="NetworkLinkType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://earth.google.com/kml/2.2}AbstractFeatureType">
 *       &lt;sequence>
 *         &lt;element ref="{http://earth.google.com/kml/2.2}refreshVisibility" minOccurs="0"/>
 *         &lt;element ref="{http://earth.google.com/kml/2.2}flyToView" minOccurs="0"/>
 *         &lt;choice minOccurs="0">
 *           &lt;element ref="{http://earth.google.com/kml/2.2}Link" minOccurs="0"/>
 *           &lt;element ref="{http://earth.google.com/kml/2.2}Url" minOccurs="0"/>
 *         &lt;/choice>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "NetworkLinkType", propOrder = {
    "rest"
})
public class NetworkLinkType
    extends AbstractFeatureType
{

    @XmlElementRefs({
        @XmlElementRef(name = "flyToView", namespace = "http://earth.google.com/kml/2.2", type = JAXBElement.class),
        @XmlElementRef(name = "Link", namespace = "http://earth.google.com/kml/2.2", type = JAXBElement.class),
        @XmlElementRef(name = "Url", namespace = "http://earth.google.com/kml/2.2", type = JAXBElement.class),
        @XmlElementRef(name = "refreshVisibility", namespace = "http://earth.google.com/kml/2.2", type = JAXBElement.class)
    })
    protected List<JAXBElement<?>> rest;

    /**
     * Gets the rest of the content model. 
     * 
     * <p>
     * You are getting this "catch-all" property because of the following reason: 
     * The field name "Link" is used by two different parts of a schema. See: 
     * line 619 of file:/C:/p4/RouteConverter/navigation-formats/src/doc/kml/kml22beta.xsd
     * line 340 of file:/C:/p4/RouteConverter/navigation-formats/src/doc/kml/kml22beta.xsd
     * <p>
     * To get rid of this property, apply a property customization to one 
     * of both of the following declarations to change their names: 
     * Gets the value of the rest property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the rest property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRest().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     * {@link JAXBElement }{@code <}{@link LinkType }{@code >}
     * {@link JAXBElement }{@code <}{@link LinkType }{@code >}
     * {@link JAXBElement }{@code <}{@link Boolean }{@code >}
     * 
     * 
     */
    public List<JAXBElement<?>> getRest() {
        if (rest == null) {
            rest = new ArrayList<>();
        }
        return this.rest;
    }

}
