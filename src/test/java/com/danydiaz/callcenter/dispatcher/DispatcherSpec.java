package com.danydiaz.callcenter.dispatcher;

import com.danydiaz.callcenter.factory.CallFactory;
import com.danydiaz.callcenter.factory.EmployeeFactory;
import com.danydiaz.callcenter.model.Call;
import com.danydiaz.callcenter.model.Employee;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

/**
 * The type Dispatcher spec.
 *
 * @author <a href="dany.diaz91@hotmail.com">Dany Diaz</a>
 * @version 1.0
 * @date 25/07/2018
 */
public class DispatcherSpec {

    private Dispatcher dispatcher;
    private ExecutorService executorService;

    @Before
    public void setUp() {

        dispatcher = new Dispatcher();
        executorService = Executors.newSingleThreadExecutor();
    }

    @After
    public void setDown() {

        executorService.shutdown();
    }

    @Test
    public void when10IncomingCallsAndThereAreAvailableEmployeeThen10ReceivedCalls() throws InterruptedException {

        final int numOfCalls = 10;
        executorService.execute(dispatcher);

        generateEmployees(4, 3, 3)
                .forEach(dispatcher::addEmployee);

        generateCalls(numOfCalls)
                .forEach(dispatcher::dispatchCall);

        executorService.awaitTermination(11, TimeUnit.SECONDS);

        assertEquals(numOfCalls, dispatcher.countFinishedCalls());
    }

    @Test
    public void when20IncomingCallsAndThereAreAvailableEmployeeThen20ReceivedCalls() throws InterruptedException {

        final int numOfCalls = 20;
        executorService.execute(dispatcher);

        generateEmployees(4, 3, 3)
                .forEach(dispatcher::addEmployee);

        // Se generan 20 llamadas, pero solo permite tener en lista 10.
        // Solo procesará las demas llamadas hasta que la lista tenga un campo libre.
        generateCalls(numOfCalls)
                .forEach(dispatcher::dispatchCall);

        executorService.awaitTermination(21, TimeUnit.SECONDS);

        assertEquals(numOfCalls, dispatcher.countFinishedCalls());
    }

    private List<Employee> generateEmployees(int numOperators, int numSupervisors, int numDirectors) {

        Stream<Employee> operators = Stream.generate(EmployeeFactory::newOperator)
                .limit(numOperators);

        Stream<Employee> supervisors = Stream.generate(EmployeeFactory::newSupervisor)
                .limit(numSupervisors);

        Stream<Employee> directors = Stream.generate(EmployeeFactory::newDirector)
                .limit(numDirectors);

        return Stream.concat(Stream.concat(operators, supervisors), directors)
                .collect(Collectors.toList());
    }

    private List<Call> generateCalls(int numOfCalls) {

        return Stream.generate(CallFactory::newCallWithRandomDurationBetween5to10Seconds)
                .limit(numOfCalls)
                .collect(Collectors.toList());
    }
}