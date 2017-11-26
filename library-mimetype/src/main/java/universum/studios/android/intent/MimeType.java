/*
 * =================================================================================================
 *                             Copyright (C) 2014 Martin Albedinsky
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

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * This class specifies the most commonly used <b>MIME</b> types for a content distributed on devices
 * powered by the Android.
 *
 * <h3>Text types:</h3>
 * <ul>
 * <li>{@link #TEXT}</li>
 * <li>{@link #TEXT_PLAIN}</li>
 * <li>{@link #TEXT_HTML}</li>
 * </ul>
 *
 * <h3>Image types:</h3>
 * <ul>
 * <li>{@link #IMAGE}</li>
 * <li>{@link #IMAGE_JPEG}</li>
 * <li>{@link #IMAGE_PNG}</li>
 * <li>{@link #IMAGE_BITMAP}</li>
 * </ul>
 *
 * <h3>Audio types:</h3>
 * <ul>
 * <li>{@link #AUDIO}</li>
 * <li>{@link #AUDIO_MP3}</li>
 * <li>{@link #AUDIO_MP4}</li>
 * <li>{@link #AUDIO_MPEG}</li>
 * </ul>
 *
 * <h3>Video types</h3>
 * <ul>
 * <li>{@link #VIDEO}</li>
 * <li>{@link #VIDEO_MP4}</li>
 * <li>{@link #VIDEO_MPEG}</li>
 * <li>{@link #VIDEO_JPEG}</li>
 * <li>{@link #VIDEO_3GP}</li>
 * <li>{@link #VIDEO_3GPP2}</li>
 * </ul>
 *
 * @author Martin Albedinsky
 */
public abstract class MimeType {

	/**
	 * Mime type targeting <b>overall</b> text content.
	 */
	public static final String TEXT = "text/*";

	/**
	 * Mime type targeting <b>plain</b> text content.
	 */
	public static final String TEXT_PLAIN = "text/plain";

	/**
	 * Mime type targeting <b>HTML</b> text content.
	 */
	public static final String TEXT_HTML = "text/html";

	/**
	 * Mime type targeting <b>overall</b> image content.
	 */
	public static final String IMAGE = "image/*";

	/**
	 * Mime type targeting <b>JPEG</b> image content.
	 */
	public static final String IMAGE_JPEG = "image/jpeg";

	/**
	 * Mime type targeting <b>PNG</b> image content.
	 */
	public static final String IMAGE_PNG = "image/png";

	/**
	 * Mime type targeting <b>Bitmap</b> image content.
	 */
	public static final String IMAGE_BITMAP = "image/bmp";

	/**
	 * Mime type targeting <b>overall</b> audio content.
	 */
	public static final String AUDIO = "audio/*";

	/**
	 * Mime type targeting <b>MPEG</b> audio content.
	 */
	public static final String AUDIO_MPEG = "audio/mpeg";

	/**
	 * Mime type targeting <b>MP3</b> audio content.
	 */
	public static final String AUDIO_MP3 = "audio/mp3";

	/**
	 * Mime type targeting <b>MP4</b> audio content.
	 */
	public static final String AUDIO_MP4 = "audio/mp4";

	/**
	 * Mime type targeting <b>overall</b> video content.
	 */
	public static final String VIDEO = "video/*";

	/**
	 * Mime type targeting <b>JPEG</b> video content.
	 */
	public static final String VIDEO_JPEG = "video/jpeg";

	/**
	 * Mime type targeting <b>MPEG</b> video content.
	 */
	public static final String VIDEO_MPEG = "video/mpeg";

	/**
	 * Mime type targeting <b>MP4</b> video content.
	 */
	public static final String VIDEO_MP4 = "video/mp4";

	/**
	 * Mime type targeting <b>3GP</b> video content.
	 */
	public static final String VIDEO_3GP = "video/3gp";

	/**
	 * Mime type targeting <b>3GPP</b> video content.
	 */
	public static final String VIDEO_3GPP = "video/3gpp";

	/**
	 * Mime type targeting <b>3GPP2</b> video content.
	 */
	public static final String VIDEO_3GPP2 = "video/3gpp2";

	/**
	 * Defines an annotation for determining set of available mime-type values specified in {@link MimeType}.
	 */
	@StringDef({
			TEXT, TEXT_PLAIN, TEXT_HTML,
			IMAGE, IMAGE_JPEG, IMAGE_PNG, IMAGE_BITMAP,
			AUDIO, AUDIO_MPEG, AUDIO_MP3, AUDIO_MP4,
			VIDEO, VIDEO_JPEG, VIDEO_MPEG, VIDEO_MP4, VIDEO_3GP, VIDEO_3GPP, VIDEO_3GPP2
	})
	@Retention(RetentionPolicy.SOURCE)
	public @interface Value {
	}

	/**
	 * Creates a new empty instance of MimeType. Inheritance hierarchies should declare theirs
	 * constructors private in order to became a standard utility classes.
	 */
	protected MimeType() {
		// We allow to override this class only so it may be used as container for other application
		// specific/needed mime types.
	}
}
