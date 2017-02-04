package org.fundresearch.enums;

public enum ReturnIndices {

    CODE(0), DATE(1), RETURNS(2);

    private int i;

    ReturnIndices(int i) {
        this.i = i;
    }

    public int index() {
        return i;
    }
}
