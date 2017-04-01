package util;

import annotations.IotField;
import exceptions.ConvertException;
import exceptions.FieldException;

import java.lang.reflect.Field;
import java.util.*;

/**
 * Created by lsj on 17-4-1.
 */
public class IotFieldConverter {
    private static  class IotFeildValue{
        private int position;
        private byte[] value;

        public IotFeildValue(int position, byte[] value) {
            this.position = position;
            this.value = value;
        }

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        public byte[] getValue() {
            return value;
        }

        public void setValue(byte[] value) {
            this.value = value;
        }
    }
    /**
     * 对象转换为字节数组
     *
     * */
    public static byte[] entity2bytes(Object entity)throws IllegalAccessException,  ConvertException, FieldException{
        if (entity==null){
            throw new IllegalArgumentException("entity参数不可以为空");
        }
        List<IotFeildValue> valueList=new ArrayList<IotFeildValue>();
        Field[] fields=entity.getClass().getDeclaredFields();
        boolean hasField=false;
        for(Field field:fields){
            field.setAccessible(true);
            IotField iotField=field.getAnnotation(IotField.class);
            if (iotField!=null){
                hasField=true;
                int position=iotField.position();
                int length=iotField.length();
                Object  val=field.get(entity);
                valueList.add(IotFieldConverter.object2IotFieldValue(val, position, length));
            }
        }
        if (!hasField){
            throw new FieldException("当前的对象不支持转换");
        }
        Collections.sort(valueList, new Comparator<IotFeildValue>() {
            @Override
            public int compare(IotFeildValue o1, IotFeildValue o2) {
                Integer id1=o1.getPosition();
                Integer id2=o2.getPosition();

                return id1.compareTo(id2);
            }
        });
        byte[] current = new byte[0];
        for (IotFeildValue value : valueList) {
            current = byteMerger(current, value.getValue());
        }
        return current;

    }
    /**
     * 字节数组转化为对象
     *
     * */
    public static <T> T bytes2entity(T entity, byte[] bytes) throws IllegalAccessException, ConvertException, FieldException{

        if (entity == null) {
            throw new IllegalArgumentException("entity不能为null");
        }
        if (bytes == null) {
            throw new IllegalArgumentException("bytes不能为null");
        }

        Field[] fields = entity.getClass().getDeclaredFields();
        boolean hasRkField = false;
        for (Field field : fields) {
            // 抑制Java的访问控制检查
            field.setAccessible(true);
            IotField mRkField = field.getAnnotation(IotField.class);
            if (mRkField != null) {
                hasRkField = true;

                int position = mRkField.position();
                int length = mRkField.length();

                if ((position + length) > bytes.length) {
                    throw new ConvertException("字节数组长度不够不能填充属性" + field.getName());
                }
                byte[] value = Arrays.copyOfRange(bytes, position, position + length);
                if (value.length > 0) {
                    // 获取属性的类型
                    String type = field.getType().toString();

                    if (type.endsWith("String")) {

                        field.set(entity, new String(value));

                    } else if (type.endsWith("long") || type.endsWith("Long")) {

                        field.setLong(entity, ByteConvert.bytesToLong(adjustBytes(value,8)));

                    } else if (type.endsWith("int") || type.endsWith("Integer")) {

                        field.setInt(entity, ByteConvert.bytesToInt(adjustBytes(value,4)));

                    } else if (type.endsWith("short") || type.endsWith("Short")) {

                        field.setShort(entity, ByteConvert.bytesToShort(adjustBytes(value,2)));

                    } else if (type.endsWith("byte") || type.endsWith("Byte")) {

                        field.setByte(entity, value[0]);

                    } else if (type.endsWith("[B")) {

                        field.set(entity, value);

                    } else {

                        throw new ConvertException("不支持字节数组转换成属性");

                    }
                }
            }
        }
        if (!hasRkField) {
            throw new ConvertException("当前对象不支持转换");
        }

        return entity;
    }
    private static IotFeildValue object2IotFieldValue(Object obj, int position, int maxLength) throws ConvertException {

        byte[] bytes = null;
        if (obj instanceof String) {

            bytes = ((String) obj).getBytes();

        } else if (obj instanceof Long) {

            bytes = ByteConvert.longToBytes((Long) obj);

        } else if (obj instanceof Integer) {

            bytes = ByteConvert.intToBytes((Integer) obj);

        } else if (obj instanceof Short) {

            bytes = ByteConvert.shortToBytes((Short) obj);

        } else if (obj instanceof Byte) {

            bytes = new byte[]{(Byte) obj};

        } else if (obj instanceof byte[]) {

            bytes = (byte[]) obj;

        }

        if (bytes != null) {

            return new IotFeildValue(position, adjustBytes(bytes,maxLength));

        } else {

            throw new ConvertException("注解标注的属性不能转换成字节数组");

        }

    }
    private static byte[] adjustBytes(byte[] bytes,int maxLength){

        if (bytes.length > maxLength) {
            bytes = Arrays.copyOfRange(bytes, bytes.length - maxLength, bytes.length);
        } else if (bytes.length < maxLength) {
            byte[] append = new byte[maxLength - bytes.length];
            bytes = byteMerger(append, bytes);
        }
        return bytes;
    }
    private static byte[] byteMerger(byte[] byte_1, byte[] byte_2) {
        byte[] byte_3 = new byte[byte_1.length + byte_2.length];
        System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);
        System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);
        return byte_3;
    }


}
