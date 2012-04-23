package temp.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.*;

import javax.persistence.Query;
import java.util.List;

public class GroupDAOHibernate extends GenericHibernateDAO<Group, Long> implements GroupDAO {

    private final Logger log = LoggerFactory.getLogger("server.dao.GroupDAOHibernate");
    static final DAOFactory daoFactory = DAOFactory.instance(DAOFactory.HIBERNATE);

    @Override
    public Long addToUser(Long userID, Long groupID) {

        UserDAO userDAO = daoFactory.getUserDAO(getSession());
        User user = userDAO.get(userID);
        Group group = get(groupID);

        GroupUser gu = new GroupUser(user, group);
        getSession().persist(gu);
//		getSession().flush();
        return gu.getId();
    }

    @Override
    public void delete(Long id) {
        Group group = get(id);
        makeTransient(group);
        flush();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Group> list() {
        Query q = getSession().createQuery("select g from CmnGroup g");
        return q.getResultList();
    }

    @Override
    public Group get(Long id) {
        return getSession().find(Group.class, id);
    }

    @Override
    public Group findByUser(User user) {
        Query q = getSession().createQuery("select g from CmnGroup g, CmnGroupUser gu where g.is_user=true and gu.user.id=:id and g.id=gu.group.id");
        q.setParameter("id", user.getId());
        return (Group) q.getSingleResult();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<Group> findAllByParent(Group group) {
        Query q = getSession().createQuery("select g from CmnGroup g where g.parent=:parent");
        q.setParameter("parent", group);
        return q.getResultList();
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<User> findAllUsers(Group group) {
        Query q = getSession().createQuery("select gu.user from CmnGroupUser gu where gu.group=:group");
        q.setParameter("group", group);
        return q.getResultList();
    }

    @Override
    public AclEntry addToAcl(Long aclID, Long groupID) {
        AclDAO aclDAO = daoFactory.getAclDAO(getSession());
        Acl acl = aclDAO.get(aclID);
        Group group = get(groupID);
        log.debug("acl: " + acl + " group: " + group);
        AclEntry ae = new AclEntry(acl, group);
        log.debug("aclEntry: " + ae + " consists of: " + ae.getGroup().getId() + " and " + ae.getAcl().getId());
        getSession().persist(ae);

        return ae;
    }

    @Override
    public void removeFromAcl(Long aclID, Long groupID) {
        Query q = getSession().createQuery("select ae from AclEntry ae where ae.acl.id=:aclid and ae.group.id=:groupid");
        q.setParameter("aclid", aclID);
        q.setParameter("groupid", groupID);
        AclEntry ae = (AclEntry) q.getSingleResult();
        getSession().remove(ae);
    }

    @Override
    public Group findByName(String name) {
        Query q = getSession().createQuery("select g from CmnGroup g where name=:name");
        q.setParameter("name", name);
        List<Group> groups = q.getResultList();
        return groups.isEmpty() ? null : groups.get(0);
    }

    @Override
    public void removeUserFromGroup(Long userId, Long groupId) {
        Group group = get(groupId);
        UserDAO userDao = daoFactory.getUserDAO(getSession());
        User user = userDao.get(userId);
        Query q = getSession().createNamedQuery("selectGroupUserByUserAndGroup");
        q.setParameter("group", group);
        q.setParameter("user", user);
        GroupUser gu = (GroupUser) q.getSingleResult();
        user.getGroupUsers().remove(gu);
        group.getGroupUsers().remove(gu);
        getSession().remove(gu);
        getSession().flush();
    }


}
