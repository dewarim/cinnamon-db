package cinnamon.trigger.impl;

import cinnamon.ObjectSystemData;
import cinnamon.PoBox;
import cinnamon.relation.Relation;
import cinnamon.trigger.ITrigger;
import cinnamon.utils.ParamParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class RelationChangeTrigger implements ITrigger {

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
            try{
                ObjectSystemData osd = ObjectSystemData.get((String) poBox.params.get("id"));
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
        Map<String,Object> params = poBox.params;
        String command = poBox.command;

        ObjectSystemData osd = null;
        if(commandIdMap.containsKey(command) && params.containsKey(commandIdMap.get( command))){
            osd = ObjectSystemData.get((String) params.get(commandIdMap.get(command)));
        }
        else if(params.containsKey("_RelationChangeTrigger_delete_id_")){
            // latestHead / latestBranch may have changed, among others
            osd = ObjectSystemData.get((String) params.get("_RelationChangeTrigger_delete_id_"));
        }
        else if(command.equals("create")){
            // latestHead / latestBranch may have changed, among others
            String responseContent = poBox.response.getContent();
            String id = ParamParser.parseXmlToDocument(responseContent).selectSingleNode("objectId").getText();
            osd = ObjectSystemData.get((String) params.get(id));
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