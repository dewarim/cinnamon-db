package cinnamon.transformation

import cinnamon.transformer.ITransformer
import cinnamon.Format
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import cinnamon.exceptions.CinnamonException
import org.dom4j.Element
import org.dom4j.DocumentHelper
import cinnamon.i18n.LocalMessage

class Transformer  implements Serializable {

    static constraints = {
        name unique: true
    }
    
    static mapping = {
        cache true
        table('transformers')
        version 'obj_version'
    }
    String name
    Class<? extends ITransformer> transformerClass;
    Format sourceFormat;
    Format targetFormat;

    public Transformer(){

    }

    public Transformer(String name, Class<? extends ITransformer> transformerClass,
                       Format sourceFormat, Format targetFormat){
        this.name = name;
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

        if (name != that.name) return false
        if (sourceFormat != that.sourceFormat) return false
        if (targetFormat != that.targetFormat) return false
        if (transformerClass != that.transformerClass) return false

        return true
    }

    int hashCode() {
        int result
        result = (name != null ? name.hashCode() : 0)
        result = 31 * result + (transformerClass != null ? transformerClass.hashCode() : 0)      
        return result
    }
}
