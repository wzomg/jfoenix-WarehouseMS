package patterns.prototype;

public class Goods implements Cloneable {
    private Integer gid;
    private String gName;
    private String gShelf;
    private int gCnt;
    private double gPrice;

    public Goods() {
    }

    public Goods(String gName, String gShelf, int gCnt, double gPrice) {
        this.gName = gName;
        this.gShelf = gShelf;
        this.gCnt = gCnt;
        this.gPrice = gPrice;
    }

    public Integer getGid() {
        return gid;
    }

    public void setGid(Integer gid) {
        this.gid = gid;
    }

    public String getgName() {
        return gName;
    }

    public void setgName(String gName) {
        this.gName = gName;
    }

    public String getgShelf() {
        return gShelf;
    }

    public void setgShelf(String gShelf) {
        this.gShelf = gShelf;
    }

    public int getgCnt() {
        return gCnt;
    }

    public void setgCnt(int gCnt) {
        this.gCnt = gCnt;
    }

    public double getgPrice() {
        return gPrice;
    }

    public void setgPrice(double gPrice) {
        this.gPrice = gPrice;
    }

    // 每次点击添加按钮之后就克隆一个货物类
    @Override
    public Object clone() {
        Goods goods = null;
        try {
            goods = (Goods) super.clone();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return goods;
    }

    @Override
    public String toString() {
        return "Goods{" +
                "gid=" + gid +
                ", gName='" + gName + '\'' +
                ", gShelf='" + gShelf + '\'' +
                ", gCnt=" + gCnt +
                ", gPrice=" + gPrice +
                '}';
    }
}
