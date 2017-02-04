package org.fundresearch.enums;

public enum BenchmarkIndices {

    CODE(0), NAME(1);

    private int i;

    BenchmarkIndices(int i) {
        this.i = i;
    }

    public int index() {
        return i;
    }

}
