package com.github.marcusvoltolim.fileprocessor.decompressors;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import net.sf.sevenzipjbinding.SevenZip;
import net.sf.sevenzipjbinding.SevenZipNativeInitializationException;
import org.apache.tika.io.IOUtils;

public class DecompressAllFormatsUsingSevenZipLib extends AbstractDecompress {

    public DecompressAllFormatsUsingSevenZipLib(final boolean ignoreFolder) {
        super(ignoreFolder);
        try {
            SevenZip.initSevenZipFromPlatformJAR();
            log.info("7-Zip-JBinding library was initialized");
        } catch (SevenZipNativeInitializationException e) {
            log.severe(e.getMessage());
        }
    }

    @Override
    public File decompress(InputStream inputStream) throws IOException {
        final File dirTempOut = getTempDir();
        final File tempFile = getTempFile(dirTempOut, "");
        IOUtils.copy(inputStream, new FileOutputStream(tempFile));

        final Map<String, byte[]> extract = ExtractItemsStandardCallback.extract(tempFile, ignoreFolder);

        extract.forEach((fileName, bytes) -> {
            final Path path = Paths.get(dirTempOut.getAbsolutePath(), fileName);
            try {
                Files.write(path, bytes);
            } catch (IOException e) {
                log.severe(e.getMessage());
            }
        });

        return dirTempOut;
    }

}
