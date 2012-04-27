package cinnamon.i18n

import org.dom4j.Element

class Message implements Serializable {

    static constraints = {
        message unique: ['language']
    }

    static mapping = {
        cache true
        table('messages')
        version 'obj_version'
        language column: 'ui_language_id'
    }

    String message
    UiLanguage language
    String translation


    public Message(){}

    public Message(Map<String,String> fields){
        message = fields.get("message");
        translation = fields.get("translation");
        language = UiLanguage.get(Long.parseLong(fields.get("ui_language_id")));
    }

    public Message(String message, UiLanguage language, String translation){
        this.message = message;
        this.language = language;
        this.translation = translation;
    }

    public void toXmlElement(Element root){
        Element msg = root.addElement("message");
        msg.addElement("id").addText(String.valueOf(id));
        msg.addElement("message").addText( message);
        msg.addElement("languageId").addText( String.valueOf(language.getId()));
        msg.addElement("translation").addText( translation );
    }
}
