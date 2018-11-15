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
import android.util.Patterns;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;

/**
 * A {@link BaseIntent} builder implementation providing API for building and starting of intents
 * targeting a <b>web content</b> related applications.
 * <p>
 * This intent builder requires only a web Url to be specified via {@link #url(String)}.
 * <p>
 * Valid Url for the web intent must comply with {@link Patterns#WEB_URL} matcher.
 *
 * @author Martin Albedinsky
 */
public class WebIntent extends BaseIntent<WebIntent> {

	/*
	 * Constants ===================================================================================
	 */

	/**
	 * Log TAG.
	 */
	// private static final String TAG = "WebIntent";

	/*
	 * Interface ===================================================================================
	 */

	/*
	 * Static members ==============================================================================
	 */

	/**
	 * Matcher for the valid web URL.
	 *
	 * @see Patterns#WEB_URL
	 */
	public static final Matcher WEB_URL_MATCHER = Patterns.WEB_URL.matcher("");

	/**
	 * HTTP prefix for the valid web URL which does not contain a protocol part.
	 */
	@VisibleForTesting static final String HTTP_PREFIX = "http://";

	/**
	 * Matcher for valid HTTP format. Checks for the "http://" or "https://" prefix at the begin of
	 * the specified web URL.
	 */
	@VisibleForTesting static final Matcher HTTP_FORMAT_MATCHER = Pattern.compile("^(http|https):\\/\\/(.+)").matcher("");

	/*
	 * Members =====================================================================================
	 */

	/**
	 * URL to show in a web browser.
	 */
	private String url;

	/*
	 * Constructors ================================================================================
	 */

	/*
	 * Methods =====================================================================================
	 */

	/**
	 * Sets an URL that should be loaded into framework's web browser. <b>Note</b>, that the specified
	 * <var>url</var> must be valid web URL otherwise will be not accepted.
	 * <p>
	 * If the given <var>url</var> is valid, but doesn't contain HTTP or HTTPS prefix, then the HTTP
	 * (http://) prefix will be attached to it.
	 *
	 * @param url The desired URL to load into web browser.
	 * @return This intent builder to allow methods chaining.
	 *
	 * @see #url()
	 */
	public WebIntent url(@NonNull final String url) {
		if (WEB_URL_MATCHER.reset(url).matches()) {
			if (HTTP_FORMAT_MATCHER.reset(url).matches()) {
				this.url = url;
			} else {
				this.url = HTTP_PREFIX + url;
			}
		}
		return this;
	}

	/**
	 * Returns the web URL to be loaded into web browser.
	 *
	 * @return Valid URL or empty string if not specified yet.
	 *
	 * @see #url(String)
	 */
	@NonNull public String url() {
		return url == null ? "" : url;
	}

	/**
	 */
	@Override protected void ensureCanBuildOrThrow() {
		super.ensureCanBuildOrThrow();
		if (TextUtils.isEmpty(url)) {
			throw cannotBuildIntentException("No or invalid URL specified.");
		}
	}

	/**
	 */
	@Override @NonNull protected Intent onBuild(@NonNull final Context context) {
		return new Intent(Intent.ACTION_VIEW, Uri.parse(url));
	}

	/*
	 * Inner classes ===============================================================================
	 */
}
