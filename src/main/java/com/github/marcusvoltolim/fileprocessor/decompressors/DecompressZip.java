package com.github.marcusvoltolim.fileprocessor.decompressors;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.commons.compress.utils.IOUtils;

/**
 * Classe responsavel por descompactar arquivos .zip em unica pasta, ou seja,
 * independente da hierarquia de pastas existens no .zip, todos os arquivos serao extraidos no mesmo nivel.
 */
public class DecompressZip extends AbstractDecompress {

    private static final Charset IBM_850 = Charset.forName("IBM850");

    public DecompressZip(final boolean ignoreFolder) {
        super(ignoreFolder);
    }

    @Override
    public File decompress(InputStream inputStream) throws IOException {
        final File outDir = getTempDir();

        try (final ZipInputStream zipInputStream = new ZipInputStream(inputStream, IBM_850)) {
            ZipEntry zipEntry;

            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                if (ignoreFolder && zipEntry.isDirectory()) {
                    zipInputStream.closeEntry();
                    continue;
                }

                final File newFile = newFile(outDir, zipEntry.getName());
                logging(newFile);

                try (final FileOutputStream out = new FileOutputStream(newFile)) {
                    IOUtils.copy(zipInputStream, out);
                }
                zipInputStream.closeEntry();
            }
        }

        return outDir;
    }

}