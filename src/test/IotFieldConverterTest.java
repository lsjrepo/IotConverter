package test;

import org.junit.Test;
import util.IotFieldConverter;

import static org.junit.Assert.*;

/**
 * Created by lsj on 17-4-1.
 */
public class IotFieldConverterTest {

    @Test
    public void entity2bytes() throws Exception {
        DeviceInfo mDeviceInfo = new DeviceInfo();
        mDeviceInfo.setModel("LSJ1234");
        mDeviceInfo.setVersionCode(98);
        byte[] bytes = IotFieldConverter.entity2bytes(mDeviceInfo);
        assertArrayEquals(new byte[]{83,74,49,50,51,52,0,98},bytes);
    }

    @Test
    public void bytes2entity() throws Exception {
        byte[] bytes = new byte[]{83,74,49,50,51,52,0,98};
        DeviceInfo mDeviceInfo = new DeviceInfo();
        mDeviceInfo = IotFieldConverter.bytes2entity(mDeviceInfo,bytes);
        String s=mDeviceInfo.getModel();

    }

}