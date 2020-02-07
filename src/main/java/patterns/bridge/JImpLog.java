package patterns.bridge;

// java平台
public class JImpLog extends ImpLog {
    @Override
    public void execute(String msg) {
        System.err.println("在java平台上：" + msg);
    }
}
