import cinnamon.index.IndexGroup
import cinnamon.index.IndexItem
import cinnamon.index.IndexType

def descIndexer = IndexType.findByName('xpath.descending_string_indexer')
def strIndexer = IndexType.findByName('xpath.string_indexer')

fixture {

    ditaGroup(IndexGroup, name: 'dita.content')

    conbody(IndexItem, name: 'ditacontent.concept', indexType: descIndexer, systemic: false,
            searchCondition: "string(/sysMeta/object/appName)='dita'",
            indexGroup: ditaGroup, forContent: true, forMetadata: false, forSysMeta: false,
            searchString: "/concept/conbody", fieldname: 'dita.content'
    )
    reference(IndexItem, name: 'ditacontent.reference', indexType: descIndexer, systemic: false,
            searchCondition: "string(/sysMeta/object/appName)='dita'",
            indexGroup: ditaGroup, forContent: true, forMetadata: false, forSysMeta: false,
            searchString: "/reference/refbody", fieldname: 'dita.content'
    )
    task(IndexItem, name: 'ditacontent.task', indexType: descIndexer, systemic: false,
            searchCondition: "string(/sysMeta/object/appName)='dita'",
            indexGroup: ditaGroup, forContent: true, forMetadata: false, forSysMeta: false,
            searchString: "/task/taskbody", fieldname: 'dita.content'
    )
    topic(IndexItem, name: 'ditacontent.topic', indexType: descIndexer, systemic: false,
            searchCondition: "string(/sysMeta/object/appName)='dita'",
            indexGroup: ditaGroup, forContent: true, forMetadata: false, forSysMeta: false,
            searchString: "/topic/body", fieldname: 'dita.content'
    )
    title(IndexItem, name: 'ditacontent.title', indexType: strIndexer, systemic: false,
            searchCondition: "string(/sysMeta/object/appName)='dita'",
            indexGroup: ditaGroup, forContent: true, forMetadata: false, forSysMeta: false,
            searchString: "//title", fieldname: 'dita.title'
    )


}