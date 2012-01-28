package com.flickr4java.flickr;

import com.flickr4java.flickr.util.XMLUtilities;

import org.apache.axis.message.SOAPBody;
import org.apache.axis.message.SOAPEnvelope;
import org.apache.axis.message.SOAPFault;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.util.Collection;
import java.util.Iterator;

/**
 * Flickr SOAP Response object.
 *
 * @author Matt Ray
 */
public class SOAPResponse implements Response {

    private String stat;
    private Collection payload;

    private String errorCode;
    private String errorMessage;
    
    private SOAPEnvelope envelope;

    public SOAPResponse(SOAPEnvelope envelope){
        this.envelope = envelope;
    }
    
    public void parse(Document document) {
        try {
            SOAPBody body = (SOAPBody)envelope.getBody();

            if (Flickr.debugStream) {
                System.out.println("SOAP RESPONSE.parse");
                System.out.println(body.getAsString());
            }
            
            SOAPFault fault = (SOAPFault)body.getFault();
            if (fault != null) {
                System.err.println("FAULT: "+fault.getAsString());
                errorCode = fault.getFaultCode();
                errorMessage = fault.getFaultString();
            } else {
                for (Iterator i = body.getChildElements(); i.hasNext(); ) {
                    Element bodyelement = (Element)i.next();
                    bodyelement.normalize();
                    // TODO: Verify that the payload is always a single XML node
                    payload = XMLUtilities.getChildElements(bodyelement);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getStat() {
        return null;
    }
    
    public Element getPayload() {
        Iterator iter = payload.iterator();
        if (iter.hasNext()) {
            return (Element) iter.next();
        }
        throw new RuntimeException("SOAP response payload has no elements");
    }

    public Collection getPayloadCollection() {
        return payload;
    }

    public boolean isError() {
        return errorCode != null;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }



}
