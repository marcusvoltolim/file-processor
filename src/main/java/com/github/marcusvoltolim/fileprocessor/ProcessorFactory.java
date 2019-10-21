package com.github.marcusvoltolim.fileprocessor;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

import com.github.marcusvoltolim.fileprocessor.compress.CompressZip;
import com.github.marcusvoltolim.fileprocessor.compress.ICompress;
import com.github.marcusvoltolim.fileprocessor.decompressors.Decompress7z;
import com.github.marcusvoltolim.fileprocessor.decompressors.DecompressAllFormatsUsingSevenZipLib;
import com.github.marcusvoltolim.fileprocessor.decompressors.DecompressRar;
import com.github.marcusvoltolim.fileprocessor.decompressors.DecompressZip;
import com.github.marcusvoltolim.fileprocessor.decompressors.IDecompress;
import com.github.marcusvoltolim.fileprocessor.utils.MediaTypeUtils;

public class ProcessorFactory {

    private static final Logger LOG = Logger.getLogger(ProcessorFactory.class.getName());
    private static final String RAR5_MAGIC_NUMBER = "526172211A070100";

    private ProcessorFactory() {

    }

    public static ProcessorFactory getInstance() {
        return new ProcessorFactory();
    }

    public IDecompress getDecompress() {
        return new DecompressAllFormatsUsingSevenZipLib(true);
    }

    /**
     * Implementation specified for each extension.
     */
    @SuppressWarnings("unused")
    public IDecompress getDecompressSpecified(final InputStream inputStream, final boolean ignoreFolder) throws IOException {
        final String mediaType = MediaTypeUtils.getMediaType(inputStream);
        if (MediaTypeUtils.isZip(mediaType)) {
            loggingDecompress(DecompressZip.class, ".zip");
            return new DecompressZip(ignoreFolder);
        } else if (MediaTypeUtils.is7z(mediaType)) {
            loggingDecompress(Decompress7z.class, ".7z");
            return new Decompress7z(ignoreFolder);
        } else if (MediaTypeUtils.isRar(mediaType)) {
            if (isRar5(inputStream)) {
                loggingDecompress(DecompressAllFormatsUsingSevenZipLib.class, ".rar5");
                return new DecompressAllFormatsUsingSevenZipLib(ignoreFolder);
            } else {
                loggingDecompress(DecompressRar.class, ".rar");
                return new DecompressRar(ignoreFolder);
            }
        } else {
            LOG.warning("No file-specific decompressors found: " + mediaType + " " +
                ". It will be using the generic: " + DecompressAllFormatsUsingSevenZipLib.class.getName());
            return new DecompressAllFormatsUsingSevenZipLib(ignoreFolder);
        }
    }

    public ICompress getCompress(String mediaType) {
        if (MediaTypeUtils.isZip(mediaType)) {
            return new CompressZip();
        } else {
            throw new IllegalArgumentException("Compression not supported by file: " + mediaType);
        }
    }

    private static void loggingDecompress(final Class classe, final String extension) {
        LOG.info("Decompress: " + classe.getName() + " found for file: " + extension);
    }

    private static boolean isRar5(final InputStream inputStream) throws IOException {
        final byte[] magicNumber = new byte[8];
        inputStream.read(magicNumber, 0, 8);
        return RAR5_MAGIC_NUMBER.equalsIgnoreCase(bytesToHex(magicNumber));
    }

    private static String bytesToHex(byte[] bytes) {
        final StringBuilder sb = new StringBuilder();
        for (byte aByte : bytes) {
            sb.append(String.format("%02x", aByte));
        }
        return sb.toString();
    }

}