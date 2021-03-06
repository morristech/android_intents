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
import android.os.Parcelable;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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
 * @since 1.0
 */
public class ShareIntent extends BaseIntent<ShareIntent> {

	/*
	 * Constants ===================================================================================
	 */

	/**
	 * Log TAG.
	 */
	// private static final String TAG = "ShareIntent";

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
	 * Mime type of content to share.
	 */
	private String dataType = MimeType.TEXT;

	/**
	 * Title for share message.
	 */
	private CharSequence title;

	/**
	 * Text for share message.
	 */
	private CharSequence content;

	/**
	 * Uri to content to share.
	 */
	private Uri uri;

	/**
	 * List of Uris to content to share.
	 */
	private List<Uri> uris;

	/*
	 * Constructors ================================================================================
	 */

	/*
	 * Methods =====================================================================================
	 */

	/**
	 * Sets a content for the sharing intent.
	 *
	 * @param text The desired content text. May be {@code null} to clear the current one.
	 * @return This intent builder to allow methods chaining.
	 *
	 * @see #content()
	 */
	public ShareIntent content(@Nullable final CharSequence text) {
		this.content = text;
		return this;
	}

	/**
	 * Returns the text that will be used as content for the sharing intent.
	 *
	 * @return Content for sharing intent or empty string if not specified yet.
	 *
	 * @see #content(CharSequence)
	 */
	@NonNull public CharSequence content() {
		return content == null ? "" : content;
	}

	/**
	 * Sets an Uri to content that should be shared.
	 *
	 * @param uri The desired content Uri. May be {@code null} to clear the current one.
	 * @return This intent builder to allow methods chaining.
	 *
	 * @see #uri()
	 */
	public ShareIntent uri(@Nullable final Uri uri) {
		this.uri = uri;
		return this;
	}

	/**
	 * Returns the Uri to content to be shared.
	 *
	 * @return Content Uri to share or {@code null} if not specified yet.
	 *
	 * @see #uri(Uri)
	 */
	@Nullable public Uri uri() {
		return uri;
	}

	/**
	 * Same as {@link #uris(List)} for variable array of Uris.
	 *
	 * @param uris The desired array of content uris.
	 *
	 * @see #uris()
	 */
	public ShareIntent uris(@NonNull final Uri... uris) {
		return uris(Arrays.asList(uris));
	}

	/**
	 * Sets a list of Uris to content that should be shared.
	 *
	 * @param uris The desired list of content uris. May be {@code null} to clear the current one.
	 * @return This intent builder to allow methods chaining.
	 *
	 * @see #uris(Uri...)
	 * @see #uris()
	 */
	public ShareIntent uris(@Nullable final List<Uri> uris) {
		this.uris = uris;
		return this;
	}

	/**
	 * Returns the list of Uris to content to be shared.
	 *
	 * @return Content Uris to share or {@link Collections#EMPTY_LIST} if there were no Uris specified
	 * yet.
	 *
	 * @see #uris(Uri...)
	 * @see #uris(List)
	 */
	@NonNull public List<Uri> uris() {
		return uris == null ? Collections.<Uri>emptyList() : new ArrayList<>(uris);
	}

	/**
	 * Sets a MIME type of the content passed via {@link #content(CharSequence)} or {@link #uri(Uri)}
	 * or {@link #uris(List)}.
	 * <p>
	 * Default value: <b>{@link MimeType#TEXT}</b>
	 *
	 * @param type The desired content MIME type.
	 * @return This intent builder to allow methods chaining.
	 *
	 * @see #mimeType()
	 */
	public ShareIntent mimeType(@NonNull @MimeType.Value final String type) {
		this.dataType = type;
		return this;
	}

	/**
	 * Returns the MIME type of the content to be shared.
	 *
	 * @return One of mime types specified in {@link MimeType} class.
	 *
	 * @see #mimeType(String)
	 */
	@NonNull @MimeType.Value public String mimeType() {
		return dataType;
	}

	/**
	 * Sets a title for sharing intent.
	 *
	 * @param title The desired title text. May be {@code null} to clear the current one.
	 * @return This intent builder to allow methods chaining.
	 * @see #title()
	 */
	public ShareIntent title(@Nullable final CharSequence title) {
		this.title = title;
		return this;
	}

	/**
	 * Returns the title text that will be used as title for sharing intent.
	 *
	 * @return Title for sharing intent or empty string if not specified yet.
	 *
	 * @see #title(CharSequence)
	 */
	@NonNull public CharSequence title() {
		return title == null ? "" : title;
	}

	/**
	 */
	@Override protected void ensureCanBuildOrThrow() {
		super.ensureCanBuildOrThrow();
		if (TextUtils.isEmpty(content) && uri == null && uris == null) {
			throw cannotBuildIntentException("No content to share specified.");
		}
		if (TextUtils.isEmpty(dataType)) {
			throw cannotBuildIntentException("No content's MIME type specified.");
		}
	}

	/**
	 */
	@Override @NonNull protected Intent onBuild(@NonNull final Context context) {
		final Intent intent = new Intent(Intent.ACTION_SEND);
		intent.setType(dataType);
		if (!TextUtils.isEmpty(title)) {
			intent.putExtra(Intent.EXTRA_TITLE, title);
		}
		if (!TextUtils.isEmpty(content)) {
			intent.putExtra(Intent.EXTRA_TEXT, content);
		}
		if (uri != null) {
			intent.putExtra(Intent.EXTRA_STREAM, uri);
		} else if (uris != null) {
			intent.setAction(Intent.ACTION_SEND_MULTIPLE);
			intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, new ArrayList<Parcelable>(uris));
		}
		return intent;
	}

	/**
	 */
	@Override protected boolean onStartWith(@NonNull final IntentStarter starter, @NonNull final Intent intent) {
		return super.onStartWith(starter, Intent.createChooser(intent, dialogTitle));
	}

	/*
	 * Inner classes ===============================================================================
	 */
}