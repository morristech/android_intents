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
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.Patterns;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;

/**
 * A {@link BaseIntent} builder implementation providing API for building and starting of intents
 * targeting a <b>e-mail</b> related applications.
 * <p>
 * Primary e-mail addresses may be specified via {@link #to(String)} or {@link #to(String...)}.
 * <b>Carbon copy</b> addresses may be specified via {@link #cc(String)} or {@link #cc(String...)}.
 * If <b>blind carbon copy</b> addresses are also desired, these may be specified via {@link #bcc(String)}
 * or {@link #bcc(String...)}.
 *
 * @author Martin Albedinsky
 */
public class EmailIntent extends BaseIntent<EmailIntent> {

	/**
	 * Interface ===================================================================================
	 */

	/**
	 * Constants ===================================================================================
	 */

	/**
	 * Log TAG.
	 */
	private static final String TAG = "EmailIntent";

	/**
	 * Uri scheme for <b>e-mail</b> targeting intents.
	 * <p>
	 * Constant value: <b>mailto</b>
	 */
	public static final String URI_SCHEME = "mailto";

	/**
	 * Static members ==============================================================================
	 */

	/**
	 * Matcher for valid e-mail address.
	 *
	 * @see Patterns#EMAIL_ADDRESS
	 */
	public static final Matcher EMAIL_MATCHER = Patterns.EMAIL_ADDRESS.matcher("");

	/**
	 * Members =====================================================================================
	 */

	/**
	 * Subject of an email to send.
	 */
	private CharSequence mSubject;

	/**
	 * Message of an email to send.
	 */
	private CharSequence mMessage;

	/**
	 * Array with e-mail addresses to send e-mail to.
	 */
	private List<String> mAddresses;

	/**
	 * Array with e-mail addresses for carbon copy to send e-mail to.
	 */
	private List<String> mCcAddresses;

	/**
	 * Array with e-mail addresses for blind carbon copy to send e-mail to.
	 */
	private List<String> mBccAddresses;

	/**
	 * Constructors ================================================================================
	 */

	/**
	 * Creates a new instance of EmailIntent.
	 *
	 * @see #to(String)
	 * @see #cc(String)
	 * @see #bcc(String)
	 */
	public EmailIntent() {
	}

	/**
	 * Methods =====================================================================================
	 */

	/**
	 * Appends the given <var>address</var> to the current <b>primary</b> e-mail addresses to which
	 * to send an e-mail.
	 * <p>
	 * <b>Note</b>, that only valid e-mail address will be added.
	 *
	 * @param address The desired e-mail address to add.
	 * @return This intent builder to allow methods chaining.
	 * @see #to(String...)
	 * @see #addresses()
	 */
	public EmailIntent to(@NonNull String address) {
		if (mAddresses == null) this.mAddresses = new ArrayList<>(1);
		appendEmailAddress(mAddresses, address);
		return this;
	}

	/**
	 * Same as {@link #to(List)} for variable array of addresses.
	 *
	 * @param addresses The desired array of e-mail addresses to add.
	 */
	public EmailIntent to(@NonNull String... addresses) {
		return to(Arrays.asList(addresses));
	}

	/**
	 * Appends the given set of <var>addresses</var> to the current <b>primary</b> e-mail addresses
	 * to which to send an e-mail.
	 * <p>
	 * <b>Note</b>, that only valid e-mail addresses will be added.
	 *
	 * @param addresses The desired set of e-mail addresses to add. May be {@code null} to clear the
	 *                  current one.
	 * @return This intent builder to allow methods chaining.
	 * @see #addresses()
	 */
	public EmailIntent to(@Nullable List<String> addresses) {
		if (addresses != null) {
			if (mAddresses == null) {
				this.mAddresses = new ArrayList<>(addresses.size());
			}
			appendEmailAddresses(mAddresses, addresses);
		} else {
			this.mAddresses = null;
		}
		return this;
	}

	/**
	 * Returns the list with valid <b>primary</b> e-mail addresses.
	 *
	 * @return List with e-mail addresses specified via one of <b>to(...)</b> methods or
	 * {@link Collections#EMPTY_LIST} if there were no addresses specified yet.
	 * @see #to(String)
	 * @see #to(String...)
	 * @see #to(List)
	 */
	@NonNull
	@SuppressWarnings("unchecked")
	public List<String> addresses() {
		return mAddresses != null ? new ArrayList<>(mAddresses) : Collections.EMPTY_LIST;
	}

