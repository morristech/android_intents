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
import android.util.Log;
import android.util.Patterns;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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
 * @since 1.0
 */
public class EmailIntent extends BaseIntent<EmailIntent> {

	/*
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

	/*
	 * Interface ===================================================================================
	 */

	/*
	 * Static members ==============================================================================
	 */

	/**
	 * Matcher for valid e-mail address.
	 *
	 * @see Patterns#EMAIL_ADDRESS
	 */
	public static final Matcher EMAIL_MATCHER = Patterns.EMAIL_ADDRESS.matcher("");

	/*
	 * Members =====================================================================================
	 */

	/**
	 * Subject of an email to send.
	 */
	private CharSequence subject;

	/**
	 * Message of an email to send.
	 */
	private CharSequence message;

	/**
	 * Array with e-mail addresses to send e-mail to.
	 */
	private List<String> addresses;

	/**
	 * Array with e-mail addresses for carbon copy to send e-mail to.
	 */
	private List<String> ccAddresses;

	/**
	 * Array with e-mail addresses for blind carbon copy to send e-mail to.
	 */
	private List<String> bccAddresses;

	/*
	 * Constructors ================================================================================
	 */

	/*
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
	 *
	 * @see #to(String...)
	 * @see #addresses()
	 */
	public EmailIntent to(@NonNull final String address) {
		if (addresses == null) {
			this.addresses = new ArrayList<>(1);
		}
		appendEmailAddress(addresses, address);
		return this;
	}

