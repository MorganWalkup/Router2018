package com.morganwalkup.support;

/**
 * Interface for all Factory classes
 * Created by morganwalkup on 1/25/18.
 */

public interface Factory<T,S> {

    /**
     * Public method to get the desired item of the specified type
     * @param type - The type of object to be created determined at run time
     * @param data - The data to be used to construct the desired object
     * @return object - This object will be of the specified U data type
     */
    <U extends T> U getItem(int type, S data);


}
