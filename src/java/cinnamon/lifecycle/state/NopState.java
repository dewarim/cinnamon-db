package cinnamon.lifecycle.state;

import cinnamon.ObjectSystemData;
import cinnamon.lifecycle.BaseLifeCycleState;
import cinnamon.lifecycle.IState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;

/**
 * Does nothing, accepts exiting to and entering from every other state.
 * Useful for lifecycles whose states are mostly declarative instead of functional.
 */
public class NopState extends BaseLifeCycleState implements IState {

    Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public List<IState> getExitStates(ObjectSystemData osd) {
        return states;
    }

    @Override
    public Boolean checkEnteringObject(ObjectSystemData osd, String params) {
        return true;
    }

    @Override
    public void enter(ObjectSystemData osd, String params) {
        log.debug("OSD "+osd.getId()+" entered NopState");
    }

    @Override
    public void exit(ObjectSystemData osd, IState nextState, String params) {
        log.debug("OSD "+osd.getId()+" left NopState.");
    }
}
