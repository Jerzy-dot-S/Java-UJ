import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class AnnotationBasedHelper implements SQLiteHelper {

    @Override
    public String toSQL(Object object, String tableName) {
        List<String> columns = new ArrayList<>();

        for (Field f : object.getClass().getFields()) {
            if (isSqlColumn(f)) {
                columns.add(buildColumnDefinition(f));
            }
        }

        StringBuilder sql = new StringBuilder();
        sql.append("CREATE TABLE ")
           .append(tableName)
           .append(" (\n");

        for (int i = 0; i < columns.size(); i++) {
            sql.append("  ")
               .append(columns.get(i));

            if (i < columns.size() - 1) {
                sql.append(",\n");
            }
        }

        sql.append("\n);");
        return sql.toString();
    }

    private boolean isSqlColumn(Field field) {
        return field.isAnnotationPresent(SQL.class)
                && resolveSqlType(field.getType()) != null;
    }

    private String buildColumnDefinition(Field field) {
        return field.getName() + " " + resolveSqlType(field.getType());
    }

    private String resolveSqlType(Class<?> javaType) {
        if (javaType == String.class) {
            return "TEXT";
        }

        if (javaType == int.class || javaType == Integer.class ||
            javaType == long.class || javaType == Long.class) {
            return "INTEGER";
        }

        if (javaType == float.class || javaType == Float.class ||
            javaType == double.class || javaType == Double.class) {
            return "REAL";
        }

        return null;
    }
}