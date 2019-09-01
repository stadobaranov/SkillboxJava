package core.reporting;

import core.CurrencyMap;

public class MovementReportRecord {
    private final String serviceName;
    private final CurrencyMap sums;

    public MovementReportRecord(String serviceName, CurrencyMap sums) {
        this.serviceName = serviceName;
        this.sums = sums;
    }

    public String getServiceName() {
        return serviceName;
    }

    public CurrencyMap getSums() {
        return sums;
    }
}
