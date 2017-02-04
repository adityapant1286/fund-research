package org.fundresearch.util;

import java.io.File;
import java.nio.file.Paths;

/**
 * Utility class for unit test.
 */
public class TestUtil {

    private static final String FILE_SEPERATOR = System.getProperty("file.separator");
    private static final String TEST_DATA_DIR = "testData";

    public static File getCurrentDirFile() {

        return new File(getCurrentDir());
    }

    public static String getCurrentDir() {
        return Paths.get("").toAbsolutePath().normalize().toString();
    }

    public static String getTestDataDir() {
        final StringBuilder sb = new StringBuilder(getCurrentDirFile().getPath());
        sb.append(FILE_SEPERATOR);
        sb.append(TEST_DATA_DIR);

        return sb.toString();
    }


}
