import cinnamon.relation.RelationResolver
import cinnamon.relation.resolver.FixedRelationResolver
import cinnamon.relation.resolver.LatestBranchResolver
import cinnamon.relation.resolver.LatestHeadResolver

fixture {
    
    fixedResolver(RelationResolver, name: FixedRelationResolver.simpleName,
            resolverClass: FixedRelationResolver, config: '<meta/>')
    latestHeadResolver(RelationResolver, name: LatestHeadResolver.simpleName,
            resolverClass: LatestHeadResolver, config: '<meta/>')
    latestBranchResolver(RelationResolver, name: LatestBranchResolver.simpleName,
            resolverClass: LatestBranchResolver, config: '<meta/>')

}