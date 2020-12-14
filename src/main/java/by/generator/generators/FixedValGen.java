package by.generator.generators;

import au.com.anthonybruno.generator.Generator;
import by.generator.Column;

public class FixedValGen implements Generator<String> {

    private String value;

    public FixedValGen(Column column) {
        this.value = column.getFixedVal();
    }

    @Override
    public String generate() {
        return value;
    }
}
