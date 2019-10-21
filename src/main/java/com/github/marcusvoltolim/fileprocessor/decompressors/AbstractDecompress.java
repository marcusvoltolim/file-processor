package com.github.marcusvoltolim.fileprocessor.decompressors;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.Normalizer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import org.apache.tika.io.FilenameUtils;

public abstract class AbstractDecompress implements IDecompress {

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss - ");
    private static final Pattern PATTERN = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
    protected final Logger log;

    protected final boolean ignoreFolder;

    public AbstractDecompress(final boolean ignoreFolder) {
        this.ignoreFolder = ignoreFolder;
        this.log = Logger.getLogger(this.getClass().getName());
    }

    protected File getTempDir(String... prefixes) throws IOException {
        final String prefix = prefixes.length == 0 ? "" : prefixes[0];
        final Path path = Files.createTempDirectory(prefix + LocalDateTime.now().format(FORMATTER));
        return path.toFile();
    }

    protected File getTempFile(final File tempDir, final String extension) throws IOException {
        final Path path = Files.createTempFile(Paths.get(tempDir.getPath()), null, extension);
        final File file = path.toFile();
        file.deleteOnExit();
        return file;
    }

    /**
     * These method guards against writing files to the file system outside of the target folder. This vulnerability is called Zip Slip.
     */
    protected File newFile(File destDir, String name) throws IOException {
        File destFile = new File(destDir, removerAcentos(FilenameUtils.getName(name)));
        String destDirPath = destDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();

        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw new IOException("Entry is outside of the target dir: " + name);
        }

        return destFile;
    }

    private static String removerAcentos(String s) {
        final StringBuilder sb = new StringBuilder(Normalizer.normalize(s, Normalizer.Form.NFD));
        return PATTERN.matcher(sb).replaceAll("");
    }

    protected void logging(File newFile) {
        log.info("Unzipping to " + newFile.getAbsolutePath());
    }

}