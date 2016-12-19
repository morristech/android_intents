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
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * A {@link BaseIntent} builder implementation providing API for building and starting of intents
 * targeting a <b>content sharing</b> related applications.
 * <p>
 * This intent builder requires a mime type of the sharing content to be specified via {@link #mimeType(String)}.
 * You may use one of predefined mime types from {@link MimeType} class. The content to be shared
 * may be specified as string based content via {@link #content(CharSequence)} or if there is to be
 * shared a content that is represented by an Uri, such Uri may be specified via {@link #uri(Uri)}
 * or {@link #uris(List)}.
 *
 * @author Martin Albedinsky
 */
public class ShareIntent extends BaseIntent<ShareIntent> {

	/**
	 * Interface ===================================================================================
	 */

	/**
	 * Constants ===================================================================================
	 */

	/**
	 * Log TAG.
	 */
	// private static final String TAG = "ShareIntent";

	/**
	 * Static members ==============================================================================
	 */

	/**
	 * Members =====================================================================================
	 */

	/**
	 * Mime type of content to share.
	 */
	private String mDataType = MimeType.TEXT;

	/**
	 * Title for share message.
	 */
	private CharSequence mTitle;

	/**
	 * Text for share message.
	 */
	private CharSequence mContent;

	/**
	 * Uri to content to share.
	 */
	private Uri mUri;

	/**
	 * List of Uris to content to share.
	 */
	private List<Uri> mUris;

	/**
	 * Constructors ================================================================================
	 */

	/**
	 * Creates a new instance of ShareIntent.
	 *
	 * @see #content(CharSequence)
	 * @see #mimeType(String)
	 * @see #uri(Uri)
	 */
	public ShareIntent() {
	}

	/**
	 * Methods =====================================================================================
	 */

	/**
	 * Sets a content for the sharing intent.
	 *
	 * @param text The desired content text. May be {@code null} to clear the current one.
	 * @return This intent builder to allow methods chaining.
	 * @see #content()
	 */
	public ShareIntent content(@Nullable CharSequence text) {
		this.mContent = text;
		return this;
	}

	/**
	 * Returns the text that will be used as content for the sharing intent.
	 *
	 * @return Content for sharing intent or empty string if not specified yet.
	 * @see #content(CharSequence)
	 */
	@NonNull
	public CharSequence content() {
		return mContent != null ? mContent : "";
	}

	/**
	 * Sets an Uri to content that should be shared.
	 *
	 * @param uri The desired content Uri. May be {@code null} to clear the current one.
	 * @return This intent builder to allow methods chaining.
	 * @see #uri()
	 */
	public ShareIntent uri(@Nullable Uri uri) {
		this.mUri = uri;
		return this;
	}

	/**
	 * Returns the Uri to content to be shared.
	 *
	 * @return Content Uri to share or {@code null} if not specified yet.
	 * @see #uri(Uri)
	 */
	@Nullable
	public Uri uri() {
		return mUri;
	}

	/**
	 * Same as {@link #uris(List)} for variable array of Uris.
	 *
	 * @param uris The desired array of content uris.
	 * @see #uris()
	 */
	public ShareIntent uris(@NonNull Uri... uris) {
		return uris(Arrays.asList(uris));
	}

	/**
	 * Sets a list of Uris to content that should be shared.
	 *
	 * @param uris The desired list of content uris. May be {@code null} to clear the current one.
	 * @return This intent builder to allow methods chaining.
	 * @see #uris(Uri...)
	 * @see #uris()
	 */
	public ShareIntent uris(@Nullable List<Uri> uris) {
		this.mUris = uris;
		return this;
	}

	/**
	 * Returns the list of Uris to content to be shared.
	 *
	 * @return Content Uris to share or {@link Collections#EMPTY_LIST} if there were no Uris specified
	 * yet.
	 * @see #uris(Uri...)
	 * @see #uris(List)
	 */
	@NonNull
	@SuppressWarnings("unchecked")
	public List<Uri> uris() {
		return mUris != null ? new ArrayList<>(mUris) : Collections.EMPTY_LIST;
	}

	/**
	 * Sets a MIME type of the content passed via {@link #content(CharSequence)} or {@link #uri(Uri)}
	 * or {@link #uris(List)}.
	 * <p>
	 * Default value: <b>{@link MimeType#TEXT}</b>
	 *
	 * @param type The desired content MIME type.
	 * @return This intent builder to allow methods chaining.
	 * @see #mimeType()
	 */
	public ShareIntent mimeType(@NonNull @MimeType.Value String type) {
		this.mDataType = type;
		return this;
	}

	/**
	 * Returns the MIME type of the content to be shared.
	 *
	 * @return One of mime types specified in {@link MimeType} class.
	 * @see #mimeType(String)
	 */
	@NonNull
	@MimeType.Value
	public String mimeType() {
		return mDataType;
	}

	/**
	 * Sets a title for sharing intent.
	 *
	 * @param title The desired title text. May be {@code null} to clear the current one.
	 * @return This intent builder to allow methods chaining.
	 * @see #title()
	 */
	public ShareIntent title(@Nullable CharSequence title) {
		this.mTitle = title;
		return this;
	}

	/**
	 * Returns the title text that will be used as title for sharing intent.
	 *
	 * @return Title for sharing intent or empty string if not specified yet.
	 * @see #title(CharSequence)
	 */
	@NonNull
	public CharSequence title() {
		return mTitle != null ? mTitle : "";
	}

	/**
	 */
	@Override
	protected void ensureCanBuildOrThrow() {
		super.ensureCanBuildOrThrow();
		if (TextUtils.isEmpty(mContent) && mUri == null && mUris == null) {
			throw cannotBuildIntentException("No content to share specified.");
		}
		if (TextUtils.isEmpty(mDataType)) {
			throw cannotBuildIntentException("No content's MIME type specified.");
		}
	}

	/**
	 */
	@NonNull
	@Override
	protected Intent onBuild(@NonNull Context context) {
		final Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType(mDataType);
		if (!TextUtils.isEmpty(mTitle)) {
			intent.putExtra(Intent.EXTRA_TITLE, mTitle);
		}
		if (!TextUtils.isEmpty(mContent)) {
			intent.putExtra(Intent.EXTRA_TEXT, mContent);
		}
		if (mUri != null) {
			intent.putExtra(Intent.EXTRA_STREAM, mUri);
		} else if (mUris != null) {
			intent.setAction(Intent.ACTION_SEND_MULTIPLE);
			intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, new ArrayList<Parcelable>(mUris));
		}
		return intent;
	}

	/**
	 */
	@Override
	protected boolean onStartWith(@NonNull IntentStarter starter, @NonNull Intent intent) {
		return super.onStartWith(starter, Intent.createChooser(intent, mDialogTitle));
	}

	/**
	 * Inner classes ===============================================================================
	 */
}
