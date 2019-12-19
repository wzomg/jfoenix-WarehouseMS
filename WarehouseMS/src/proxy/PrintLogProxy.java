package proxy;

public class PrintLogProxy implements IPrintLog {

    private IPrintLog iPrintLog;

    public PrintLogProxy(IPrintLog iPrintLog) {
        this.iPrintLog = iPrintLog;
    }

    public void setiPrintLog(IPrintLog iPrintLog) {
        this.iPrintLog = iPrintLog;
    }

    @Override
    public void output(String str) {
        iPrintLog.output(str);
    }
}
