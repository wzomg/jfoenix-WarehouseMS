package patterns.bridge;

// 文本文件日志
public class TextFileLog extends Log{
    public TextFileLog(ImpLog impLog) {
        super(impLog);
    }

    @Override
    public void write(String log) {
        getImpLog().execute(log);
    }
}
