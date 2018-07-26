package com.danydiaz.callcenter.model;

import com.danydiaz.callcenter.common.CallStatus;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.java.Log;

/**
 * The type Call.
 *
 * @author <a href="dany.diaz91@hotmail.com">Dany Diaz</a>
 * @version 1.0
 * @date 24 /07/2018
 */
@Log
@ToString
public class Call {

    @Getter
    private final Integer duration;
    @Setter
    private CallStatus status;

    private Call(@NonNull Integer duration) {
        this.duration = duration;
        this.status = CallStatus.WAITING;
    }

    /**
     * Create a call with a specific duration
     *
     * @param duration the duration
     * @return the call
     */
    public static Call of(@NonNull Integer duration) {
        log.info(String.format("Creating Call [Duration: %s seconds]", duration));
        return new Call(duration);
    }

    /**
     * Is waiting.
     *
     * @return the boolean
     */
    public boolean isWaiting() {
        return CallStatus.WAITING.equals(status);
    }

    /**
     * Is connected.
     *
     * @return the boolean
     */
    public boolean isConnected() {
        return CallStatus.CONNECTED.equals(status);
    }

    /**
     * Is finished.
     *
     * @return the boolean
     */
    public boolean isFinished() {
        return CallStatus.FINISHED.equals(status);
    }
}
