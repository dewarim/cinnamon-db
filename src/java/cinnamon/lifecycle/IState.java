package cinnamon.lifecycle;

import server.data.ObjectSystemData;

import java.util.List;

/**
 *
 */
public interface IState {

    List<IState> getExitStates(ObjectSystemData osd);
    
    void setExitStates(List<IState> states);

    /*
     TODO: add a proper response object
     the user will most likely want to know the reason why this action fails / is forbidden.
    */
    Boolean checkEnteringObject(ObjectSystemData osd, String params);

    void enter(ObjectSystemData osd, String params);

    void exit(ObjectSystemData osd, IState nextState, String params);

}
