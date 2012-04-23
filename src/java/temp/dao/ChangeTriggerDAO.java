package temp.dao;

import server.trigger.ChangeTrigger;

import java.util.List;

public interface ChangeTriggerDAO extends GenericDAO<ChangeTrigger, Long> {
	ChangeTrigger get(Long id);
	
	List<ChangeTrigger> findAllByCommandAndPreAndActiveOrderByRanking(String command);
	List<ChangeTrigger> findAllByCommandAndPostAndActiveOrderByRanking(String command);
}