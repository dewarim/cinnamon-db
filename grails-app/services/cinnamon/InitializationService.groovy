package cinnamon

import cinnamon.index.IndexType
import cinnamon.index.IndexType.DataType
import cinnamon.index.IndexItem
import cinnamon.global.Constants
import cinnamon.index.IndexGroup
import cinnamon.index.valueAssistance.DefaultProvider
import cinnamon.index.indexer.ParentFolderPathIndexer
import cinnamon.index.indexer.CompleteStringIndexer
import cinnamon.index.indexer.TimeXPathIndexer
import cinnamon.index.indexer.DecimalXPathIndexer
import cinnamon.index.indexer.ReverseCompleteStringIndexer
import cinnamon.index.indexer.ReverseStringIndexer
import cinnamon.index.indexer.DefaultIndexer
import cinnamon.index.indexer.DateTimeIndexer
import cinnamon.index.indexer.IntegerXPathIndexer
import cinnamon.index.indexer.DateXPathIndexer
import cinnamon.index.indexer.BooleanXPathIndexer


class InitializationService {

    /**
     * Create index entries for folders and objects.
     */
    public void createFolderSysMetaItems(){
        IndexGroup iGroup = IndexGroup.findByName(Constants.INDEX_GROUP_DEFAULT)
        IndexType type = IndexType.findByName("xpath.string_indexer");
        List<IndexItem> items = new ArrayList<IndexItem>();
        items.add(new IndexItem(Constants.INDEX_FOLDER_NAME, "/sysMeta/folder/name", "true()", Constants.FIELD_FOLDER_NAME, type,
                false, "<vaParams/>", true, iGroup, false, false, true));

        type = IndexType.findByName("xpath.integer_indexer");
        items.add(new IndexItem(Constants.INDEX_FOLDER_PARENT_ID, "/sysMeta/folder/parentId",
                "true()", Constants.FIELD_FOLDER_PARENT_ID, type,
                false, "<vaParams/>", true, iGroup, false, false, true));
        items.add(new IndexItem(Constants.INDEX_FOLDER_TYPE, "/sysMeta/folder/typeId", "true()", Constants.FIELD_FOLDER_TYPE, type,
                false, "<vaParams/>", true, iGroup, false, false, true));
        items.add(new IndexItem(Constants.INDEX_FOLDER_OWNER, "/sysMeta/folder/owner/id", "true()", Constants.FIELD_FOLDER_OWNER, type,
                false, "<vaParams/>", true, iGroup, false, false, true));

        type = IndexType.findByName("xpath.parent_folder_path_indexer");
        items.add(new IndexItem(Constants.INDEX_FOLDER_PATH, "/sysMeta/folder/parentId",
                "true()", Constants.FIELD_FOLDER_PATH, type,
                false, "<vaParams/>", true, iGroup, false, false, true));
        persistItems(items);

    }

