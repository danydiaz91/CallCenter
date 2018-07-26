package com.danydiaz.callcenter;

import com.danydiaz.callcenter.dispatcher.Dispatcher;
import com.danydiaz.callcenter.factory.CallFactory;
import com.danydiaz.callcenter.factory.EmployeeFactory;
import com.danydiaz.callcenter.model.Call;
import com.danydiaz.callcenter.model.Employee;
import lombok.extern.java.Log;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The type Call center.
 *
 * @author <a href="dany.diaz91@hotmail.com">Dany Diaz</a>
 * @version 1.0
 * @date 25 /07/2018
 */
@Log
public class CallCenter {

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) throws InterruptedException {
        Dispatcher dispatcher = new Dispatcher();
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        executorService.execute(dispatcher);

        generateEmployees(6, 3, 1)
                .forEach(dispatcher::addEmployee);

        generateCalls(10)
                .forEach(dispatcher::dispatchCall);

        executorService.awaitTermination(11, TimeUnit.SECONDS);
        executorService.shutdown();
        log.info(String.format("%s calls was processed", dispatcher.countFinishedCalls()));
        System.exit(0);
    }

    /**
     * Generate employees list.
     *
     * @param numOperators   the num operators
     * @param numSupervisors the num supervisors
     * @param numDirectors   the num directors
     * @return the list
     */
    public static List<Employee> generateEmployees(int numOperators, int numSupervisors, int numDirectors) {

        Stream<Employee> operators = Stream.generate(EmployeeFactory::newOperator)
                .limit(numOperators);

        Stream<Employee> supervisors = Stream.generate(EmployeeFactory::newSupervisor)
                .limit(numSupervisors);

        Stream<Employee> directors = Stream.generate(EmployeeFactory::newDirector)
                .limit(numDirectors);

        return Stream.concat(Stream.concat(operators, supervisors), directors)
                .collect(Collectors.toList());
    }

    /**
     * Generate calls list.
     *
     * @param numOfCalls the num of calls
     * @return the list
     */
    public static List<Call> generateCalls(int numOfCalls) {

        return Stream.generate(CallFactory::newCallWithRandomDurationBetween5to10Seconds)
                .limit(numOfCalls)
                .collect(Collectors.toList());
    }
}
