package cinnamon.i18n

import cinnamon.global.Constants
import cinnamon.utils.ParamParser
import org.dom4j.Element

class Language {



    static constraints = {
        isoCode(unique: true, size: 1..Constants.MAX_ISO_CODE_LENGTH)
        metadata(size: 1..Constants.METADATA_SIZE)
    }

    static mapping = {
        table('languages')
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

    public void toXmlElement(Element root) {
        Element lang = root.addElement("language");
        lang.addElement("id").addText(String.valueOf(id));
        lang.addElement("sysName").addText(isoCode);
        lang.addElement("name").addText(LocalMessage.loc(getIsoCode()));
        lang.add(ParamParser.parseXml(getMetadata(), null));
    }

}
