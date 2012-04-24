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

import java.util.List;

/**
 * Change the ACL of the OSD to the state defined in the XML params.
 */
public class ChangeAclState extends BaseLifeCycleState implements IState {

    Logger log = LoggerFactory.getLogger(this.getClass());
    static DAOFactory daoFactory = DAOFactory.instance(DAOFactory.HIBERNATE);

    @Override
    public List<IState> getExitStates(ObjectSystemData osd) {
        return states;
    }

    @Override
    public Boolean checkEnteringObject(ObjectSystemData osd, String params) {
        // currently, you can change to the ACL state from any other state.
        return true;
    }

    @Override
    public void enter(ObjectSystemData osd, String params) {
        log.debug("osd "+osd.getId()+" entered ChangeAclState.");
        Document doc = ParamParser.parseXmlToDocument(params);
        String aclName = doc.selectSingleNode("//aclName").getText();
        AclDAO aclDao = daoFactory.getAclDAO(HibernateSession.getLocalEntityManager());
        Acl acl = aclDao.findByName(aclName);
        if(acl == null){
            throw new CinnamonException("error.acl.not_found", aclName);
        }
        log.debug("Setting acl from "+osd.getAcl().getName() + " to "+ aclName);
        osd.setAcl(acl);
    }

    @Override
    public void exit(ObjectSystemData osd, IState nextState, String params) {
        log.debug("osd "+osd.getId()+" left ChangeAclState.");
    }
}
