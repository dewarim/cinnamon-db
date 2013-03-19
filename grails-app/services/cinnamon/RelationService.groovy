package cinnamon

import cinnamon.relation.Relation
import cinnamon.relation.RelationType
import cinnamon.relation.resolver.RelationSide

class RelationService {

    /**
     * Update relations of all OSDs of this OSD's version tree.
     * After a part of a relation has been updated, all other relations which target
     * this object's version tree have to be updated also because the change to one object may
     * require other relations to change.<br>
     * Example:<br>
     * An image, which is referenced by several relations from documents which
     * use it, is updated and increases its version number. Now all relations which
     * use the LatestHeadResolver need to update their link to this new version.
     * @param changedOsd OSD which has already been updated (or does not need an update).
     */
    void updateRelations(ObjectSystemData changedOsd) {
        for (ObjectSystemData osd : ObjectSystemData.findAllByRoot(changedOsd.root)) {
            for (Relation relation : Relation.findAllByLeftOSD(osd)) {
                relation.leftOSD = relation.getType().findOsdVersion(relation, osd, RelationSide.LEFT);
            }
            for (Relation relation : Relation.findAllByRightOSD(osd)) {
                relation.rightOSD = relation.getType().findOsdVersion(relation, osd, RelationSide.RIGHT);
            }
        }
    }

    /**
     * Find or create a relation of the specified type and left/right objects.
     * Does not use metadata parameter to determine if the relation already exists,
     * but update the relation with the given metadata.
     * @param type the type of the relation
     * @param leftOsd the left part of the relation
     * @param rightOsd the right part of the relation
     * @param metadata the metadata - if the relation exists, the current metadata
     * will be replaced.
     * @return the new or updated relation
     */
    Relation findOrCreateRelation(RelationType type, ObjectSystemData leftOsd,
                                  ObjectSystemData rightOsd, String metadata) {
        def relation = Relation.findByTypeAndLeftOSDAndRightOSD(type, leftOsd, rightOsd)
        if (!relation) {
            relation = new Relation(type, leftOsd, rightOsd, metadata)
            relation.save()
        }
        else{
            if (relation.metadata != metadata){
                relation.metadata = metadata
            }
        }
        return relation
    }

}
