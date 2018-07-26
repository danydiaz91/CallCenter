package com.danydiaz.callcenter.factory;

import com.danydiaz.callcenter.model.Call;

import java.util.Random;

/**
 * The type Call factory.
 *
 * @author <a href="dany.diaz91@hotmail.com">Dany Diaz</a>
 * @version 1.0
 * @date 24 /07/2018
 */
public final class CallFactory {

    /**
     * New call with random duration between 5 to 10 seconds call.
     *
     * @return the call
     */
    public static Call newCallWithRandomDurationBetween5to10Seconds() {

        return Call.of(new Random().nextInt(6) + 5);
    }
}
