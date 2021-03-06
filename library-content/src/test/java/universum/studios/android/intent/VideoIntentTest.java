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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author Martin Albedinsky
 */
public final class VideoIntentTest extends RobolectricTestCase {

	@Test public void testCreateGalleryIntent() {
		// Act:
		final Intent intent = VideoIntent.createGalleryIntent();
		// Assert:
		assertThat(intent, is(notNullValue()));
		assertThat(intent.getAction(), is(Intent.ACTION_GET_CONTENT));
		assertThat(intent.getType(), is(MimeType.VIDEO));
	}

	@Test public void testCreateCameraIntent() {
		// Act:
		final Intent intent = VideoIntent.createCameraIntent();
		// Assert:
		assertThat(intent, is(notNullValue()));
		assertThat(intent.getAction(), is(MediaStore.ACTION_VIDEO_CAPTURE));
		assertThat(intent.getParcelableExtra(MediaStore.EXTRA_OUTPUT), is(nullValue()));
	}

	@Test public void testCreateCameraIntentWithOutputFile() {
		// Arrange:
		final File outputFile = new File("content://android/data/video", "camera-video.mp4");
		// Act:
		final Intent intent = VideoIntent.createCameraIntent(outputFile);
		// Assert:
		assertThat(intent, is(notNullValue()));
		assertThat(intent.getAction(), is(MediaStore.ACTION_VIDEO_CAPTURE));
		assertThat(intent.<Uri>getParcelableExtra(MediaStore.EXTRA_OUTPUT), is(Uri.fromFile(outputFile)));
	}

	@Test public void testCreateCameraIntentWithNullOutputFile() {
		// Act:
		final Intent intent = VideoIntent.createCameraIntent((File) null);
		// Assert:
		assertThat(intent, is(notNullValue()));
		assertThat(intent.getAction(), is(MediaStore.ACTION_VIDEO_CAPTURE));
		assertThat(intent.getParcelableExtra(MediaStore.EXTRA_OUTPUT), is(nullValue()));
	}

	@Test public void testCreateCameraIntentWithOutputUri() {
		// Arrange:
		final Uri outputUri = Uri.parse("content://android/data/video/camera-video.mp4");
		// Act:
		final Intent intent = VideoIntent.createCameraIntent(outputUri);
		// Assert:
		assertThat(intent, is(notNullValue()));
		assertThat(intent.getAction(), is(MediaStore.ACTION_VIDEO_CAPTURE));
		assertThat(intent.<Uri>getParcelableExtra(MediaStore.EXTRA_OUTPUT), is(outputUri));
	}

	@Test public void testInstantiation() {
		// Act:
		final VideoIntent intent = new VideoIntent();
		// Assert:
		assertThat(intent.handlers(), is(Collections.EMPTY_LIST));
		assertThat(intent.dataType(), is(nullValue()));
	}

	@Test public void testWithDefaultHandlers() {
		// Arrange:
		final VideoIntent intent = new VideoIntent();
		// Act:
		intent.withDefaultHandlers(context);
		// Assert:
		final List<ContentIntent.ContentHandler> handlers = intent.handlers();
		assertThat(handlers, is(notNullValue()));
		assertThat(handlers.size(), is(2));
		final ContentIntent.ContentHandler galleryHandler = handlers.get(0);
		assertThat(galleryHandler.name().toString(), is("Gallery"));
		assertThat(galleryHandler.requestCode(), is(VideoIntent.REQUEST_CODE_GALLERY));
		final ContentIntent.ContentHandler cameraHandler = handlers.get(1);
		assertThat(cameraHandler.name().toString(), is("Camera"));
		assertThat(cameraHandler.requestCode(), is(VideoIntent.REQUEST_CODE_CAMERA));
	}

	@Test public void testInput() {
		// Arrange:
		final VideoIntent intent = new VideoIntent();
		// Act:
		intent.input(new File("input.tmp"));
		// Assert:
		assertThat(intent.dataType(), is(MimeType.VIDEO));
	}

	@Test public void testInputWithNullValue() {
		// Arrange:
		final VideoIntent intent = new VideoIntent();
		intent.input(new File("input.tmp"));
		assertThat(intent.dataType(), is(notNullValue()));
		// Act:
		intent.input((Uri) null);
		// Assert:
		assertThat(intent.uri(), is(nullValue()));
		assertThat(intent.dataType(), is(nullValue()));
	}

	@Test public void testOutputBeforeWithDefaultHandlers() {
		// Arrange:
		final File outputFile = new File("output.tmp");
		final VideoIntent intent = new VideoIntent();
		// Act:
		intent.output(outputFile);
		intent.withDefaultHandlers(context);
		final ContentIntent.ContentHandler cameraHandler = intent.handlers().get(1);
		assertThat(cameraHandler.intent().<Uri>getParcelableExtra(MediaStore.EXTRA_OUTPUT), is(Uri.fromFile(outputFile)));
	}

	@Test public void testOutputAfterWithDefaultHandlers() {
		// Arrange:
		final File outputFile = new File("output.tmp");
		final VideoIntent intent = new VideoIntent();
		intent.withDefaultHandlers(context);
		final ContentIntent.ContentHandler cameraHandler = intent.handlers().get(1);
		final Intent cameraIntent = cameraHandler.intent();
		assertThat(cameraIntent.getParcelableExtra(MediaStore.EXTRA_OUTPUT), is(nullValue()));
		// Act:
		intent.output(outputFile);
		// Assert:
		assertThat(cameraIntent.<Uri>getParcelableExtra(MediaStore.EXTRA_OUTPUT), is(Uri.fromFile(outputFile)));
	}

	@Test public void testNullOutputAfterWithDefaultHandlers() {
		// Arrange:
		final VideoIntent intent = new VideoIntent();
		intent.withDefaultHandlers(context);
		intent.output(new File("output.tmp"));
		final ContentIntent.ContentHandler cameraHandler = intent.handlers().get(1);
		final Intent cameraIntent = cameraHandler.intent();
		// Act:
		intent.output((Uri) null);
		// Assert:
		assertThat(cameraIntent.<Uri>getParcelableExtra(MediaStore.EXTRA_OUTPUT), is(nullValue()));
	}
}