package agilor.distributed.relational.data.exceptions;

import java.sql.SQLException;

/**
 * Created by LQ on 2015/12/31.
 */
public class SqlHandlerException extends Exception {

    private ExceptionTypes type = ExceptionTypes.NULL;

    public SqlHandlerException(ExceptionTypes type)
    {
        this.type = type;
    }

    public SqlHandlerException(Throwable e) {
        super(e);
        if (e instanceof SQLException) {
            SQLException sql_ex = (SQLException) e;

            switch (sql_ex.getErrorCode()) {
                case 1062:
                    this.type = ExceptionTypes.SQL_ONLY_UNIQUE;
                    break;
                case 1452:
                    this.type = ExceptionTypes.SQL_FOREIGN_KEY;
                    break;
                default:
                    System.out.println(sql_ex.getErrorCode());
            }
        }
    }

    @Override
    public String getMessage() {
        return "sql error :" + type;
    }


    public ExceptionTypes getType() {
        return type;
    }
}
