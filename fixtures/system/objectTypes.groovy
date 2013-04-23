import cinnamon.ObjectType
import cinnamon.global.Constants

fixture {

    renderType(ObjectType, name: Constants.OBJECT_TYPE_RENDER_TASK, config: '<meta/>')
    cartType(ObjectType, name: Constants.OBJTYPE_CART, config: '<meta/>')
    configType(ObjectType, name: Constants.OBJTYPE_CONFIG, config: '<meta/>')
    renditionType(ObjectType, name: Constants.OBJTYPE_RENDITION, config: '<meta/>')
    searchType(ObjectType, name: Constants.OBJTYPE_SEARCH, config: '<meta/>')
    supportType(ObjectType, name: Constants.OBJTYPE_SUPPORTING_DOCUMENT, config: '<meta/>')
    documentType(ObjectType, name: Constants.OBJTYPE_DOCUMENT, config: '<meta/>')
    translationType(ObjectType, name: Constants.OBJTYPE_TRANSLATION_TASK, config: '<meta/>')
    imageType(ObjectType, name: Constants.OBJTYPE_IMAGE, config: '<meta/>')
    workflowType(ObjectType, name: Constants.OBJTYPE_WORKFLOW, config: '<meta/>')
    workflowTemplateType(ObjectType, name: Constants.OBJTYPE_WORKFLOW_TEMPLATE, config: '<meta/>')
    taskType(ObjectType, name: Constants.OBJTYPE_TASK, config: '<meta/>')
    taskDefinitionType(ObjectType, name: Constants.OBJTYPE_TASK_DEFINITION, config: '<meta/>')
    defaultType(ObjectType, name:Constants.OBJTYPE_DEFAULT, config:'<meta/>')
    notificationType(ObjectType, name:Constants.OBJTYPE_NOTIFICATION, config:'<meta/>')
    
}