package cinnamon.lifecycle.state;

import cinnamon.ObjectSystemData;
import cinnamon.lifecycle.BaseLifeCycleState;
import cinnamon.lifecycle.IState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 *
 */
public class DemoAuthoringState extends BaseLifeCycleState implements IState {

    Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public List<IState> getExitStates(ObjectSystemData osd) {
        return states;
    }

    @Override
    public Boolean checkEnteringObject(ObjectSystemData osd, String params) {
        // object must not be in "published" state. Anything else is ok.
        if(osd.getState() == null){
            return true;
        }
        else if(osd.getState().getName().equals("published")){
             log.debug("osd is already in published state.");
            return false;
        }        
        return true;
    }

    @Override
    public void enter(ObjectSystemData osd, String params) {
        log.debug("osd "+osd.getId()+" entered DemoAuthoringState.");
    }

    @Override
    public void exit(ObjectSystemData osd, IState nextState, String params) {
        log.debug("osd "+osd.getId()+" left DemoAuthoringState.");
    }
}
