package cinnamon.trigger.impl

import cinnamon.PoBox
import cinnamon.trigger.ChangeTrigger
import cinnamon.trigger.ITrigger
import cinnamon.utils.ParamParser
import org.apache.http.Header
import org.apache.http.HttpResponse
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpPost
import org.apache.http.client.methods.RequestBuilder
import org.apache.http.impl.client.DefaultHttpClient
import org.apache.http.impl.client.HttpClientBuilder
import org.apache.http.params.BasicHttpParams
import org.slf4j.Logger
import org.slf4j.LoggerFactory


/**
 *
 */
public class MicroserviceChangeTrigger implements ITrigger {

    Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public PoBox executePreCommand(PoBox poBox, ChangeTrigger changeTrigger) {
        log.debug("preCommand of MicroserviceChangeTrigger");

        try {
            def url = findRemoteUrl(changeTrigger.config)
            if (!url) {
                log.warn("Found microserviceChangeTrigger without valid remoteServer url. Config is: " + 
                        changeTrigger.config)
                return poBox;
            }
            
            def request = poBox.request
            HttpClient httpClient = HttpClientBuilder.create().build()
            def requestCopy = RequestBuilder.create("POST")
            requestCopy.setUri(url)
            def headerNames = request.headerNames
            while(headerNames.hasMoreElements()){
                def headerName = headerNames.nextElement()
                requestCopy.setHeader(headerName, request.getHeader(headerName))   
            }
            for(Map.Entry entry : poBox.params){
                if(entry.key instanceof String && entry.value instanceof String) {
                    requestCopy.addParameter(entry.key, entry.value)
                }
                else{
                    log.debug("Entry "+entry+" is not <String,String>")
                }
            }
            HttpResponse httpResponse = httpClient.execute(requestCopy.build())
//            if(httpResponse.statusCode == HttpStatus.SC_OK){
                log.debug("Microservice post returned :"+ httpResponse.statusLine)
//            }
        }
        catch (Exception e) {
            log.debug("Failed to execute microserviceChangeTrigger.", e);
        }
        return poBox;
    }

    @Override
    public PoBox executePostCommand(PoBox poBox, ChangeTrigger changeTrigger) {
        log.debug("postCommand MicroserviceChangeTrigger");
        
        try {
            def url = findRemoteUrl(changeTrigger.config)
            if (!url) {
                log.warn("Found microserviceChangeTrigger without valid remoteServer url. Config is: " + 
                        changeTrigger.config)
                return poBox;
            }

            def request = poBox.request
            HttpClient httpClient = HttpClientBuilder.create().build()
            def requestCopy = RequestBuilder.create("POST")
            requestCopy.setUri(url)
            def headerNames = request.headerNames
            while(headerNames.hasMoreElements()){
                def headerName = headerNames.nextElement()
                requestCopy.setHeader(headerName, request.getHeader(headerName))
            }
            for(Map.Entry entry : poBox.params){
                if(entry.key instanceof String && entry.value instanceof String) {
                    requestCopy.addParameter(entry.key, entry.value)
                }
                else{
                    log.debug("Entry "+entry+" is not <String,String>")
                }
            }
            for(Map.Entry entry : poBox.model){
                requestCopy.addParameter(String.valueOf(entry.key), String.valueOf(entry.value))
            }
            HttpResponse httpResponse = httpClient.execute(requestCopy.build())
            log.debug("Microservice post returned :"+ httpResponse.statusLine)
            
            // Note: looks like post-filters just cannot set new headers once render() has been called.
            // this is rather unfortunate, since XML responses are always generated via render.
            // so this is more of a proof of conecpt:
            ByteArrayOutputStream baos = new ByteArrayOutputStream()
            httpResponse.entity.writeTo(baos)
            poBox.response.addHeader("ctResponse-"+ changeTrigger.triggerType.name, baos.toString("UTF-8") )
            httpResponse.allHeaders.each { Header header ->
                poBox.response.addHeader("ctHeader-"+changeTrigger.triggerType.name+"-"+header.name, header.value)
            }
        }
        catch (Exception e) {
            log.debug("Failed to execute microserviceChangeTrigger.", e);
        }
        return poBox;
        
    }

    String findRemoteUrl(String config) {
        def configDoc = ParamParser.parseXmlToDocument(config, "error.param.config");
        return configDoc.selectNodes("//remoteServer")?.get(0)?.text
    }
    
    
}

