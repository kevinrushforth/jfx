/*
 * Copyright (c) 2024, Oracle and/or its affiliates. All rights reserved.
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

package test;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

// Test to check JUnit5 default timeout
public class HangTest {

    private int sum = 1;

    @Test
    void loopShouldTimeout() {
        assertTrue(this != null);
        // This will hang without a default timeout and fail if there is one
        while (true) {
            // some computation
            for (int i = 0; i < 100; i++) {
                sum = sum * 17 + i;
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                throw new AssertionError(ex);
            }
        }
    }

    static void sleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ex) {
            throw new RuntimeException(ex);
        }
    }

    @Test
    void deadlockShouldTimeout() throws InterruptedException {
        assertTrue(this != null);

        Object lock1 = new Object();
        Object lock2 = new Object();

        // The deadlock will hang the test without a default timeout
        // and fail if there is one

        var thr1 = new Thread(() -> {
            System.err.println("thr 1: attempting to lock lock1");
            synchronized(lock1) {
                System.err.println("thr 1: locked lock1");
                sleep(2000);
                System.err.println("thr 1: attempting to lock lock2");
                synchronized(lock2) {
                    System.err.println("thr 1: locked lock2");
                }
                System.err.println("thr 1: unlocked lock2");
            }
            System.err.println("thr 1: unlocked lock1");
        });

        var thr2 = new Thread(() -> {
            sleep(1000);
            System.err.println("thr 2: attempting to lock lock2");
            synchronized(lock2) {
                System.err.println("thr 2: locked lock2");
                System.err.println("thr 2: attempting to lock lock1");
                synchronized(lock1) {
                    System.err.println("thr 2: locked lock1");
                }
                System.err.println("thr 2: unlocked lock1");
            }
            System.err.println("thr 2: unlocked lock2");
        });

        thr1.start();
        thr2.start();

        thr1.join();
        thr2.join();
    }

}
