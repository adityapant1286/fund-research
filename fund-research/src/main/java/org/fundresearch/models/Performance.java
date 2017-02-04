package org.fundresearch.models;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Performance data model bean.
 */
public class Performance {

    private String fundName;
    private Date date;
    private BigDecimal excess;
    private String outPerformanceText;
    private BigDecimal returns;
    private Integer rank;

    public Performance() {
    }

    public Performance(String fundName, Date date, BigDecimal excess, String outPerformanceText, BigDecimal returns, Integer rank) {
        this.fundName = fundName;
        this.date = date;
        this.excess = excess;
        this.outPerformanceText = outPerformanceText;
        this.returns = returns;
        this.rank = rank;
    }

    public String getFundName() {
        return fundName;
    }

    public void setFundName(String fundName) {
        this.fundName = fundName;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public BigDecimal getExcess() {
        return excess;
    }

    public void setExcess(BigDecimal excess) {
        this.excess = excess;
    }

    public String getOutPerformanceText() {
        return outPerformanceText;
    }

    public void setOutPerformanceText(String outPerformanceText) {
        this.outPerformanceText = outPerformanceText;
    }

    public BigDecimal getReturns() {
        return returns;
    }

    public void setReturns(BigDecimal returns) {
        this.returns = returns;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Performance that = (Performance) o;

        if (!fundName.equals(that.fundName)) return false;
        if (!date.equals(that.date)) return false;
        if (!excess.equals(that.excess)) return false;
        if (outPerformanceText != null ? !outPerformanceText.equals(that.outPerformanceText) : that.outPerformanceText != null)
            return false;
        if (!returns.equals(that.returns)) return false;
        return rank != null ? rank.equals(that.rank) : that.rank == null;

    }

    @Override
    public int hashCode() {
        int result = fundName.hashCode();
        result = 31 * result + date.hashCode();
        result = 31 * result + excess.hashCode();
        result = 31 * result + (outPerformanceText != null ? outPerformanceText.hashCode() : 0);
        result = 31 * result + returns.hashCode();
        result = 31 * result + (rank != null ? rank.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Performance{" +
                "fundName='" + fundName + '\'' +
                ", date=" + date +
                ", excess=" + excess +
                ", outPerformanceText='" + outPerformanceText + '\'' +
                ", returns=" + returns +
                ", rank=" + rank +
                '}';
    }
}
