package drawable.tencent.com.factory.model.api;

/**
 * Created by Administrator on 2018/1/8 0008.
 */

public class RegiseModel {
    private String name;
    private String password;
    private String account;
    private String pishId;

    public RegiseModel(String password, String name, String account) {
      this(password,name,account,null);
    }

    public RegiseModel(String password, String name, String account, String pishId) {
        this.password = password;
        this.name = name;
        this.account = account;
        this.pishId = pishId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return account;
    }

    public void setPhone(String account) {
        this.account = account;
    }

    public String getPishId() {
        return pishId;
    }

    public void setPishId(String pishId) {
        this.pishId = pishId;
    }
}
