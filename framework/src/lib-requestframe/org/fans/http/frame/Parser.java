package org.fans.http.frame;

/**
 * Created by lu on 2014/9/17.
 */
public interface Parser<T> {

    public  T parse(String result);
}
