/*
 * =================================================================================================
 *                             Copyright (C) 2016 Universum Studios
 * =================================================================================================
 *         Licensed under the Apache License, Version 2.0 or later (further "License" only).
 * -------------------------------------------------------------------------------------------------
 * You may use this file only in compliance with the License. More details and copy of this License 
 * you may obtain at
 * 
 * 		http://www.apache.org/licenses/LICENSE-2.0
 * 
 * You can redistribute, modify or publish any part of the code written within this file but as it 
 * is described in the License, the software distributed under the License is distributed on an 
 * "AS IS" BASIS, WITHOUT WARRANTIES or CONDITIONS OF ANY KIND.
 * 
 * See the License for the specific language governing permissions and limitations under the License.
 * =================================================================================================
 */
package universum.studios.android.intent;

import android.util.Log;

/**
 * <b>This class has been deprecated and will be removed in the next release.</b>
 * <p>
 * Configuration options for the Intents library.
 *
 * @author Martin Albedinsky
 * @deprecated Not used.
 */
@Deprecated
public final class IntentsConfig {

	/**
	 * <b>This field has been deprecated and will be removed in the next release.</b>
	 * <p>
	 * Flag indicating whether the <b>verbose</b> output for the Intents library trough log-cat is
	 * enabled or not.
	 *
	 * @see Log#v(String, String)
	 * @deprecated Not used.
	 */
	@Deprecated
	public static boolean LOG_ENABLED = true;

	/**
	 * <b>This field has been deprecated and will be removed in the next release.</b>
	 * <p>
	 * Flag indicating whether the <b>debug</b> output for the Intents library trough log-cat is
	 * enabled or not.
	 *
	 * @see Log#d(String, String)
	 * @deprecated Not used.
	 */
	@Deprecated
	public static boolean DEBUG_LOG_ENABLED = false;

	/**
	 */
	private IntentsConfig() {
		// Not allowed to be instantiated publicly.
	}
}
