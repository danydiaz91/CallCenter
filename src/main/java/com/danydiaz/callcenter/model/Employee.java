package com.danydiaz.callcenter.model;

import com.danydiaz.callcenter.common.CallStatus;
import com.danydiaz.callcenter.common.EmployeeStatus;
import com.danydiaz.callcenter.common.EmployeeType;
import com.danydiaz.callcenter.exception.CallInterruptedException;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.java.Log;

import java.util.Optional;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

/**
 * The type Employee.
 *
 * @author <a href="dany.diaz91@hotmail.com">Dany Diaz</a>
 * @version 1.0
 * @date 24 /07/2018
 */
@Log
public class Employee implements Runnable {

    @Getter
    private EmployeeType type;
    @Setter
    private EmployeeStatus status;
    @Setter
    private boolean active;
    private SynchronousQueue<Call> phone;
    @Getter
    private LinkedBlockingQueue<Call> receivedCalls;


    private Employee(EmployeeType type) {

        this.type = type;
        this.status = EmployeeStatus.AVAILABLE;
        this.active = true;
        this.receivedCalls = new LinkedBlockingQueue<>();
        this.phone = new SynchronousQueue<>();
    }

    /**
     * Create an employee, defining its type.
     *
     * @param type the type
     * @return the employee
     */
    public static Employee of(@NonNull EmployeeType type) {

        log.info(String.format("Creating Employee [Type: %s]", type));
        return new Employee(type);
    }

    /**
     * Is available.
     *
     * @return the boolean
     */
    public boolean isAvailable() {

        return EmployeeStatus.AVAILABLE.equals(status);
    }

    /**
     * Receive call.
     *
     * @param call the call
     */
    public void tryReceiveCall(Call call) {

        try {
            call.setStatus(CallStatus.CONNECTED);
            setStatus(EmployeeStatus.BUSY);
            this.phone.put(call);
        } catch (InterruptedException e) {
            log.severe(String.format("A %s could not be received", call));
            throw new CallInterruptedException(e);
        }
    }

    /**
     * Keep call.
     *
     * @param call the call
     */
    private void tryKeepCall(@NonNull Call call) {

        try {
            call.setStatus(CallStatus.CONNECTED);
            setStatus(EmployeeStatus.BUSY);

            log.info(String.format("[Employee: %s] works on a %s", Thread.currentThread().getName(), call));
            TimeUnit.SECONDS.sleep(call.getDuration());
        } catch (InterruptedException e) {
            log.severe(String.format("[Employee: %s] A %s was interrupted", Thread.currentThread().getName(), call));
            throw new CallInterruptedException(e);
        } finally {
            finishCall(call);
        }
    }

    /**
     * Finish call.
     *
     * @param call the call
     */
    private void finishCall(@NonNull Call call) {

        call.setStatus(CallStatus.FINISHED);
        setStatus(EmployeeStatus.AVAILABLE);

        receivedCalls.add(call);
        log.info(String.format("[Employee: %s] Finished a %s", Thread.currentThread().getName(),
                call));
    }

    @Override
    public void run() {

        log.info(String.format("[Employee: %s] is working!", Thread.currentThread().getName()));

        while (active) Optional.ofNullable(phone.poll())
                .ifPresent(this::tryKeepCall);
    }
}
