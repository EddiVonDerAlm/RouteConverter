//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-646 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.08.13 at 10:12:26 PM MESZ 
//


package slash.navigation.kml.binding22;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import slash.navigation.kml.binding22gx.LatLonQuadType;


/**
 * <p>Java class for GroundOverlayType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GroundOverlayType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.opengis.net/kml/2.2}AbstractOverlayType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.opengis.net/kml/2.2}altitude" minOccurs="0"/>
 *         &lt;element ref="{http://www.opengis.net/kml/2.2}altitudeModeGroup" minOccurs="0"/>
 *         &lt;element ref="{http://www.opengis.net/kml/2.2}LatLonBox" minOccurs="0"/>
 *         &lt;element ref="{http://www.opengis.net/kml/2.2}GroundOverlaySimpleExtensionGroup" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.opengis.net/kml/2.2}GroundOverlayObjectExtensionGroup" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GroundOverlayType", propOrder = {
    "altitude",
    "altitudeModeGroup",
    "latLonBox",
    "groundOverlaySimpleExtensionGroup",
    "groundOverlayObjectExtensionGroup"
})
public class GroundOverlayType
    extends AbstractOverlayType
{

    @XmlElement(defaultValue = "0.0")
    protected Double altitude;
    @XmlElementRef(name = "altitudeModeGroup", namespace = "http://www.opengis.net/kml/2.2", type = JAXBElement.class)
    protected JAXBElement<?> altitudeModeGroup;
    @XmlElement(name = "LatLonBox")
    protected LatLonBoxType latLonBox;
    @XmlElement(name = "GroundOverlaySimpleExtensionGroup")
    @XmlSchemaType(name = "anySimpleType")
    protected List<Object> groundOverlaySimpleExtensionGroup;
    @XmlElementRef(name = "GroundOverlayObjectExtensionGroup", namespace = "http://www.opengis.net/kml/2.2", type = JAXBElement.class)
    protected List<JAXBElement<? extends AbstractObjectType>> groundOverlayObjectExtensionGroup;

    /**
     * Gets the value of the altitude property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getAltitude() {
        return altitude;
    }

    /**
     * Sets the value of the altitude property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setAltitude(Double value) {
        this.altitude = value;
    }

    /**
     * Gets the value of the altitudeModeGroup property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link slash.navigation.kml.binding22.AltitudeModeEnumType }{@code >}
     *     {@link JAXBElement }{@code <}{@link slash.navigation.kml.binding22gx.AltitudeModeEnumType }{@code >}
     *     {@link JAXBElement }{@code <}{@link Object }{@code >}
     *     
     */
    public JAXBElement<?> getAltitudeModeGroup() {
        return altitudeModeGroup;
    }

    /**
     * Sets the value of the altitudeModeGroup property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link slash.navigation.kml.binding22.AltitudeModeEnumType }{@code >}
     *     {@link JAXBElement }{@code <}{@link slash.navigation.kml.binding22gx.AltitudeModeEnumType }{@code >}
     *     {@link JAXBElement }{@code <}{@link Object }{@code >}
     *     
     */
    public void setAltitudeModeGroup(JAXBElement<?> value) {
        this.altitudeModeGroup = ((JAXBElement<?> ) value);
    }

    /**
     * Gets the value of the latLonBox property.
     * 
     * @return
     *     possible object is
     *     {@link LatLonBoxType }
     *     
     */
    public LatLonBoxType getLatLonBox() {
        return latLonBox;
    }

    /**
     * Sets the value of the latLonBox property.
     * 
     * @param value
     *     allowed object is
     *     {@link LatLonBoxType }
     *     
     */
    public void setLatLonBox(LatLonBoxType value) {
        this.latLonBox = value;
    }

    /**
     * Gets the value of the groundOverlaySimpleExtensionGroup property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the groundOverlaySimpleExtensionGroup property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getGroundOverlaySimpleExtensionGroup().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Object }
     * 
     * 
     */
    public List<Object> getGroundOverlaySimpleExtensionGroup() {
        if (groundOverlaySimpleExtensionGroup == null) {
            groundOverlaySimpleExtensionGroup = new ArrayList<Object>();
        }
        return this.groundOverlaySimpleExtensionGroup;
    }

    /**
     * Gets the value of the groundOverlayObjectExtensionGroup property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the groundOverlayObjectExtensionGroup property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getGroundOverlayObjectExtensionGroup().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link AbstractObjectType }{@code >}
     * {@link JAXBElement }{@code <}{@link LatLonQuadType }{@code >}
     * 
     * 
     */
    public List<JAXBElement<? extends AbstractObjectType>> getGroundOverlayObjectExtensionGroup() {
        if (groundOverlayObjectExtensionGroup == null) {
            groundOverlayObjectExtensionGroup = new ArrayList<JAXBElement<? extends AbstractObjectType>>();
        }
        return this.groundOverlayObjectExtensionGroup;
    }

}
