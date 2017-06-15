package org.fans.http.frame;

/**
 * Created by lu on 2015/7/20.
 */
public interface Parser<T> {

    public  T parse(String result);
}
