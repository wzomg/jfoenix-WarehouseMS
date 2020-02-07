package patterns.bridge;

// 数据库日志
public class DataBaseLog extends Log{

    public DataBaseLog(ImpLog impLog) {
        super(impLog);
    }

    @Override
    public void write(String log) {
        getImpLog().execute("数据库日志：" + log);
    }
}
