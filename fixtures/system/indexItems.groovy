import cinnamon.global.Constants
import cinnamon.index.IndexGroup
import cinnamon.index.IndexItem

include('system/indexTypes')

fixture {

    defGroup(IndexGroup, name: Constants.INDEX_GROUP_DEFAULT)

    // index folder system metadata:
    folderName(IndexItem, name: Constants.INDEX_FOLDER_NAME, indexType: stringIndexer, systemic: true,
            indexGroup: defGroup, forContent: false, forMetadata: false, forSysMeta: true,
            searchString: '/sysMeta/folder/name', fieldname: Constants.FIELD_FOLDER_NAME,
    )
    folderParent(IndexItem, name: Constants.INDEX_FOLDER_PARENT_ID, indexType: intIndexer, systemic: true,
            indexGroup: defGroup, forContent: false, forMetadata: false, forSysMeta: true,
            searchString: '/sysMeta/folder/parentId', fieldname: Constants.FIELD_FOLDER_PARENT_ID,
    )
    folderType(IndexItem, name: Constants.INDEX_FOLDER_TYPE, indexType: intIndexer, systemic: true,
            indexGroup: defGroup, forContent: false, forMetadata: false, forSysMeta: true,
            searchString: '/sysMeta/folder/typeId', fieldname: Constants.FIELD_FOLDER_TYPE,
    )
    folderOwner(IndexItem, name: Constants.INDEX_FOLDER_OWNER, indexType: intIndexer, systemic: true,
            indexGroup: defGroup, forContent: false, forMetadata: false, forSysMeta: true,
            searchString: '/sysMeta/folder/owner/id', fieldname: Constants.FIELD_FOLDER_OWNER,
    )
    folderPath(IndexItem, name: Constants.INDEX_FOLDER_PATH, indexType: pathIndexer, systemic: true,
            indexGroup: defGroup, forContent: false, forMetadata: false, forSysMeta: true,
            searchString: '/sysMeta/folder/parentId', fieldname: Constants.FIELD_FOLDER_PATH,
    )

    // OSD system metadata:
    procState(IndexItem, name: Constants.INDEX_PROCSTATE, indexType: stringIndexer, systemic: true,
            indexGroup: defGroup, forContent: false, forMetadata: false, forSysMeta: true,
            searchString: '/sysMeta/object/procstate', fieldname: Constants.FIELD_PROCSTATE
    )
    appname(IndexItem, name: Constants.INDEX_APPNAME, indexType: stringIndexer, systemic: true,
            indexGroup: defGroup, forContent: false, forMetadata: false, forSysMeta: true,
            searchString: '/sysMeta/object/appname', fieldname: Constants.FIELD_APPNAME
    )
    versionStr(IndexItem, name: Constants.INDEX_VERSION, indexType: stringIndexer, systemic: true,
            indexGroup: defGroup, forContent: false, forMetadata: false, forSysMeta: true,
            searchString: '/sysMeta/object/version', fieldname: Constants.FIELD_VERSION,
    )
    osdName(IndexItem, name: Constants.INDEX_NAME, indexType: stringIndexer, systemic: true,
            indexGroup: defGroup, forContent: false, forMetadata: false, forSysMeta: true,
            searchString: '/sysMeta/object/name', fieldname: Constants.FIELD_NAME,
    )
    objectTypeName(IndexItem, name: Constants.INDEX_OBJECT_TYPE_NAME, indexType: completeIndexer, systemic: true,
            indexGroup: defGroup, forContent: false, forMetadata: false, forSysMeta: true,
            searchString: '/sysMeta/object/objectType/sysName', fieldname: Constants.FIELD_OBJECT_TYPE_NAME,
    )
    rootId(IndexItem, name: Constants.INDEX_ROOT, indexType: intIndexer, systemic: true,
            indexGroup: defGroup, forContent: false, forMetadata: false, forSysMeta: true,
            searchString: '/sysMeta/object/rootId', fieldname: Constants.FIELD_ROOT,
    )
    osdParent(IndexItem, name: Constants.INDEX_PARENT_ID, indexType: intIndexer, systemic: true,
            indexGroup: defGroup, forContent: false, forMetadata: false, forSysMeta: true,
            searchString: '/sysMeta/object/parentId', fieldname: Constants.FIELD_PARENT_ID,
    )
    contentSize(IndexItem, name: Constants.INDEX_CONTENT_SIZE, indexType: intIndexer, systemic: true,
            indexGroup: defGroup, forContent: false, forMetadata: false, forSysMeta: true,
            searchString: '/sysMeta/object/contentsize', fieldname: Constants.FIELD_CONTENT_SIZE,
    )
    lockedBy(IndexItem, name: Constants.INDEX_LOCKED_BY, indexType: intIndexer, systemic: true,
            indexGroup: defGroup, forContent: false, forMetadata: false, forSysMeta: true,
            searchString: '/sysMeta/object/lockedBy/id', fieldname: Constants.FIELD_LOCKED_BY,
    )
    creator(IndexItem, name: Constants.INDEX_CREATOR, indexType: intIndexer, systemic: true,
            indexGroup: defGroup, forContent: false, forMetadata: false, forSysMeta: true,
            searchString: '/sysMeta/object/creator/id', fieldname: Constants.FIELD_CREATOR,
    )
    osdOwner(IndexItem, name: Constants.INDEX_OWNER, indexType: intIndexer, systemic: true,
            indexGroup: defGroup, forContent: false, forMetadata: false, forSysMeta: true,
            searchString: '/sysMeta/object/owner/id', fieldname: Constants.FIELD_OWNER,
    )
    modifier(IndexItem, name: Constants.INDEX_MODIFIER, indexType: intIndexer, systemic: true,
            indexGroup: defGroup, forContent: false, forMetadata: false, forSysMeta: true,
            searchString: '/sysMeta/object/modifier/id', fieldname: Constants.FIELD_MODIFIER,
    )
    osdFormat(IndexItem, name: Constants.INDEX_FORMAT, indexType: intIndexer, systemic: true,
            indexGroup: defGroup, forContent: false, forMetadata: false, forSysMeta: true,
            searchString: '/sysMeta/object/modifier/id', fieldname: Constants.FIELD_MODIFIER,
    )
    osdTypeId(IndexItem, name: Constants.INDEX_OBJECT_TYPE, indexType: intIndexer, systemic: true,
            indexGroup: defGroup, forContent: false, forMetadata: false, forSysMeta: true,
            searchString: '/sysMeta/object/objectType/id', fieldname: Constants.FIELD_OBJECT_TYPE,
    )
    language(IndexItem, name: Constants.INDEX_LANGUAGE, indexType: intIndexer, systemic: true,
            indexGroup: defGroup, forContent: false, forMetadata: false, forSysMeta: true,
            searchString: '/sysMeta/object/language/id', fieldname: Constants.FIELD_LANGUAGE,
    )
    acl(IndexItem, name: Constants.INDEX_ACL, indexType: intIndexer, systemic: true,
            indexGroup: defGroup, forContent: false, forMetadata: false, forSysMeta: true,
            searchString: '/sysMeta/object/aclId', fieldname: Constants.FIELD_ACL,
    )
    lcs(IndexItem, name: Constants.INDEX_LIFECYCLE_STATE, indexType: intIndexer, systemic: true,
            indexGroup: defGroup, forContent: false, forMetadata: false, forSysMeta: true,
            searchString: '/sysMeta/object/lifeCycleState', fieldname: Constants.FIELD_LIFECYCLE_STATE,
    )
    osdCreatedDate(IndexItem, name: Constants.INDEX_CREATED_DATE, indexType: dateIndexer, systemic: true,
            indexGroup: defGroup, forContent: false, forMetadata: false, forSysMeta: true,
            searchString: '/sysMeta/object/created', fieldname: Constants.FIELD_CREATED_DATE,
    )
    osdModifiedDate(IndexItem, name: Constants.INDEX_MODIFIED_DATE, indexType: dateIndexer, systemic: true,
            indexGroup: defGroup, forContent: false, forMetadata: false, forSysMeta: true,
            searchString: '/sysMeta/object/modified', fieldname: Constants.FIELD_MODIFIED_DATE,
    )
    osdCreatedTime(IndexItem, name: Constants.INDEX_CREATED_TIME, indexType: timeIndexer, systemic: true,
            indexGroup: defGroup, forContent: false, forMetadata: false, forSysMeta: true,
            searchString: '/sysMeta/object/created', fieldname: Constants.FIELD_CREATED_TIME,
    )
    osdModifiedTime(IndexItem, name: Constants.INDEX_MODIFIED_TIME, indexType: timeIndexer, systemic: true,
            indexGroup: defGroup, forContent: false, forMetadata: false, forSysMeta: true,
            searchString: '/sysMeta/object/modified', fieldname: Constants.FIELD_MODIFIED_TIME,
    )
    latestHead(IndexItem, name: Constants.INDEX_LATEST_HEAD, indexType: booleanIndexer, systemic: true,
            indexGroup: defGroup, forContent: false, forMetadata: false, forSysMeta: true,
            searchString: '/sysMeta/folder/latestHead', fieldname: Constants.FIELD_LATEST_HEAD,
    )
    latestBranch(IndexItem, name: Constants.INDEX_LATEST_BRANCH, indexType: booleanIndexer, systemic: true,
            indexGroup: defGroup, forContent: false, forMetadata: false, forSysMeta: true,
            searchString: '/sysMeta/folder/latestBranch', fieldname: Constants.FIELD_LATEST_BRANCH,
    )
    osdFolderPath(IndexItem, name: Constants.INDEX_PATH, indexType: pathIndexer, systemic: true,
            indexGroup: defGroup, forContent: false, forMetadata: false, forSysMeta: true,
            searchString: '/sysMeta/folder/parentId', fieldname: Constants.FIELD_PATH,
    )

    // Tika Metaset:
    procState(IndexItem, name: Constants.INDEX_TIKA, indexType: descIndexer, systemic: true,
            indexGroup: defGroup, forContent: false, forMetadata: true, forSysMeta: false,
            searchString: "/meta/metaset[@type='tika']/html/body", fieldname: Constants.FIELD_TIKA
    )

}