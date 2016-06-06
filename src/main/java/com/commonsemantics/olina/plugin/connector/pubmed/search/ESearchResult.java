//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.1.3-b01-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2009.06.03 at 01:03:34 AM EDT 
//


package com.commonsemantics.olina.plugin.connector.pubmed.search;

import javax.xml.bind.annotation.*;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;choice&gt;
 *           &lt;sequence&gt;
 *             &lt;element ref="{}Count"/&gt;
 *             &lt;sequence minOccurs="0"&gt;
 *               &lt;element ref="{}RetMax"/&gt;
 *               &lt;element ref="{}RetStart"/&gt;
 *               &lt;element ref="{}QueryKey" minOccurs="0"/&gt;
 *               &lt;element ref="{}WebEnv" minOccurs="0"/&gt;
 *               &lt;element ref="{}IdList"/&gt;
 *               &lt;element ref="{}TranslationSet"/&gt;
 *               &lt;element ref="{}TranslationStack" minOccurs="0"/&gt;
 *               &lt;element ref="{}QueryTranslation"/&gt;
 *             &lt;/sequence&gt;
 *           &lt;/sequence&gt;
 *           &lt;element ref="{}ERROR"/&gt;
 *         &lt;/choice&gt;
 *         &lt;element ref="{}ErrorList" minOccurs="0"/&gt;
 *         &lt;element ref="{}WarningList" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "count",
    "retMax",
    "retStart",
    "queryKey",
    "webEnv",
    "idList",
    "translationSet",
    "translationStack",
    "queryTranslation",
    "error",
    "errorList",
    "warningList"
})
@XmlRootElement(name = "eSearchResult")
public class ESearchResult {

    @XmlElement(name = "Count")
    protected Count count;
    @XmlElement(name = "RetMax")
    protected RetMax retMax;
    @XmlElement(name = "RetStart")
    protected RetStart retStart;
    @XmlElement(name = "QueryKey")
    protected QueryKey queryKey;
    @XmlElement(name = "WebEnv")
    protected WebEnv webEnv;
    @XmlElement(name = "IdList")
    protected IdList idList;
    @XmlElement(name = "TranslationSet")
    protected TranslationSet translationSet;
    @XmlElement(name = "TranslationStack")
    protected TranslationStack translationStack;
    @XmlElement(name = "QueryTranslation")
    protected QueryTranslation queryTranslation;
    @XmlElement(name = "ERROR")
    protected ERROR error;
    @XmlElement(name = "ErrorList")
    protected ErrorList errorList;
    @XmlElement(name = "WarningList")
    protected WarningList warningList;

    /**
     * Gets the value of the count property.
     * 
     * @return
     *     possible object is
     *     {@link Count }
     *     
     */
    public Count getCount() {
        return count;
    }

    /**
     * Sets the value of the count property.
     * 
     * @param value
     *     allowed object is
     *     {@link Count }
     *     
     */
    public void setCount(Count value) {
        this.count = value;
    }

    /**
     * Gets the value of the retMax property.
     * 
     * @return
     *     possible object is
     *     {@link RetMax }
     *     
     */
    public RetMax getRetMax() {
        return retMax;
    }

    /**
     * Sets the value of the retMax property.
     * 
     * @param value
     *     allowed object is
     *     {@link RetMax }
     *     
     */
    public void setRetMax(RetMax value) {
        this.retMax = value;
    }

    /**
     * Gets the value of the retStart property.
     * 
     * @return
     *     possible object is
     *     {@link RetStart }
     *     
     */
    public RetStart getRetStart() {
        return retStart;
    }

    /**
     * Sets the value of the retStart property.
     * 
     * @param value
     *     allowed object is
     *     {@link RetStart }
     *     
     */
    public void setRetStart(RetStart value) {
        this.retStart = value;
    }

    /**
     * Gets the value of the queryKey property.
     * 
     * @return
     *     possible object is
     *     {@link QueryKey }
     *     
     */
    public QueryKey getQueryKey() {
        return queryKey;
    }

    /**
     * Sets the value of the queryKey property.
     * 
     * @param value
     *     allowed object is
     *     {@link QueryKey }
     *     
     */
    public void setQueryKey(QueryKey value) {
        this.queryKey = value;
    }

    /**
     * Gets the value of the webEnv property.
     * 
     * @return
     *     possible object is
     *     {@link WebEnv }
     *     
     */
    public WebEnv getWebEnv() {
        return webEnv;
    }

    /**
     * Sets the value of the webEnv property.
     * 
     * @param value
     *     allowed object is
     *     {@link WebEnv }
     *     
     */
    public void setWebEnv(WebEnv value) {
        this.webEnv = value;
    }

    /**
     * Gets the value of the idList property.
     * 
     * @return
     *     possible object is
     *     {@link IdList }
     *     
     */
    public IdList getIdList() {
        return idList;
    }

    /**
     * Sets the value of the idList property.
     * 
     * @param value
     *     allowed object is
     *     {@link IdList }
     *     
     */
    public void setIdList(IdList value) {
        this.idList = value;
    }

    /**
     * Gets the value of the translationSet property.
     * 
     * @return
     *     possible object is
     *     {@link TranslationSet }
     *     
     */
    public TranslationSet getTranslationSet() {
        return translationSet;
    }

    /**
     * Sets the value of the translationSet property.
     * 
     * @param value
     *     allowed object is
     *     {@link TranslationSet }
     *     
     */
    public void setTranslationSet(TranslationSet value) {
        this.translationSet = value;
    }

    /**
     * Gets the value of the translationStack property.
     * 
     * @return
     *     possible object is
     *     {@link TranslationStack }
     *     
     */
    public TranslationStack getTranslationStack() {
        return translationStack;
    }

    /**
     * Sets the value of the translationStack property.
     * 
     * @param value
     *     allowed object is
     *     {@link TranslationStack }
     *     
     */
    public void setTranslationStack(TranslationStack value) {
        this.translationStack = value;
    }

    /**
     * Gets the value of the queryTranslation property.
     * 
     * @return
     *     possible object is
     *     {@link QueryTranslation }
     *     
     */
    public QueryTranslation getQueryTranslation() {
        return queryTranslation;
    }

    /**
     * Sets the value of the queryTranslation property.
     * 
     * @param value
     *     allowed object is
     *     {@link QueryTranslation }
     *     
     */
    public void setQueryTranslation(QueryTranslation value) {
        this.queryTranslation = value;
    }

    /**
     * Gets the value of the error property.
     * 
     * @return
     *     possible object is
     *     {@link ERROR }
     *     
     */
    public ERROR getERROR() {
        return error;
    }

    /**
     * Sets the value of the error property.
     * 
     * @param value
     *     allowed object is
     *     {@link ERROR }
     *     
     */
    public void setERROR(ERROR value) {
        this.error = value;
    }

    /**
     * Gets the value of the errorList property.
     * 
     * @return
     *     possible object is
     *     {@link ErrorList }
     *     
     */
    public ErrorList getErrorList() {
        return errorList;
    }

    /**
     * Sets the value of the errorList property.
     * 
     * @param value
     *     allowed object is
     *     {@link ErrorList }
     *     
     */
    public void setErrorList(ErrorList value) {
        this.errorList = value;
    }

    /**
     * Gets the value of the warningList property.
     * 
     * @return
     *     possible object is
     *     {@link WarningList }
     *     
     */
    public WarningList getWarningList() {
        return warningList;
    }

    /**
     * Sets the value of the warningList property.
     * 
     * @param value
     *     allowed object is
     *     {@link WarningList }
     *     
     */
    public void setWarningList(WarningList value) {
        this.warningList = value;
    }

}
