package drawable.tencent.com.factory.model.api;

/**
 * Created by Administrator on 2018/1/10 0010.
 */

public class LoginModel {
    private String password;
    private String account;
    private String pishId;

    public LoginModel(String account, String password) {
        this.account = account;
        this.password = password;
    }

    public LoginModel(String password, String account, String pishId) {
        this.password = password;
        this.account = account;
        this.pishId = pishId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPishId() {
        return pishId;
    }

    public void setPishId(String pishId) {
        this.pishId = pishId;
    }
}
