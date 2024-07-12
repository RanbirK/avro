/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package project;

import java.security.Permission;

class SystemUtil {
    private static final String PERMISSION_PREFIX = "exitVM.";

    static class ExitTrappedException extends SecurityException {
        private final int status;

        ExitTrappedException(int status) {
            super("Trapped System.exit(" + status + ")");
            this.status = status;
        }

        int getStatus() {
            return status;
        }
    }

    static void forbidSystemExitCall() {
        final SecurityManager securityManager = new SecurityManager() {
            public void checkPermission(Permission permission) {
                String permissionName = permission.getName();
                if (permissionName.startsWith(PERMISSION_PREFIX)) {
                    String suffix = permissionName.substring(PERMISSION_PREFIX.length());
                    int status = Integer.parseInt(suffix);
                    throw new ExitTrappedException(status);
                }
            }
        };
        System.setSecurityManager(securityManager);
    }

    static void allowSystemExitCall() {
        System.setSecurityManager(null);
    }
}
