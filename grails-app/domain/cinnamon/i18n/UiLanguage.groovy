package cinnamon.i18n

import org.dom4j.Element
import javax.persistence.EntityManager
import cinnamon.exceptions.CinnamonConfigurationException

class UiLanguage implements Serializable{

    static constraints = {
        isoCode unique: true, size: 1..32
    }

    static mapping = {
        table('ui_languages')
    }

    String isoCode

    public void toXmlElement(Element root){
        Element lang = root.addElement("language");
        lang.addElement("id").addText(String.valueOf(id));
        lang.addElement("sysName").addText( isoCode );
        lang.addElement("name").addText(LocalMessage.loc(getIsoCode()));
    }

    boolean equals(o) {
        if (this.is(o)) return true
        if (!(o instanceof UiLanguage)) return false

        UiLanguage that = (UiLanguage) o

        if (isoCode != that.isoCode) return false

        return true
    }

    int hashCode() {
        return (isoCode != null ? isoCode.hashCode() : 0)
    }

    /**
     * Find the default language. At the moment, this is hardcoded to "und", the
     * undetermined language.
     * TODO: set default language by Config.
     * @return the default language or null
     *
     */
    public static UiLanguage getDefaultLanguage(){
        def lang = UiLanguage.findByIsoCode("und");
        if (lang == null) {
            throw new CinnamonConfigurationException("No default language configured! You must at least configure 'und' for undetermined language.");
        }
        return lang;
    }

    /**
     * Set fields of a language object via a Map.
     * Currently accepted fields:
     * <ul>
     * 		<li>[iso_code]= new ISO-code</li>
     * </ul>
     *
     * @param id	the id of the language (Long)
     * @param fields map of the fields to be set
     */
    public void update(Map<String, String> fields){
        if (fields.containsKey("iso_code")) {
            String isoCode = fields.get("iso_code");
            setIsoCode(isoCode);
        }
    }
}
