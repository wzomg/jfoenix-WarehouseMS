package memento;

// 发起人，状态变化者
public class UserInfoOriginator {
    // 当前状态
    private String userid;
    private String userpwd;
    private String userphone;
    private String useraddress;

    // 保存到备忘录
    public Memento saveToMemento(){
        return new Memento(userid, userpwd,userphone,useraddress);
    }

    // 返回备忘录状态
    public void restoreFromMemento(Memento memento){
        this.userid = memento.getUserid();
        this.userpwd = memento.getUserpwd();
        this.userphone = memento.getUserphone();
        this.useraddress = memento.getUseraddress();
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
        return "UserInfoOriginator{" +
                "userid='" + userid + '\'' +
                ", userpwd='" + userpwd + '\'' +
                ", userphone='" + userphone + '\'' +
                ", useraddress='" + useraddress + '\'' +
                '}';
    }
}
