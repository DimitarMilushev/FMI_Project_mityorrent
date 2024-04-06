package main.java.repository;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.*;

abstract class CSVRepository {
    protected final File tableFile;
    protected final CSVFormat format;
    protected CSVRepository(Class<? extends Enum<?>> headers, File tableFile) throws IOException {
        this.format = CSVFormat.DEFAULT
                .builder()
                .setHeader(headers)
                .setSkipHeaderRecord(true)
                .build();
        this.tableFile = tableFile;
        this.ensureFile();
    }

    private void ensureFile() throws IOException {
        if (tableFile.createNewFile()) {
            initHeaders();
            System.out.println("Created table file " + tableFile.getName());
        }
    }

    private void initHeaders() throws IOException {
        final CSVFormat withHeaders = format.builder().setSkipHeaderRecord(false).build();
        try (CSVPrinter printer = new CSVPrinter(new FileWriter(tableFile), withHeaders)) {
            // Printing an empty record just to initialize the file with headers
            printer.println();
        }
    }

    /**
     * Removes all the file contents.
     *
     * @throws IOException
     */
    protected void drop() throws IOException {
        try (final var writer = new FileWriter(tableFile)) {
            writer.write("");
        }
        initHeaders();
    }
}