    public void createOSDSysMetaItems(){
        IndexGroup iGroup = IndexGroup.findByName(Constants.INDEX_GROUP_DEFAULT)
        IndexType type = IndexType.findByName("xpath.string_indexer");
        List<IndexItem> items = new ArrayList<IndexItem>();
        // String
        items.add(new IndexItem(Constants.INDEX_PROCSTATE, "/sysMeta/object/procstate",
                "true()", Constants.FIELD_PROCSTATE, type,
                false, "<vaParams/>", true, iGroup, false, false, true));
        items.add(new IndexItem(Constants.INDEX_APPNAME, "/sysMeta/object/appname",
                "true()", Constants.FIELD_APPNAME, type,
                false, "<vaParams/>", true, iGroup, false, false, true));
        items.add(new IndexItem(Constants.INDEX_VERSION, "/sysMeta/object/version",
                "true()", Constants.FIELD_VERSION, type,
                false, "<vaParams/>", true, iGroup, false, false, true));
        items.add(new IndexItem(Constants.INDEX_NAME, "/sysMeta/object/name",
                "true()", Constants.FIELD_NAME, type,
                false, "<vaParams/>", true, iGroup, false, false, true));

        type = IndexType.findByName("xpath.complete_string_indexer");
        items.add(new IndexItem(Constants.INDEX_OBJECT_TYPE_NAME, "/sysMeta/object/objectType/sysName",
                "true()", Constants.FIELD_OBJECT_TYPE_NAME, type,
                false, "<vaParams/>", true, iGroup, false, false, true));

        // Integer
        type = IndexType.findByName("xpath.integer_indexer");
        items.add(new IndexItem(Constants.INDEX_ROOT, "/sysMeta/object/rootId","true()",
                Constants.FIELD_ROOT, type, false, "<vaParams/>", true, iGroup, false, false, true));
        items.add(new IndexItem(Constants.INDEX_PARENT_ID, "/sysMeta/object/parentId", "true()",
                Constants.FIELD_PARENT_ID, type, false, "<vaParams/>", true, iGroup, false, false, true));
        items.add(new IndexItem(Constants.INDEX_CONTENT_SIZE, "/sysMeta/object/contentsize",
                "true()", Constants.FIELD_CONTENT_SIZE, type, false, "<vaParams/>", true, iGroup, false, false, true));
        items.add(new IndexItem(Constants.INDEX_LOCKED_BY, "/sysMeta/object/lockedBy/id",
                "true()", Constants.FIELD_LOCKED_BY, type, false, "<vaParams/>", true, iGroup, false, false, true));
        items.add(new IndexItem(Constants.INDEX_CREATOR, "/sysMeta/object/creator/id",
                "true()", Constants.FIELD_CREATOR, type, false, "<vaParams/>", true, iGroup, false, false, true));
        items.add(new IndexItem(Constants.INDEX_MODIFIER, "/sysMeta/object/modifier/id",
                "true()", Constants.FIELD_MODIFIER, type, false, "<vaParams/>", true, iGroup, false, false, true));
        items.add(new IndexItem(Constants.INDEX_OWNER, "/sysMeta/object/owner/id",
                "true()", Constants.FIELD_OWNER, type, false, "<vaParams/>", true, iGroup, false, false, true));
        items.add(new IndexItem(Constants.INDEX_FORMAT, "/sysMeta/object/format/id",
                "true()", Constants.FIELD_FORMAT, type, false, "<vaParams/>", true, iGroup, false, false, true));
        items.add(new IndexItem(Constants.INDEX_OBJECT_TYPE, "/sysMeta/object/objectType/id",
                "true()", Constants.FIELD_OBJECT_TYPE, type, false, "<vaParams/>", true, iGroup, false, false, true));
        items.add(new IndexItem(Constants.INDEX_LANGUAGE, "/sysMeta/object/language/id",
                "true()", Constants.FIELD_LANGUAGE, type, false, "<vaParams/>", true, iGroup, false, false, true));
        items.add(new IndexItem(Constants.INDEX_ACL, "/sysMeta/object/aclId",
                "true()", Constants.FIELD_ACL, type, false, "<vaParams/>", true, iGroup, false, false, true));
        items.add(new IndexItem(Constants.INDEX_LIFECYCLE_STATE, "/sysMeta/object/lifeCycleState",
                "true()", Constants.FIELD_LIFECYCLE_STATE, type, false, "<vaParams/>", true, iGroup, false, false, true));

        // Date
        type = IndexType.findByName("xpath.date_indexer");
        items.add(new IndexItem(Constants.INDEX_CREATED_DATE, "/sysMeta/object/created",
                "true()", Constants.FIELD_CREATED_DATE, type, false, "<vaParams/>", true, iGroup, false, false, true));
        items.add(new IndexItem(Constants.INDEX_MODIFIED_DATE, "/sysMeta/object/modified",
                "true()", Constants.FIELD_MODIFIED_DATE , type, false, "<vaParams/>", true, iGroup, false, false, true));

        // Time
        type = IndexType.findByName("xpath.time_indexer");
        items.add(new IndexItem(Constants.INDEX_CREATED_TIME, "/sysMeta/object/created",
                "true()", Constants.FIELD_CREATED_TIME, type, false, "<vaParams/>", true, iGroup, false, false, true));
        items.add(new IndexItem(Constants.INDEX_MODIFIED_TIME, "/sysMeta/object/modified",
                "true()", Constants.FIELD_MODIFIED_TIME, type, false, "<vaParams/>", true, iGroup, false, false, true));

        // Boolean
        type = IndexType.findByName("xpath.boolean_indexer");
        items.add(new IndexItem(Constants.INDEX_LATEST_HEAD, "/sysMeta/object/latestHead",
                "true()", Constants.FIELD_LATEST_HEAD, type, false, "<vaParams/>", true, iGroup, false, false, true));
        items.add(new IndexItem(Constants.INDEX_LATEST_BRANCH, "/sysMeta/object/latestBranch",
                "true()", Constants.FIELD_LATEST_BRANCH, type, false, "<vaParams/>", true, iGroup, false, false, true));

        // ParentFolderPath
        type = IndexType.findByName("xpath.parent_folder_path_indexer");
        items.add(new IndexItem(Constants.INDEX_PATH, "/sysMeta/object/parentId",
                "true()", Constants.FIELD_FOLDER_PATH, type, false, "<vaParams/>", true, iGroup, false, false, true));

        persistItems(items);
    }

    public void initializeIndexTypes(){
        List<IndexType> itList = new ArrayList<IndexType>();

        itList.add(new IndexType("xpath.boolean_indexer",
                BooleanXPathIndexer.class, DefaultProvider.class,
                DataType.BOOLEAN));

        itList.add(new IndexType("xpath.date_indexer",
                DateXPathIndexer.class, DefaultProvider.class, DataType.DATE_TIME));

        itList.add(new IndexType("xpath.integer_indexer",
                IntegerXPathIndexer.class, DefaultProvider.class, DataType.INTEGER));

        itList.add(new IndexType("xpath.date_time_indexer",
                DateTimeIndexer.class, DefaultProvider.class, DataType.INTEGER));

        itList.add(new IndexType("xpath.string_indexer",
                DefaultIndexer.class, DefaultProvider.class, DataType.TEXT));

        itList.add(new IndexType("xpath.decimal_indexer",
                DecimalXPathIndexer.class, DefaultProvider.class, DataType.DECIMAL));

        itList.add(new IndexType("xpath.time_indexer",
                TimeXPathIndexer.class, DefaultProvider.class, DataType.TIME));

        itList.add(new IndexType("xpath.reverse_string_indexer",
                ReverseStringIndexer.class, DefaultProvider.class, DataType.STRING));

        itList.add(new IndexType("xpath.reverse_complete_string_indexer",
                ReverseCompleteStringIndexer.class, DefaultProvider.class, DataType.STRING));

        itList.add(new IndexType("xpath.complete_string_indexer",
                CompleteStringIndexer.class, DefaultProvider.class, DataType.STRING));

        itList.add(new IndexType("xpath.parent_folder_path_indexer",
                ParentFolderPathIndexer.class, DefaultProvider.class, DataType.STRING));

        for(IndexType type : itList){
            type.save()
        }
    }

    public IndexGroup createDefaultIndexGroup(){
        def iGroup = new IndexGroup(Constants.INDEX_GROUP_DEFAULT);
        iGroup.save()
        return iGroup;
    }

    void persistItems(Collection<IndexItem> items){
        for(IndexItem item : items){
            item.save();
        }
    }
}
