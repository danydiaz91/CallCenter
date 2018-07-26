package com.danydiaz.callcenter.factory;

import com.danydiaz.callcenter.common.EmployeeType;
import com.danydiaz.callcenter.model.Employee;

/**
 * The type Employee factory.
 *
 * @author <a href="dany.diaz91@hotmail.com">Dany Diaz</a>
 * @version 1.0
 * @date 24 /07/2018
 */
public final class EmployeeFactory {

    /**
     * Private Constructor
     */
    private EmployeeFactory() {
        throw new IllegalStateException();
    }

    /**
     * New director employee.
     *
     * @return the director
     */
    public static Employee newDirector() {
        return Employee.of(EmployeeType.DIRECTOR);
    }

    /**
     * New operator employee.
     *
     * @return the operator
     */
    public static Employee newOperator() {
        return Employee.of(EmployeeType.OPERATOR);
    }

    /**
     * New supervisor employee.
     *
     * @return the supervisor
     */
    public static Employee newSupervisor() {
        return Employee.of(EmployeeType.SUPERVISOR);
    }
}
