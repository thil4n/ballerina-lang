/*
 *  Copyright (c) 2025, WSO2 LLC. (http://www.wso2.com).
 *
 *  WSO2 LLC. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */

package io.ballerina.types.subtypedata;

import io.ballerina.types.Atom;
import io.ballerina.types.Bdd;

/**
 * Internal node of a BDD, which represents a disjunction of conjunctions of atoms.
 *
 * @since 2201.12.0
 */
public interface BddNode extends Bdd {

    static BddNode create(Atom atom, Bdd left, Bdd middle, Bdd right) {
        if (isSimpleNode(left, middle, right)) {
            return new BddNodeSimple(atom);
        }
        return new BddNodeImpl(atom, left, middle, right);
    }

    private static boolean isSimpleNode(Bdd left, Bdd middle, Bdd right) {
        return left instanceof BddAllOrNothing leftNode && leftNode.isAll() &&
                middle instanceof BddAllOrNothing middleNode && middleNode.isNothing() &&
                right instanceof BddAllOrNothing rightNode && rightNode.isNothing();
    }

    Atom atom();

    Bdd left();

    Bdd middle();

    Bdd right();
}
