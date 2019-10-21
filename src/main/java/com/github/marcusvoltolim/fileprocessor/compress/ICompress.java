package com.github.marcusvoltolim.fileprocessor.compress;

import java.io.File;
import java.io.IOException;
import java.util.List;

public interface ICompress {

    File compress(List<File> files) throws IOException;

}
