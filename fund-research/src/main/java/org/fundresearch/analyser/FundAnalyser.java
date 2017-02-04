package org.fundresearch.analyser;

import org.fundresearch.enums.BenchmarkIndices;
import org.fundresearch.enums.FundIndices;
import org.fundresearch.enums.ReturnIndices;
import org.fundresearch.enums.ReturnsType;
import org.fundresearch.models.Benchmark;
import org.fundresearch.models.Fund;
import org.fundresearch.models.Performance;
import org.fundresearch.models.Returns;
import org.fundresearch.util.AppUtil;

import java.math.BigDecimal;
import java.util.*;

/**
 * It has one parameterize constructor which takes 4 files path.<br>
 * Caller needs to invoke generateMonthlyOutPerformance() by passing
 * destination directory path.<br>
 * <p>
 * This analyser performs following operations<br>
 * <ul>
 * <li>Extract Fund data from Fund CSV file</li>
 * <li>Extract Fund Return data from Fund ReturnCSV file</li>
 * <li>Extract Benchmark Return data from Benchmark Return CSV file</li>
 * <li>Calculate Excess</li>
 * <li>Calculate Performance</li>
 * <li>Generate monthly performance</li>
 * </ul>
 *
 * @author aditya
 * @see Fund
 * @see Benchmark
 * @see Returns
 * @see Performance
 * @see AppUtil
 * <br>
 * Created by aditya on 02-Feb-17.
 */
public class FundAnalyser {

    private String fundCsvPath;
    private String benchmarkCsvPath;
    private String fundReturnCsvPath;
    private String benchmarkReturnCsvPath;

    /**
     * Parameterize constructor
     * <ul>
     * <li>Fund CSV path</li>
     * <li>Benchmark CSV path - (optional: can be null)</li>
     * <li>Fund Return CSV path</li>
     * <li>Benchmark Return CSV path</li>
     * </ul>
     *
     * @param fundCsvPath            - path of a fund csv file
     * @param benchmarkCsvPath       - path of a benchmark csv file
     * @param fundReturnCsvPath      - path of a fund return csv file
     * @param benchmarkReturnCsvPath - path of a benchmark return csv file
     */
    public FundAnalyser(String fundCsvPath, String benchmarkCsvPath,
                        String fundReturnCsvPath, String benchmarkReturnCsvPath) {
        this.fundCsvPath = fundCsvPath;
        this.benchmarkCsvPath = benchmarkCsvPath;
        this.fundReturnCsvPath = fundReturnCsvPath;
        this.benchmarkReturnCsvPath = benchmarkReturnCsvPath;
    }

    /**
     * This function extracts various data from the input files.<br>
     * It calculates Excess and Performance.<br>
     * Sorts the by date and then by rank for per monthly report.<br>
     * Lastly, it writes this data in to a file.
     *
     * @param destinationDirectoryPath - A directory path where the report file should be store.
     * @param outputFileName           - (optional) Either null or the user defined file name
     *                                 for the output report. <b>Default: monthlyOutPerformance.csv</b>
     * @see AppUtil
     * @see Fund
     * @see Benchmark
     * @see Returns
     * @see Performance
     */
    public void generateMonthlyOutPerformance(String destinationDirectoryPath, String outputFileName) {

        if (!AppUtil.isFileValid(destinationDirectoryPath))
            throw new IllegalArgumentException("Directory path not exists: " + destinationDirectoryPath);

        if (!AppUtil.isValidFileName(outputFileName))
            throw new IllegalArgumentException("Not a valid CSV file name: " + outputFileName);

        Map<String, Fund> fundMap =
                extractFundData(AppUtil.readFileLines(fundCsvPath));

        List<Returns> fundReturnsData =
                extractFundReturnsData(AppUtil.readFileLines(fundReturnCsvPath));

        Map<Date, Returns> benchmarkReturnsData =
                extractBenchmarkReturnsData(AppUtil.readFileLines(benchmarkReturnCsvPath));

        Map<Date, List<Performance>> excessAndPerformance =
                calculateExcessAndPerformance(fundReturnsData, benchmarkReturnsData, fundMap);

        List<Performance> performanceList = new ArrayList<>();

        excessAndPerformance
                .entrySet()
                .stream()
                .forEach(e -> {

                    // Sort by rank
                    List<Performance> value = e.getValue();
                    value.sort((o1, o2) -> o2.getReturns().compareTo(o1.getReturns()));

                    final int[] rank = {1};
                    value.forEach(p -> p.setRank(rank[0]++));

                    performanceList.addAll(value);
                });

        AppUtil.cleanupMap(excessAndPerformance);
        // write to a file
        AppUtil.writeToFile(performanceList, destinationDirectoryPath, outputFileName);
    }

