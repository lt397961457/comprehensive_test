package fastdfs;

import java.io.InputStream;
import java.util.Properties;

/**
 * @ClassName PropertyUtil
 * @Description 读取配置文件的内容（key，value）
 * @author zhangkai
 * @date 2017年7月18日
 */
public class PropertyUtil {
    public static final Properties PROP = new Properties();

    /** 
     * @Method: get 
     * @Description: 读取配置文件的内容（key，value）
     * @param key
     * @return String
     * @throws 
     */
    public static String get(String key) {
        if (PROP.isEmpty()) {
            try {
                InputStream in = PropertyUtil.class.getResourceAsStream("/fastdfs-client.properties");
                PROP.load(in);
                in.close();
            } catch (Exception e) {}
        }
        return PROP.getProperty(key);
    }
}