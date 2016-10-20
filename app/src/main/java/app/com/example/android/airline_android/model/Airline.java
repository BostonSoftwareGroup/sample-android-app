package app.com.example.android.airline_android.model;

import java.io.Serializable;

public class Airline implements Serializable {
    private String code;
    private String defaultName;
    private String logoURL;
    private String name;
    private String phone;
    private String site;
    private String usName;

    public String getCode() {
        return code;
    }

    public String getDefaultName() {
        return defaultName;
    }

    public String getLogoUrl() {
        return logoURL;
    }

    public String getFormedLogoUrl() {
        return "https://a1.r9cdn.net" + logoURL;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public String getSite() {
        return site;
    }

    public String getUsName() {
        return usName;
    }
}
