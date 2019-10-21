package com.github.marcusvoltolim.fileprocessor.decompressors;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.sun.javafx.PlatformUtil;
import org.apache.commons.compress.utils.IOUtils;

/**
 * Decompress all formats using external tool.
 * Under development
 */
public class DecompressAllFormatsUsingExternalTool extends AbstractDecompress {

    private static final String SEVEN_ZIP_WINDOWS = "C:\\Program Files\\7-Zip\\7z x ";
    private static final String SEVEN_ZIP_LINUX_MAC = "p7zip -d -f";
    private static final String UNRAR_LINUX_MAC = "unrar x";
    private static final String UNZIP_LINUX_MAC = "unzip";

    public DecompressAllFormatsUsingExternalTool(final boolean ignoreFolder) {
        super(ignoreFolder);
    }

    @Override
    public File decompress(InputStream inputStream) throws IOException {
        final File dirOut = getTempDir();
        final File tempFileRar = getTempFile(dirOut, ".rar");

        String command = null;
        try (FileOutputStream outFileRar = new FileOutputStream(tempFileRar)) {
            IOUtils.copy(inputStream, outFileRar);
        }

        if (PlatformUtil.isWindows()) {
            command = SEVEN_ZIP_WINDOWS + tempFileRar.getPath() + " -o" + dirOut.getPath();
        } else if (PlatformUtil.isLinux() || PlatformUtil.isMac()) {
            command = SEVEN_ZIP_LINUX_MAC + tempFileRar.getPath() + " -c" + dirOut.getPath();
        }

        if (command != null) {
            final Process process = Runtime.getRuntime().exec(command);
            try {
                process.waitFor();
            } catch (InterruptedException e) {
                throw new IOException(e.getMessage(), e);
            }
        }

        return dirOut;
    }

}