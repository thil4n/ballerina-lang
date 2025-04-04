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

package io.ballerina.runtime.api.types.semtype;

/**
 * Represent the BDD atom.
 *
 * @since 2201.12.0
 */
public sealed interface Atom permits RecAtom, TypeAtom {

    /**
     * Get the index of the atom. For {@code TypeAtoms} this is a unique index within the {@code Env}. Each
     * {@code RecAtom} that points to the same {@code TypeAtom} will have the same index.
     *
     * @return index of the atom
     */
    int index();
}
