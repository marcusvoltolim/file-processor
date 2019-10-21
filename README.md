# file-processor
Utilitary Decompress and Compress Files (included RAR5)

I initially implemented the extraction for the following extensions:
* .zip (DecompressZip) -> Native Java;
* .rar (DecompressRar) -> com.github.axet: java-unrar: 1.7.0-8
* .7z (Decompress7z) -> org.apache.commons: commons-compress: 1.19

However it was not compatible with RAR5, searching I found lib: sevenzipjbinding-16.02-2.01beta.jar.

I implemented from it DecompressAllFormatsUsingSevenZipLib, which can extract all the most popular compression formats.

PS: My english is bad
