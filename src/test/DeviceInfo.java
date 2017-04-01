package test;

import annotations.IotField;

/**
 * Created by lsj on 17-4-1.
 */
public class DeviceInfo {

    @IotField(position = 0,length = 6)
    private String model;

    @IotField(position = 6,length = 2)
    private int versionCode;

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }
}
