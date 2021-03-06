package cinnamon.lifecycle.state;

import cinnamon.Acl;
import cinnamon.ObjectSystemData;
import cinnamon.exceptions.CinnamonException;
import cinnamon.lifecycle.BaseLifeCycleState;
import cinnamon.lifecycle.IState;
import cinnamon.utils.ParamParser;
import org.dom4j.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Change the ACL of the OSD to the state defined in the XML params.
 */
public class ChangeAclState extends BaseLifeCycleState implements IState {

    Logger log = LoggerFactory.getLogger(this.class)

    @Override
    public List<IState> getExitStates(ObjectSystemData osd) {
        return states
    }

    @Override
    public Boolean checkEnteringObject(ObjectSystemData osd, String params) {
        // currently, you can change to the ACL state from any other state.
        return true
    }

    @Override
    public void enter(ObjectSystemData osd, String params) {
        log.debug("osd " + osd.id + " entered ChangeAclState.")
        Document doc = ParamParser.parseXmlToDocument(params)
        String aclName = doc.selectSingleNode("//aclName").text
        Acl acl = Acl.findByName(aclName)
        if (acl == null) {
            throw new CinnamonException("error.acl.not_found", aclName)
        }
        log.debug("Setting acl from " + osd.acl.name + " to " + aclName)
        osd.acl = acl
    }

    @Override
    public void exit(ObjectSystemData osd, IState nextState, String params) {
        log.debug("osd " + osd.id + " left ChangeAclState.");
    }
}
