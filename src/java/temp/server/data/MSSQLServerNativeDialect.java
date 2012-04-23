package temp.server.data;

import org.hibernate.HibernateException;
import org.hibernate.dialect.SQLServerDialect;

import java.sql.Types;

public class MSSQLServerNativeDialect extends SQLServerDialect {
    public MSSQLServerNativeDialect() {
        super();
        registerColumnType(Types.VARCHAR, "nvarchar($l)");
        registerColumnType(Types.CLOB, "nvarchar(max)");
    }

   public String getTypeName(int code, int length, int precision, int scale) throws HibernateException {
       if(code != 2005) {
           return super.getTypeName(code, length, precision, scale);
       } else {
           return "ntext";
       }
   }
}
