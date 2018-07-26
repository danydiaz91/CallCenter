package com.danydiaz.callcenter.factory;

import com.danydiaz.callcenter.common.EmployeeType;
import com.danydiaz.callcenter.model.Employee;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * The type Employee factory spec.
 *
 * @author <a href="dany.diaz91@hotmail.com">Dany Diaz</a>
 * @version 1.0
 * @date 24 /07/2018
 */
public class EmployeeFactorySpec {

    @Test
    public void newDirector() {
        final Employee director = EmployeeFactory.newDirector();
        assertEquals(EmployeeType.DIRECTOR, director.getType());
    }

    @Test
    public void newOperator() {
        final Employee operator = EmployeeFactory.newOperator();
        assertEquals(EmployeeType.OPERATOR, operator.getType());
    }

    @Test
    public void newSupervisor() {
        final Employee supervisor = EmployeeFactory.newSupervisor();
        assertEquals(EmployeeType.SUPERVISOR, supervisor.getType());
    }
}