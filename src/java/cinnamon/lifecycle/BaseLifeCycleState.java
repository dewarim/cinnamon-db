package cinnamon.lifecycle;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class BaseLifeCycleState {

    protected List<IState> states = new ArrayList<IState>();

    public void setExitStates(List<IState> states){
        this.states = states;
    }

}
