package org.fundresearch.util;

import org.fundresearch.analyser.FundAnalyser;
import org.fundresearch.models.Performance;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This class provides utility functions for IO operations, conversions and formatting,
 * validation, cleanup which will be used throughout the application.
 *
 * @author aditya
 *         Created by aditya on 02-Feb-17.
 * @see FundAnalyser
 * @see Performance
 */
public final class AppUtil {

    /**
     * Line separater character found in each line of input file.
     */
    public static final String LINE_SEPARATOR = ",";
    /**
     * This is a default date firmat fr displaying date. (dd/MM/yyyy)
     */
    private static final String DEFAULT_DATE_FORMAT = "dd/MM/yyyy";
    /**
     * This will be used to parse or format a date.
     */
    private static final SimpleDateFormat SDF = new SimpleDateFormat();
    private static final String OUTPUT_FORMAT = "%1$15s %2$15s %3$15s %4$20s %5$15s %6$15s";

    /**
     * Private constructor to prevent creating
     * instance outside of this class.
     */
    private AppUtil() {
    }

// Conversion and formatting utilities //

    /**
     * Tries to parse an input string to {@link Date} type.
     *
     * @param dateStr - string representation of a date
     * @return Date parsed using an input, else null
     */
    public static Date toDate(String dateStr) {

        try {
            SDF.applyPattern(DEFAULT_DATE_FORMAT);
            return SDF.parse(dateStr);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Unable to parse to date " + dateStr, e);
        }
    }

    /**
     * Converts an input date into default date format. i.e. dd/MM/yyyy
     *
     * @param input - date input passed as arguments
     * @return Formatted string representation of an input date
     */
    public static String getDefaultFormattedDate(Date input) {
        return getFormattedDate(input, DEFAULT_DATE_FORMAT);
    }

    /**
     * It round-half-down the input into 2 scale.
     *
     * @param input - big decimal input value
     * @return Returns decimal value rounded to 2 scale
     */
    public static BigDecimal roundToTwoScale(BigDecimal input) {
        return roundToScale(input, 2);
    }

    /**
     * It round-half-down the input into user specified scale.
     *
     * @param input - big decimal input value
     * @param scale - required scale for rounding
     * @return Returns decimal value rounded to user specified scale
     */
    public static BigDecimal roundToScale(BigDecimal input, int scale) {

        if (input == null) throw new IllegalArgumentException("Invalid descimal value");

        return input.setScale(scale, BigDecimal.ROUND_HALF_DOWN);
    }

    /**
     * Converts an input date into user defined date format.
     * This also checks the input is valid and throws exception in case
     * illegal parameters passed as input.
     *
     * @param input   - date input passed as arguments
     * @param pattern - user defined date format
     * @return Formatted string representation of an input date
     */
    public static String getFormattedDate(Date input, String pattern) {
        if (input == null)
            throw new IllegalArgumentException("Invalid input date: " + input);
        if (pattern == null || pattern.length() == 0)
            throw new IllegalArgumentException("Invalid pattern: " + pattern);

        SDF.applyPattern(pattern);
        return SDF.format(input);
    }

    private static String getHeaders() {
        String headers = String.format(OUTPUT_FORMAT, "FundName", "Date", "Excess"
                , "OutPerformance", "Return", "Rank");
        headers += "\n";
        headers += String.format(OUTPUT_FORMAT, "------------", "-----------", "----------"
                , "---------------", "----------", "--------");
        headers += "\n";
        return headers;
    }

// IO utilities //

    /**
     * Builder pattern.<br>
     * This builds the paths specified as arguments.
     *
     * @param paths - an array of multiple directories and
     *              single file path
     * @return a path built from arguments
     */
    public static String buildPath(String... paths) {
        final StringBuilder sb = new StringBuilder();

        for (String path : paths) {
            sb.append(path);
            if (path.lastIndexOf(".csv") == -1)
                sb.append(File.separator);
        }
        return sb.toString();
    }

