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
public class DemoReviewState extends BaseLifeCycleState implements IState {

    Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public List<IState> getExitStates(ObjectSystemData osd) {
        return states;
    }

    @Override
    public Boolean checkEnteringObject(ObjectSystemData osd, String params) {
        // object must be in "authoring" state.
        if(osd.getState() == null){
            log.debug("osd has no lifecycle state.");
            return false;
        }
        else if(osd.getState().getName().equals("authoring")){
            return true;
        }
        log.debug("osd has wrong lifecycle state: "+osd.getState().getName());
        return false;
    }

    @Override
    public void enter(ObjectSystemData osd, String params) {
        log.debug("osd ${osd.id} entered DemoReviewState.")
    }

    @Override
    public void exit(ObjectSystemData osd, IState nextState, String params) {
        log.debug("osd ${osd.id} left DemoReviewState.")
    }
}