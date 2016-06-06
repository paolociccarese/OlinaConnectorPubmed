//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2015.02.16 at 10:49:30 PM EST 
//


package com.commonsemantics.olina.plugin.connector.pubmed.dtd150101.xml;

import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.ArrayList;
import java.util.List;


/**
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "lastNameOrForeNameOrInitialsOrSuffixOrCollectiveName",
    "identifier",
    "affiliationInfo"
})
@XmlRootElement(name = "Author")
public class Author {

    @XmlAttribute(name = "ValidYN")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String validYN;
    @XmlElements({
        @XmlElement(name = "LastName", required = true, type = LastName.class),
        @XmlElement(name = "ForeName", required = true, type = ForeName.class),
        @XmlElement(name = "Initials", required = true, type = Initials.class),
        @XmlElement(name = "Suffix", required = true, type = Suffix.class),
        @XmlElement(name = "CollectiveName", required = true, type = CollectiveName.class)
    })
    protected List<Object> lastNameOrForeNameOrInitialsOrSuffixOrCollectiveName;
    @XmlElement(name = "Identifier")
    protected List<Identifier> identifier;
    @XmlElement(name = "AffiliationInfo")
    protected List<AffiliationInfo> affiliationInfo;

    /**
     * Gets the value of the validYN property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getValidYN() {
        if (validYN == null) {
            return "Y";
        } else {
            return validYN;
        }
    }

    /**
     * Sets the value of the validYN property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setValidYN(String value) {
        this.validYN = value;
    }

    /**
     * Gets the value of the lastNameOrForeNameOrInitialsOrSuffixOrCollectiveName property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the lastNameOrForeNameOrInitialsOrSuffixOrCollectiveName property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLastNameOrForeNameOrInitialsOrSuffixOrCollectiveName().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link LastName }
     * {@link ForeName }
     * {@link Initials }
     * {@link Suffix }
     * {@link CollectiveName }
     * 
     * 
     */
    public List<Object> getLastNameOrForeNameOrInitialsOrSuffixOrCollectiveName() {
        if (lastNameOrForeNameOrInitialsOrSuffixOrCollectiveName == null) {
            lastNameOrForeNameOrInitialsOrSuffixOrCollectiveName = new ArrayList<Object>();
        }
        return this.lastNameOrForeNameOrInitialsOrSuffixOrCollectiveName;
    }

    /**
     * Gets the value of the identifier property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the identifier property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getIdentifier().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Identifier }
     * 
     * 
     */
    public List<Identifier> getIdentifier() {
        if (identifier == null) {
            identifier = new ArrayList<Identifier>();
        }
        return this.identifier;
    }

    /**
     * Gets the value of the affiliationInfo property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the affiliationInfo property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAffiliationInfo().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link AffiliationInfo }
     * 
     * 
     */
    public List<AffiliationInfo> getAffiliationInfo() {
        if (affiliationInfo == null) {
            affiliationInfo = new ArrayList<AffiliationInfo>();
        }
        return this.affiliationInfo;
    }

}
