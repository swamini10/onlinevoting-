package com.onlinevoting.enums;

public enum VotingStatus {
    YES("Yes"),
    NO("No");

    private final String displayName;

    VotingStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

}