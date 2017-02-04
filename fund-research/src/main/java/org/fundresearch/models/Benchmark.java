package org.fundresearch.models;

/**
 * Benchmark data model bean
 */
public class Benchmark {

    private String code;
    private String name;

    public Benchmark() {
    }

    public Benchmark(String code, String name) {
        this.code = code;
        this.name = name;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Benchmark benchmark = (Benchmark) o;

        if (code != null ? !code.equals(benchmark.code) : benchmark.code != null) return false;
        return name != null ? name.equals(benchmark.name) : benchmark.name == null;

    }

    @Override
    public int hashCode() {
        int result = code != null ? code.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Benchmark{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
