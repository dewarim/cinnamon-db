package cinnamon.trigger

import cinnamon.exceptions.CinnamonException
import org.dom4j.Element
import org.dom4j.DocumentHelper
import cinnamon.i18n.LocalMessage
import cinnamon.global.Constants

class ChangeTriggerType  implements Serializable {

    static constraints = {
        name unique:true, size: 1..Constants.NAME_LENGTH
        description size: 1..Constants.DESCRIPTION_SIZE
    }

    static hasMany = [changeTriggers:ChangeTrigger]
    
    static mapping = {
        cache true
        version 'obj_version'
        table 'change_trigger_types'
    }
    
    String name
    String description
    Class<? extends ITrigger> triggerClass;

    public ChangeTriggerType(){

    }

    @SuppressWarnings("unchecked")
    public ChangeTriggerType(Map<String,String> cmd){
        name		= cmd.get("name");
        description = cmd.get("description");
        try{
            triggerClass = (Class<? extends ITrigger>) Class.forName(cmd.get("trigger_class"));
        }
        catch (ClassNotFoundException e) {
            throw new CinnamonException("error.loading.class",e);
        }

    }

    public ChangeTriggerType(String name, String description, Class<? extends ITrigger> triggerClass){
        this.name = name;
        this.description = description;
        this.triggerClass = triggerClass;
    }

    /**
     * Add the ChangeTriggerType's fields as child-elements to a new element with the given name.
     * If the type parameter is null, simply return an empty element.
     * @param elementName the root element of the ChangeTriggerType's XML serialization.
     * For example, if you want
     * <pre>
     *  {@code
     *  <ctt><id>5</id><name>...</ctt>
     * }
     * </pre>
     * you have to pass "ctt" as elementName.
     * @param type the ChangeTriggerType that you want to serialize.
     * @return the new element.
     */
    public static Element asElement(String elementName, ChangeTriggerType type){
        Element e = DocumentHelper.createElement(elementName);
        if(type != null){
            e.addElement("id").addText(String.valueOf(type.getId()));
            e.addElement("name").addText( LocalMessage.loc(type.getName()));
            e.addElement("description").addText( LocalMessage.loc(type.getDescription()));
            e.addElement("triggerClass").addText(type.triggerClass.getName());
        }
        return e;
    }

    boolean equals(o) {
        if (this.is(o)) return true
        if (!(o instanceof ChangeTriggerType)) return false

        ChangeTriggerType that = (ChangeTriggerType) o

        if (description != that.description) return false
        if (name != that.name) return false
        if (triggerClass != that.triggerClass) return false

        return true
    }

    int hashCode() {
        int result
        result = (name != null ? name.hashCode() : 0)
        result = 31 * result + (description != null ? description.hashCode() : 0)
        result = 31 * result + (triggerClass != null ? triggerClass.hashCode() : 0)
        return result
    }
}
