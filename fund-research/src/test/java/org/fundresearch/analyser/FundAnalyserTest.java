package org.fundresearch.analyser;

import org.fundresearch.util.AppUtil;
import org.fundresearch.util.TestUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Fund and Benchmark data analyser test class.
 */
public class FundAnalyserTest {

    private static final String testDataDir = TestUtil.getTestDataDir();
    private FundAnalyser fundAnalyser;

    @Before
    public void init() {
    }

    @After
    public void cleanUp() {
    }

    @Test
    public void testMonthlyOutPerformance() {
        String fundPath = AppUtil.buildPath(testDataDir, "fund.csv");
        String benchmarkPath = AppUtil.buildPath(testDataDir, "benchmark.csv");
        String fundReturnPath = AppUtil.buildPath(testDataDir, "fundReturnSeries.csv");
        String benchmarkReturnPath = AppUtil.buildPath(testDataDir, "benchReturnSeries.csv");

        fundAnalyser = new FundAnalyser(fundPath, benchmarkPath, fundReturnPath, benchmarkReturnPath);

        fundAnalyser.generateMonthlyOutPerformance(testDataDir, "monthlyOutPerformance.csv");
    }

}
