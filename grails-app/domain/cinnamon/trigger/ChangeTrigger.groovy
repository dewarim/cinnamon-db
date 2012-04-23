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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChangeTrigger)) return false;

        ChangeTrigger that = (ChangeTrigger) o;

        if (active != null ? !active.equals(that.active) : that.active != null) return false;
        if (command != null ? !command.equals(that.command) : that.command != null) return false;
        if (postTrigger != null ? !postTrigger.equals(that.postTrigger) : that.postTrigger != null) return false;
        if (preTrigger != null ? !preTrigger.equals(that.preTrigger) : that.preTrigger != null) return false;
        if (ranking != null ? !ranking.equals(that.ranking) : that.ranking != null) return false;
        if (config != null ? !config.equals(that.config) : that.config != null) return false;
        if (triggerType != null ? !triggerType.equals(that.triggerType) : that.triggerType != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = triggerType != null ? triggerType.hashCode() : 0;
        result = 31 * result + (ranking != null ? ranking.hashCode() : 0);
        result = 31 * result + (command != null ? command.hashCode() : 0);
        return result;
    }
}
