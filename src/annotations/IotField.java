package annotations;

import java.lang.annotation.*;

/**
 * Created by lsj on 17-4-1.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface IotField {
    /**字段在数组中的位置*/
    public int position();
    /**字段的长度*/
    public  int length();
}
