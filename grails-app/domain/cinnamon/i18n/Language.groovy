package cinnamon.i18n

import cinnamon.global.Constants
import cinnamon.utils.ParamParser
import org.dom4j.Element

class Language implements Serializable {

    static constraints = {
        isoCode(unique: true, size: 1..Constants.MAX_ISO_CODE_LENGTH)
        metadata(size: 1..Constants.METADATA_SIZE)
    }

    static mapping = {
        cache true
        table('languages')
        version 'obj_version'
    }

    String isoCode
    String metadata = "<meta />"

    public Language() {

    }

    public Language(String isoCode) {
        this.isoCode = isoCode;
    }

    public Language(String isoCode, String metadata) {
        this.isoCode = isoCode;
        setMetadata(metadata);
    }

    public Language(Map<String, String> cmd) {
        isoCode = cmd.get("iso_code") ?: cmd.get('isoCode');
        setMetadata(cmd.get("metadata"));
    }

    public void setMetadata(String metadata) {
        if (metadata == null || metadata.trim().length() == 0) {
            this.metadata = "<meta/>";
        }
        else {
            ParamParser.parseXmlToDocument(metadata, "error.param.metadata");
            this.metadata = metadata;
        }
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

    // TODO: generate XML in view.
    public void toXmlElement(Element root) {
        Element lang = root.addElement("language");
        lang.addElement("id").addText(String.valueOf(id));
        lang.addElement("sysName").addText(isoCode);
        lang.addElement("name").addText(LocalMessage.loc(getIsoCode()));
        lang.add(ParamParser.parseXml(getMetadata(), null));
    }

    boolean equals(o) {
        if (this.is(o)) return true
        if (!(o instanceof Language)) return false

        Language language = (Language) o

        if (isoCode != language.isoCode) return false
        if (metadata != language.metadata) return false

        return true
    }

    int hashCode() {
        int result
        result = (isoCode != null ? isoCode.hashCode() : 0)
        result = 31 * result + (metadata != null ? metadata.hashCode() : 0)
        return result
    }
}
