package com.danydiaz.callcenter.model;

import com.danydiaz.callcenter.common.CallStatus;
import com.danydiaz.callcenter.factory.CallFactory;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * The type Call spec.
 *
 * @author <a href="dany.diaz91@hotmail.com">Dany Diaz</a>
 * @version 1.0
 * @date 24 /07/2018
 */
public class CallSpec {

    private Call call;

    @Before
    public void setUp() {
        call = CallFactory.newCallWithRandomDurationBetween5to10Seconds();
    }

    @Test(expected = NullPointerException.class)
    public void whenInstantiateWithNullParamThenNullPointerException() {
        Call.of(null);
    }

    @Test
    public void whenInstantiatedThenWaitingStatusIsSet() {
        assertTrue(call.isWaiting());
    }

    @Test
    public void whenNewStatusIsSetThenValidationStatusChange() {
        call.setStatus(CallStatus.CONNECTED);
        assertTrue(call.isConnected());

        call.setStatus(CallStatus.FINISHED);
        assertTrue(call.isFinished());
    }
}