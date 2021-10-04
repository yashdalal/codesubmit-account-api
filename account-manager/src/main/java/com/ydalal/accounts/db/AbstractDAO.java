package com.ydalal.accounts.db;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.ydalal.accounts.utils.FileUtils;

import java.io.File;
import java.io.IOException;

/**
 * Abstract base class to pull out reusable logic and entities such as ObjectMapper,
 * ObjectWriter
 */
public abstract class AbstractDAO {
    final File file;
    public static final ObjectMapper mapper = new ObjectMapper();
    public static final ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());

    AbstractDAO() throws IOException {
        mapper.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, true);
        this.file = new File(getPath());
        initialize();
    }

    void initialize() throws IOException {
        if (FileUtils.fileIsEmpty(file)) {
            file.createNewFile();
        }
    }

    abstract String getPath();
}
