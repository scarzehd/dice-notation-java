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

package com.bernardomg.tabletop.dice.notation.operand;

import com.bernardomg.tabletop.dice.Dice;

import lombok.Data;
import lombok.NonNull;

/**
 * Default implementation of the dice operand.
 *
 * @author Bernardo Mart&iacute;nez Garrido
 */
@Data
public final class DefaultDiceOperand implements DiceOperand {

    /**
     * Operand dice value.
     */
    @NonNull
    private final Dice dice;

    @Override
    public final String getExpression() {
        int keepDrop = dice.getKeep();

        if (Math.abs(keepDrop) == dice.getQuantity()) {
            return String.format("%dd%d", getDice().getQuantity(), getDice().getSides());
        }

        if (dice.isDrop()) {
            keepDrop = (int)(Math.signum(keepDrop) * -(dice.getQuantity() - Math.abs(keepDrop)));
        }

        return String.format("%dd%d%s%s%d", getDice().getQuantity(), getDice().getSides(), dice.isDrop() ? "d" : "k", keepDrop < 0 ? "h" : "l", keepDrop);
    }

}