    /**
     * It iterates fund return list, from each fund it retrieves date.<br>
     * Using this date it retrieves corresponding Benchmark from
     * already populated benchmark return map.<br>
     * It calculates excess and out performance.<br>
     * Populates these values into monthly performance tree map sorted
     * based on date (key) in descending order.
     *
     * @param fundReturns      - a list of fund return objects
     * @param benchmarkReturns - a map of benchmark return objects
     * @param fundMap          - a map of fund objects
     * @return a monthly performance {@link Map}
     */
    private Map<Date, List<Performance>> calculateExcessAndPerformance(List<Returns> fundReturns,
                                                                       Map<Date, Returns> benchmarkReturns,
                                                                       Map<String, Fund> fundMap) {

        // Tree map with descending order Lambda expressions
        Map<Date, List<Performance>> monthlyPerformanceMap = new TreeMap<>((o1, o2) -> o2.compareTo(o1));

        fundReturns.forEach(fundReturn -> {
            Date fundReturnDate = fundReturn.getDate();
            Returns benchmarkReturn = benchmarkReturns.get(fundReturnDate);
            if (benchmarkReturn == null) return;
            BigDecimal excess = calculateExcess(fundReturn.getReturns(), benchmarkReturn.getReturns());
            String performance = calculatePerformance(excess);
            String fundName = fundMap.get(fundReturn.getCode()).getName();

            if (!monthlyPerformanceMap.containsKey(fundReturnDate) || monthlyPerformanceMap.get(fundReturnDate) == null) {
                monthlyPerformanceMap.put(fundReturnDate, new ArrayList<>());
            }
            monthlyPerformanceMap
                    .get(fundReturnDate)
                    .add(new Performance(fundName, fundReturnDate, excess, performance, AppUtil.roundToTwoScale(fundReturn.getReturns()), 0));
        });

        AppUtil.cleanupCollections(fundReturns);
        AppUtil.cleanupMap(benchmarkReturns);
        AppUtil.cleanupMap(fundMap);


        return monthlyPerformanceMap;
    }

    /**
     * Calculates the excess by using following formula.<br>
     * {@code (Excess = Fund return - Benchmark return) }
     *
     * @param fundReturn      - decimal value of a Fund return
     * @param benchmarkReturn - decimal value of a Benchmark return
     * @return Two scale calculates excess value
     */
    private BigDecimal calculateExcess(BigDecimal fundReturn, BigDecimal benchmarkReturn) {

        BigDecimal excess = fundReturn.subtract(benchmarkReturn);
        return AppUtil.roundToTwoScale(excess);
    }

    /**
     * It calculates the performance values based on the calculated excess.<br>
     *
     * @param excess - calculated excess value (rounded to two scale)
     * @return 1. 'outPerformed', if excess is greater than 1. <br>
     * 2. 'underPerformed', if excess is less than -1. <br>
     * 3. nothing otherwise
     */
    private String calculatePerformance(BigDecimal excess) {

        if (excess.intValue() <= -1) return "Under Performed";

        if (excess.intValue() >= 1) return "Out Performed";

        return " ";
    }

