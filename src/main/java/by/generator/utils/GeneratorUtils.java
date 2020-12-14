package by.generator.utils;

import au.com.anthonybruno.definition.FieldDefinition;
import au.com.anthonybruno.definition.StartDefinition;
import au.com.anthonybruno.generator.Generator;
import by.generator.Column;
import by.generator.generators.DistinctValueGen;
import by.generator.generators.FixedValGen;
import by.generator.generators.SeqGen;
import by.generator.Table;
import com.github.javafaker.Faker;
import com.github.javafaker.service.FakeValuesService;
import com.github.javafaker.service.RandomService;
import com.opencsv.CSVWriter;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.stream.Collectors;

public class GeneratorUtils {

    private static final java.io.File FILE_DIRECTORY = new java.io.File("src" + java.io.File.separator + "main" + File.separator + "resources");
    private static final FakeValuesService fakeValuesService = new FakeValuesService(new Locale("en-GB"), new RandomService());
    private static final Faker faker = Faker.instance();
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

    public static Table jsonToObject(String fileName) throws IOException {
        File file = new File(FILE_DIRECTORY, fileName);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(file, Table.class);
    }


    public static FieldDefinition generateColumns(ArrayList<Column> columns, StartDefinition generator){
        FieldDefinition fieldDefinition = generator.addField(columns.get(0).getColName(), columns.get(0).getGenerator());
        for (int i = 1; i < columns.size(); i++) {
            Column column = columns.get(i);
            fieldDefinition.addField(column.getColName(), column.getGenerator());
        }
        return fieldDefinition;
    }

    public static void assignGeneratorType(ArrayList<Column> columns){
        columns.forEach(GeneratorUtils::setGenerator);
    }

    private static Generator<Long> setPKGenerator(Column column) {
        return new SeqGen(column.getStartFrom());
    }

    private static Generator<String> setFixedValGenerator(Column column) {
        return new FixedValGen(column);
    }

    private static void setGenerator(Column column) {
        switch (column.getColType()) {
            case NUMBER : {
                column.setGenerator(setRegExpGenerator(column));
//                faker.number().randomDouble(1000,0,100)
            }
            case INTEGER: {
                if (column.getPattern() == null) {
                    Integer colLength = column.getColLength() == null ? column.getColType().getDefaultLength() : column.getColLength();
                    column.setPattern("#{" + colLength +"}" );
                }
                if (column.getColumnClass() != null && column.getDistValCnt() != null) {
                    column.setGenerator(setDistValGenerator(column, setClassGenerator(column)));
                }
                else if (column.getColumnClass() != null) {
                    column.setGenerator(setClassGenerator(column));
                }
                else if (column.getDistValCnt() != null) {
                    column.setGenerator(setDistValGenerator(column, setRegExpGenerator(column)));
                }
                else {
                    column.setGenerator(setRegExpGenerator(column));
                }
                break;
            }
            case VARCHAR:
            {
                if (column.getPattern() == null) {
                    Integer colLength = column.getColLength() == null ? column.getColType().getDefaultLength() : column.getColLength();
                    column.setPattern("?{" + colLength +"}" );
                }
                if (column.getColumnClass() != null && column.getDistValCnt() != null) {
                    column.setGenerator(setDistValGenerator(column, setClassGenerator(column)));
                }
                else if (column.getColumnClass() != null) {
                    column.setGenerator(setClassGenerator(column));
                }
                else if (column.getDistValCnt() != null) {
                    column.setGenerator(setDistValGenerator(column, setRegExpGenerator(column)));
                }
                else {
                    column.setGenerator(setRegExpGenerator(column));
                }
                break;
            }
            case BOOLEAN:
            {
                column.setGenerator(setBoolGenerator());
                break;
            }
            case DATE:
            {
                if (column.getColumnClass() != null && column.getDistValCnt() != null) {
                    column.setGenerator(setDistValGenerator(column, setClassGenerator(column)));
                }
                else if (column.getColumnClass() != null) {
                    column.setGenerator(setClassGenerator(column));
                }
                break;
            }
        }
    }

    private static Generator<String> setRegExpGenerator(Column column) {
            return () -> fakeValuesService.regexify(column.getPattern());
    }

    private static Generator<Boolean> setBoolGenerator() {
        return () -> (new Random()).nextBoolean();
    }

    private static Generator<String> setDistValGenerator(Column column, Generator<String> generator) {
        DistinctValueGen<String> gen = new DistinctValueGen<>();
        gen.setDistValCnt(column.getDistValCnt());
        gen.setGenerator(generator);
        return gen;
    }

    private static Generator setClassGenerator(Column column) {
        switch (column.getColumnClass() ) {
            case "name" : {
                return () -> faker.name().fullName();
            }
            case "company_name" : {
                return () -> faker.company().name();
            }
            case "address":
            {
                return () -> faker.address().fullAddress();
            }
            case "zip":
            {
                return () -> faker.address().zipCode();
            }
            case "city":
            {
                return () -> faker.address().cityName();
            }
            case "country":
            {
                return () -> faker.address().country();
            }
            case "date":
            {
                return () -> dateFormat.format(faker.date().birthday());
            }
            case "sequence":
            {
                return setPKGenerator(column);
            }
            case "fixedValue":
            {
                return setFixedValGenerator(column);
            }
            default :
                return setRegExpGenerator(column);
        }
    }


//    public static void writeDataLineByLine(String filePath, Table table, Integer size)
//    {
//        File file = new File(filePath);
//        try {
//            FileWriter outputFile = new FileWriter(file);
//            CSVWriter writer = new CSVWriter(outputFile);
//
//            for (int i = 0; i < size; i++) {
//                String[] row = table.getColumns().stream()
//                        .map(column -> column.getGenerator().generate().toString())
//                        .toArray(String[]::new);
//                writer.writeNext(row);
//            }
//            writer.close();
//        }
//        catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//    }

//    public static void writeDataAtOnce(String filePath, Table table, Integer size)
//    {
//        File file = new File(filePath);
//        try {
//            FileWriter outputFile = new FileWriter(file);
//            CSVWriter writer = new CSVWriter(outputFile);
//            // create a List which contains String array
//            List<String[]> data = new ArrayList<String[]>();
//            for (int i = 0; i < size; i++) {
//                String[] row = table.getColumns().stream()
//                        .map(column -> column.getGenerator().generate().toString())
//                        .toArray(String[]::new);
//                data.add(row);
//            }
//            writer.writeAll(data);
//            writer.close();
//        }
//        catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//    }

}
