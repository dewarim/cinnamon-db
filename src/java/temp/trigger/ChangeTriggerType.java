// cinnamon - the Open Enterprise CMS project
// Copyright (C) 2007-2009 Horner GmbH (http://www.horner-project.eu)
// 
// This library is free software; you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either
// version 2.1 of the License, or (at your option) any later version.
// 
// This library is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
// 
// You should have received a copy of the GNU Lesser General Public
// License along with this library; if not, write to the Free Software
// Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
// (or visit: http://www.gnu.org/licenses/lgpl.html)

package temp.trigger;

import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.exceptions.CinnamonException;
import server.global.Constants;
import server.i18n.LocalMessage;
import server.trigger.ITrigger;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Map;

@Entity
@Table(name = "change_trigger_types",
		uniqueConstraints = {@UniqueConstraint(columnNames={"name"})}
)
public class ChangeTriggerType implements Serializable {

	private static final long	serialVersionUID	= 1L;

	@Id @GeneratedValue
	@Column(name = "id")
	private long 	id;
	
	@Column(name = "name",
			length = Constants.NAME_LENGTH,
			nullable = false)
	private String name;
	
	@Column(name = "description",
			length = Constants.DESCRIPTION_SIZE,
			nullable = false)
	private String description;
	
	@Column(name = "trigger_class",
			length = Constants.NAME_LENGTH,
			nullable = false)
	private Class<? extends ITrigger> triggerClass;
	
	
	@Version
	@Column(name="obj_version")
	@SuppressWarnings("unused")
	private Long obj_version = 0L;
	
	public ChangeTriggerType(){
		
	}
	
	@SuppressWarnings("unchecked")
	public ChangeTriggerType(Map<String,String> cmd){
		Logger log=LoggerFactory.getLogger(this.getClass());log.debug("ctor");
		name		= cmd.get("name");
		description = cmd.get("description");
		try{
			triggerClass = (Class<? extends ITrigger>) Class.forName(cmd.get("trigger_class"));
		}
		catch (ClassNotFoundException e) {
			throw new CinnamonException("error.loading.class",e);
		}
		
	}

	public ChangeTriggerType(String name, String description, Class<? extends ITrigger> triggerClass){
		this.name = name;
		this.description = description;
		this.triggerClass = triggerClass;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
		
	public long getId() {
		return id;
	}
	
	@SuppressWarnings("unused")
	private void setId(long id) {
		this.id = id;
	}

	/**
	 * Add the ChangeTriggerType's fields as child-elements to a new element with the given name. 
	 * If the type parameter is null, simply return an empty element.
	 * @param elementName the root element of the ChangeTriggerType's XML serialization.
     * For example, if you want
     * <pre>
     *  {@code
     *  <ctt><id>5</id><name>...</ctt>
     * }
     * </pre>
     * you have to pass "ctt" as elementName.
	 * @param type the ChangeTriggerType that you want to serialize.
	 * @return the new element.
	 */
	public static Element asElement(String elementName, ChangeTriggerType type){
		Element e = DocumentHelper.createElement(elementName);
		if(type != null){
			e.addElement("id").addText(String.valueOf(type.getId()));
			e.addElement("name").addText( LocalMessage.loc(type.getName()));
			e.addElement("description").addText( LocalMessage.loc(type.getDescription()));
			e.addElement("triggerClass").addText(type.triggerClass.getName());
		}
		return e;
	}

	/**
	 * @return the triggerClass
	 */
	public Class<? extends ITrigger> getTriggerClass() {
		return triggerClass;
	}

	/**
	 * @param triggerClass the triggerClass to set
	 */
	public void setTriggerClass(Class<? extends ITrigger> triggerClass) {
		this.triggerClass = triggerClass;
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChangeTriggerType)) return false;

        ChangeTriggerType that = (ChangeTriggerType) o;

        if (description != null ? !description.equals(that.description) : that.description != null) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (triggerClass != null ? !triggerClass.equals(that.triggerClass) : that.triggerClass != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
