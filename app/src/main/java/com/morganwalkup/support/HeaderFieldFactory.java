package com.morganwalkup.support;

import com.morganwalkup.networks.Constants;
import com.morganwalkup.networks.datagramFields.CRC;
import com.morganwalkup.networks.datagramFields.DatagramPayloadField;
import com.morganwalkup.networks.datagramFields.LL2PAddressField;
import com.morganwalkup.networks.datagramFields.LL2PTypeField;
import com.morganwalkup.networks.datagramFields.HeaderField;

/**
 * Factory generating HeaderFields
 * Created by morganwalkup on 1/25/18.
 */

public class HeaderFieldFactory implements Factory<HeaderField, String> {

    /** The one and only instance of HeaderFieldFactory */
    private static final HeaderFieldFactory ourInstance = new HeaderFieldFactory();

    public static HeaderFieldFactory getInstance() {
        return ourInstance;
    }

    /**
     * Empty constructor
     */
    private HeaderFieldFactory() {
    }

    /**
     * Generates an instance of the class type specified, using the given data
     * @param type - The type of the object to create
     * @param inputData - The data used to create the object
     * @param <U> - The type of the created object (must extend HeaderField)
     * @return The newly created object
     */
    public <U extends HeaderField> U getItem(int type, String inputData) {
        switch(type) {
            case Constants.LL2P_DESTINATION_ADDRESS:
                return (U) new LL2PAddressField(inputData, false);
            case Constants.LL2P_SOURCE_ADDRESS:
                return (U) new LL2PAddressField(inputData, true);
            case Constants.LL2P_TYPE:
                return (U) new LL2PTypeField(inputData);
            case Constants.LL2P_PAYLOAD:
                return (U) new DatagramPayloadField(inputData);
            case Constants.LL2P_CRC:
                return (U) new CRC(inputData);
            default:
                return null;
        }
    }
}
