import cinnamon.index.DataType
import cinnamon.index.IndexType
import cinnamon.index.Indexer
import cinnamon.index.indexer.BooleanXPathIndexer
import cinnamon.index.indexer.CompleteStringExpressionIndexer
import cinnamon.index.indexer.CompleteStringIndexer
import cinnamon.index.indexer.CountIndexer
import cinnamon.index.indexer.DateTimeIndexer
import cinnamon.index.indexer.DateXPathIndexer
import cinnamon.index.indexer.DecimalXPathIndexer
import cinnamon.index.indexer.DefaultIndexer
import cinnamon.index.indexer.DescendingCompleteStringIndexer
import cinnamon.index.indexer.DescendingReverseCompleteStringIndexer
import cinnamon.index.indexer.DescendingReverseStringIndexer
import cinnamon.index.indexer.DescendingStringIndexer
import cinnamon.index.indexer.EncodedFieldIndexer
import cinnamon.index.indexer.IntegerXPathIndexer
import cinnamon.index.indexer.ParentFolderPathIndexer
import cinnamon.index.indexer.ReverseCompleteStringIndexer
import cinnamon.index.indexer.ReverseStringIndexer
import cinnamon.index.indexer.TimeXPathIndexer
import cinnamon.index.valueAssistance.DefaultProvider

def provider = DefaultProvider

fixture {

    booleanIndexer(IndexType, name: 'xpath.boolean_indexer', dataType: DataType.BOOLEAN,
            indexerClass: BooleanXPathIndexer, vaProviderClass: provider
    )
    dateIndexer(IndexType, name:'xpath.date_indexer', dataType: DataType.DATE_TIME,
            indexerClass: DateXPathIndexer, vaProviderClass: provider
    )
    intIntexer(IndexType, name: 'xpath.integer_indexer', dataType: DataType.INTEGER,
            indexerClass: IntegerXPathIndexer, vaProviderClass: provider,
    )
    dateTimeIndexer(IndexType, name:'xpath.date_time_indexer', dataType: DataType.INTEGER,
                indexerClass: DateTimeIndexer, vaProviderClass: provider
    )
    stringIndexer(IndexType, name:'xpath.string_indexer', dataType: DataType.TEXT,
            indexerClass: DefaultIndexer , vaProviderClass: provider
    )
    decimalIndexer(IndexType, name:'xpath.decimal_indexer', dataType: DataType.DECIMAL,
            indexerClass: DecimalXPathIndexer, vaProviderClass: provider
    )
    timeIndexer(IndexType, name:'xpath.time_indexer', dataType: DataType.TIME,
            indexerClass: TimeXPathIndexer, vaProviderClass: provider
    )    
    reverseIndexer(IndexType, name:'xpath.reverse_string_indexer', dataType: DataType.STRING,
            indexerClass: ReverseStringIndexer, vaProviderClass: provider
    )
    reverseCompleteIndexer(IndexType, name:'xpath.reverse_complete_string_indexer', dataType: DataType.STRING,
            indexerClass: ReverseCompleteStringIndexer, vaProviderClass: provider
    )
    completeIndexer(IndexType, name:'xpath.complete_string_indexer', dataType: DataType.STRING,
            indexerClass:CompleteStringIndexer , vaProviderClass: provider
    )
    pathIndexer(IndexType, name:'xpath.parent_folder_path_indexer', dataType: DataType.STRING,
            indexerClass:ParentFolderPathIndexer , vaProviderClass: provider
    )  
    descIndexer(IndexType, name:'xpath.descending_string_indexer', dataType: DataType.STRING,
            indexerClass:DescendingStringIndexer , vaProviderClass: provider
    )
    descReverseIndexer(IndexType, name:'xpath.descending_reverse_string_indexer', dataType: DataType.STRING,
            indexerClass:DescendingReverseStringIndexer , vaProviderClass: provider
    )
    descReverseCompleteIndexer(IndexType, name:'xpath.descending_reverse_complete_string_indexer', dataType: DataType.STRING,
            indexerClass:DescendingReverseCompleteStringIndexer , vaProviderClass: provider
    )
    descCompleteIndexer(IndexType, name:'xpath.descending_complete_string_indexer', dataType: DataType.STRING,
            indexerClass:DescendingCompleteStringIndexer , vaProviderClass: provider
    )
    countIndexer(IndexType, name:'xpath.count_indexer', dataType: DataType.INTEGER,
            indexerClass:CountIndexer, vaProviderClass: provider
    )
    encodedFieldIndexer(IndexType, name:'xpath.encoded_field_indexer', dataType: DataType.STRING,
            indexerClass: EncodedFieldIndexer, vaProviderClass: provider
    )
    completeStringExpressionIndexer(IndexType, name:'xpath.complete_string_expression_indexer',
            dataType: DataType.STRING, indexerClass: CompleteStringExpressionIndexer, vaProviderClass: provider
    )

}