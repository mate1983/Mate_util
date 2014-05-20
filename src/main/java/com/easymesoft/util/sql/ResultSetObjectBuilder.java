package com.easymesoft.util.sql;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface ResultSetObjectBuilder {
    public abstract Object buildObj(ResultSet rs,Class objType) throws SQLException;
}