	/**
	 * Appends the given <var>address</var> to the current <b>carbon copy</b> e-mail addresses.
	 * See {@link Intent#EXTRA_CC} for more info.
	 * <p>
	 * <b>Note</b>, that only valid e-mail address will be added.
	 *
	 * @param address The desired e-mail address to add.
	 * @return This intent builder to allow methods chaining.
	 * @see #cc(String...)
	 * @see #ccAddresses()
	 */
	public EmailIntent cc(@NonNull String address) {
		if (mCcAddresses == null) this.mCcAddresses = new ArrayList<>(1);
		appendEmailAddress(mCcAddresses, address);
		return this;
	}

	/**
	 * Same as {@link #cc(List)} for variable array of addresses.
	 *
	 * @param addresses The desired array of e-mail addresses to add.
	 */
	public EmailIntent cc(@NonNull String... addresses) {
		return cc(Arrays.asList(addresses));
	}

	/**
	 * Appends the given set of <var>addresses</var> to the current <b>carbon copy</b> e-mail addresses.
	 * See {@link Intent#EXTRA_CC} for more info.
	 * <p>
	 * <b>Note</b>, that only valid e-mail addresses will be added.
	 *
	 * @param addresses The desired list of e-mail addresses to add. May be {@code null} to clear
	 *                  the current one.
	 * @return This intent builder to allow methods chaining.
	 * @see #cc(String)
	 * @see #cc(String...)
	 * @see #ccAddresses()
	 */
	public EmailIntent cc(@Nullable List<String> addresses) {
		if (addresses != null) {
			if (mCcAddresses == null) {
				this.mCcAddresses = new ArrayList<>(addresses.size());
			}
			appendEmailAddresses(mCcAddresses, addresses);
		} else {
			this.mCcAddresses = null;
		}
		return this;
	}

	/**
	 * Returns the list with valid <b>carbon copy</b> e-mail addresses.
	 *
	 * @return List with e-mail addresses specified via one of <b>cc(...)</b> methods or
	 * {@link Collections#EMPTY_LIST} if there were no addresses specified yet.
	 * @see #cc(String)
	 * @see #cc(String...)
	 * @see #cc(List)
	 */
	@NonNull
	@SuppressWarnings("unchecked")
	public List<String> ccAddresses() {
		return mCcAddresses != null ? new ArrayList<>(mCcAddresses) : Collections.EMPTY_LIST;
	}

	/**
	 * Appends the given <var>address</var> to the current <b>blind carbon copy</b> e-mail addresses.
	 * See {@link Intent#EXTRA_BCC} for more info.
	 * <p>
	 * <b>Note</b>, that only valid e-mail address will be added.
	 *
	 * @param address The desired e-mail address to add.
	 * @return This intent builder to allow methods chaining.
	 * @see #bcc(String...)
	 * @see #bccAddresses()
	 */
	public EmailIntent bcc(@NonNull String address) {
		if (mBccAddresses == null) this.mBccAddresses = new ArrayList<>(1);
		appendEmailAddress(mBccAddresses, address);
		return this;
	}

	/**
	 * Same as {@link #bcc(List)} for variable array of addresses.
	 *
	 * @param addresses The desired array of e-mail addresses to add.
	 */
	public EmailIntent bcc(@NonNull String... addresses) {
		return bcc(Arrays.asList(addresses));
	}

	/**
	 * Appends the given set of <var>addresses</var> to the current <b>blind carbon copy</b> e-mail
	 * addresses. See {@link Intent#EXTRA_CC} for more info.
	 * <p>
	 * <b>Note</b>, that only valid e-mail addresses will be added.
	 *
	 * @param addresses The desired list of e-mail addresses to add. May be {@code null} to clear
	 *                  the current one.
	 * @return This intent builder to allow methods chaining.
	 * @see #bcc(String)
	 * @see #bcc(String...)
	 * @see #bccAddresses()
	 */
	public EmailIntent bcc(@Nullable List<String> addresses) {
		if (addresses != null) {
			if (mBccAddresses == null) {
				this.mBccAddresses = new ArrayList<>(addresses.size());
			}
			appendEmailAddresses(mBccAddresses, addresses);
		} else {
			this.mBccAddresses = null;
		}
		return this;
	}

	/**
	 * Returns the list with valid <b>blind carbon copy</b> e-mail addresses.
	 *
	 * @return List with e-mail addresses specified via one of <b>bcc(...)</b> methods or
	 * {@link Collections#EMPTY_LIST} if there were no addresses specified yet.
	 * @see #bcc(String)
	 * @see #bcc(String...)
	 * @see #bcc(List)
	 */
	@NonNull
	@SuppressWarnings("unchecked")
	public List<String> bccAddresses() {
		return mBccAddresses != null ? new ArrayList<>(mBccAddresses) : Collections.EMPTY_LIST;
	}

