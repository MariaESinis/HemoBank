package com.hemoglobank.api.repository;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Converte linhas do ResultSet em Map<coluna, valor>, na ordem das colunas.
 * Serve para qualquer função/view sem precisar de uma classe por consulta.
 */
final class JdbcRows {

    private JdbcRows() {}

    /** Lê TODAS as linhas restantes do ResultSet. */
    static List<Map<String, Object>> toList(ResultSet rs) throws SQLException {
        List<Map<String, Object>> rows = new ArrayList<>();
        while (rs.next()) {
            rows.add(currentRow(rs));
        }
        return rows;
    }

    /** Lê a linha em que o cursor está (rs.next() já foi chamado). */
    static Map<String, Object> currentRow(ResultSet rs) throws SQLException {
        ResultSetMetaData meta = rs.getMetaData();
        Map<String, Object> row = new LinkedHashMap<>();
        for (int i = 1; i <= meta.getColumnCount(); i++) {
            row.put(meta.getColumnLabel(i), normalize(rs.getObject(i)));
        }
        return row;
    }

    private static Object normalize(Object value) throws SQLException {
        if (value == null) {
            return null;
        }
        if (value instanceof java.sql.Array array) {
            return array.getArray();                       // text[] / int[] -> array Java
        }
        // json / jsonb / tipos próprios do Postgres chegam como PGobject: usa o texto deles
        if ("org.postgresql.util.PGobject".equals(value.getClass().getName())) {
            return value.toString();
        }
        return value;
    }
}
