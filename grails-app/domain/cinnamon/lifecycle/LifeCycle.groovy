package cinnamon.lifecycle

import cinnamon.global.Constants
import org.dom4j.Element
import cinnamon.i18n.LocalMessage

class LifeCycle implements Serializable  {

    static constraints = {
        name unique: true, size: 1..Constants.NAME_LENGTH
        defaultState(nullable: true)
    }

    static mapping = {
        cache true
        table('lifecycles')
        version 'obj_version'
    }

    String name
    LifeCycleState defaultState

    LifeCycle() {
    }

    LifeCycle(String name, LifeCycleState defaultState) {
        this.name = name;
        this.defaultState = defaultState;
    }

    void toXmlElement(Element root){
        Element lc = root.addElement("lifecycle");
        lc.addElement("id").addText(String.valueOf(id));
        lc.addElement("name").addText( LocalMessage.loc(name) );
        lc.addElement("sysName").addText(name);
        Element ds = lc.addElement("defaultState");
        if(defaultState != null){
            defaultState.toXmlElement(ds);
        }
        Element cycleStates = lc.addElement("states");
        for(LifeCycleState lcs : fetchStates()){
            lcs.toXmlElement(cycleStates);
        }
    }

    public Set<LifeCycleState> fetchStates() {
        def lcsSet = new HashSet<LifeCycleState>()
        lcsSet.addAll(LifeCycleState.findAll("from LifeCycleState lcs where lcs.lifeCycle=:lifeCycle",
                [lifeCycle:this]))
        return lcsSet
    }

    boolean equals(o) {
        if (this.is(o)) return true
        if (!(o instanceof LifeCycle)) return false

        LifeCycle lifeCycle = (LifeCycle) o

        if (defaultState != lifeCycle.defaultState) return false
        if (name != lifeCycle.name) return false

        return true
    }

    int hashCode() {
        int result
        result = (name != null ? name.hashCode() : 0)
        result = 31 * result + (defaultState != null ? defaultState.hashCode() : 0)
        return result
    }
}
