//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vhudson-jaxb-ri-2.1-646 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.08.13 at 10:12:26 PM MESZ 
//


package slash.navigation.kml.binding22;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>Java class for AbstractViewType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AbstractViewType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://www.opengis.net/kml/2.2}AbstractObjectType">
 *       &lt;sequence>
 *         &lt;element ref="{http://www.opengis.net/kml/2.2}AbstractViewSimpleExtensionGroup" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element ref="{http://www.opengis.net/kml/2.2}AbstractViewObjectExtensionGroup" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AbstractViewType", propOrder = {
    "abstractViewSimpleExtensionGroup",
    "abstractViewObjectExtensionGroup"
})
@XmlSeeAlso({
    CameraType.class,
    LookAtType.class
})
public abstract class AbstractViewType
    extends AbstractObjectType
{

    @XmlElement(name = "AbstractViewSimpleExtensionGroup")
    @XmlSchemaType(name = "anySimpleType")
    protected List<Object> abstractViewSimpleExtensionGroup;
    @XmlElementRef(name = "AbstractViewObjectExtensionGroup", namespace = "http://www.opengis.net/kml/2.2", type = JAXBElement.class)
    protected List<JAXBElement<? extends AbstractObjectType>> abstractViewObjectExtensionGroup;

    /**
     * Gets the value of the abstractViewSimpleExtensionGroup property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the abstractViewSimpleExtensionGroup property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAbstractViewSimpleExtensionGroup().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Object }
     * 
     * 
     */
    public List<Object> getAbstractViewSimpleExtensionGroup() {
        if (abstractViewSimpleExtensionGroup == null) {
            abstractViewSimpleExtensionGroup = new ArrayList<>();
        }
        return this.abstractViewSimpleExtensionGroup;
    }

    /**
     * Gets the value of the abstractViewObjectExtensionGroup property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the abstractViewObjectExtensionGroup property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAbstractViewObjectExtensionGroup().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link JAXBElement }{@code <}{@link AbstractObjectType }{@code >}
     * {@link JAXBElement }{@code <}{@link TimeSpanType }{@code >}
     * {@link JAXBElement }{@code <}{@link TimeStampType }{@code >}
     * 
     * 
     */
    public List<JAXBElement<? extends AbstractObjectType>> getAbstractViewObjectExtensionGroup() {
        if (abstractViewObjectExtensionGroup == null) {
            abstractViewObjectExtensionGroup = new ArrayList<>();
        }
        return this.abstractViewObjectExtensionGroup;
    }

}
