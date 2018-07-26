package com.danydiaz.callcenter.model;

import com.danydiaz.callcenter.common.EmployeeComparator;
import com.danydiaz.callcenter.exception.UnavailableLinesException;
import lombok.NonNull;
import lombok.extern.java.Log;

import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * The type Network.
 *
 * @author <a href="dany.diaz91@hotmail.com">Dany Diaz</a>
 * @version 1.0
 * @date 24/07/2018
 */
@Log
public class Network extends ThreadPoolExecutor {

    private final ArrayList<Employee> employees;
    private final int availableLines;

    private Network(int availableLines) {

        super(availableLines, availableLines, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>());
        this.availableLines = availableLines;
        this.employees = new ArrayList<>(availableLines);
    }

    /**
     * Of network.
     *
     * @param availableLines the available lines
     * @return the network
     */
    public static Network of(int availableLines) {

        if (availableLines < 1) {
            throw new IllegalArgumentException("The available lines must be greater than 0");
        }

        log.info(String.format("Creating Network [Available Lines: %s]", availableLines));
        return new Network(availableLines);
    }

    /**
     * Register employee.
     *
     * @param employee the employee
     */
    public synchronized void registerEmployee(@NonNull Employee employee) {

        validateIfThereAreLinesAvailable();

        employees.add(employee);
        this.execute(employee);
    }

    /**
     * Validate if there are lines available.
     */
    private void validateIfThereAreLinesAvailable() {

        if (employees.size() >= availableLines) {
            throw new UnavailableLinesException();
        }
    }

    /**
     * Find available employee to receive call.
     *
     * @return the optional employee
     */
    public Optional<Employee> findAvailableEmployeeToReceiveCall() {

        return employees.stream()
                .sorted(new EmployeeComparator())
                .filter(Employee::isAvailable)
                .findFirst();
    }

    /**
     * Count all received calls int.
     *
     * @return the int
     */
    public int countAllFinishedCalls() {

        return employees.stream()
                .mapToInt(e -> e.getReceivedCalls().size())
                .sum();
    }

    /**
     * Count employees.
     *
     * @return the num of employees registered
     */
    public int countEmployees() {

        return employees.size();
    }
}
