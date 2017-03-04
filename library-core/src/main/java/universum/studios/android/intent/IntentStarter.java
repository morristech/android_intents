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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.AnimRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Interface used to hide implementation of a specific context in which is possible to start an {@link Intent}.
 *
 * @author Martin Albedinsky
 */
public interface IntentStarter {

	/**
	 * Returns the context with which is this started associated.
	 *
	 * @return This starter's associated context.
	 */
	@NonNull
	Context getContext();

	/**
	 * Same as {@link #startIntent(Intent, Bundle)} without additional options.
	 */
	void startIntent(@NonNull Intent intent);

	/**
	 * Starts the given <var>intent</var>.
	 *
	 * @see Activity#startActivity(Intent, Bundle)
	 */
	void startIntent(@NonNull Intent intent, @Nullable Bundle options);

	/**
	 * Same as {@link #startIntentForResult(Intent, int, Bundle)} without additional options.
	 */
	void startIntentForResult(@NonNull Intent intent, int requestCode);

	/**
	 * Starts the given <var>intent</var> to receive result.
	 *
	 * @see Activity#startActivityForResult(Intent, int, Bundle)
	 */
	void startIntentForResult(@NonNull Intent intent, int requestCode, @Nullable Bundle options);

	/**
	 * Overrides window transition of the started intent.
	 *
	 * @see Activity#overridePendingTransition(int, int)
	 */
	void overridePendingTransition(@AnimRes int enterAnimRes, @AnimRes int exitAnimRes);
}
