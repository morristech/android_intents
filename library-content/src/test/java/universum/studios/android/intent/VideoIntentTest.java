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

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

import org.junit.Test;

import java.io.File;
import java.util.Collections;
import java.util.List;

import universum.studios.android.test.local.RobolectricTestCase;

import static junit.framework.Assert.assertEquals;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;

/**
 * @author Martin Albedinsky
 */
public final class VideoIntentTest extends RobolectricTestCase {

	@Test
	public void testCreateGalleryIntent() {
		final Intent intent = VideoIntent.createGalleryIntent();
		assertThat(intent, is(not(nullValue())));
		assertThat(intent.getAction(), is(Intent.ACTION_GET_CONTENT));
		assertThat(intent.getType(), is(MimeType.VIDEO));
	}

	@Test
	public void testCreateCameraIntent() {
		final Intent intent = VideoIntent.createCameraIntent();
		assertThat(intent, is(not(nullValue())));
		assertThat(intent.getAction(), is(MediaStore.ACTION_VIDEO_CAPTURE));
		assertThat(intent.getParcelableExtra(MediaStore.EXTRA_OUTPUT), is(nullValue()));
	}

	@Test
	public void testCreateCameraIntentWithOutputFile() {
		final Intent intent = VideoIntent.createCameraIntent(new File("content://android/data/video", "camera-video.mp4"));
		assertThat(intent, is(not(nullValue())));
		assertThat(intent.getAction(), is(MediaStore.ACTION_VIDEO_CAPTURE));
		assertThat(intent.<Uri>getParcelableExtra(MediaStore.EXTRA_OUTPUT), is(Uri.fromFile(new File("content://android/data/video", "camera-video.mp4"))));
	}

	@Test
	public void testCreateCameraIntentWithNullOutputFile() {
		final Intent intent = VideoIntent.createCameraIntent((File) null);
		assertThat(intent, is(not(nullValue())));
		assertThat(intent.getAction(), is(MediaStore.ACTION_VIDEO_CAPTURE));
		assertThat(intent.getParcelableExtra(MediaStore.EXTRA_OUTPUT), is(nullValue()));
	}

	@Test
	public void testCreateCameraIntentWithOutputUri() {
		final Intent intent = VideoIntent.createCameraIntent(Uri.parse("content://android/data/video/camera-video.mp4"));
		assertThat(intent, is(not(nullValue())));
		assertThat(intent.getAction(), is(MediaStore.ACTION_VIDEO_CAPTURE));
		assertThat(intent.<Uri>getParcelableExtra(MediaStore.EXTRA_OUTPUT), is(Uri.parse("content://android/data/video/camera-video.mp4")));
	}

	@Test
	public void testWithDefaultHandlers() {
		final VideoIntent intent = new VideoIntent();
		assertEquals(Collections.EMPTY_LIST, intent.handlers());
		intent.withDefaultHandlers(application);
		final List<ContentIntent.ContentHandler> handlers = intent.handlers();
		assertThat(handlers, is(not(nullValue())));
		assertThat(handlers.size(), is(2));
		final ContentIntent.ContentHandler galleryHandler = handlers.get(0);
		assertThat(galleryHandler.name().toString(), is("Gallery"));
		assertThat(galleryHandler.requestCode(), is(VideoIntent.REQUEST_CODE_GALLERY));
		final ContentIntent.ContentHandler cameraHandler = handlers.get(1);
		assertThat(cameraHandler.name().toString(), is("Camera"));
		assertThat(cameraHandler.requestCode(), is(VideoIntent.REQUEST_CODE_CAMERA));
	}

	@Test
	public void testInput() {
		final VideoIntent intent = new VideoIntent();
		assertThat(intent.dataType(), is(nullValue()));
		intent.input(new File("file.tmp"));
		assertThat(intent.dataType(), is(MimeType.VIDEO));
	}

	@Test
	public void testInputWithNullValue() {
		final VideoIntent intent = new VideoIntent();
		intent.input(new File("file.tmp"));
		assertThat(intent.dataType(), is(not(nullValue())));
		intent.input((Uri) null);
		assertThat(intent.uri(), is(nullValue()));
		assertThat(intent.dataType(), is(nullValue()));
	}

	@Test
	public void testOutputBeforeWithDefaultHandlers() {
		final VideoIntent intent = new VideoIntent();
		intent.output(new File("file.tmp"));
		intent.withDefaultHandlers(application);
		final ContentIntent.ContentHandler cameraHandler = intent.handlers().get(1);
		assertThat(cameraHandler.intent().<Uri>getParcelableExtra(MediaStore.EXTRA_OUTPUT), is(Uri.fromFile(new File("file.tmp"))));
	}

	@Test
	public void testOutputAfterWithDefaultHandlers() {
		final VideoIntent intent = new VideoIntent();
		intent.withDefaultHandlers(application);
		final ContentIntent.ContentHandler cameraHandler = intent.handlers().get(1);
		final Intent cameraIntent = cameraHandler.intent();
		assertThat(cameraIntent.getParcelableExtra(MediaStore.EXTRA_OUTPUT), is(nullValue()));
		intent.output(new File("file.tmp"));
		assertThat(cameraIntent.<Uri>getParcelableExtra(MediaStore.EXTRA_OUTPUT), is(Uri.fromFile(new File("file.tmp"))));
	}

	@Test
	public void testNullOutputAfterWithDefaultHandlers() {
		final VideoIntent intent = new VideoIntent();
		intent.withDefaultHandlers(application);
		intent.output(new File("file.tmp"));
		final ContentIntent.ContentHandler cameraHandler = intent.handlers().get(1);
		final Intent cameraIntent = cameraHandler.intent();
		intent.output((Uri) null);
		assertThat(cameraIntent.<Uri>getParcelableExtra(MediaStore.EXTRA_OUTPUT), is(nullValue()));
	}
}