	/**
	 * Same as {@link #to(List)} for variable array of addresses.
	 *
	 * @param addresses The desired array of e-mail addresses to add.
	 */
	public EmailIntent to(@NonNull final String... addresses) {
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
	 *
	 * @see #addresses()
	 */
	public EmailIntent to(@Nullable final List<String> addresses) {
		if (addresses == null) {
			this.addresses = null;
		} else {
			if (this.addresses == null) {
				this.addresses = new ArrayList<>(addresses.size());
			}
			appendEmailAddresses(this.addresses, addresses);
		}
		return this;
	}

	/**
	 * Returns the list with valid <b>primary</b> e-mail addresses.
	 *
	 * @return List with e-mail addresses specified via one of <b>to(...)</b> methods or
	 * {@link Collections#EMPTY_LIST} if there were no addresses specified yet.
	 *
	 * @see #to(String)
	 * @see #to(String...)
	 * @see #to(List)
	 */
	@NonNull public List<String> addresses() {
		return addresses == null ? Collections.<String>emptyList() : new ArrayList<>(addresses);
	}

	/**
	 * Appends the given <var>address</var> to the current <b>carbon copy</b> e-mail addresses.
	 * See {@link Intent#EXTRA_CC} for more info.
	 * <p>
	 * <b>Note</b>, that only valid e-mail address will be added.
	 *
	 * @param address The desired e-mail address to add.
	 * @return This intent builder to allow methods chaining.
	 *
	 * @see #cc(String...)
	 * @see #ccAddresses()
	 */
	public EmailIntent cc(@NonNull final String address) {
		if (ccAddresses == null){
			this.ccAddresses = new ArrayList<>(1);
		}
		appendEmailAddress(ccAddresses, address);
		return this;
	}

	/**
	 * Same as {@link #cc(List)} for variable array of addresses.
	 *
	 * @param addresses The desired array of e-mail addresses to add.
	 */
	public EmailIntent cc(@NonNull final String... addresses) {
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
	 *
	 * @see #cc(String)
	 * @see #cc(String...)
	 * @see #ccAddresses()
	 */
	public EmailIntent cc(@Nullable final List<String> addresses) {
		if (addresses == null) {
			this.ccAddresses = null;
		} else {
			if (ccAddresses == null) {
				this.ccAddresses = new ArrayList<>(addresses.size());
			}
			appendEmailAddresses(ccAddresses, addresses);
		}
		return this;
	}

	/**
	 * Returns the list with valid <b>carbon copy</b> e-mail addresses.
	 *
	 * @return List with e-mail addresses specified via one of <b>cc(...)</b> methods or
	 * {@link Collections#EMPTY_LIST} if there were no addresses specified yet.
	 *
	 * @see #cc(String)
	 * @see #cc(String...)
	 * @see #cc(List)
	 */
	@NonNull public List<String> ccAddresses() {
		return ccAddresses == null ? Collections.<String>emptyList() : new ArrayList<>(ccAddresses);
	}

	/**
	 * Appends the given <var>address</var> to the current <b>blind carbon copy</b> e-mail addresses.
	 * See {@link Intent#EXTRA_BCC} for more info.
	 * <p>
	 * <b>Note</b>, that only valid e-mail address will be added.
	 *
	 * @param address The desired e-mail address to add.
	 * @return This intent builder to allow methods chaining.
	 *
	 * @see #bcc(String...)
	 * @see #bccAddresses()
	 */
	public EmailIntent bcc(@NonNull final String address) {
		if (bccAddresses == null) {
			this.bccAddresses = new ArrayList<>(1);
		}
		appendEmailAddress(bccAddresses, address);
		return this;
	}

	/**
	 * Same as {@link #bcc(List)} for variable array of addresses.
	 *
	 * @param addresses The desired array of e-mail addresses to add.
	 */
	public EmailIntent bcc(@NonNull final String... addresses) {
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
	 *
	 * @see #bcc(String)
	 * @see #bcc(String...)
	 * @see #bccAddresses()
	 */
	public EmailIntent bcc(@Nullable final List<String> addresses) {
		if (addresses == null) {
			this.bccAddresses = null;
		} else {
			if (bccAddresses == null) {
				this.bccAddresses = new ArrayList<>(addresses.size());
			}
			appendEmailAddresses(bccAddresses, addresses);
		}
		return this;
	}

	/**
	 * Returns the list with valid <b>blind carbon copy</b> e-mail addresses.
	 *
	 * @return List with e-mail addresses specified via one of <b>bcc(...)</b> methods or
	 * {@link Collections#EMPTY_LIST} if there were no addresses specified yet.
	 *
	 * @see #bcc(String)
	 * @see #bcc(String...)
	 * @see #bcc(List)
	 */
	@NonNull public List<String> bccAddresses() {
		return bccAddresses == null ? Collections.<String>emptyList() : new ArrayList<>(bccAddresses);
	}

	/**
	 * Set a subject for e-mail to send. See {@link Intent#EXTRA_SUBJECT} for more info.
	 *
	 * @param subject The desired subject text. May be {@code null} to clear the current one.
	 * @return This intent builder to allow methods chaining.
	 *
	 * @see #subject()
	 */
	public EmailIntent subject(@NonNull final CharSequence subject) {
		this.subject = subject;
		return this;
	}

	/**
	 * Returns the subject for e-mail to send.
	 *
	 * @return Subject of e-mail or empty text if not specified yet.
	 *
	 * @see #subject(CharSequence)
	 */
	@NonNull public CharSequence subject() {
		return subject == null ? "" : subject;
	}

	/**
	 * Sets a message for e-mail to send.
	 *
	 * @param message The desired message text. May be {@code null} to clear the current one.
	 * @return This intent builder to allow methods chaining.
	 *
	 * @see #message()
	 */
	public EmailIntent message(@NonNull final CharSequence message) {
		this.message = message;
		return this;
	}

	/**
	 * Returns the message for e-mail to send.
	 *
	 * @return Message of e-mail or empty text if not specified yet.
	 *
	 * @see #message(CharSequence)
	 */
	@NonNull public CharSequence message() {
		return message == null ? "" : message;
	}

	/**
	 */
	@Override protected void ensureCanBuildOrThrow() {
		super.ensureCanBuildOrThrow();
		if (addresses == null) {
			throw cannotBuildIntentException("No e-mail address/-es specified.");
		}
	}

	/**
	 */
	@Override @NonNull protected Intent onBuild(@NonNull final Context context) {
		final Intent intent = new Intent(Intent.ACTION_SENDTO, createUri(addresses));
		intent.putExtra(Intent.EXTRA_SUBJECT, subject);
		intent.putExtra(Intent.EXTRA_TEXT, message);
		if (ccAddresses != null) {
			intent.putExtra(Intent.EXTRA_CC, addressesToArray(ccAddresses));
		}
		if (bccAddresses != null) {
			intent.putExtra(Intent.EXTRA_BCC, addressesToArray(bccAddresses));
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
	@Nullable public static Uri createUri(@NonNull final List<String> addresses) {
		final int n = addresses.size();
		if (n == 0) {
			return null;
		}
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
	private static String[] addressesToArray(final List<String> addresses) {
		final String[] addressesArray = new String[addresses.size()];
		addresses.toArray(addressesArray);
		return addressesArray;
	}

	/**
	 */
	@Override protected boolean onStartWith(@NonNull final IntentStarter starter, @NonNull final Intent intent) {
		return super.onStartWith(starter, Intent.createChooser(intent, dialogTitle));
	}

	/**
	 * Same as {@link #appendEmailAddress(List, String)} for list of addresses.
	 *
	 * @param list      The list into which to append the addresses.
	 * @param addresses Addresses to append.
	 */
	private static void appendEmailAddresses(final List<String> list, final List<String> addresses) {
		if (!addresses.isEmpty()) for (final String address : addresses) appendEmailAddress(list, address);
	}

	/**
	 * Appends the given <var>address</var> into the given <var>list</var>. The address will be added
	 * into the given list only if it is valid e-mail address.
	 *
	 * @param list    The list into which to append the address.
	 * @param address Address to append.
	 */
	private static void appendEmailAddress(final List<String> list, final String address) {
		if (EMAIL_MATCHER.reset(address).matches()) {
			list.add(address);
			return;
		}
		Log.e(TAG, "Invalid e-mail address('" + address + "') specified.");
	}

	/*
	 * Inner classes ===============================================================================
	 */
}