package org.fundresearch.util;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Utility test class.
 */
public class AppUtilTest {

    private static final String testDataDir = TestUtil.getTestDataDir();

    @Test
    public void testFileNotExists() {
        String filePath = AppUtil.buildPath(testDataDir, "notExists.csv");
        List<String> fileLines = AppUtil.readFileLines(filePath);
        assertNotNull(fileLines);
        assertTrue(fileLines.size() == 0);
    }

    @Test
    public void testFileExists() {
        String filePath = AppUtil.buildPath(testDataDir, "fund.csv");
        List<String> fileLines = AppUtil.readFileLines(filePath);
        assertNotNull(fileLines);
        assertFalse(fileLines.size() == 0);
        assertTrue(fileLines.size() > 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRoundingInputNull() {
        AppUtil.roundToTwoScale(null);

        AppUtil.roundToScale(null, 2);
    }

    @Test
    public void testRounding() {
        BigDecimal twoScale = AppUtil.roundToTwoScale(new BigDecimal(1.108745));
        System.out.println(twoScale);
        assertEquals(twoScale.doubleValue(), 1.11d);
        assertTrue(twoScale.floatValue() == 1.11f);

        twoScale = AppUtil.roundToTwoScale(new BigDecimal(1.108745));

        assertNotSame(twoScale, new BigDecimal(1.11));
        assertFalse(twoScale.equals(1.11));

        BigDecimal threeScale = AppUtil.roundToScale(new BigDecimal(1.108745), 3);
        System.out.println(threeScale);
        assertEquals(threeScale.doubleValue(), 1.109d);
        assertTrue(threeScale.floatValue() == 1.109f);

        threeScale = AppUtil.roundToScale(new BigDecimal(1.108745), 3);

        assertNotSame(threeScale, new BigDecimal(1.109));
        assertFalse(threeScale.equals(1.109));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToDateException() {
        Date date = AppUtil.toDate("02/June/2016");
    }

    @Test
    public void testToDate() {

        Date date = AppUtil.toDate("02/02/2016");

        assertNotNull(date);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDefaultFormattedDateNull() {

        String formattedDate = AppUtil.getFormattedDate(new Date(), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFormattedDateNull() {

        String formattedDate = AppUtil.getDefaultFormattedDate(new Date());

        String formattedDate1 = AppUtil.getFormattedDate(new Date(), null);
    }

    @Test
    public void testDefaultFormattedDate() {

        Calendar cal = Calendar.getInstance();
        String formattedDate = AppUtil.getDefaultFormattedDate(cal.getTime());

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(datePartToString(cal.get(Calendar.DATE)));
        stringBuilder.append("/");
        stringBuilder.append(datePartToString(cal.get(Calendar.MONTH) + 1));
        stringBuilder.append("/");
        stringBuilder.append(cal.get(Calendar.YEAR));

        assertNotNull(formattedDate);
        assertEquals("Invalid date", stringBuilder.toString(), formattedDate);

        stringBuilder.delete(0, stringBuilder.length());

        stringBuilder.append(cal.get(Calendar.DATE));
        stringBuilder.append("/");
        stringBuilder.append(cal.get(Calendar.MONTH));
        stringBuilder.append("/");
        stringBuilder.append(cal.get(Calendar.YEAR));

        assertNotSame("Valid date", stringBuilder.toString(), formattedDate);
    }

    @Test
    public void testFormattedDate() {
        Calendar cal = Calendar.getInstance();
        int month = 2;
        cal.set(2016, month - 1, 2);

        String ddmmyyyy = AppUtil.getFormattedDate(cal.getTime(), "dd/MM/yyyy");
        assertNotNull(ddmmyyyy);
        assertEquals("Invalid date", "02/02/2016", ddmmyyyy);

        month = 10;
        cal.set(2016, month - 1, 2);
        String ddmmmyyyy = AppUtil.getFormattedDate(cal.getTime(), "dd/MMM/yyyy");
        assertNotNull(ddmmmyyyy);
        assertEquals("Invalid date", "02/Oct/2016", ddmmmyyyy);
    }

    private String datePartToString(int datePart) {

        return (datePart + "").length() == 1 ? ("0" + datePart) : (datePart + "");
    }
}
