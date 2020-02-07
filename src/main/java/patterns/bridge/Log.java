package patterns.bridge;

// 抽象日志
public  abstract class Log {
    private ImpLog impLog;

    public Log(ImpLog impLog) {
        super();
        this.impLog = impLog;
    }

    abstract public void write(String log);

    public ImpLog getImpLog() {
        return impLog;
    }

    public void setImpLog(ImpLog impLog) {
        this.impLog = impLog;
    }
}