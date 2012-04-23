package temp.dao;

import server.AclEntry;
import server.Group;
import server.User;

import javax.persistence.NoResultException;
import java.util.List;

public interface GroupDAO extends GenericDAO<Group, Long> {
    /**
     * Add a user to this group. <em>This creates a CmnGroupUser object!</em>
     *
     * @param userID  ID of the user to add to this group
     * @param groupID ID of the group to add the user to
     * @return id of the CmnGroupUser object
     */
    Long addToUser(Long userID, Long groupID);

    /**
     * add a group to a target acl. <em>This creates an AclEntry object!</em>
     *
     * @param aclID
     * @param groupID
     * @return AclEntry object
     */
    AclEntry addToAcl(Long aclID, Long groupID);

    void removeFromAcl(Long aclID, Long groupID);

    /**
     * @return CmnGroup    The personal group of this user.
     */
    Group findByUser(User user) throws NoResultException;

    /**
     * Find all child groups of a given parent group. Does not descent into sub groups.
     * @param parent the group whose child groups you want to fetch.
     * @return a List of group objects which share the given group as a parent.
     */
    List<Group> findAllByParent(Group parent);

    /**
     * Find all users of a group. Does not descent into sub groups.
     * @param group The group whose users you want to fetch.
     * @return a list of UserAccount objects which share the given group.
     */
    List<User> findAllUsers(Group group);

    /**
     * Find a group by a given name.
     * @param name the name of the group you are searching for.
     * @return the group - or null if it does not exist.
     */
    Group findByName(String name);

    void removeUserFromGroup(Long userId, Long groupId);
}
