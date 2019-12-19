package memento;

// 备忘录，记录状态变化者的某一个时间点的状态
public class Memento {
    private String userid;
    private String userpwd;
    private String userphone;
    private String useraddress;

    public Memento(String userid, String userpwd, String userphone, String useraddress) {
        this.userid = userid;
        this.userpwd = userpwd;
        this.userphone = userphone;
        this.useraddress = useraddress;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUserpwd() {
        return userpwd;
    }

    public void setUserpwd(String userpwd) {
        this.userpwd = userpwd;
    }

    public String getUserphone() {
        return userphone;
    }

    public void setUserphone(String userphone) {
        this.userphone = userphone;
    }

    public String getUseraddress() {
        return useraddress;
    }

    public void setUseraddress(String useraddress) {
        this.useraddress = useraddress;
    }

    @Override
    public String toString() {
        return "Memento{" +
                "userid='" + userid + '\'' +
                ", userpwd='" + userpwd + '\'' +
                ", userphone='" + userphone + '\'' +
                ", useraddress='" + useraddress + '\'' +
                '}';
    }
}
