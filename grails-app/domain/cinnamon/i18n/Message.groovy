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

    boolean equals(o) {
        if (this.is(o)) return true
        if (!(o instanceof Message)) return false

        Message message1 = (Message) o

        if (language != message1.language) return false
        if (message != message1.message) return false
        if (translation != message1.translation) return false

        return true
    }

    int hashCode() {
        int result
        result = (message != null ? message.hashCode() : 0)
        result = 31 * result + (language != null ? language.hashCode() : 0)
        result = 31 * result + (translation != null ? translation.hashCode() : 0)
        return result
    }
}
