/*
 * *************************************************************************************************
 *                                 Copyright 2018 Universum Studios
 * *************************************************************************************************
 *                  Licensed under the Apache License, Version 2.0 (the "License")
 * -------------------------------------------------------------------------------------------------
 * You may not use this file except in compliance with the License. You may obtain a copy of the
 * License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied.
 *
 * See the License for the specific language governing permissions and limitations under the License.
 * *************************************************************************************************
 */
package universum.studios.android.test.local;

import android.os.Build;

/**
 * Utility class for local tests.
 *
 * @author Martin Albedinsky
 */
public final class TestUtils {

	/**
	 * Boolean flag indicating whether the current device is an Android emulator or not.
	 */
	public static final boolean EMULATOR = Build.FINGERPRINT.startsWith("generic");

	/**
	 * Base (relative) path to storage of the current device under the test.
	 */
	public static final String STORAGE_BASE_PATH = EMULATOR ? "/storage/sdcard" : "/storage/emulated/0";

	/**
	 */
	private TestUtils() {
		// Not allowed to be instantiated publicly.
		throw new UnsupportedOperationException();
	}
}