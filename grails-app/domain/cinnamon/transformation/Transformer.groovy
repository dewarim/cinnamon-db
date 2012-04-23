package cinnamon.transformation

import temp.transformation.ITransformer
import cinnamon.Format
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import javax.persistence.EntityManager
import cinnamon.exceptions.CinnamonException
import org.dom4j.Element
import org.dom4j.DocumentHelper
import cinnamon.i18n.LocalMessage

class Transformer {

    static constraints = {

    }
    String name
    String description
    Class<? extends ITransformer> transformerClass;
    Format sourceFormat;
    Format targetFormat;

    public Transformer(){

    }

    @SuppressWarnings("unchecked")
    public Transformer(Map<String,String> cmd){
        Logger log=LoggerFactory.getLogger(this.getClass());log.debug("ctor");
        name		= cmd.get("name");
        description = cmd.get("description");
        try{
            transformerClass = (Class<? extends ITransformer>) Class.forName(cmd.get("transformer_class"));
        }
        catch (ClassNotFoundException e) {
            throw new CinnamonException("error.loading.class",e);
        }
        sourceFormat = Format.get(Long.parseLong(cmd.get("source_format_id")));
        targetFormat = Format.get(Long.parseLong(cmd.get("target_format_id")));
    }

    public Transformer(String name, String description, Class<? extends ITransformer> transformerClass,
                       Format sourceFormat, Format targetFormat){
        this.name = name;
        this.description = description;
        this.transformerClass = transformerClass;
        this.sourceFormat = sourceFormat;
        this.targetFormat = targetFormat;
    }

    public String getName() {
        return name;
    }

    /**
     * Add the Transformer's fields as child-elements to a new element with the given name.
     * If the type parameter is null, simply return an empty element.
     * @param elementName
     * @param transformer
     * @return the new element.
     */
    public static Element asElement(String elementName, Transformer transformer){
        Element e = DocumentHelper.createElement(elementName);
        if(transformer != null){
            e.addElement("id").addText(String.valueOf(transformer.getId()));
            e.addElement("name").addText( LocalMessage.loc(transformer.getName()));
            e.addElement("description").addText( LocalMessage.loc(transformer.getDescription()));
            e.addElement("transformerClass").addText(transformer.transformerClass.getName());
            e.add(Format.asElement("sourceFormat", transformer.getSourceFormat()));
            e.add(Format.asElement("targetFormat", transformer.getSourceFormat()));
        }
        return e;
    }

    boolean equals(o) {
        if (this.is(o)) return true
        if (!(o instanceof Transformer)) return false

        Transformer that = (Transformer) o

        if (description != that.description) return false
        if (name != that.name) return false
        if (sourceFormat != that.sourceFormat) return false
        if (targetFormat != that.targetFormat) return false
        if (transformerClass != that.transformerClass) return false

        return true
    }

    int hashCode() {
        int result
        result = (name != null ? name.hashCode() : 0)
        result = 31 * result + (description != null ? description.hashCode() : 0)
        result = 31 * result + (transformerClass != null ? transformerClass.hashCode() : 0)
        result = 31 * result + (sourceFormat != null ? sourceFormat.hashCode() : 0)
        result = 31 * result + (targetFormat != null ? targetFormat.hashCode() : 0)
        return result
    }
}
