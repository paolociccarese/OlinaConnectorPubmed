//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.02.16 at 10:49:30 PM EST 
//


package com.commonsemantics.olina.plugin.connector.pubmed.dtd150101.xml;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.NormalizedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "value"
})
@XmlRootElement(name = "QualifierName")
public class QualifierName {

    @XmlAttribute(name = "MajorTopicYN")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String majorTopicYN;
    @XmlAttribute(name = "UI", required = true)
    @XmlJavaTypeAdapter(NormalizedStringAdapter.class)
    protected String ui;
    @XmlValue
    protected String value;

    /**
     * Gets the value of the majorTopicYN property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMajorTopicYN() {
        if (majorTopicYN == null) {
            return "N";
        } else {
            return majorTopicYN;
        }
    }

    /**
     * Sets the value of the majorTopicYN property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMajorTopicYN(String value) {
        this.majorTopicYN = value;
    }

    /**
     * Gets the value of the ui property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUI() {
        return ui;
    }

    /**
     * Sets the value of the ui property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUI(String value) {
        this.ui = value;
    }

    /**
     * Gets the value of the value property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getvalue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setvalue(String value) {
        this.value = value;
    }

}
