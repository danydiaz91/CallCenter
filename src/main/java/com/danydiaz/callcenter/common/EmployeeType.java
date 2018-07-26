package com.danydiaz.callcenter.common;

import lombok.Getter;

/**
 * The enum Employee type.
 *
 * @author <a href="dany.diaz91@hotmail.com">Dany Diaz</a>
 * @version 1.0
 * @date 24/07/2018
 */
public enum EmployeeType {

    DIRECTOR(2),
    OPERATOR(0),
    SUPERVISOR(1);

    @Getter
    private int rank;

    EmployeeType(int rank) {
        this.rank = rank;
    }
}
