package cinnamon

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import cinnamon.interfaces.IMetasetOwner
import cinnamon.interfaces.IMetasetJoin

import cinnamon.global.PermissionName
import cinnamon.exceptions.CinnamonException

/**
 * Collected methods for metaset treatment
 */
public class MetasetService {

    Logger log = LoggerFactory.getLogger(this.getClass());

    /**
     * If more than one item references this metaset, create a new branch for the current owner.
     *
     * @param content the new content to set
     * @param metaset the Metaset to update
     * @param owner   if branching is necessary, this object will be the owner of the new branch.
     */
    public void updateWithBranch(String content, IMetasetOwner owner, Metaset metaset) {
        Collection<IMetasetOwner> owners = fetchOwners(owner, metaset.type);
        if (owners.size() > 1) {
            // remove existing metaset:
            IMetasetJoin metasetJoin = owner.fetchMetasetJoin(metaset.type);
            if(metasetJoin != null){
                if(metasetJoin.isDirty()){
                    metasetJoin.save(flush:true)
                }
                metasetJoin.doDelete();
            }
            // create branch:
            Metaset branch = new Metaset(content, metaset.getType());
            branch.save();
            owner.addMetaset(branch);
        } else {
            metaset.setContent(content);
        }
    }

    /**
     * Try to delete all references to this metaset, then this metaset itself. Throws a CinnamonException if there
     * are any ACL problems.
     *
     * @param type         the type of Metaset to delete
     * @param currentOwner an item that references this metaset.
     * @param deletePolicy the policy for deletions, determines for example if an exception is thrown if not all
     *                     references can be deleted.
     * @param validator    a validator object to check the ACLs if deletion is allowed.
     *                     Delete metaset requires WRITE_METADATA permission.
     */
    public Collection<IMetasetOwner> deleteMetaset(IMetasetOwner currentOwner, MetasetType type, Validator validator, DeletePolicy deletePolicy) {
        List<IMetasetOwner> affectedOwners = new ArrayList<IMetasetOwner>();
        for (IMetasetOwner owner : fetchOwners(currentOwner, type)) {
            Acl acl = owner.getAcl();
            try {
                if (currentOwner instanceof ObjectSystemData) {
                    validator.validatePermission(acl, PermissionName.WRITE_OBJECT_CUSTOM_METADATA);
                } else {
                    validator.validatePermission(acl, PermissionName.EDIT_FOLDER);
                }
            } catch (Exception e) {
                log.debug("Not allowed to remove metaset reference:" + e.getLocalizedMessage());
                if (deletePolicy.equals(DeletePolicy.COMPLETE)) {
                    throw new CinnamonException(e);
                } else {
                    // policy: ALLOWED == continue with next item.
                    continue;
                }
            }
            IMetasetJoin metasetJoin = owner.fetchMetasetJoin(type);
            metasetJoin.doDelete();
            affectedOwners.add(owner);
        }
        return affectedOwners;
    }

    public Collection<IMetasetOwner> fetchOwners(IMetasetOwner owner, MetasetType type) {
        Metaset metaset = owner.fetchMetaset(type.name);
        return fetchOwners(metaset);
    }

    public Collection<IMetasetOwner> fetchOwners(Metaset metaset) {

        // fetch OsdMetasets and FolderMetasets:
        List<IMetasetOwner> osdMetasets = ObjectSystemData.findAll("from OsdMetaset o where o.metaset=:metaset", [metaset:metaset])
        List<IMetasetOwner> folderMetasets = Folder.findAll("from FolderMetaset fm where fm.metaset=:metaset", [metaset:metaset]);

        // combine collections:
        List<IMetasetOwner> owners = new ArrayList<IMetasetOwner>(osdMetasets.size() + folderMetasets.size());
        owners.addAll(folderMetasets);
        owners.addAll(osdMetasets);
        return owners;
    }

    public void unlinkMetaset(IMetasetOwner owner, Metaset metaset) {
        if(! metaset){
            // nop. Example use case:
            // unlinkMetaset(osd, osd.fetchMetaset(thumbnail))
            return
        }
        IMetasetJoin metasetJoin = owner.fetchMetasetJoin(metaset.type);
        if(! metasetJoin){
            log.warn("metaset join for metaset ${metaset.id} was not found.")
            return
        }
        metasetJoin.doDelete()
        def metaCount = FolderMetaset.countByMetaset(metaset) + OsdMetaset.countByMetaset(metaset)
        if (metaCount == 0) {
            // only delete a metaset if no one else links there:
            if(metaset.folderMetasets.size() > 0){
                log.warn("Metaset #${metaset.id} still has folderMetasets. Something's wrong.")
                metaset.folderMetasets.clear()                
            }
            if(metaset.osdMetasets.size() > 0){
                log.warn("Metaset #${metaset.id} still has osdMetasets. Something's wrong.")
            }
            metaset.delete()
        }
    }

    public Metaset createMetaset(owner, metasetType, content){
        log.debug("create new metaset")
        def metaset = new Metaset(content, metasetType);
        owner.save() // we need the Hibernate Id to add a Metaset
        metaset.save()
        owner.addMetaset(metaset);
        return metaset
    }
    
    public Metaset createOrUpdateMetaset(IMetasetOwner owner, MetasetType metasetType, String content, WritePolicy writePolicy) {
        Metaset metaset = owner.fetchMetaset(metasetType.getName());
        if (metaset == null) {
            metaset = createMetaset(owner, metasetType, content)
        } else {
            // update metaset            
            switch (writePolicy) {
                case WritePolicy.WRITE:
                    metaset.setContent(content);
                    break;
                case WritePolicy.IGNORE:
                    break; // do nothing, there already is content;
                case WritePolicy.BRANCH:
                    updateWithBranch(content, owner, metaset);
                    break;
            }
            log.debug("setting metaset content to:\n" + content);
        }
        return metaset;
    }

    public void copyMetasets(IMetasetOwner source, IMetasetOwner target, String metasets) {
        if (metasets == null) {
            for (Metaset m : source.fetchMetasets()) {
                log.debug(String.format("Add Metaset %d %s from %d to %d", m.getId(), m.getType().getName(), source.getId(), target.getId()));
                target.addMetaset(m);
            }
        } else {
            for (String metasetName : metasets.split(",")) {
                Metaset metaset = source.fetchMetaset(metasetName);
                target.addMetaset(metaset);
            }
        }
    }

    public void initializeMetasets(IMetasetOwner owner, String metasets) {
        if(metasets == null){
            // nothing to do.
            return;
        }
        for (String metasetName : metasets.split(",")) {
            MetasetType metasetType = MetasetType.findByName(metasetName);
            Metaset metaset = new Metaset(metasetType.getConfig(), metasetType);
            metaset.save()
            owner.addMetaset(metaset);
        }
    }
}