	/**
	 * Set a subject for e-mail to send. See {@link Intent#EXTRA_SUBJECT} for more info.
	 *
	 * @param subject The desired subject text. May be {@code null} to clear the current one.
	 * @return This intent builder to allow methods chaining.en
	 * @see #subject()
	 */
	public EmailIntent subject(@NonNull CharSequence subject) {
		this.mSubject = subject;
		return this;
	}

	/**
	 * Returns the subject for e-mail to send.
	 *
	 * @return Subject of e-mail or empty text if not specified yet.
	 * @see #subject(CharSequence)
	 */
	@NonNull
	public CharSequence subject() {
		return mSubject != null ? mSubject : "";
	}

	/**
	 * Sets a message for e-mail to send.
	 *
	 * @param message The desired message text. May be {@code null} to clear the current one.
	 * @return This intent builder to allow methods chaining.
	 * @see #message()
	 */
	public EmailIntent message(@NonNull CharSequence message) {
		this.mMessage = message;
		return this;
	}

	/**
	 * Returns the message for e-mail to send.
	 *
	 * @return Message of e-mail or empty text if not specified yet.
	 * @see #message(CharSequence)
	 */
	@NonNull
	public CharSequence message() {
		return mMessage != null ? mMessage : "";
	}

	/**
	 */
	@Override
	protected void ensureCanBuildOrThrow() {
		super.ensureCanBuildOrThrow();
		if (mAddresses == null) {
			throw cannotBuildIntentException("No e-mail address/-es specified.");
		}
	}

	/**
	 */
	@NonNull
	@Override
	protected Intent onBuild(@NonNull Context context) {
		final Intent intent = new Intent(Intent.ACTION_SENDTO, createUri(mAddresses));
		intent.putExtra(Intent.EXTRA_SUBJECT, mSubject);
		intent.putExtra(Intent.EXTRA_TEXT, mMessage);
		if (mCcAddresses != null) {
			intent.putExtra(Intent.EXTRA_CC, addressesToArray(mCcAddresses));
		}
		if (mBccAddresses != null) {
			intent.putExtra(Intent.EXTRA_BCC, addressesToArray(mBccAddresses));
		}
		return intent;
	}

	/**
	 * Creates an Uri specific for the {@link Intent#ACTION_SENDTO} intent action from the given set
	 * of e-mail addresses.
	 *
	 * @param addresses List with e-mail addresses.
	 * @return Created Uri containing the specified addresses or {@code null} if the given list of
	 * addresses is empty.
	 */
	@Nullable
	public static Uri createUri(@NonNull List<String> addresses) {
		final int n = addresses.size();
		if (n == 0) return null;
		final StringBuilder data = new StringBuilder(n * 15);
		for (int i = 0; i < n; i++) {
			data.append(addresses.get(i));
			if (i < (n - 1)) {
				data.append(",");
			}
		}
		return Uri.fromParts(URI_SCHEME, data.toString(), null);
	}

	/**
	 * Transforms the specified list of addresses into array.
	 *
	 * @param addresses The list of addresses to be transformed.
	 * @return Array of same size as the specified addresses and also with the same content.
	 */
	private static String[] addressesToArray(List<String> addresses) {
		final String[] addressesArray = new String[addresses.size()];
		addresses.toArray(addressesArray);
		return addressesArray;
	}

	/**
	 */
	@Override
	protected boolean onStartWith(@NonNull IntentStarter starter, @NonNull Intent intent) {
		return super.onStartWith(starter, Intent.createChooser(intent, mDialogTitle));
	}

	/**
	 * Same as {@link #appendEmailAddress(List, String)} for list of addresses.
	 *
	 * @param list      The list into which to append the addresses.
	 * @param addresses Addresses to append.
	 */
	private static void appendEmailAddresses(List<String> list, List<String> addresses) {
		if (addresses.size() > 0) for (String address : addresses) appendEmailAddress(list, address);
	}

	/**
	 * Appends the given <var>address</var> into the given <var>list</var>. The address will be added
	 * into the given list only if it is valid e-mail address.
	 *
	 * @param list    The list into which to append the address.
	 * @param address Address to append.
	 */
	private static void appendEmailAddress(List<String> list, String address) {
		if (!EMAIL_MATCHER.reset(address).matches()) {
			Log.e(TAG, "Invalid e-mail address('" + address + "') specified.");
			return;
		}
		list.add(address);
	}

	/**
	 * Inner classes ===============================================================================
	 */
}
