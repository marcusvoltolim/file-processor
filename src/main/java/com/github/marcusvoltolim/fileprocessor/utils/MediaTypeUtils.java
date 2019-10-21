package com.github.marcusvoltolim.fileprocessor.utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.apache.tika.Tika;
import org.apache.tika.detect.AutoDetectReader;
import org.apache.tika.mime.MediaType;

public final class MediaTypeUtils {

    public static final String ZIP = MediaType.APPLICATION_ZIP.toString();
    public static final String RAR = MediaType.application("x-rar-compressed").toString();
    public static final String SEVEN_ZIP = MediaType.application("x-7z-compressed").toString();
    private static final Tika TIKA = new Tika();

    private MediaTypeUtils() {
    }

    public static String getMediaType(final InputStream stream) throws IOException {
        return TIKA.detect(stream);
    }

    public static boolean isZip(final String mediaType) {
        return ZIP.equals(mediaType);
    }

    public static boolean isRar(final String mediaType) {
        return RAR.equals(mediaType);
    }

    public static boolean is7z(final String mediaType) {
        return SEVEN_ZIP.equals(mediaType);
    }

    public static Charset getCharset(final InputStream inputStream) throws IOException {
        try {
            final AutoDetectReader autoDetectReader = new AutoDetectReader(inputStream);
            inputStream.reset();
            return autoDetectReader.getCharset();
        } catch (Exception e) {
            throw new IOException("Falha obter Charset do arquivo", e);
        }
    }

}
