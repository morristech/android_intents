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
 * <h3>Text based types:</h3>
 * <ul>
 * <li>{@link #TEXT}</li>
 * <li>{@link #TEXT_PLAIN}</li>
 * <li>{@link #TEXT_HTML}</li>
 * </ul>
 *
 * <h3>Image based types:</h3>
 * <ul>
 * <li>{@link #IMAGE}</li>
 * <li>{@link #IMAGE_JPEG}</li>
 * <li>{@link #IMAGE_PNG}</li>
 * <li>{@link #IMAGE_BITMAP}</li>
 * </ul>
 *
 * <h3>Audio based types:</h3>
 * <ul>
 * <li>{@link #AUDIO}</li>
 * <li>{@link #AUDIO_MP3}</li>
 * <li>{@link #AUDIO_MP4}</li>
 * <li>{@link #AUDIO_MPEG}</li>
 * </ul>
 *
 * <h3>Video based types</h3>
 * <ul>
 * <li>{@link #VIDEO}</li>
 * <li>{@link #VIDEO_MP4}</li>
 * <li>{@link #VIDEO_MPEG}</li>
 * <li>{@link #VIDEO_JPEG}</li>
 * <li>{@link #VIDEO_3GP}</li>
 * <li>{@link #VIDEO_3GP2}</li>
 * </ul>
 *
 * @author Martin Albedinsky
 */
public class MimeType {

	/**
	 */
	public static final String TEXT = "text/*";

	/**
	 */
	public static final String TEXT_PLAIN = "text/plain";

	/**
	 */
	public static final String TEXT_HTML = "text/html";

	/**
	 */
	public static final String IMAGE = "image/*";

	/**
	 */
	public static final String IMAGE_JPEG = "image/jpeg";

	/**
	 */
	public static final String IMAGE_PNG = "image/png";

	/**
	 */
	public static final String IMAGE_BITMAP = "image/bmp";

	/**
	 */
	public static final String AUDIO = "audio/*";

	/**
	 */
	public static final String AUDIO_MPEG = "audio/mpeg";

	/**
	 */
	public static final String AUDIO_MP3 = "audio/mp3";

	/**
	 */
	public static final String AUDIO_MP4 = "audio/mp4";

	/**
	 */
	public static final String VIDEO = "video/*";

	/**
	 */
	public static final String VIDEO_JPEG = "video/jpeg";

	/**
	 */
	public static final String VIDEO_MPEG = "video/mpeg";

	/**
	 */
	public static final String VIDEO_MP4 = "video/mp4";

	/**
	 */
	public static final String VIDEO_3GP = "video/3gpp";

	/**
	 */
	public static final String VIDEO_3GP2 = "video/3gpp2";

	/**
	 * Defines an annotation for determining set of available mime-type values specified in {@link MimeType}.
	 */
	@StringDef({
			TEXT, TEXT_PLAIN, TEXT_HTML,
			IMAGE, IMAGE_JPEG, IMAGE_PNG, IMAGE_BITMAP,
			AUDIO, AUDIO_MPEG, AUDIO_MP3, AUDIO_MP4,
			VIDEO, VIDEO_JPEG, VIDEO_MPEG, VIDEO_MP4, VIDEO_3GP, VIDEO_3GP2
	})
	@Retention(RetentionPolicy.SOURCE)
	public @interface Value {
	}
}
