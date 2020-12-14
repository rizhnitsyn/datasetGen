package by.generator;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
@NoArgsConstructor
public class Table {
    private String tableName;
    private ArrayList<Column> columns;
    private Boolean createDDL;
    private String description;
    private static final String SEPARATOR = System.lineSeparator();

    public void generateDDL() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append("CREATE TABLE ")
                .append(tableName)
                .append("(")
                .append(SEPARATOR);

        columns.forEach(col -> stringBuilder
                .append(col.getColName())
                .append(" ")
                .append(col.getColType())
                .append(setLength(col))
                .append(",")
                .append(SEPARATOR));
        stringBuilder
                .append(");");
        System.out.println(stringBuilder.toString());

    }

    private String setLength(Column column) {
        if (column.getColLength() == null) {
            return "";
        } else {
            return "(" + column.getColLength() + ")";
        }
    }

}
