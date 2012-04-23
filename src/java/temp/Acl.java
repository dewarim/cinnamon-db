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

package temp;

import org.dom4j.Element;
import server.global.Constants;
import server.i18n.LocalMessage;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

@NamedQueries(
		{
			// ACLs
			@NamedQuery(
				name = "selectAllAcls",
				query = "select a from Acl a"
			),
			@NamedQuery(
				name = "findAclByName",
				query = "select a from Acl a where a.name=:name"
			),
			
			// AclEntries
			@NamedQuery(
					name = "selectAllAclEntries",
					query = "select ae from AclEntry ae"
			),
			@NamedQuery(
					name = "selectAclEntriesByUser",
					query = "select ae from AclEntry ae where ae.acl=:acl and ae.group in (select g.group from CmnGroupUser g where g.user=:user)"
			),
			
			// AclEntryPermissions
			@NamedQuery(
				name = "selectAllAclEntryPermissions",
				query = "select aep from AclEntryPermission aep"					
			),
			
			// ChangeTrigger
			@NamedQuery(
					name = "selectAllChangeTriggers",
					query = "select c from server.trigger.ChangeTrigger c"
			),
			@NamedQuery(
					name = "findAllChangeTriggersByCommandAndPostAndActiveOrderByRanking",
					query = "select c from server.trigger.ChangeTrigger c where c.command=:command and c.postTrigger=true and c.active=true order by c.ranking"
			),
			@NamedQuery(
					name = "findAllChangeTriggersByCommandAndPreAndActiveOrderByRanking",
					query = "select c from server.trigger.ChangeTrigger c where c.command=:command and c.preTrigger=true and c.active=true order by c.ranking"
			),
			// ChangeTriggerType
			@NamedQuery(
					name = "selectAllChangeTriggerTypes",
					query = "select c from server.trigger.ChangeTriggerType c"
			),


			// ConfigEntry
			@NamedQuery(
					name = "selectAllConfigEntries",
					query= "select c from ConfigEntry c"
			),
			@NamedQuery(
					name = "selectConfigEntryByName",
					query= "select c from ConfigEntry c where c.name=:name"
			),
			
			// Folders
			@NamedQuery(
					name = "selectAllFolders",
					query = "select f from Folder f"
			),
            @NamedQuery(
					name = "selectSubfolders",
					query = "select f from Folder f where f.parent=:parent and f.parent.id != f.id order by name"
			),
			@NamedQuery(
					name = "prepareFolderReIndex",
					query = "UPDATE Folder f SET f.indexOk=NULL"
			),
			@NamedQuery(
					name = "findRootFolder",
					query = "select f from Folder f where f.name=:name and f.parent.id=f.id"
			),
			@NamedQuery(
					name = "findFolderIndexTargets",
					query = "select f from Folder f WHERE f.indexOk=NULL"
			),
            @NamedQuery(
					name = "selectFolderByParentAndName",
					query = "select f from Folder f where f.parent=:parent and f.name=:name"
			),
			// FolderType
			@NamedQuery(
					name = "selectAllFolderTypes",
					query= "select f from FolderType f"
			),
			@NamedQuery(
					name = "selectFolderTypeByName",
					query= "select f from FolderType f where f.name=:name"
			),
			
			// Formats
			@NamedQuery(
					name = "selectAllFormats",
					query = "select f from Format f"
			),
			@NamedQuery(
					name = "findFormatByName",
					query = "select f from Format f where f.name=:name"
			),
			
			// CmnGroupUser
			@NamedQuery(
					name = "selectGroupUserByUserAndGroup",
					query= "SELECT gu FROM CmnGroupUser gu WHERE user=:user AND group=:group"
			),
			
			// IndexItem
			@NamedQuery(
					name = "selectAllIndexItems",
					query = "select i from IndexItem i"
			),
			@NamedQuery(
					name = "findIndexItemByName",
					query = "SELECT i FROM IndexItem i WHERE name=:name"
			),
			
			// IndexGroup
			@NamedQuery(
					name = "selectAllIndexGroups",
					query = "select i from IndexGroup i"
			),
			@NamedQuery(
					name = "findIndexGroupByName",
					query = "SELECT i FROM IndexGroup i WHERE name=:name"
			),
			
			// IndexType
			@NamedQuery(
					name = "selectAllIndexTypes",
					query = "select i from IndexType i"
			),
			@NamedQuery(
					name = "findIndexTypeByName",
					query = "SELECT i FROM IndexType i WHERE name=:name"
			),
			
			// Languages
			@NamedQuery(
					name = "selectAllLanguages",
					query = "select l from Language l"
			),
			@NamedQuery(
					name = "findLanguageByIsoCode",
					query = "select l from Language l where l.isoCode=:isoCode"
			),

            // LifeCycles
			@NamedQuery(
					name = "selectAllLifeCycles",
					query = "select l from LifeCycle l"
			),

            // LifeCycles
            @NamedQuery(
                    name = "findLifeCycleByName",
                    query = "select l from LifeCycle l where l.name=:name"
            ),

                // LifeCycleStates
			@NamedQuery(
					name = "selectAllLifeCycleStates",
					query = "select l from LifeCycleState l"
			),
            @NamedQuery(
					name = "findLifeCycleStateByName",
					query = "select l from LifeCycleState l where l.lifeCycle=:lifeCycle and l.name=:name"
			),
                
			// Message
			@NamedQuery(
					name = "selectAllMessages",
					query = "SELECT m FROM Message m"
			),
			@NamedQuery(
					name = "findMessageByNameAndUiLanguage",
					query = "SELECT m FROM Message m WHERE m.message=:message AND m.language=:language"
			),
			
			// Objects
			@NamedQuery(
					name = "selectAllOSDs",
					query = "select o from ObjectSystemData o"
			),
			@NamedQuery(
					name = "prepareOSD_ReIndex",
					query = "UPDATE ObjectSystemData o SET o.indexOk=NULL"
			),
			@NamedQuery(
					name = "findObjectIndexTargets",
					query = "select o from ObjectSystemData o WHERE o.indexOk = NULL"
			),
            @NamedQuery(
					name = "findLatestHead",
					query = "select o from ObjectSystemData o WHERE o.latestHead = true and o.root=:root"
			),
            @NamedQuery(
					name = "findAllByLockOwner",
					query = "select o from ObjectSystemData o WHERE o.locked_by = :lockOwner"
			),
            @NamedQuery(
					name = "findLatestBranchOrderByModified",
					query = "select o from ObjectSystemData o WHERE o.latestBranch = true and o.root=:root order by o.modified desc"
			),
			@NamedQuery(
					name = "findOSDByRootAndNotSelf",
					query = "SELECT o FROM ObjectSystemData o WHERE root=:root AND NOT o=:self"
			),
			@NamedQuery(
					name = "findOSDAllVersionsLastToFirst",
					query = "select o from ObjectSystemData o where o.root=:root order by o.id desc"
			),
			@NamedQuery(
					name = "findAllVersionsOfOsdOrderByIdDesc",
					query = "select o from ObjectSystemData o where o.root=:root order by o.id desc"
			),
			@NamedQuery(
					name = "findOsdByRootAndVersion",
					query = "select o from ObjectSystemData o where o.root=:root and o.version=:version"
			),
			@NamedQuery(
					name = "findOsdsByNameAndType",
					query = "select o from ObjectSystemData o where o.name=:name and o.type=:type"
			),
            @NamedQuery(
					name = "findOsdsByParent",
					query = "select o from ObjectSystemData o where o.parent=:parent"
			),
			@NamedQuery(
					name = "findOsdsByPredecessor",
					query = "select o from ObjectSystemData o where o.predecessor=:predecessor"
			),
			@NamedQuery(
					name = "findOsdsByPredecessorOrderByIdDesc",
					query = "select o from ObjectSystemData o where o.predecessor=:predecessor"
			),
			@NamedQuery(
					name = "findAllOsdByTypeAndProcstate",
					query = "select o from ObjectSystemData o where o.type=:type and o.procstate=:procstate"
			),
			@NamedQuery(
					name = "findAllOsdByTypeAndProcstateAndOwner",
					query = "select o from ObjectSystemData o where o.type=:type and o.procstate=:procstate and o.owner=:owner "
			),
			@NamedQuery(
					name = "findAllOsdByTypeAndProcstateAndOwnerAndRelationToLeftOsd",
					query = "select o from ObjectSystemData o where o.type=:type and o.procstate=:procstate and o.owner=:owner " +
							"and o.id in (select r1.rightOSD.id from Relation r1 where r1.leftOSD=:leftOSD)"
			),
			@NamedQuery(
					name = "findAllOsdByTypeAndProcstateAndRelationToLeftOsd",
					query = "select o from ObjectSystemData o where o.type=:type and o.procstate=:procstate " +
							"and o.id in (select r1.rightOSD.id from Relation r1 where r1.leftOSD=:leftOSD)"
			),
			@NamedQuery(
					name = "findOsdsByParentAndName",
					query = "select o from ObjectSystemData o where o.parent=:parent and o.name=:name"
			),
            @NamedQuery(
					name = "findOsdsByParentAndLatestHead",
					query = "select o from ObjectSystemData o where o.parent=:parent and o.latestHead=:latestHead"
			),
            @NamedQuery(
					name = "findOsdsByParentAndLatestBranch",
					query = "select o from ObjectSystemData o where o.parent=:parent and o.latestBranch=:latestBranch"
			),
                /**
                 * Find OSDs in the given folder where latestHead or latestBranch is true.
                 */
            @NamedQuery(
					name = "findOsdsByParentAndLatestHeadOrLatestBranch",
					query = "select o from ObjectSystemData o where o.parent=:parent and (o.latestHead=:latestHead or o.latestBranch=:latestBranch)"
			),

			// ObjectTypes
			@NamedQuery(
					name = "selectAllObjectTypes",
					query = "select ot from ObjectType ot"
			),
            @NamedQuery(
                    name = "findObjectTypeByName",
                    query =  "select o from ObjectType o where o.name=:name"
            ),

			// Permissions
			@NamedQuery(
					name = "selectAllPermissions",
					query = "select p from Permission p"
			),
			@NamedQuery(
					name = "findPermissionByName",
					query = "SELECT p FROM Permission p WHERE p.name=:name"
			),
			
			// Relations
			@NamedQuery(
					name = "selectAllRelations",
					query = "select r from Relation r"
			),
			@NamedQuery(
					name = "findRelationsByLeft",
					query = "select r from Relation r where leftOSD=:left"
			),
			@NamedQuery(
					name = "findRelationsByRight",
					query = "select r from Relation r where rightOSD=:right"
			),
			@NamedQuery(
					name = "findRelationsByLeftAndRight",
					query = "select r from Relation r where leftOSD=:left and rightOSD=:right"
			),
			@NamedQuery(
					name = "findRelationByType",
					query = "select r from Relation r where type=:type"
			),
			@NamedQuery(
					name = "findRelationsByLeftAndRightAndType",
					query = "select r from Relation r where r.leftOSD=:left and r.rightOSD=:right and r.type=:type"
			),
			@NamedQuery(
					name = "findRelationsByLeftAndType",
					query = "select r from Relation r where leftOSD=:left and type=:type"
			),
			@NamedQuery(
					name = "findRelationsByRightAndType",
					query = "select r from Relation r where rightOSD=:right and type=:type"
			),			
			@NamedQuery(
					name = "findProtectedRelations",
					query = "select r from Relation r where (r.leftOSD=:osd1 and r.type.leftobjectprotected=true) or (r.rightOSD=:osd2 and r.type.rightobjectprotected=true)"
				),

			// RelationResolverTypes
			@NamedQuery(
					name = "selectAllRelationResolvers",
					query = "select rr from RelationResolver rr"
			),
			@NamedQuery(
					name = "findRelationResolverByName",
					query = "select rr from RelationResolver rr where rr.name=:name"
			),

			// RelationTypes
			@NamedQuery(
					name = "selectAllRelationTypes",
					query = "select rt from RelationType rt"
			),
			
			// Transformers
			@NamedQuery(
					name = "selectAllTransformers",
					query = "select t from server.transformation.Transformer t"		
			),
			
			// Sessions
			@NamedQuery(
					name = "selectAllSessions",
					query = "select s from Session s"
			),

            // UiLanguage
            @NamedQuery(
					name = "selectAllUiLanguages",
					query = "select l from UiLanguage l"
			),
			@NamedQuery(
					name = "findUiLanguageByIsoCode",
					query = "select l from UiLanguage l where l.isoCode=:isoCode"
			),

			// Users
			@NamedQuery(
					name = "selectAllUsers",
					query = "select u from UserAccount u"
			),
			@NamedQuery(
					name = "findUserByName",
					query = "select u from UserAccount u where name=:name"
			),
			@NamedQuery(
					name = "findUserByNameAndPassword",
					query = "select u from UserAccount u where u.name=:name and u.pwd=:pwd"
			),
			
			@NamedQuery (
					name = "selectAllCustomTables",
					query = "select c from CustomTable c"
			)
		}
)
@Entity
@Table(name = "acls",
		uniqueConstraints = {@UniqueConstraint(columnNames={"name"})}
)
public class Acl implements Serializable{
	
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	@Id @GeneratedValue
	@Column(name = "id")
	private long id = 0;

