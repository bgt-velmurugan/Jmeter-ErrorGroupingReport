/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package geterrorgroupings;

import java.util.LinkedList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;

/**
 *
 * @author vpanneerselvam
 */
public class ErrorsAndWarnings {

    int testType;
    String errorCode;
    String errorMessage;
    List<String> failureMessage;
    int count;

    public ErrorsAndWarnings() {
        testType = 0;
        errorCode = "";
        errorMessage = "";
        failureMessage = new LinkedList();
        count = 0;
    }

    public int getTestType() {
        return testType;
    }

    @XmlElement
    public void setTestType(int testType) {
        this.testType = testType;
    }

    public String getErrorCode() {
        return errorCode;
    }

    @XmlElement
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    @XmlElement
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getFailureMessage() {
        String topFailureMessage = (!failureMessage.isEmpty() ? failureMessage.get(0) : "");
        return topFailureMessage;
    }
    
    public List<String> getFailureMessageList() {
        return failureMessage;
    }

    @XmlElement
    public void setFailureMessage(String failureMessage) {
        if(!this.failureMessage.contains(failureMessage))
            this.failureMessage.add(failureMessage);
    }

    public int getCount() {
        return count;
    }

    @XmlElement
    public void setCount(int count) {
        this.count = count;
    }

    public void incrementCount(){
        this.count++;
    }
    
}
