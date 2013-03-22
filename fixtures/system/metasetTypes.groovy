import cinnamon.MetasetType

fixture{
    
    searchMs(MetasetType, name:'search', config: '<metaset />')
    cartMs(MetasetType, name:'cart', config: '<metaset />')
    translationMs(MetasetType, name:'translation_extension',config: '<metaset />')
    renderInputMs(MetasetType, name:'render_input', config: '<metaset />')
    renderOutputMs(MetasetType, name:'render_output', config:'<metaset />')
    testMs(MetasetType, name:'test', config: '<metaset />')
    tikaMs(MetasetType, name:'tika', config: '<metaset />')
    taskDefMs(MetasetType, name:'task_definition', config: '<metaset />')
    
}