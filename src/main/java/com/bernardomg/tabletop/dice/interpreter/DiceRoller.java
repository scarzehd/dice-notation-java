/**
 * Copyright 2014-2019 the original author or authors
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.bernardomg.tabletop.dice.interpreter;

import com.bernardomg.tabletop.dice.history.RollHistory;
import com.bernardomg.tabletop.dice.notation.DiceNotationExpression;
import com.bernardomg.tabletop.dice.random.NumberGenerator;
import com.bernardomg.tabletop.dice.random.RandomNumberGenerator;
import com.bernardomg.tabletop.dice.roll.DefaultRollGenerator;
import com.bernardomg.tabletop.dice.roll.RollGenerator;
import com.bernardomg.tabletop.dice.visitor.DiceRollAccumulator;
import com.bernardomg.tabletop.dice.visitor.RollTransformer;

/**
 * Dice notation expression which simulates rolling the expression.
 * <p>
 * As some values, such as dice, represent random numbers the transformer may
 * not return the same result each time it is executed for the same expression.
 * <p>
 * The random value will be generated by a {@link NumberGenerator} contained in
 * the transformer, which can be set through the constructor. Otherwise the
 * default one, a {@link RandomNumberGenerator}, will be used.
 * 
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
public final class DiceRoller implements DiceInterpreter<RollHistory> {

    /**
     * Wrapped interpreter. Configured for the required operations.
     */
    private final DiceInterpreter<RollHistory> wrapped;

    /**
     * Default constructor.
     */
    public DiceRoller() {
        this(new DefaultRollGenerator());
    }

    /**
     * Constructs a transformer using the received roller for simulating rolls.
     * 
     * @param generator
     *            the random number generator to use
     */
    public DiceRoller(final NumberGenerator generator) {
        this(new DefaultRollGenerator(generator));
    }

    /**
     * Constructs a transformer using the received roller for simulating rolls
     * and the received transformer on the rolls.
     * 
     * @param generator
     *            the random number generator to use
     * @param trans
     *            transformer to apply
     */
    public DiceRoller(final NumberGenerator generator,
            final RollTransformer trans) {
        this(new DefaultRollGenerator(generator), trans);
    }

    /**
     * Constructs a transformer using the received roll generator for simulating
     * rolls.
     * 
     * @param roller
     *            the roller to use
     */
    public DiceRoller(final RollGenerator roller) {
        super();

        wrapped = new ConfigurableInterpreter<>(new PostorderTraverser(),
                new DiceRollAccumulator(roller));
    }

    /**
     * Constructs a transformer using the received roll generator for simulating
     * rolls and the received transformer on the rolls.
     * 
     * @param roller
     *            the roller to use
     * @param trans
     *            transformer to apply
     */
    public DiceRoller(final RollGenerator roller, final RollTransformer trans) {
        super();

        wrapped = new ConfigurableInterpreter<>(new PostorderTraverser(),
                new DiceRollAccumulator(roller, trans));
    }

    /**
     * Constructs a transformer using the transformer on the rolls.
     * 
     * @param trans
     *            transformer to apply
     */
    public DiceRoller(final RollTransformer trans) {
        this(new DefaultRollGenerator(), trans);
    }

    @Override
    public final RollHistory
            transform(final DiceNotationExpression expression) {
        return wrapped.transform(expression);
    }

}
