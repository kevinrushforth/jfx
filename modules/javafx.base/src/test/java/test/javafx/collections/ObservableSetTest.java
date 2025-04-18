/*
 * Copyright (c) 2011, 2022, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package test.javafx.collections;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;
import org.junit.jupiter.api.Test;

import static test.javafx.collections.MockSetObserver.Call.*;
import static test.javafx.collections.MockSetObserver.Tuple.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.ParameterizedTest;

public class ObservableSetTest {

    Callable<ObservableSet<String>> setFactory;
    private ObservableSet<String> observableSet;
    private MockSetObserver<String> observer;

    public static Collection createParameters() {
        Object[][] data = new Object[][] {
            { TestedObservableSets.HASH_SET },
            { TestedObservableSets.TREE_SET },
            { TestedObservableSets.LINKED_HASH_SET },
            { TestedObservableSets.CHECKED_OBSERVABLE_HASH_SET },
            { TestedObservableSets.SYNCHRONIZED_OBSERVABLE_HASH_SET },
            { TestedObservableSets.OBSERVABLE_SET_PROPERTY }
         };
        return Arrays.asList(data);
    }

    private void setUp(Callable<ObservableSet<String>> setFactory) throws Exception {
        this.setFactory = setFactory;
        observableSet = setFactory.call();
        observer = new MockSetObserver<>();
        observableSet.addListener(observer);

        useSetData("one", "two", "foo");
    }

    /**
     * Modifies the set in the fixture to use the strings passed in instead of
     * the default strings, and re-creates the observable set and the observer.
     * If no strings are passed in, the result is an empty set.
     *
     * @param strings the strings to use for the list in the fixture
     */
    void useSetData(String... strings) {
        observableSet.clear();
        observableSet.addAll(Arrays.asList(strings));
        observer.clear();
    }

    @ParameterizedTest
    @MethodSource("createParameters")
    public void testAddRemove(Callable<ObservableSet<String>> setFactory) throws Exception {
        setUp(setFactory);
        observableSet.add("observedFoo");
        observableSet.add("foo");
        assertTrue(observableSet.contains("observedFoo"));

        observableSet.remove("observedFoo");
        observableSet.remove("foo");
        observableSet.remove("bar");
        observableSet.add("one");

        assertFalse(observableSet.contains("foo"));

        observer.assertAdded(0, tup("observedFoo"));
        observer.assertRemoved(1, tup("observedFoo"));
        observer.assertRemoved(2, tup("foo"));

        assertEquals(observer.getCallsNumber(), 3);
    }

    @ParameterizedTest
    @MethodSource("createParameters")
    @SuppressWarnings("unchecked")
    public void testAddAll(Callable<ObservableSet<String>> setFactory) throws Exception {
        setUp(setFactory);
        Set<String> set = new HashSet<>();
        set.add("oFoo");
        set.add("pFoo");
        set.add("foo");
        set.add("one");
        observableSet.addAll(set);

        assertTrue(observableSet.contains("oFoo"));
        observer.assertMultipleCalls(call(null, "oFoo"), call(null, "pFoo"));
    }

    @ParameterizedTest
    @MethodSource("createParameters")
    @SuppressWarnings("unchecked")
    public void testRemoveAll(Callable<ObservableSet<String>> setFactory) throws Exception {
        setUp(setFactory);
        observableSet.removeAll(Arrays.asList("one", "two", "three"));

        observer.assertMultipleRemoved(tup("one"), tup("two"));
        assertTrue(observableSet.size() == 1);
    }

    @ParameterizedTest
    @MethodSource("createParameters")
    @SuppressWarnings("unchecked")
    public void testClear(Callable<ObservableSet<String>> setFactory) throws Exception {
        setUp(setFactory);
        observableSet.clear();

        assertTrue(observableSet.isEmpty());
        observer.assertMultipleRemoved(tup("one"), tup("two"), tup("foo"));

    }

    @ParameterizedTest
    @MethodSource("createParameters")
    public void testRetainAll(Callable<ObservableSet<String>> setFactory) throws Exception {
        setUp(setFactory);
        observableSet.retainAll(Arrays.asList("one", "two", "three"));

        observer.assertRemoved(tup("foo"));
        assertTrue(observableSet.size() == 2);
    }

    @ParameterizedTest
    @MethodSource("createParameters")
    public void testIterator(Callable<ObservableSet<String>> setFactory) throws Exception {
        setUp(setFactory);
        Iterator<String> iterator = observableSet.iterator();
        assertTrue(iterator.hasNext());

        String toBeRemoved = iterator.next();
        iterator.remove();

        assertTrue(observableSet.size() == 2);
        observer.assertRemoved(tup(toBeRemoved));
    }

    @ParameterizedTest
    @MethodSource("createParameters")
    public void testOther(Callable<ObservableSet<String>> setFactory) throws Exception {
        setUp(setFactory);
        assertEquals(3, observableSet.size());
        assertFalse(observableSet.isEmpty());

        assertTrue(observableSet.contains("foo"));
        assertFalse(observableSet.contains("bar"));
    }

    @ParameterizedTest
    @MethodSource("createParameters")
    public void testNull(Callable<ObservableSet<String>> setFactory) throws Exception {
        setUp(setFactory);
        if (setFactory instanceof TestedObservableSets.CallableTreeSetImpl) {
            return; // TreeSet doesn't accept nulls
        }
        observableSet.add(null);
        assertEquals(4, observableSet.size());

        observer.assertAdded(tup((String)null));

        observableSet.remove(null);
        assertEquals(3, observableSet.size());
        observer.assertRemoved(tup((String)null));
    }


    @ParameterizedTest
    @MethodSource("createParameters")
    public void testObserverCanRemoveObservers(Callable<ObservableSet<String>> setFactory) throws Exception {
        setUp(setFactory);
        final SetChangeListener<String> listObserver = change -> {
            change.getSet().removeListener(observer);
        };
        observableSet.addListener(listObserver);
        observableSet.add("x");
        observer.clear();
        observableSet.add("y");
        observer.check0();
        observableSet.removeListener(listObserver);


        final StringSetChangeListener listener = new StringSetChangeListener();
        observableSet.addListener(listener);
        observableSet.add("z");
        assertEquals(listener.counter, 1);
        observableSet.add("zz");
        assertEquals(listener.counter, 1);
    }


    private static class StringSetChangeListener implements SetChangeListener<String> {

        private int counter;

        @Override
        public void onChanged(final Change<? extends String> change) {
            change.getSet().removeListener(this);
            ++counter;
        }
    }

    @ParameterizedTest
    @MethodSource("createParameters")
    public void testEqualsAndHashCode(Callable<ObservableSet<String>> setFactory) throws Exception {
        setUp(setFactory);
        final Set<String> other = new HashSet<>(Arrays.asList("one", "two", "foo"));
        assertTrue(observableSet.equals(other));
        assertEquals(observableSet.hashCode(), other.hashCode());
    }
}
