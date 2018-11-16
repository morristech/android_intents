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

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;

/**
 * A {@link BaseIntent} builder implementation providing API for building and starting of intents
 * targeting the <b>Android Play Store</b> application.
 * <p>
 * This intent builder does not require any of its parameters to be set. Package name of the
 * application component that starts this intent will be used as application Id by default. If this
 * intent is to be started for a specific application, id of that application may be specified via
 * {@link #applicationId(String)}.
 *
 * @author Martin Albedinsky
 * @since 1.0
 */
public final class PlayIntent extends BaseIntent<PlayIntent> {

	/*
	 * Constants ===================================================================================
	 */

	/**
	 * Log TAG.
	 */
	// private static final String TAG = "PlayIntent";

	/**
	 * Base for the url to view a particular application within Play Store.
	 */
	@VisibleForTesting static final String VIEW_URL_BASE = "https://play.google.com/store/apps/details?id=";

	/*
	 * Interface ===================================================================================
	 */

	/*
	 * Static members ==============================================================================
	 */

	/*
	 * Members =====================================================================================
	 */

	/**
	 * Unique ID of an Android application to view in Play Store.
	 */
	private String applicationId;

	/*
	 * Constructors ================================================================================
	 */

	/*
	 * Methods =====================================================================================
	 */

	/**
	 * Sets a unique ID of an Android application to be viewed in Play Store.
	 *
	 * @param applicationId ID of the desired application to view in store. May be {@code null}
	 *                      to use the current application's package name.
	 * @return This intent builder to allow methods chaining.
	 *
	 * @see #applicationId()
	 */
	public PlayIntent applicationId(@NonNull final String applicationId) {
		this.applicationId = applicationId;
		return this;
	}

	/**
	 * Returns the unique ID of an Android application to be viewed in Play Store.
	 *
	 * @return Application ID or empty string if not specified yet.
	 *
	 * @see #applicationId(String)
	 */
	@NonNull public String applicationId() {
		return applicationId == null ? "" : applicationId;
	}

	/**
	 */
	@Override @NonNull protected Intent onBuild(@NonNull final Context context) {
		return new Intent(Intent.ACTION_VIEW).setData(Uri.parse(VIEW_URL_BASE + (
				TextUtils.isEmpty(applicationId) ?
						context.getPackageName() :
						applicationId
		)));
	}

	/*
	 * Inner classes ===============================================================================
	 */
}