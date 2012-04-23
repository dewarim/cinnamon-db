package temp.dao;

import server.trigger.ChangeTriggerType;

public interface ChangeTriggerTypeDAO extends GenericDAO<ChangeTriggerType, Long> {

	ChangeTriggerType get(Long id);

    ChangeTriggerType get(String id);

}