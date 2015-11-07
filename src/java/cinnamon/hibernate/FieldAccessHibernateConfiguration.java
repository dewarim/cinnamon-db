package cinnamon.hibernate;

import org.codehaus.groovy.grails.orm.hibernate.cfg.DefaultGrailsDomainConfiguration;
import org.hibernate.MappingException;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Property;

import java.util.Iterator;

/**
 * Make Hibernate use direct field access to prevent loading of objects causing side effects.
 * One problematic area is OSD.setMetadata(...), which will transparently convert data in the old metadata
 * format to metasets. But when Hibernate loads the object from the database and uses the setter, 
 * this conversion will fail on the not-quite-completely loaded object graph.
 * 
 * see: http://stackoverflow.com/questions/4850768/grails-field-access-with-gorm
 * see: http://grails.1312388.n4.nabble.com/GORM-setting-access-quot-field-quot-td1592837.html#a1594428
 * 
 * @Deprecated: No longer works with Hibernate4
 */
@Deprecated
public class FieldAccessHibernateConfiguration extends DefaultGrailsDomainConfiguration {

    private static final long serialVersionUID = 1;

    private boolean _alreadyProcessed;

    @SuppressWarnings("unchecked")
    @Override
    protected void secondPassCompile() throws MappingException {
        super.secondPassCompile();

        if (_alreadyProcessed) {
            return;
        }

        for (PersistentClass pc : classes.values()) {

            // pc.getIdentifierProperty().setPropertyAccessorName("field");

            for (Iterator iter = pc.getPropertyIterator(); iter.hasNext(); ) {
                Property property = (Property)iter.next();
                property.setPropertyAccessorName("field");
            }
        }

        _alreadyProcessed = true;
    }
}
