package org.fundresearch.util;

import org.fundresearch.exceptions.ApplicationException;

import java.io.Closeable;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This class provides utility functions for IO operations,
 * which will be used throughout the application.
 *
 * @author aditya
 *         Created by aditya on 02-Feb-17.
 */
public final class IOUtil {

    /**
     * Private constructor to prevent creating
     * instance outside of this class.
     */
    private IOUtil() {
    }

    /**
     * It reads all the lines of the given filePath and store it into an array.
     *
     * @param filePath - a path of the file to be read.
     * @return List if lines read from a file
     */
    public static List<String> readFileLines(String filePath) throws ApplicationException {

        List<String> lineList = new ArrayList<>(0);

        try (Stream<String> streams = Files.lines(Paths.get(filePath))) {

            lineList = streams.collect(Collectors.toList());

        } catch (IOException e) {
            throw new ApplicationException("Unable to read file");
        }
        return lineList;
    }

    /**
     * A generic function to close any input/output stream object
     * which are type of {@link Closeable}. Additionally, it prevents
     * throwing IOException to the caller by ignoring exception in the catch block.
     *
     * @param closeables - list of instances of type {@link Closeable}
     */
    public static void closeQuitely(Closeable... closeables) {
        for (Closeable closeable : closeables) {
            if (closeable != null) {
                try {
                    closeable.close();
                } catch (IOException e) {
                    // ignore
                }
            }
        }
    }
}
