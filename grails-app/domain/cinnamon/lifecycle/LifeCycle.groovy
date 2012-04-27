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
        for(LifeCycleState lcs : states){
            lcs.toXmlElement(cycleStates);
        }
    }

    public Set<LifeCycleState> fetchStates() {
        return LifeCycleState.findAll("from LifeCycleState lcs where lcs.lifeCycle=:lifeCycle",
                [lifeCycle:this]).toSet();
    }


    @Override
    boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LifeCycle)) return false;

        LifeCycle lifeCycle = (LifeCycle) o;

        if (defaultState != null ? !defaultState.equals(lifeCycle.defaultState) : lifeCycle.defaultState != null)
            return false;
        if (name != null ? !name.equals(lifeCycle.name) : lifeCycle.name != null) return false;

        return true;
    }

    @Override
    int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

}
