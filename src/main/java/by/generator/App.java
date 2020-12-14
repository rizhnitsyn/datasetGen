package by.generator;
import au.com.anthonybruno.Gen;
import au.com.anthonybruno.definition.FieldDefinition;
import au.com.anthonybruno.definition.StartDefinition;
import au.com.anthonybruno.settings.CsvSettings;
import by.generator.utils.GeneratorUtils;
import com.github.javafaker.Faker;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

import static java.lang.System.currentTimeMillis;
import static java.lang.System.setOut;

/**
 * Hello world!
 *
 */
public class App

{
    public static void main( String[] args ) throws IOException {
        StartDefinition generator = Gen.start();
        Table table = GeneratorUtils.jsonToObject("table_definition.json");

        table.generateDDL();

        GeneratorUtils.assignGeneratorType(table.getColumns());
        FieldDefinition gen = GeneratorUtils.generateColumns(table.getColumns(), generator);


        for (int i = 0; i < 1; i++) {
            long start1 = currentTimeMillis();
            gen
                    .generate(100000) //1000 rows will be generated
//                    .generate(20) //1000 rows will be generated
                    .asCsv(new CsvSettings(false))
                    .toFile(table.getTableName() + (i+1) + ".csv");
            long start2 = currentTimeMillis();
            System.out.println("loop n:" + (i + 1) + " " + (start2-start1)/1000);
        }
    }
}
