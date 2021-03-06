import cinnamon.MetasetType

def types = ['search', 'cart', 'translation_extension', 'test',
        'render_input', 'render_output',
        'tika', 'task_definition', 'transition', 'workflow_template',
        'log', 'notification', 'translation_folder', 'translation_task', 
        'thumbnail'
]

fixture{
    
    types.each{name ->
        "${name}"(MetasetType, name:name, config:'<metaset />')
    }
    
}