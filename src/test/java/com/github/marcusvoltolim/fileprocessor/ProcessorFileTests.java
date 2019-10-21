package com.github.marcusvoltolim.fileprocessor;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProcessorFileTests {


    @Test
    void testDecompressZip() throws IOException {
        File dirOut = ProcessorFile.decompress(getFile("go-janeiro-2019.zip"));

        assertTrue(dirOut.isDirectory());
        assertEquals(3, getAmountFiles(dirOut));
    }

    private BufferedInputStream getFile(String path) {
        return new BufferedInputStream(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream(path)));
    }

    private int getAmountFiles(File dirOut) {
        return Objects.requireNonNull(dirOut.list()).length;
    }

    @Test
    void testDecompressRar() throws IOException {
        final File dirOut = ProcessorFile.decompress(getFile("go-janeiro-2017.rar"));

        assertTrue(dirOut.isDirectory());
        assertEquals(3, getAmountFiles(dirOut));
    }

    @Test
    void testDecompressRar5() throws IOException {
        final File dirOut = ProcessorFile.decompress(getFile("go-janeiro-2018-RAR5.rar"));

        assertTrue(dirOut.isDirectory());
        assertEquals(3, getAmountFiles(dirOut));
    }

    @Test
    void testDecompress7Z() throws IOException {
        final File dirOut = ProcessorFile.decompress(getFile("go-janeiro-2018.7z"));

        assertTrue(dirOut.isDirectory());
        assertEquals(3, getAmountFiles(dirOut));
    }

}
