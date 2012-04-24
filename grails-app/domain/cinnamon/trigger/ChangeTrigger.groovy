package cinnamon.trigger

import cinnamon.global.Constants
import cinnamon.utils.ParamParser

/**
 * A ChangeTrigger is a class which decouples a CinnamonMethod and a Trigger-class. Every time an API-method
 * of the CinnamonServer is invoked which is marked with CinnamonMethod@trigger=true, the server checks
 * if a trigger condition is defined and executes the Trigger-classes before and after invoking the command.
 * <br/>
 * <h2>Purpose of ChangeTriggers</h2>
 * Add transformation functions for specific object types, for example have a PDF-renderer create a new
 * PDF rendition every time an object of ObjectType "document" is changed.
 */
class ChangeTrigger {

    static constraints = {
        config size: 1..Constants.METADATA_SIZE
    }

    static mapping = {
        table('change_triggers')
    }

    ChangeTriggerType triggerType
    Integer ranking = 1
    String command
    Boolean active = false
    Boolean preTrigger = false
    Boolean postTrigger = false
    String config = "<config />"

    public ChangeTrigger() {
    }

    public ChangeTrigger(String command, ChangeTriggerType ctt) {
        triggerType = ctt;
    }

    public ChangeTrigger(Map<String, String> fields) {
        ranking = Integer.parseInt(fields.get("ranking"));
        command = fields.get("command");
        active = Boolean.parseBoolean(fields.get("active"));
        preTrigger = Boolean.parseBoolean(fields.get("pre_trigger"));
        postTrigger = Boolean.parseBoolean(fields.get("post_trigger"));
        setConfig(fields.get("config"));
        triggerType = ChangeTriggerType.get(Long.parseLong(fields.get("trigger_type_id")));
    }

    public ChangeTrigger(String command, ChangeTriggerType ctt, Integer ranking,
                         Boolean active, Boolean preTrigger, Boolean postTrigger, String config) {
        this(command, ctt, ranking, active, preTrigger, postTrigger);
        setConfig(config);
    }

    public ChangeTrigger(String command, ChangeTriggerType ctt, Integer ranking,
                         Boolean active, Boolean preTrigger, Boolean postTrigger) {
        triggerType = ctt;
        this.ranking = ranking;
        this.command = command;
        this.active = active;
        this.preTrigger = preTrigger;
        this.postTrigger = postTrigger;
    }

    /**
     * Set the config for the ITrigger class.
     * This should be a string in XML format (depends on actual implementation).
     * For an example, see {@link cinnamon.lifecycle.state.ChangeAclState}.
     * If parameter config is null or an empty string, the default value "&gt;config /&lt;"
     * is used.
     *
     * @param config the configuration string.
     */
    public void setConfig(String config) {
        if (config == null || config.trim().length() == 0) {
            this.config = "<config />";
        } else {
            ParamParser.parseXmlToDocument(config, "error.param.config");
            this.config = config;
        }
    }

    // TODO implement and rename
    public static List<ChangeTrigger> findAllByCommandAndPostAndActiveOrderByRanking(String command) {
//        Query q = getSession().createNamedQuery("findAllChangeTriggersByCommandAndPostAndActiveOrderByRanking");
//        q.setParameter("command", command);
//        return q.getResultList();
        return null
    }

    // TODO: implement and rename
    public static List<ChangeTrigger> findAllByCommandAndPreAndActiveOrderByRanking(String command) {
//        Query q = getSession().createNamedQuery("findAllChangeTriggersByCommandAndPreAndActiveOrderByRanking");
//        q.setParameter("command", command);
//        return q.getResultList();
        return null
    }

}
