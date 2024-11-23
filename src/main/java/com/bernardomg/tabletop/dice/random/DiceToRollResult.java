/**
 * Copyright 2014-2023 the original author or authors
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

package com.bernardomg.tabletop.dice.random;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import com.bernardomg.tabletop.dice.Dice;
import com.bernardomg.tabletop.dice.history.DefaultRollResult;
import com.bernardomg.tabletop.dice.history.RollResult;

import lombok.extern.slf4j.Slf4j;

/**
 * Function for transforming a {@code Dice} to a {@code RollResult}, simulating rolls.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 *
 */
@Slf4j
public final class DiceToRollResult implements Function<Dice, RollResult> {

    /**
     * The random numbers generator.
     * <p>
     * Combined with the data in the rolled this, this will generate a random value in an interval.
     */
    private final NumberGenerator numberGenerator;

    /**
     * Default constructor.
     */
    public DiceToRollResult() {
        super();

        numberGenerator = new RandomNumberGenerator();
    }

    /**
     * Constructs a function with the specified generator.
     *
     * @param generator
     *            generator to use
     */
    public DiceToRollResult(final NumberGenerator generator) {
        super();

        numberGenerator = Objects.requireNonNull(generator, "Received a null pointer as generator");
    }

    @Override
    public final RollResult apply(final Dice dice) {
        final Iterable<Integer> rolls;
        Integer                 total;

        rolls = numberGenerator.generate(dice);

        List<Integer> keep_list = new ArrayList<>();

        List<Integer> sorted = new ArrayList<>();
        rolls.forEach(sorted::add);
        Collections.sort(sorted);

        if (dice.getKeep() > 0) {
            Collections.reverse(sorted);
        }

        int keep = Math.abs(dice.getKeep());

        if (keep > sorted.size()) {
            keep = sorted.size();
        }

        for (int i = 0; i < keep; i++) {
            keep_list.add(sorted.get(i));
        }

        total = 0;

        for (final Integer roll : keep_list) {
            total += roll;
        }

        log.debug("Rolled {}", dice);
        log.debug("Generated rolls: {}", rolls);
        log.debug("Total roll: {}", total);

        return new DefaultRollResult(dice, keep_list, total);
    }

}
