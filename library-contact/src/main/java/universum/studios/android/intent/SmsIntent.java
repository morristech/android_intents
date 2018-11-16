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
import androidx.annotation.Nullable;

/**
 * A {@link BaseIntent} builder implementation providing API for building and starting of intents
 * targeting an <b>SMS sending</b> related applications.
 * <p>
 * This intent builder requires only a phone number to be specified via {@link #phoneNumber(String)}.
 *
 * @author Martin Albedinsky
 * @since 1.0
 *
 * @see DialerIntent
 */
public final class SmsIntent extends BaseIntent {

	/*
	 * Constants ===================================================================================
	 */

	/**
	 * Log TAG.
	 */
	// private static final String TAG = "SmsIntent";

	/*
	 * Interface ===================================================================================
	 */

	/*
	 * Static members ==============================================================================
	 */

	/**
	 * Uri scheme for <b>SMS message</b> targeting intents.
	 * <p>
	 * Constant value: <b>sms</b>
	 */
	public static final String URI_SCHEME = "sms";

	/*
	 * Members =====================================================================================
	 */

	/**
	 * Phone number to send SMS to.
	 */
	private String phoneNumber;

	/**
	 * Body of an SMS to send.
	 */
	private CharSequence body;

	/*
	 * Constructors ================================================================================
	 */

	/*
	 * Methods =====================================================================================
	 */

	/**
	 * Sets a phone number that should be passed to the SMS application.
	 *
	 * @param number The desired phone number. May be {@code null} to clear the current one.
	 * @return This intent builder to allow methods chaining.
	 *
	 * @see #phoneNumber()
	 */
	public SmsIntent phoneNumber(@Nullable final String number) {
		this.phoneNumber = number;
		return this;
	}

	/**
	 * Returns the phone number that will be passed to the SMS application.
	 *
	 * @return Phone number or empty string if not specified yet.
	 *
	 * @see #phoneNumber(String)
	 */
	@NonNull public String phoneNumber() {
		return phoneNumber == null ? "" : phoneNumber;
	}

	/**
	 * Sets a body for SMS to send.
	 *
	 * @param body The desired body text. May be {@code null} to clear the current one.
	 * @return This intent builder to allow methods chaining.
	 *
	 * @see #body()
	 */
	public SmsIntent body(@Nullable final CharSequence body) {
		this.body = body;
		return this;
	}

	/**
	 * Returns the body for SMS to send.
	 *
	 * @return Message of SMS or empty text if not specified yet.
	 *
	 * @see #body(CharSequence)
	 */
	@NonNull public CharSequence body() {
		return body == null ? "" : body;
	}

	/**
	 */
	@Override protected void ensureCanBuildOrThrow() {
		super.ensureCanBuildOrThrow();
		if (TextUtils.isEmpty(phoneNumber)) {
			throw cannotBuildIntentException("No phone number specified.");
		}
	}

	/**
	 */
	@Override @NonNull protected Intent onBuild(@NonNull final Context context) {
		final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.fromParts(URI_SCHEME, phoneNumber, null));
		if (!TextUtils.isEmpty(body)) {
			intent.putExtra("sms_body", body);
		}
		return intent;
	}

	/*
	 * Inner classes ===============================================================================
	 */
}