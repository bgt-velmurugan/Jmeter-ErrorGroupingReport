/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geterrorgroupings;

import java.io.File;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.apache.commons.lang.StringEscapeUtils;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author vpanneerselvam
 */
public class ParseXmlFile {

    Map<String, ErrorsAndWarnings> errorList;

    public ParseXmlFile() {
        errorList = new HashMap<String, ErrorsAndWarnings>();
    }

    public void XMLReaderFeedComparator(String FileName) {
        try {
            String xmlRootNode = "job";
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();

            DefaultHandler handler = new DefaultHandler() { //TODO: Redesign to follow iterator pattern
                //TODO: Validate the encoding support and make it configurable

                boolean StartElement = false;
                boolean EndElement = false;
                String XML = ""; //TODO: Make it as StringBuilder
                String ElementName;
                boolean print = false;

                public void startElement(String uri, String localName, String qName,
                        Attributes attributes)
                        throws SAXException {

                    if (qName.equalsIgnoreCase(xmlRootNode)) {//TODO: Make it configurable
                        XML = "";
                    }

                    XML += "<" + qName + ">";

                    int length = attributes.getLength();
                    for (int i = 0; i < length; i++) {
                        String name = attributes.getQName(i);
                        String value = StringEscapeUtils.escapeXml(attributes.getValue(i));
                        XML += " " + name + "=" + value;
                    }
                }

                public void endElement(String uri, String localName, String qName) {
                    try {
                        if (qName.equalsIgnoreCase(xmlRootNode)) {
                            XML += "</" + qName + ">";

                            //parseXmlData(XML);
                            //System.out.println(XML);
                            //FeedComparator.FeedComparator(csvfile,XML , CoutryCode, FeedType);
                            //logger.info(XML);
                            //Thread.sleep(1000);
                        } else {
                            XML += "</" + qName + ">";
                        }
                    } catch (Exception e) {

                    }

                }

                public void characters(char ch[], int start, int length) {
                    String value = new String(ch, start, length);
                    value = StringEscapeUtils.escapeXml(value);
                    XML += value;
                }

            };

            saxParser.parse(FileName, handler);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void XMLReaderFeedComparator_ReportSpecific(String FileName, String outPath) {
        try {
            String xmlRootNode = "httpSample";
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();

            DefaultHandler handler = new DefaultHandler() { //TODO: Redesign to follow iterator pattern
                //TODO: Validate the encoding support and make it configurable

                ErrorsAndWarnings errorItem = new ErrorsAndWarnings();
                boolean isReportWatchEnabled = false;
//                                boolean isFailureMessage = false;
                String key = "";
                String responseCode = "";
                String responseMessage = "";
                String failureMessage = "";
//                                List<String> failureMessage = new LinkedList<String>();
                String XML = ""; //TODO: Make it as StringBuilder
                String ElementName;
                boolean print = false;

                public void startElement(String uri, String localName, String qName,
                        Attributes attributes)
                        throws SAXException {

                    if (qName.equalsIgnoreCase(xmlRootNode)) {//TODO: Make it configurable
                        XML = "";
                        isReportWatchEnabled = false;
                        errorItem = new ErrorsAndWarnings();
                    }

                    XML += "<" + qName + ">";
                    failureMessage = "";

                    int length = attributes.getLength();
                    for (int i = 0; i < length; i++) {
                        String name = attributes.getQName(i);
                        String value = StringEscapeUtils.escapeXml(attributes.getValue(i));
                        XML += " " + name + "=" + value;

                        if (qName.equalsIgnoreCase("httpSample") && name.equalsIgnoreCase("s") && value.equalsIgnoreCase("false")) {
                            isReportWatchEnabled = true;
                        } else if (name.equalsIgnoreCase("rc")) {
                            responseCode = value;
                        } else if (name.equalsIgnoreCase("rm")) {
                            responseMessage = value;
                        }
                    }

                    if (isReportWatchEnabled) {
                        String input = responseCode + "-" + responseMessage;
                        key = getMd5(input);
                        errorItem.setErrorCode(responseCode);
                        errorItem.setErrorMessage(responseMessage);
                    }
                }

                public void endElement(String uri, String localName, String qName) {
                    try {
                        if (qName.equalsIgnoreCase(xmlRootNode)) {
                            XML += "</" + qName + ">";

//                                                        parseXmlData(XML);
//                                            if(!errorList.containsKey(key)){
//                                                errorList.put(key, errorItem);
//                                            }
                            if (isReportWatchEnabled) {
                                if (!errorList.containsKey(key)) {
                                    errorList.put(key, errorItem);
                                } else if (errorList.containsKey(key)) {
                                    ErrorsAndWarnings iter = (ErrorsAndWarnings) errorList.get(key);

                                    for (Iterator<String> itr1 = iter.failureMessage.iterator(); itr1.hasNext();) {
                                        String msg = itr1.next();
                                        iter.incrementCount();
                                        if (!iter.failureMessage.contains(msg)) {
                                            iter.failureMessage.add(itr1.next());
                                        }
                                    }

                                    errorList.put(key, iter);
                                }
                            }

                            //System.out.println(XML);
                            //FeedComparator.FeedComparator(csvfile,XML , CoutryCode, FeedType);
                            //logger.info(XML);
                            //Thread.sleep(1000);
                        } else {
                            XML += "</" + qName + ">";

                            if (qName.equalsIgnoreCase("failureMessage")) {
                                errorItem.incrementCount();
                                if (isReportWatchEnabled && failureMessage.length() > 0 && !errorItem.failureMessage.contains(failureMessage)) {
                                    errorItem.failureMessage.add(failureMessage);
                                }
                            }

                        }
                    } catch (Exception e) {

                    }

                }

                public void characters(char ch[], int start, int length) {
                    String value = new String(ch, start, length);
                    value = StringEscapeUtils.escapeXml(value);
                    XML += value;
                    failureMessage += value;
                }

            };

            saxParser.parse(FileName, handler);

        } catch (Exception e) {
            e.printStackTrace();
        }

        //System.out.println(errorList);
        WriteOutput(outPath);

    }

    public void WriteOutput(String outPath) {
        try {
            FileWriter fw = new FileWriter(outPath);
            fw.write("ErrorCode,ErrorMessage,FailureMessage,Count");
            fw.write("\r\n");

            for (Map.Entry<String, ErrorsAndWarnings> entry : errorList.entrySet()) {
                String message = "";
                message += entry.getValue().errorCode;
                message += "," + entry.getValue().errorMessage;
                message += "," + entry.getValue().getFailureMessage().replace("\r", " ").replace("\n", " ");
                message += "," + Integer.toString(entry.getValue().getCount());
                fw.write(message);
                fw.write("\r\n");
            }
            fw.close();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public String getMd5(String input) {
        try {

            // Static getInstance method is called with hashing MD5 
            MessageDigest md = MessageDigest.getInstance("MD5");

            // digest() method is called to calculate message digest 
            //  of an input digest() return array of byte 
            byte[] messageDigest = md.digest(input.getBytes());

            // Convert byte array into signum representation 
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value 
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        } // For specifying wrong message digest algorithms 
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

}