	@Column(name = "name",
			length = Constants.NAME_LENGTH,
			nullable = false)
	private String name;
	
	@Column(name = "description",
			length = Constants.DESCRIPTION_SIZE,
			nullable = false)
	private String description;

	@Version
	@Column(name="obj_version")
	@SuppressWarnings("unused")
	private Long obj_version = 0L;
	
	@OneToMany(mappedBy = "acl",
			cascade = { CascadeType.PERSIST, CascadeType.REMOVE }
	)
	private Set<AclEntry> aclEntries = new HashSet<AclEntry>();
	
	@OneToMany(mappedBy = "acl",
			cascade = { CascadeType.PERSIST, CascadeType.REMOVE }
	)
	private Set<CustomTable> customTables= new HashSet<CustomTable>();
	
	private transient Map<User, List<AclEntry>> userEntries = new HashMap<User, List<AclEntry>>();
	
	public Acl (){	

	}

	public Acl(Map<String, String> cmd){
		name = cmd.get("name");
		description = cmd.get("description");
	}

	public Acl(String name, String description){
		this.name = name;
		this.description = description;
	}
	
	public long getId() {
		return id;
	}
	
	@SuppressWarnings("unused")
	private void setId(long id) {
		this.id = id;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Transient
	public Set<AclEntry> getAclEntries() {
		return aclEntries;
	}

	public void setAclEntries(Set<AclEntry> aclEntries) {
		this.aclEntries = aclEntries;
	}


	/**
	 * @return the customTables
	 */
	public Set<CustomTable> getCustomTables() {
		return customTables;
	}

	/**
	 * @param customTables the customTables to set
	 */
	public void setCustomTables(Set<CustomTable> customTables) {
		this.customTables = customTables;
	}

	/**
	 * Add the folder as XML Element "acl" to the parameter Element.
	 * @param root the root element to which the acl node will be added.
	 */
	public void toXmlElement(Element root){
		Element acl = root.addElement("acl");
		acl.addElement("id").addText(String.valueOf(getId()) );
		acl.addElement("name").addText( LocalMessage.loc(getName()));
        acl.addElement("sysName").addText(getName());
		acl.addElement("description").addText( LocalMessage.loc(getDescription()));
	}
	
	/**
	 * Cache the results of a query for the aclEntries a user has for this Acl.
	 * @param user the user whose AclEntries are being queried.
	 * @param em current EntityManager
	 * @return a List of AclEntries to which the user belongs.
	 */
	@SuppressWarnings("unchecked")
	public List<AclEntry> getUserEntries(User user, EntityManager em){
		if(userEntries.containsKey(user)){
			return userEntries.get(user);
		}
		else{
			Query q = em.createNamedQuery("selectAclEntriesByUser");
			q.setParameter("acl", this);
			q.setParameter("user", user);
			userEntries.put(user, (List<AclEntry>) q.getResultList()); 
			return userEntries.get(user);
		}	
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Acl)) return false;

        Acl acl = (Acl) o;

        if (description != null ? !description.equals(acl.description) : acl.description != null) return false;
//        if (name != null ? !name.equals(acl.name) : acl.name != null) return false;
        return !(name != null ? !name.equals(acl.name) : acl.name != null);

        }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    public String toString(){
        return "Acl #"+id+": "+name+" ("+description+")";
    }
}
