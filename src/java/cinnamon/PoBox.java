package cinnamon;

import org.codehaus.groovy.grails.commons.GrailsApplication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * PoBox (ParameterObjectBox) - store parameters for ChangeTriggers, controller invocation and
 * CinnamonResponse handling.
 */
public class PoBox {

    public GrailsApplication grailsApplication;
    public HttpServletResponse response;
    public HttpServletRequest request;
    public UserAccount user;
    public String repository;
    public Map params;
    public Map model;
    public String controller;
    public String action;
    public Boolean endProcessing = false;

    public PoBox() {
    }

    public PoBox(HttpServletRequest request, HttpServletResponse response, UserAccount user, String repository,
                 Map params, Map model, String controllerName, String actionName, GrailsApplication grailsApplication) {
        this.response = response;
        this.user = user;
        this.repository = repository;
        this.params = params;
        this.controller = controllerName;
        this.action = actionName;
        this.model = model;
        this.grailsApplication = grailsApplication;
        this.request = request;
    }

    public PoBox(HttpServletRequest request, UserAccount user, String repository, Map params, String controllerName,
                 String actionName, GrailsApplication grailsApplication) {
        this.user = user;
        this.repository = repository;
        this.params = params;
        this.controller = controllerName;
        this.action = actionName;
        this.grailsApplication = grailsApplication;
        this.request = request;
    }
}
