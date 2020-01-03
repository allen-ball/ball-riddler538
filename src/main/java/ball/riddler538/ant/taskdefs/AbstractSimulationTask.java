/*
 * $Id$
 *
 * Copyright 2016 - 2019 Allen D. Ball.  All rights reserved.
 */
package ball.riddler538.ant.taskdefs;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static lombok.AccessLevel.PROTECTED;

/**
 * Abstract base {@link.uri http://ant.apache.org/ Ant}
 * {@link org.apache.tools.ant.Task} for simulations.
 *
 * {@ant.task}
 *
 * @author {@link.uri mailto:ball@hcf.dev Allen D. Ball}
 * @version $Revision$
 */
@NoArgsConstructor(access = PROTECTED)
public abstract class AbstractSimulationTask extends AbstractTask {
    @Getter @Setter
    private int count = 1000000;

    protected float asPercent(Number numerator, Number denominator) {
        return (numerator.floatValue() * 100) / denominator.floatValue();
    }
}
