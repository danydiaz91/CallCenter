package com.danydiaz.callcenter.model;

import com.danydiaz.callcenter.factory.CallFactory;
import com.danydiaz.callcenter.factory.EmployeeFactory;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * The type Employee spec.
 *
 * @author <a href="dany.diaz91@hotmail.com">Dany Diaz</a>
 * @version 1.0
 * @date 24 /07/2018
 */
public class EmployeeSpec {

    private Employee director;
    private Employee operator;
    private Employee supervisor;

    @Before
    public void setUp() {

        director = EmployeeFactory.newDirector();
        operator = EmployeeFactory.newOperator();
        supervisor = EmployeeFactory.newSupervisor();
    }

    @Test(expected = NullPointerException.class)
    public void whenTryInstantiateWithNullParamThenNullPointerException() {

        Employee.of(null);
    }

    @Test
    public void whenInstantiatedThenAvailableStateIsSet() {

        assertTrue(director.isAvailable());
        assertTrue(operator.isAvailable());
        assertTrue(supervisor.isAvailable());
    }

    @Test
    public void whenReceiveCallThenRegistered() throws InterruptedException {

        Call call = CallFactory.newCallWithRandomDurationBetween5to10Seconds();
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(operator);

        operator.tryReceiveCall(call);
        TimeUnit.SECONDS.sleep(1);

        assertFalse(operator.isAvailable());

        executor.awaitTermination(call.getDuration(), TimeUnit.SECONDS);

        assertTrue(operator.isAvailable());
        assertEquals(1, operator.getReceivedCalls().size());
    }
}