package com.danydiaz.callcenter.model;

import com.danydiaz.callcenter.common.EmployeeStatus;
import com.danydiaz.callcenter.common.EmployeeType;
import com.danydiaz.callcenter.exception.UnavailableLinesException;
import com.danydiaz.callcenter.factory.EmployeeFactory;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * The type Network spec.
 *
 * @author <a href="dany.diaz91@hotmail.com">Dany Diaz</a>
 * @version 1.0
 * @date 24/07/2018
 */
public class NetworkSpec {

    private static final int AVAILABLE_LINES = 10;

    private Network network;

    @Before
    public void setUp() {
        network = Network.of(AVAILABLE_LINES);
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenInstantiatedWithZeroThenIllegalArgumentException() {

        Network.of(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenInstantiatedWithNegativeNumberThenIllegalArgumentException() {

        Network.of(-1);
    }

    @Test(expected = NullPointerException.class)
    public void whenRegisterEmployeeWithNullParamThenNullPointerException() {

        network.registerEmployee(null);
    }

    @Test(expected = UnavailableLinesException.class)
    public void whenRegisterMoreEmployeesThanExpectedThenUnavailableLinesException() {

        generateEmployees(AVAILABLE_LINES + 1, 0, 0)
                .forEach(network::registerEmployee);
    }

    @Test
    public void whenRegisterEmployeeThenCountOfEmployeesRiseUp() {

        int expectedCount = network.countEmployees() + 1;
        network.registerEmployee(EmployeeFactory.newOperator());

        assertEquals(expectedCount, network.countEmployees());
    }

    @Test
    public void whenOperatorAreAvailableThenOperator() {
        generateEmployees(1, 2, 2).forEach(network::registerEmployee);

        Optional<Employee> employeeOptional = network.findAvailableEmployeeToReceiveCall();

        assertTrue(employeeOptional.isPresent());
        assertEquals(EmployeeType.OPERATOR, employeeOptional.get().getType());
    }

    @Test
    public void whenAllOperatorsAreBusyThenSupervisor() {
        generateEmployees(3, 0, 0)
                .stream().peek(e -> e.setStatus(EmployeeStatus.BUSY))
                .forEach(network::registerEmployee);

        generateEmployees(0, 2, 3).forEach(network::registerEmployee);

        Optional<Employee> employeeOptional = network.findAvailableEmployeeToReceiveCall();

        assertTrue(employeeOptional.isPresent());
        assertEquals(EmployeeType.SUPERVISOR, employeeOptional.get().getType());
    }

    @Test
    public void whenAllOperatorsAndSupervisorsAreBusyThenDirector() {
        generateEmployees(4, 3, 0)
                .stream().peek(e -> e.setStatus(EmployeeStatus.BUSY))
                .forEach(network::registerEmployee);

        generateEmployees(0, 0, 3).forEach(network::registerEmployee);

        Optional<Employee> employeeOptional = network.findAvailableEmployeeToReceiveCall();

        assertTrue(employeeOptional.isPresent());
        assertEquals(EmployeeType.DIRECTOR, employeeOptional.get().getType());
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
}