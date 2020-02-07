package patterns.proxy;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PrintLogSon implements IPrintLog{
    @Override
    public void output(String str) {
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:MM:SS");
        System.err.println("日志打印时间：" + s.format(new Date()));
        System.err.println(str);
    }
}
