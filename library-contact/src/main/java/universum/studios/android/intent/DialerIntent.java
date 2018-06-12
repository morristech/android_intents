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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

/**
 * A {@link BaseIntent} builder implementation providing API for building and starting of intents
 * targeting a <b>dialer</b> related applications.
 * <p>
 * This intent builder requires only a phone number to be specified via {@link #phoneNumber(String)}.
 *
 * @author Martin Albedinsky
 * @since 1.0
 *
 * @see SmsIntent
 */
public class DialerIntent extends BaseIntent<DialerIntent> {

	/*
	 * Constants ===================================================================================
	 */

	/**
	 * Log TAG.
	 */
	// private static final String TAG = "DialerIntent";

	/**
	 * Uri scheme for <b>phone dialer</b> targeting intents.
	 * <p>
	 * Constant value: <b>tel</b>
	 */
	public static final String URI_SCHEME = "tel";

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
	 * Phone number to dial via dialer.
	 */
	private String mPhoneNumber;

	/*
	 * Constructors ================================================================================
	 */

	/*
	 * Methods =====================================================================================
	 */

	/**
	 * Sets a phone number that should be passed to the dialer application.
	 *
	 * @param number The desired phone number. May be {@code null} to clear the current one.
	 * @return This intent builder to allow methods chaining.
	 * @see #phoneNumber()
	 */
	public DialerIntent phoneNumber(@Nullable final String number) {
		this.mPhoneNumber = number;
		return this;
	}

	/**
	 * Returns the phone number that will be passed to the dialer application.
	 *
	 * @return Phone number or empty string if not specified yet.
	 * @see #phoneNumber(String)
	 */
	@NonNull
	public String phoneNumber() {
		return mPhoneNumber == null ? "" : mPhoneNumber;
	}

	/**
	 */
	@Override
	protected void ensureCanBuildOrThrow() {
		super.ensureCanBuildOrThrow();
		if (TextUtils.isEmpty(mPhoneNumber)) {
			throw cannotBuildIntentException("No phone number specified.");
		}
	}

	/**
	 */
	@NonNull
	@Override
	protected Intent onBuild(@NonNull final Context context) {
		return new Intent(Intent.ACTION_DIAL, Uri.fromParts(URI_SCHEME, mPhoneNumber, null));
	}

	/*
	 * Inner classes ===============================================================================
	 */
}