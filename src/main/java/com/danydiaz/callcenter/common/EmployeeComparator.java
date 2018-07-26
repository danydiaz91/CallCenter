package com.danydiaz.callcenter.common;

import com.danydiaz.callcenter.model.Employee;

import java.util.Comparator;

/**
 * The type Employee comparator.
 *
 * @author <a href="dany.diaz91@hotmail.com">Dany Diaz</a>
 * @version 1.0
 * @date 24 /07/2018
 */
public class EmployeeComparator implements Comparator<Employee> {

    @Override
    public int compare(Employee o1, Employee o2) {

        return Integer.compare(o1.getType().getRank(), o2.getType().getRank());
    }
}
