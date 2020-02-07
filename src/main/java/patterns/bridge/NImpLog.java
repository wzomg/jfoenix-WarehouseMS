package patterns.bridge;

// net平台
public class NImpLog extends ImpLog{
    @Override
    public void execute(String msg) {
        System.out.println("在Net平台上："+msg);
    }
}
