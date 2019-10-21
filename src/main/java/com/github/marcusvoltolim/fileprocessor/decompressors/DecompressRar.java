package com.github.marcusvoltolim.fileprocessor.decompressors;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import de.innosystec.unrar.Archive;
import de.innosystec.unrar.NativeStorage;
import de.innosystec.unrar.rarfile.FileHeader;
import org.apache.commons.compress.utils.IOUtils;

public class DecompressRar extends AbstractDecompress {

    public DecompressRar(final boolean ignoreFolder) {
        super(ignoreFolder);
    }

    @Override
    public File decompress(final InputStream inputStream) throws IOException {
        final File dirOut = getTempDir();
        final File tempFileRar = getTempFile(dirOut, ".rar");

        try (FileOutputStream outFileRar = new FileOutputStream(tempFileRar)) {
            inputStream.reset();
            IOUtils.copy(inputStream, outFileRar);
        }
        try (Archive archive = new Archive(new NativeStorage(tempFileRar))) {
            final List<FileHeader> list = archive.getFileHeaders();

            for (FileHeader header : list) {
                if (ignoreFolder && header.isDirectory()) {
                    continue;
                }
                final File newFile = newFile(dirOut, getFileName(header));
                logging(newFile);

                try (FileOutputStream out = new FileOutputStream(newFile)) {
                    archive.extractFile(header, out);
                }
            }

        } catch (Exception e) {
            throw new IOException(e);
        } finally {
            tempFileRar.delete();
        }

        return dirOut;
    }

    private String getFileName(FileHeader header) {
        return header.getFileNameW().length() > 0 ? header.getFileNameW() : header.getFileNameString();
    }

}