import cinnamon.global.Constants
import cinnamon.relation.RelationType

include('system/resolvers')

fixture {

    childRelation(RelationType, name: Constants.RELATION_TYPE_CHILD,
            leftobjectprotected: false, rightobjectprotected: true,
            leftResolver: fixedResolver, rightResolver: fixedResolver,
            cloneOnLeftCopy: true
    )
    emptyChildRelation(RelationType, name: Constants.RELATION_TYPE_CHILD_NO_CONTENT,
            leftobjectprotected: false, rightobjectprotected: true,
            leftResolver: fixedResolver, rightResolver: fixedResolver,
            cloneOnLeftCopy: true
    )
    renditionRelation(RelationType, name: Constants.RELATION_TYPE_RENDITION,
            leftobjectprotected: true, rightobjectprotected: false,
            leftResolver: fixedResolver, rightResolver: fixedResolver
    )
    translationSrcRelation(RelationType, name: Constants.RELATION_TYPE_TRANSLATION_SOURCE,
            leftobjectprotected: true, rightobjectprotected: false,
            leftResolver: fixedResolver, rightResolver: fixedResolver
    )
    translationRootRelation(RelationType, name: Constants.RELATION_TYPE_TRANSLATION_ROOT,
            leftobjectprotected: true, rightobjectprotected: false,
            leftResolver: fixedResolver, rightResolver: fixedResolver
    )
    translationSrcList(RelationType, name: Constants.RELATION_TYPE_TRANSLATION_SOURCE_LIST,
            leftobjectprotected: false, rightobjectprotected: true,
            leftResolver: fixedResolver, rightResolver: fixedResolver
    )
    translationTargetList(RelationType, name: Constants.RELATION_TYPE_TRANSLATION_TARGET_LIST,
            leftobjectprotected: false, rightobjectprotected: true,
            leftResolver: fixedResolver, rightResolver: fixedResolver
    )

}