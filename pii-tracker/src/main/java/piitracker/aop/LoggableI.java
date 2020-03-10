package piitracker.aop;

// any logged DTO should implement this and have toString return toSafeString()
public interface LoggableI {
    // should never include PII
    public String toSafeString();

    // may include PII
    public String toFullString();
}
