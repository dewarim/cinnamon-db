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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.io.Serializable;


/**
 * Member is the intermediate class to associate users and groups.
 * @author ingo
 * 
 */

@Entity
@Table(name = "group_users")
public class GroupUser implements Serializable {

    private transient Logger log = LoggerFactory.getLogger(this.getClass());

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;
	
	@Id
    @GeneratedValue
	@Column(name = "id")
	private long id;
	
	@ManyToOne
	@JoinColumn(name = "user_id",
				nullable = false)
	private User user;
	
	@ManyToOne()
	@JoinColumn(name = "group_id",
			nullable = false)
	private Group group;

	@Version
	@Column(name="obj_version")
	@SuppressWarnings("unused")
	private Long obj_version = 0L;
	
	public GroupUser(){	
	}

	public GroupUser(User user, Group group){
		this.user = user;
		this.group = group;
		
		// Guarantee referential integrity
		user.getGroupUsers().add(this);
		group.getGroupUsers().add(this);
	}
	
	public Group getGroup() {
		return group;
	}

	public void setGroup(Group group) {
		this.group = group;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String toString(){
		return "CmnGroupUser: UserAccount: "+user.getId()+" CmnGroup:"+group.getId();
	}
	
	public long getId() {
		return id;
	}

	@SuppressWarnings("unused")
	private void setId(long id) {
		this.id = id;
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GroupUser)) return false;

        GroupUser groupUser = (GroupUser) o;
        if (group != null ? !group.equals(groupUser.group) : groupUser.group != null) return false;
        if (user != null ? !user.equals(groupUser.user) : groupUser.user != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = user != null ? user.hashCode() : 0;
        result = 31 * result + (group != null ? group.hashCode() : 0);
        return result;
    }
}
