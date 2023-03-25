package com.random.hopper.filters;

public class TrinaryWorldFilterParameters {
    private String description;
    private boolean allowTrue;
    private boolean allowFalse;

    public TrinaryWorldFilterParameters(String description, boolean allowTrue, boolean allowFalse) {
        this.description = description;
        this.allowTrue = allowTrue;
        this.allowFalse = allowFalse;
    }

    public String getDescription() {
        return description;
    }

    public boolean getAllowTrue() {
        return allowTrue;
    }

    public boolean getAllowFalse() {
        return allowFalse;
    }

    @Override
    public String toString() {
        return description;
    }

    public String toStringDebug() {
        return String.format("(%s) %s %s", description, allowTrue, allowFalse);
    }
}
