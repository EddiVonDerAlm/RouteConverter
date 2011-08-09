//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.0.5-b02-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2008.10.12 at 02:39:09 PM CEST 
//


package slash.navigation.kml.binding22beta;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for AbstractOverlayType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="AbstractOverlayType">
 *   &lt;complexContent>
 *     &lt;extension base="{http://earth.google.com/kml/2.2}AbstractFeatureType">
 *       &lt;sequence>
 *         &lt;element ref="{http://earth.google.com/kml/2.2}color" minOccurs="0"/>
 *         &lt;element ref="{http://earth.google.com/kml/2.2}drawOrder" minOccurs="0"/>
 *         &lt;element ref="{http://earth.google.com/kml/2.2}Icon" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AbstractOverlayType", propOrder = {
    "color",
    "drawOrder",
    "icon"
})
public abstract class AbstractOverlayType
    extends AbstractFeatureType
{

    @XmlElement(type = String.class, defaultValue = "ffffffff")
    @XmlJavaTypeAdapter(HexBinaryAdapter.class)
    protected byte[] color;
    @XmlElement(defaultValue = "0")
    protected Integer drawOrder;
    @XmlElement(name = "Icon")
    protected LinkType icon;

    /**
     * Gets the value of the color property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public byte[] getColor() {
        return color;
    }

    /**
     * Sets the value of the color property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setColor(byte[] value) {
        this.color = ((byte[]) value);
    }

    /**
     * Gets the value of the drawOrder property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getDrawOrder() {
        return drawOrder;
    }

    /**
     * Sets the value of the drawOrder property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setDrawOrder(Integer value) {
        this.drawOrder = value;
    }

    /**
     * Gets the value of the icon property.
     * 
     * @return
     *     possible object is
     *     {@link LinkType }
     *     
     */
    public LinkType getIcon() {
        return icon;
    }

    /**
     * Sets the value of the icon property.
     * 
     * @param value
     *     allowed object is
     *     {@link LinkType }
     *     
     */
    public void setIcon(LinkType value) {
        this.icon = value;
    }

}
