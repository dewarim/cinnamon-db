import cinnamon.global.Constants
import cinnamon.lifecycle.LifeCycle
import cinnamon.lifecycle.LifeCycleState
import cinnamon.lifecycle.state.NopState

def lcList = [
        Constants.RENDERSERVER_RENDER_TASK_FAILED,
        Constants.RENDERSERVER_RENDER_TASK_FINISHED,
        Constants.RENDERSERVER_RENDER_TASK_RENDERING,
        Constants.RENDERSERVER_RENDER_TASK_NEW
]

include('system/lifeCycles')

fixture{

    lcList.each{name ->
        "${name}Lcs"(LifeCycleState, name: name, stateClass: NopState,
                config: '<meta />',
                lifeCycle: renderLc,
                lifeCycleStateForCopy: null
        )
    }

}