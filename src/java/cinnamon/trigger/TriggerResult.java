package cinnamon.trigger;

import cinnamon.interfaces.Response;

/**
 * 
 */
public class TriggerResult {
    
    Response response = null;
    Boolean endProcessing = false;

    public TriggerResult() {
    }

    public TriggerResult(Response response, Boolean endProcessing) {
        this.response = response;
        this.endProcessing = endProcessing;
    }

    public Response getResponse() {
        return response;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    public Boolean getEndProcessing() {
        return endProcessing;
    }

    public void setEndProcessing(Boolean endProcessing) {
        this.endProcessing = endProcessing;
    }
}
