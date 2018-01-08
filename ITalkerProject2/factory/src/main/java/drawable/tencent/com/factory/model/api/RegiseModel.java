package drawable.tencent.com.factory.model.api;

/**
 * Created by Administrator on 2018/1/8 0008.
 */

public class RegiseModel {
    private String name;
    private String password;
    private String phone;
    private String pishId;

    public RegiseModel(String password, String name, String phone) {
      this(password,name,phone,null);
    }

    public RegiseModel(String password, String name, String phone, String pishId) {
        this.password = password;
        this.name = name;
        this.phone = phone;
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
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPishId() {
        return pishId;
    }

    public void setPishId(String pishId) {
        this.pishId = pishId;
    }
}
