package org.fans.http.frame.toolbox.packet;


/**
 * 对返回接口的抽象，没有任何字段，所以不会和任何服务端接口耦合， <br>
 * 虽然服务器返回的接口字段不同，但大致方法归类如下
 * 
 * @author Ludaiqian
 * 
 * @param <Result>
 * @since 1.0
 */
public interface ApiResponse<Result> extends ApiPacket {
	/**
	 * 是否成功
	 * 
	 * @return
	 */
	public boolean isSuccess();

	/**
	 * 响应代码
	 * 
	 * @return
	 */
	public int getResultCode();

	/**
	 * 数据
	 * 
	 * @return
	 */
	public Result getData();

	/**
	 * error
	 * 
	 * @return
	 */
	public String getMessage();

}
