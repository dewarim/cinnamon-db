package temp.trigger.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.Relation;
import server.User;
import server.dao.DAOFactory;
import server.dao.HibernateDAOFactory;
import server.dao.ObjectSystemDataDAO;
import server.data.ObjectSystemData;
import server.helpers.PoBox;
import server.interfaces.Repository;
import server.interfaces.Response;
import server.trigger.ITrigger;
import server.trigger.TriggerResult;
import utils.HibernateSession;
import utils.ParamParser;

import javax.persistence.EntityManager;
import java.util.HashMap;
import java.util.Map;

public class RelationChangeTrigger implements ITrigger {
    static DAOFactory daoFactory = DAOFactory.instance(HibernateDAOFactory.class);

    Logger log = LoggerFactory.getLogger(this.getClass());

    @Override
    public PoBox executePreCommand(PoBox poBox, String config) {
        log.debug("preCommand of RelationChangeTrigger");
        if(poBox.command.equals("delete")){
            /*
             * If an osd is deleted, we will be unable to use it later on to find the version tree
             * whose relations need updates. So, we store the id here - unless the delete command
             * refers to a lonely version one without descendants (in that case there will be no
             * relations left after deletion).
             */
            EntityManager em = HibernateSession.getLocalEntityManager();
            ObjectSystemDataDAO oDao = daoFactory.getObjectSystemDataDAO(em);
            try{
                ObjectSystemData osd = oDao.get((String) poBox.params.get("id"));
                if(osd.getRoot().equals(osd) && osd.getLatestBranch() && osd.getLatestHead()){
                    // There is only this one object, so we got nothing to do here.
                }
                else{
                   poBox.params.put("_RelationChangeTrigger_delete_id_", String.valueOf(osd.getRoot().getId()));
                }
            }
            catch (Exception e){
                log.debug("Failed to load osd.");
            }
        }
        return poBox;
    }

    @Override
    public PoBox executePostCommand(PoBox poBox, String config) {
        log.debug("postCommand RelationChangeTrigger");
        EntityManager em = HibernateSession.getLocalEntityManager();
        ObjectSystemDataDAO oDao = daoFactory.getObjectSystemDataDAO(em);
        Map<String,Object> params = poBox.params;
        String command = poBox.command;

        ObjectSystemData osd = null;
        if(commandIdMap.containsKey(command) && params.containsKey(commandIdMap.get( command))){
            osd = oDao.get((String) params.get(commandIdMap.get(command)));
        }
        else if(params.containsKey("_RelationChangeTrigger_delete_id_")){
            // latestHead / latestBranch may have changed, among others
            osd = oDao.get((String) params.get("_RelationChangeTrigger_delete_id_"));
        }
        else if(command.equals("create")){
            // latestHead / latestBranch may have changed, among others
            String responseContent = poBox.response.getContent();
            String id = ParamParser.parseXmlToDocument(responseContent).selectSingleNode("objectId").getText();
            osd = oDao.get(id);
        }

        if(osd == null){
              log.debug("target of "+command+" not found");
        }
        else{
            Relation.updateRelations(osd);
        }
        return poBox;
    }

    static Map<String, String> commandIdMap;
    static {
        commandMapBuilder();
    }

    static void commandMapBuilder(){
        commandIdMap = new HashMap<String, String>();
        commandIdMap.put("setmeta", "id");
        commandIdMap.put("setcontent", "id");
        commandIdMap.put("version", "preid");
        commandIdMap.put("setsysmeta", "id");
    }
}