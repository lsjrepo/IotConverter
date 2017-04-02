# IotConverter

**Intruduction:**
IotFieldConverter is a tool which is mainly used in device management.
**usage:**
In the Iot development.we often  encounter such a scenario:

![image](https://github.com/roninCoderJ/IotConverter/raw/master/Pic/a.png)

byte array convert to object :
2 steps:
1.Use IotField annotation:

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
2.Call bytes2entity() method:

   
    public void entity2bytes() throws Exception {
        DeviceInfo mDeviceInfo = new DeviceInfo();
        mDeviceInfo.setModel("LSJ1234");
        mDeviceInfo.setVersionCode(98);
        byte[] bytes = IotFieldConverter.entity2bytes(mDeviceInfo);
        assertArrayEquals(new byte[]{83,74,49,50,51,52,0,98},bytes);
    }
    
 object convert to byte array:
  

    
     public void bytes2entity() throws Exception {
        byte[] bytes = new byte[]{83,74,49,50,51,52,0,98};
        DeviceInfo mDeviceInfo = new DeviceInfo();
        mDeviceInfo = IotFieldConverter.bytes2entity(mDeviceInfo,bytes);
        String s=mDeviceInfo.getModel();

    }
    
