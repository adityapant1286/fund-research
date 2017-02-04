package org.fundresearch.models;

import org.fundresearch.enums.ReturnsType;

import java.math.BigDecimal;
import java.util.Date;

/**
 * FundReturn/BenchmarkReturn data model bean.
 */
public class Returns {

    private ReturnsType type;
    private String code;
    private Date date;
    private BigDecimal returns;

    public Returns() {
    }

    public Returns(ReturnsType type, String code, Date date, BigDecimal returns) {
        this.type = type;
        this.code = code;
        this.date = date;
        this.returns = returns;
    }

    public ReturnsType getType() {
        return type;
    }

    public void setType(ReturnsType type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public BigDecimal getReturns() {
        return returns;
    }

    public void setReturns(BigDecimal returns) {
        this.returns = returns;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Returns returns1 = (Returns) o;

        if (type != returns1.type) return false;
        if (code != null ? !code.equals(returns1.code) : returns1.code != null) return false;
        if (date != null ? !date.equals(returns1.date) : returns1.date != null) return false;
        return returns != null ? returns.equals(returns1.returns) : returns1.returns == null;

    }

    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 13 * result + (code != null ? code.hashCode() : 0);
        result = 17 * result + (date != null ? date.hashCode() : 0);
        result = 31 * result + (returns != null ? returns.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Returns{" +
                "type=" + type +
                ", code='" + code + '\'' +
                ", date=" + date +
                ", returns=" + returns +
                '}';
    }
}
