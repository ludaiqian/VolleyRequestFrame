package org.fans.http.frame.toolbox;

import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.fans.http.frame.Serializer;
import org.fans.http.frame.packet.Name;

import java.lang.reflect.Field;
import java.lang.reflect.Type;

public class JsonSerializer<T> implements Serializer<T> {
	/**
	 * <pre>
	 *  默认的属性名->接口参数名转换策略
	 * 当有@{@link Name} 注解时，返回Name注解的值
	 * 否则 直接返回属性名称
	 * </pre>
	 */
	public static final FieldNamingStrategy DEFAULT_STRATEGY = new FieldNamingStrategy() {

		@Override
		public String translateName(Field f) {
			Name key = f.getAnnotation(Name.class);
			return key != null ? key.value() : f.getName();
			// .substring(0,1). toUpperCase() + f.getName ().substring(1)
		}
	};
	private Gson gson;
	public static final JsonSerializer<Object> DEFAULT = new JsonSerializer<Object>(DEFAULT_STRATEGY);

	public JsonSerializer(FieldNamingStrategy strategy) {
		super();
		gson = new GsonBuilder().setFieldNamingStrategy(strategy).create();
	}

	@Override
	public String serialize(T apiPacket) {
		return gson.toJson(apiPacket);
	}

	@Override
	public T deserialize(String result, Type type) {
		return gson.fromJson(result, type);
	}

}
