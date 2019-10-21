package com.github.marcusvoltolim.fileprocessor.decompressors;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public interface IDecompress {

    File decompress(final InputStream inputStream) throws IOException;

}