    /**
     * It extracts the benchmark data from each line and creates an object {@link Benchmark}
     * Each line contains comma separated following fields<br>
     * | Benchmark Code | Benchmark Name |
     *
     * @param fileLines - lines read from benchmark csv file
     * @return A set of benchmark objects
     */
    private Set<Benchmark> extractBenchmarkData(List<String> fileLines) {

        final Set<Benchmark> set = new LinkedHashSet<>(0);

        fileLines.forEach(line -> {

            String[] split = line.split(AppUtil.LINE_SEPARATOR);
            if (split.length == 2)
                set.add(new Benchmark(split[BenchmarkIndices.CODE.index()].trim(),
                        split[BenchmarkIndices.NAME.index()].trim()));
        });

        // Clean up the list of read lines from file to free resources.
        // Thus can be reuse.
        AppUtil.cleanupCollections(fileLines);

        return set;
    }

    /**
     * It extracts the fund data from each line and creates an object {@link Benchmark}
     * Each line contains comma separated following fields<br>
     * | Fund Code | Fund Name | Benchmark Code |
     *
     * @param fileLines - lines read from fund csv file
     * @return A map of fund objects where key=fundCode, value=fundObject
     */
    private Map<String, Fund> extractFundData(List<String> fileLines) {

        final Map<String, Fund> map = new LinkedHashMap<>(0);

        fileLines.forEach(line -> {

            String[] split = line.split(AppUtil.LINE_SEPARATOR);
            if (split.length == 3)
                map.put(split[FundIndices.CODE.index()].trim(),
                        new Fund(split[FundIndices.CODE.index()].trim(),
                                split[FundIndices.NAME.index()].trim(),
                                split[FundIndices.BENCHMARK_CODE.index()].trim()));
        });

        // Clean up the list of read lines from file to free resources.
        // Thus can be reuse.
        AppUtil.cleanupCollections(fileLines);

        return map;
    }

    /**
     * It extracts the returns data of Fund from each line and creates an object {@link Returns}
     * Each line contains comma separated following fields<br>
     * | Code | Date | Return |
     *
     * @param returnLines - lines read from fund csv file
     * @return A list of Fund Returns objects. This list is sorted by date in descending order.
     */
    private List<Returns> extractFundReturnsData(List<String> returnLines) {
        final List<Returns> list = new ArrayList<>();

        returnLines.forEach(line -> {
            String[] split = line.split(AppUtil.LINE_SEPARATOR);

            if (split.length == 3)
                list.add(new Returns(ReturnsType.FUND,
                        split[ReturnIndices.CODE.index()],
                        AppUtil.toDate(split[ReturnIndices.DATE.index()]),
                        new BigDecimal(split[ReturnIndices.RETURNS.index()])));
        });

        AppUtil.cleanupCollections(returnLines);
        Comparator<Returns> byDateDesc = (d1, d2) -> d2.getDate().compareTo(d1.getDate());
        Collections.sort(list, byDateDesc);

        return list;
    }

    /**
     * It extracts the returns data of Benchmark from each line and creates an object {@link Returns}
     * Each line contains comma separated following fields<br>
     * | Code | Date | Return |
     *
     * @param returnLines - lines read from fund csv file
     * @return A map of Benchmark Returns objects
     */
    private Map<Date, Returns> extractBenchmarkReturnsData(List<String> returnLines) {
        final Map<Date, Returns> map = new TreeMap<>((o1, o2) -> o2.compareTo(o1));

        returnLines.forEach(line -> {
            String[] split = line.split(AppUtil.LINE_SEPARATOR);

            if (split.length == 3) {
                Date date = AppUtil.toDate(split[ReturnIndices.DATE.index()]);
                map.put(date,
                        new Returns(ReturnsType.BENCHMARK,
                                split[ReturnIndices.CODE.index()],
                                date,
                                new BigDecimal(split[ReturnIndices.RETURNS.index()])));
            }
        });
        AppUtil.cleanupCollections(returnLines);
        return map;
    }

}
