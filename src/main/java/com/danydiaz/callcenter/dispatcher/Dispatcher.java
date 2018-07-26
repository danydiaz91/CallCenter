package com.danydiaz.callcenter.dispatcher;

import com.danydiaz.callcenter.exception.CallInterruptedException;
import com.danydiaz.callcenter.model.Call;
import com.danydiaz.callcenter.model.Employee;
import com.danydiaz.callcenter.model.Network;
import lombok.extern.java.Log;

import java.util.Optional;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * The type Default dispatcher.
 *
 * @author <a href="dany.diaz91@hotmail.com">Dany Diaz</a>
 * @version 1.0
 * @date 25/07/2018
 */
@Log
public class Dispatcher implements Runnable {

    private static final Integer MAX_CONCURRENT_CAllS = 10;
    private boolean active;
    private ArrayBlockingQueue<Call> incomingCalls;
    private Network network;

    /**
     * Instantiates a new Default dispatcher.
     */
    public Dispatcher() {
        active = true;
        incomingCalls = new ArrayBlockingQueue<>(MAX_CONCURRENT_CAllS);
        network = Network.of(MAX_CONCURRENT_CAllS);
    }


    /**
     * Count finished calls
     *
     * @return count of finished calls
     */
    public int countFinishedCalls() {
        return network.countAllFinishedCalls();
    }

    /**
     * Add employee.
     *
     * @param employee the employee
     */
    public void addEmployee(Employee employee) {
        network.registerEmployee(employee);
    }

    /**
     * Dispatch call.
     *
     * @param call the call
     * @return the boolean
     */
    public void dispatchCall(Call call) {
        try {
            // Cuando la lista ya tenga las 10 llamadas,
            // por medio del metodo put esperará hasta que una
            // llamada sea liberada y poder agregar la nueva.
            incomingCalls.put(call);
        } catch (InterruptedException e) {
            throw new CallInterruptedException(e);
        }
    }

    @Override
    public void run() {

        while (active) {
            if (!incomingCalls.isEmpty()) {

                Optional<Employee> availableEmployee = network.findAvailableEmployeeToReceiveCall();
                availableEmployee.ifPresent(e -> {
                    Call call = incomingCalls.poll();
                    log.info(String.format("Dispatching a %s", call));
                    e.tryReceiveCall(call);
                });
            }
        }
    }

}
