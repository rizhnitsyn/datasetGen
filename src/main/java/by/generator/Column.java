package by.generator;

import au.com.anthonybruno.generator.Generator;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Column {
    private String colName;
    private ColTypes colType;
    private Integer colLength;
    private String constraint;
    private String pattern;
    private String fixedVal;
    private String columnClass;
    private Integer distValCnt;
    private Integer startFrom = 1;
    private Generator generator;
}
