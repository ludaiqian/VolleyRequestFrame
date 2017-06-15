package org.fans.http.frame.toolbox;

import java.lang.reflect.Type;

public interface Serializer<T> {

	/**
	 * 序列化
	 * 
	 * @param apiPacket
	 * @return
	 */
	public String serialize(T apiPacket);

	/**
	 * 反序列化
	 * 
	 * @param result
	 * @param type
	 * @return
	 */
	public T deserialize(String result, Type type);
}
