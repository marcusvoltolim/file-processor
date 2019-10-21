package com.github.marcusvoltolim.fileprocessor;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public final class ProcessorFile {

    private ProcessorFile() {
    }

    public static File decompress(final InputStream inputStream) throws IOException {
        return ProcessorFactory.getInstance().getDecompress().decompress(inputStream);
    }

    public static File compress(final List<File> files, final String mediaType) throws IOException {
        return ProcessorFactory.getInstance().getCompress(mediaType).compress(files);
    }

}