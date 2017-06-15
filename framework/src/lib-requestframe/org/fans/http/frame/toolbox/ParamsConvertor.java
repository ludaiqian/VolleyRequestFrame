package org.fans.http.frame.toolbox;

import org.fans.http.frame.Convertor;
import org.fans.http.frame.HttpParams;
import org.fans.http.frame.PacketFieldExcluder;
import org.fans.http.frame.packet.ApiRequest;

import java.lang.reflect.Field;

/**
 * Created by lu on 2016/4/20.
 */
public class ParamsConvertor implements Convertor {


    private PacketFieldExcluder excluder = new PacketFieldExcluder();

    @Override
    public HttpParams convert(ApiRequest request) {
        HttpParams params = new HttpParams();
        Class<?> clazz = request.getClass();
        while (clazz != Object.class) {
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                // field.geta
                field.setAccessible(true);
                if (!excluder.excludeField(field)) {
                    try {
                        Object value = field.get(request);
                        String key = JsonSerializer.DEFAULT_STRATEGY.translateName(field);
                        params.put(key, String.valueOf(value));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            clazz = clazz.getSuperclass();
        }
        return params;
    }
}
