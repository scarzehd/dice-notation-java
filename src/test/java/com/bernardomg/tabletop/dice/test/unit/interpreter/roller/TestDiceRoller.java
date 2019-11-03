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

package com.bernardomg.tabletop.dice.test.unit.interpreter.roller;

import java.util.Arrays;
import java.util.Iterator;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import com.bernardomg.tabletop.dice.Dice;
import com.bernardomg.tabletop.dice.history.RollHistory;
import com.bernardomg.tabletop.dice.history.RollResult;
import com.bernardomg.tabletop.dice.interpreter.DiceRoller;
import com.bernardomg.tabletop.dice.notation.DiceNotationExpression;
import com.bernardomg.tabletop.dice.notation.operand.DefaultDiceOperand;
import com.bernardomg.tabletop.dice.random.NumberGenerator;
import com.google.common.collect.Iterables;

/**
 * Unit tests for {@link DiceRoller}, verifying that handles dice correctly.
 * 
 * @author Bernardo Mart&iacute;nez Garrido
 */
@RunWith(JUnitPlatform.class)
public final class TestDiceRoller {

    /**
     * Default constructor.
     */
    public TestDiceRoller() {
        super();
    }

    /**
     * Verifies that the roller returns the total roll when there is a single
     * value.
     */
    @Test
    public final void testTransform_AddSingleValue() {
        final Dice dice;
        final DiceNotationExpression expression;
        final Integer rolled;
        final NumberGenerator generator;

        // Mocks dice
        dice = Mockito.mock(Dice.class);
        Mockito.when(dice.getQuantity()).thenReturn(1);
        Mockito.when(dice.getSides()).thenReturn(1);

        // Mocks generator
        generator = Mockito.mock(NumberGenerator.class);
        Mockito.when(generator.generate((Dice) ArgumentMatchers.any()))
                .thenReturn(Arrays.asList(5));

        expression = new DefaultDiceOperand(dice);

        rolled = new DiceRoller(generator).transform(expression).getTotalRoll();

        Assertions.assertEquals(new Integer(5), rolled);
    }

    /**
     * Verifies that the roller returns the total roll as a sum of all the
     * generated values.
     */
    @Test
    public final void testTransform_AddsTotalRoll() {
        final Dice dice;
        final DiceNotationExpression expression;
        final Integer rolled;
        final NumberGenerator generator;

        // Mocks dice
        dice = Mockito.mock(Dice.class);
        Mockito.when(dice.getQuantity()).thenReturn(3);
        Mockito.when(dice.getSides()).thenReturn(1);

        // Mocks generator
        generator = Mockito.mock(NumberGenerator.class);
        Mockito.when(generator.generate((Dice) ArgumentMatchers.any()))
                .thenReturn(Arrays.asList(1, 2, 3));

        expression = new DefaultDiceOperand(dice);

        rolled = new DiceRoller(generator).transform(expression).getTotalRoll();

        Assertions.assertEquals(new Integer(6), rolled);
    }

    /**
     * Verifies that the roller generates the text with a single value.
     */
    @Test
    public final void testTransform_AggregatesSingleText() {
        final Dice dice;
        final DiceNotationExpression expression;
        final NumberGenerator generator;
        final RollHistory result;

        // Mocks dice
        dice = Mockito.mock(Dice.class);
        Mockito.when(dice.getQuantity()).thenReturn(1);
        Mockito.when(dice.getSides()).thenReturn(1);

        // Mocks generator
        generator = Mockito.mock(NumberGenerator.class);
        Mockito.when(generator.generate((Dice) ArgumentMatchers.any()))
                .thenReturn(Arrays.asList(5));

        expression = new DefaultDiceOperand(dice);

        result = new DiceRoller(generator).transform(expression);

        Assertions.assertEquals("5", result.toString());
    }

    /**
     * Verifies that the roller generates the text by aggregating the values.
     */
    @Test
    public final void testTransform_AggregatesText() {
        final Dice dice;
        final DiceNotationExpression expression;
        final NumberGenerator generator;
        final RollHistory result;

        // Mocks dice
        dice = Mockito.mock(Dice.class);
        Mockito.when(dice.getQuantity()).thenReturn(3);
        Mockito.when(dice.getSides()).thenReturn(1);

        // Mocks generator
        generator = Mockito.mock(NumberGenerator.class);
        Mockito.when(generator.generate((Dice) ArgumentMatchers.any()))
                .thenReturn(Arrays.asList(5, 7, 2));

        expression = new DefaultDiceOperand(dice);

        result = new DiceRoller(generator).transform(expression);

        Assertions.assertEquals("[5, 7, 2]", result.toString());
    }

    /**
     * Verifies that the roller returns a value for each dice set.
     */
    @Test
    public final void testTransform_ReturnsHistory() {
        final Dice dice;
        final DiceNotationExpression expression;
        final Iterable<RollResult> rolled;
        final RollResult rolledValues;
        final NumberGenerator generator;
        final Iterator<Integer> rolls;

        // Mocks dice
        dice = Mockito.mock(Dice.class);
        Mockito.when(dice.getQuantity()).thenReturn(2);
        Mockito.when(dice.getSides()).thenReturn(1);

        // Mocks generator
        generator = Mockito.mock(NumberGenerator.class);
        Mockito.when(generator.generate((Dice) ArgumentMatchers.any()))
                .thenReturn(Arrays.asList(1, 2));

        expression = new DefaultDiceOperand(dice);

        rolled = new DiceRoller(generator).transform(expression)
                .getRollResults();
        rolledValues = rolled.iterator().next();

        // Single dice
        Assertions.assertEquals(1, Iterables.size(rolled));

        // Two values
        Assertions.assertEquals(2, Iterables.size(rolledValues.getAllRolls()));

        rolls = rolledValues.getAllRolls().iterator();

        Assertions.assertEquals(new Integer(1), rolls.next());
        Assertions.assertEquals(new Integer(2), rolls.next());
    }

}
