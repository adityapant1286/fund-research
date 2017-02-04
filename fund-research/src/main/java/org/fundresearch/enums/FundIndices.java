package org.fundresearch.enums;

public enum FundIndices {

    CODE(0), NAME(1), BENCHMARK_CODE(2);

    private int i;

    FundIndices(int i) {
        this.i = i;
    }

    public int index() {
        return i;
    }
}
