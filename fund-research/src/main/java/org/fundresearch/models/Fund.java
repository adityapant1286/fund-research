package org.fundresearch.models;

/**
 * Fund data model bean.
 */
public class Fund {

    private String code;
    private String name;
    private String benchmarkCode;

    public Fund() {
    }

    public Fund(String code, String name, String benchmarkCode) {
        this.code = code;
        this.name = name;
        this.benchmarkCode = benchmarkCode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBenchmarkCode() {
        return benchmarkCode;
    }

    public void setBenchmarkCode(String benchmarkCode) {
        this.benchmarkCode = benchmarkCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Fund fund = (Fund) o;

        if (code != null ? !code.equals(fund.code) : fund.code != null) return false;
        if (name != null ? !name.equals(fund.name) : fund.name != null) return false;
        return benchmarkCode != null ? benchmarkCode.equals(fund.benchmarkCode) : fund.benchmarkCode == null;

    }

    @Override
    public int hashCode() {
        int result = code != null ? code.hashCode() : 0;
        result = 13 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (benchmarkCode != null ? benchmarkCode.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Fund{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", benchmarkCode='" + benchmarkCode + '\'' +
                '}';
    }
}
