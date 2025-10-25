package com.runcible.abbot.web.model;

import java.util.ArrayList;
import java.util.List;

import org.springframework.validation.ObjectError;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class ValidationResponse
{
    public ValidationResponse()
    {
        this.status = "SUCCESS";
        this.errorMessageList = new ArrayList<ObjectError>();
        this.generalErrorText = "";
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public List<ObjectError> getErrorMessageList()
    {
        return this.errorMessageList;
    }

    public void setErrorMessageList(List<ObjectError> errorMessageList)
    {
        this.errorMessageList = errorMessageList;
    }
    
    /**
     * A general validation error not specific to a field
     * @return The error text
     */
    public String getGeneralErrorText()
    {
        return generalErrorText;
    }

    /**
     * A general validation error not specific to a field
     * @param generalErrorText The error text
     */
    public void setGeneralErrorText(String generalErrorText)
    {
        this.generalErrorText = generalErrorText;
    }

    private String status;
    private String generalErrorText;
    private List<ObjectError> errorMessageList;
}
