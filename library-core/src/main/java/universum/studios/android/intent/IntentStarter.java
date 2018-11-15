/*
 * *************************************************************************************************
 *                                 Copyright 2016 Universum Studios
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
package universum.studios.android.intent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.AnimRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Interface used to hide implementation of a specific context in which is possible to start an {@link Intent}.
 *
 * @author Martin Albedinsky
 * @since 1.0
 */
public interface IntentStarter {

	/**
	 * Returns the context with which is this started associated.
	 *
	 * @return This starter's associated context.
	 */
	@NonNull Context getContext();

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