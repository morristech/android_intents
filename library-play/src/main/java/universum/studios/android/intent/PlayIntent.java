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

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.text.TextUtils;

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
	@VisibleForTesting
	static final String VIEW_URL_BASE = "https://play.google.com/store/apps/details?id=";

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
	private String mApplicationId;

	/*
	 * Constructors ================================================================================
	 */

	/*
	 * Methods =====================================================================================
	 */

	/**
	 * <b>This method has been deprecated and will be removed in the next release.</b>
	 * <p>
	 * Sets a package name of an Android application to be viewed in Play Store.
	 *
	 * @param packageName Package name of the desired application to view in store. May be {@code null}
	 *                    to use the current application's package.
	 * @return This intent builder to allow methods chaining.
	 * @see #packageName()
	 * @deprecated Use {@link #applicationId(String)} instead.
	 */
	@Deprecated
	public PlayIntent packageName(@NonNull final String packageName) {
		return applicationId(packageName);
	}

	/**
	 * <b>This method has been deprecated and will be removed in the next release.</b>
	 * <p>
	 * Returns the package name of an Android application to be viewed in Play Store.
	 *
	 * @return Package name or empty string if not specified yet.
	 * @see #packageName(String)
	 * @deprecated Use {@link #applicationId()} instead.
	 */
	@NonNull
	@Deprecated
	public String packageName() {
		return applicationId();
	}

	/**
	 * Sets a unique ID of an Android application to be viewed in Play Store.
	 *
	 * @param applicationId ID of the desired application to view in store. May be {@code null}
	 *                      to use the current application's package name.
	 * @return This intent builder to allow methods chaining.
	 * @see #applicationId()
	 */
	public PlayIntent applicationId(@NonNull final String applicationId) {
		this.mApplicationId = applicationId;
		return this;
	}

	/**
	 * Returns the unique ID of an Android application to be viewed in Play Store.
	 *
	 * @return Application ID or empty string if not specified yet.
	 * @see #applicationId(String)
	 */
	@NonNull
	public String applicationId() {
		return mApplicationId == null ? "" : mApplicationId;
	}

	/**
	 */
	@NonNull
	@Override
	protected Intent onBuild(@NonNull final Context context) {
		return new Intent(Intent.ACTION_VIEW).setData(Uri.parse(VIEW_URL_BASE + (
				TextUtils.isEmpty(mApplicationId) ?
						context.getPackageName() :
						mApplicationId
		)));
	}

	/*
	 * Inner classes ===============================================================================
	 */
}
