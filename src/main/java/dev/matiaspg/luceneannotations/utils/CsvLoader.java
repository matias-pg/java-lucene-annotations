package dev.matiaspg.luceneannotations.utils;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

public class CsvLoader {
    public static Iterable<CSVRecord> load(Reader reader) throws IOException {
        BufferedReader fileReader = new BufferedReader(reader);
        CSVFormat format = CSVFormat.Builder.create().setHeader().setSkipHeaderRecord(true).build();
        CSVParser csvParser = new CSVParser(fileReader, format);

        return csvParser.getRecords();
    }
}
