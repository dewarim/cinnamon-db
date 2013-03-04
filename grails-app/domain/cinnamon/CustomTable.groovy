package cinnamon

import cinnamon.global.Constants


/**
 * The customtables are used in CmnServer to get jdbc-connection URLs for
 * tables and databases with individual customer data.
 * <p/>
 * This Hibernate class exists so that
 * a) the customtables-table is documented.
 * b) Hibernate can create an empty table when used with hibernate.hbm2ddl.auto=update or create.
 *
 * @author Ingo Wiarda
 */

class CustomTable implements Serializable {

    static constraints = {
        name unique: true, size: 1..Constants.NAME_LENGTH
        connstring(size: 1..512)
        jdbcDriver(size: 1..128)
    }

    static mapping = {
        cache true
        table('customtables')
        version 'obj_version'
    }
    
    String name
    String connstring
    Acl acl
    String jdbcDriver

    public CustomTable() {

    }

    // TODO: determine which constructors are needed.
    public CustomTable(String name, String connstring, String jdbcDriver, Acl acl) {
        this.name = name;
        this.connstring = connstring;
        this.acl = acl;
        this.jdbcDriver = jdbcDriver;
    }

    public CustomTable(Map<String, String> cmd) {
        name = cmd.get("name");
        connstring = cmd.get("connstring");
        jdbcDriver = cmd.get("jdbc_driver");
        acl = Acl.get(new Long(cmd.get("acl_id")));
    }

    boolean equals(o) {
        if (this.is(o)) return true
        if (!(o instanceof CustomTable)) return false

        CustomTable that = (CustomTable) o

        if (acl != that.acl) return false
        if (connstring != that.connstring) return false
        if (jdbcDriver != that.jdbcDriver) return false
        if (name != that.name) return false

        return true
    }

    int hashCode() {
        int result
        result = (name != null ? name.hashCode() : 0)
        return result
    }
}
