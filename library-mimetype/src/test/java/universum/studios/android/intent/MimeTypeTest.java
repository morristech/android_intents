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

import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import universum.studios.android.test.local.LocalTestCase;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author Martin Albedinsky
 */
public final class MimeTypeTest extends LocalTestCase {

	@Test(expected = IllegalAccessException.class)
	public void testInstantiation() throws Exception {
		// Act:
		MimeType.class.newInstance();
	}

	@Test(expected = InvocationTargetException.class)
	public void testInstantiationWithAccessibleConstructor() throws Exception {
		// Arrange:
		final Constructor<MimeType> constructor = MimeType.class.getDeclaredConstructor();
		constructor.setAccessible(true);
		// Act:
		constructor.newInstance();
	}

	@Test public void testTextTypes() {
		// Assert:
		assertThat(MimeType.TEXT, is("text/*"));
		assertThat(MimeType.TEXT_PLAIN, is("text/plain"));
		assertThat(MimeType.TEXT_HTML, is("text/html"));
	}

	@Test public void testImageTypes() {
		// Assert:
		assertThat(MimeType.IMAGE, is("image/*"));
		assertThat(MimeType.IMAGE_JPEG, is("image/jpeg"));
		assertThat(MimeType.IMAGE_PNG, is("image/png"));
		assertThat(MimeType.IMAGE_BITMAP, is("image/bmp"));
	}

	@Test public void testAudioTypes() {
		// Assert:
		assertThat(MimeType.AUDIO, is("audio/*"));
		assertThat(MimeType.AUDIO_MPEG, is("audio/mpeg"));
		assertThat(MimeType.AUDIO_MP3, is("audio/mp3"));
		assertThat(MimeType.AUDIO_MP4, is("audio/mp4"));
	}

	@Test public void testVideoTypes() {
		// Assert:
		assertThat(MimeType.VIDEO, is("video/*"));
		assertThat(MimeType.VIDEO_JPEG, is("video/jpeg"));
		assertThat(MimeType.VIDEO_MPEG, is("video/mpeg"));
		assertThat(MimeType.VIDEO_MP4, is("video/mp4"));
		assertThat(MimeType.VIDEO_3GP, is("video/3gp"));
		assertThat(MimeType.VIDEO_3GPP, is("video/3gpp"));
		assertThat(MimeType.VIDEO_3GPP2, is("video/3gpp2"));
	}
}