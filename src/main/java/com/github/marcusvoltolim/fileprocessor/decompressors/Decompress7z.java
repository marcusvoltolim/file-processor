package com.github.marcusvoltolim.fileprocessor.decompressors;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.compress.archivers.sevenz.SevenZArchiveEntry;
import org.apache.commons.compress.archivers.sevenz.SevenZFile;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.compress.utils.SeekableInMemoryByteChannel;

public class Decompress7z extends AbstractDecompress {

    public Decompress7z(final boolean ignoreFolder) {
        super(ignoreFolder);
    }

    @Override
    public File decompress(final InputStream inputStream) throws IOException {
        final File dirOut = getTempDir();
        final SeekableInMemoryByteChannel inMemoryByteChannel = new SeekableInMemoryByteChannel(IOUtils.toByteArray(inputStream));

        try (final SevenZFile sevenZFile = new SevenZFile(inMemoryByteChannel)) {
            SevenZArchiveEntry sevenZipEntry;

            while ((sevenZipEntry = sevenZFile.getNextEntry()) != null) {
                if (ignoreFolder && sevenZipEntry.isDirectory()) {
                    continue;
                }

                final File newFile = newFile(dirOut, sevenZipEntry.getName());
                logging(newFile);

                try (final FileOutputStream out = new FileOutputStream(newFile)) {
                    final byte[] content = new byte[(int) sevenZipEntry.getSize()];
                    sevenZFile.read(content, 0, content.length);
                    out.write(content);
                }
            }
        }

        return dirOut;
    }

}