    /**
     * It reads all the lines of the given filePath and store it into an array.
     * It uses Java 8 features to read contents from file and filter them.
     *
     * @param filePath - a path of the file to be read.
     * @return List if lines read from a file
     */
    public static List<String> readFileLines(String filePath) {

        List<String> lineList = new ArrayList<>(0);

        if (!isFileValid(filePath)) {
            System.out.printf("%s not exists.\n", filePath);
            return lineList;
        }

        try (Stream<String> streams = Files.lines(Paths.get(filePath))) {

            lineList = streams
                    .filter(line -> line.trim().length() > 0)
                    .collect(Collectors.toList());

        } catch (IOException e) {
            throw new IllegalArgumentException("Unable to read file " + filePath, e);
        }
        return lineList;
    }


    /**
     * It converts an object list into list of strings.
     * Using Java 8 IO feature, it writes content into a file.
     * If the directory is null or does not exists, it throws runtime exception.
     *
     * @param list     - a list of {@link Performance} objects to be write
     * @param dirPath  - Destination directory path to write a file
     * @param fileName - (optional) Either null or the user defined file name
     *                 for the output report
     */
    public static void writeToFile(List<Performance> list, String dirPath, String fileName) {

        if (!isFileValid(dirPath))
            throw new IllegalArgumentException("Invalid directory path: " + dirPath);

        String filePath = dirPath + File.separator + "monthlyOutPerformance.csv";
        System.out.printf("Writing to %s", filePath);

        final StringBuilder sb = new StringBuilder();

        List<String> linesToWrite = list.stream()
                .map(performance -> {

                    sb.delete(0, sb.length());

                    sb.append(String.format(OUTPUT_FORMAT
                            , performance.getFundName()
                            , getDefaultFormattedDate(performance.getDate())
                            , roundToTwoScale(performance.getExcess()).toString()
                            , performance.getOutPerformanceText()
                            , roundToTwoScale(performance.getReturns()).toString()
                            , performance.getRank().toString()));

//                    sb.append(performance.getFundName()); sb.append("\t");
//                    sb.append(getDefaultFormattedDate(performance.getDate())); sb.append("\t");
//                    sb.append(roundToTwoScale(performance.getExcess())); sb.append("\t");
//                    sb.append(performance.getOutPerformanceText()); sb.append("\t");
//                    sb.append(roundToTwoScale(performance.getReturns())); sb.append("\t");
//                    sb.append(performance.getRank().intValue());
                    sb.append("\n");
                    return sb.toString();
                })
                .collect(Collectors.toList());

        // Headers

        linesToWrite.add(0, getHeaders());

        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(filePath))) {

            linesToWrite.forEach(line -> {
                try {
                    writer.write(line);
                } catch (IOException e) {
                    throw new IllegalArgumentException("Error: unable to write " + line, e);
                }
            });

        } catch (IOException e) {
            throw new IllegalArgumentException("Unable to write file " + filePath, e);
        }
    }

// Validation utilities //

    /**
     * It checks whether the file is exists or not.
     *
     * @param filePath - filePath to check
     * @return true is file exists, else false
     */
    public static boolean isFileValid(String filePath) {

        if (filePath == null) return false;

        File f = new File(filePath);
        return f.exists();
    }

    /**
     * This function validates whether the file name is valid
     * CSV file or not, by its extension.
     *
     * @param fileName - CSV file name
     * @return true if the file name is valid CSV file, else false
     */
    public static boolean isValidFileName(String fileName) {

        return (fileName == null) || fileName.lastIndexOf(".csv") >= 0;
    }

// Cleanup utilities //

    /**
     * It performs the cleanup for array list or set collection passed as input
     * which is-a type {@link Collection}
     *
     * @param collections - array of collections to be cleaned up
     */
    public static void cleanupCollections(Collection... collections) {

        for (Collection c : collections)
            if (c != null) {
                c.clear();
                c = null;
            }
    }

    /**
     * It performs the cleanup for any map passed as input
     * which is-a type {@link Map}
     *
     * @param maps - array of collections to be cleaned up
     */
    public static void cleanupMap(Map... maps) {

        for (Map c : maps)
            if (c != null) {
                c.clear();
                c = null;
            }
    }

}
