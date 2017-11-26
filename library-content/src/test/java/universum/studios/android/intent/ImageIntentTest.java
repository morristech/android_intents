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

import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

import org.junit.Test;

import java.io.File;
import java.util.Collections;
import java.util.List;

import universum.studios.android.test.local.RobolectricTestCase;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;

/**
 * @author Martin Albedinsky
 */
@SuppressWarnings("ResultOfMethodCallIgnored")
public final class ImageIntentTest extends RobolectricTestCase {

	@Test
	public void testCreateGalleryIntent() {
		final Intent intent = ImageIntent.createGalleryIntent();
		assertThat(intent, is(not(nullValue())));
		assertThat(intent.getAction(), is(Intent.ACTION_GET_CONTENT));
		assertThat(intent.getType(), is(MimeType.IMAGE));
	}

	@Test
	public void testCreateCameraIntent() {
		final Intent intent = ImageIntent.createCameraIntent();
		assertThat(intent, is(not(nullValue())));
		assertThat(intent.getAction(), is(MediaStore.ACTION_IMAGE_CAPTURE));
		assertThat(intent.getParcelableExtra(MediaStore.EXTRA_OUTPUT), is(nullValue()));
	}

	@Test
	public void testCreateCameraIntentWithOutputFile() {
		final Intent intent = ImageIntent.createCameraIntent(new File("content://android/data/images", "camera-image.jpg"));
		assertThat(intent, is(not(nullValue())));
		assertThat(intent.getAction(), is(MediaStore.ACTION_IMAGE_CAPTURE));
		assertThat(intent.<Uri>getParcelableExtra(MediaStore.EXTRA_OUTPUT), is(Uri.fromFile(new File("content://android/data/images", "camera-image.jpg"))));
	}

	@Test
	public void testCreateCameraIntentWithNullOutputFile() {
		final Intent intent = ImageIntent.createCameraIntent((File) null);
		assertThat(intent, is(not(nullValue())));
		assertThat(intent.getAction(), is(MediaStore.ACTION_IMAGE_CAPTURE));
		assertThat(intent.getParcelableExtra(MediaStore.EXTRA_OUTPUT), is(nullValue()));
	}

	@Test
	public void testCreateCameraIntentWithOutputUri() {
		final Intent intent = ImageIntent.createCameraIntent(Uri.parse("content://android/data/images/camera-image.jpg"));
		assertThat(intent, is(not(nullValue())));
		assertThat(intent.getAction(), is(MediaStore.ACTION_IMAGE_CAPTURE));
		assertThat(intent.<Uri>getParcelableExtra(MediaStore.EXTRA_OUTPUT), is(Uri.parse("content://android/data/images/camera-image.jpg")));
	}

	@Test
	public void testWithDefaultHandlers() {
		final ImageIntent intent = new ImageIntent();
		assertThat(intent.handlers(), is(Collections.EMPTY_LIST));
		intent.withDefaultHandlers(mApplication);
		final List<ContentIntent.ContentHandler> handlers = intent.handlers();
		assertThat(handlers, is(not(nullValue())));
		assertThat(handlers.size(), is(2));
		final ContentIntent.ContentHandler galleryHandler = handlers.get(0);
		assertThat(galleryHandler.name().toString(), is("Gallery"));
		assertThat(galleryHandler.requestCode(), is(ImageIntent.REQUEST_CODE_GALLERY));
		final ContentIntent.ContentHandler cameraHandler = handlers.get(1);
		assertThat(cameraHandler.name().toString(), is("Camera"));
		assertThat(cameraHandler.requestCode(), is(ImageIntent.REQUEST_CODE_CAMERA));
	}

	@Test
	public void testInput() {
		final ImageIntent intent = new ImageIntent();
		assertThat(intent.dataType(), is(nullValue()));
		intent.input(new File("file.tmp"));
		assertThat(intent.dataType(), is(MimeType.IMAGE));
	}

	@Test
	public void testInputWithNullValue() {
		final ImageIntent intent = new ImageIntent();
		intent.input(new File("file.tmp"));
		assertThat(intent.dataType(), is(not(nullValue())));
		intent.input((Uri) null);
		assertThat(intent.uri(), is(nullValue()));
		assertThat(intent.dataType(), is(nullValue()));
	}

	@Test
	public void testOutputBeforeWithDefaultHandlers() {
		final ImageIntent intent = new ImageIntent();
		intent.output(new File("file.tmp"));
		intent.withDefaultHandlers(mApplication);
		final ContentIntent.ContentHandler cameraHandler = intent.handlers().get(1);
		assertThat(cameraHandler.intent().<Uri>getParcelableExtra(MediaStore.EXTRA_OUTPUT), is(Uri.fromFile(new File("file.tmp"))));
	}

	@Test
	public void testOutputAfterWithDefaultHandlers() {
		final ImageIntent intent = new ImageIntent();
		intent.withDefaultHandlers(mApplication);
		final ContentIntent.ContentHandler cameraHandler = intent.handlers().get(1);
		final Intent cameraIntent = cameraHandler.intent();
		assertThat(cameraIntent.getParcelableExtra(MediaStore.EXTRA_OUTPUT), is(nullValue()));
		intent.output(new File("file.tmp"));
		assertThat(cameraIntent.<Uri>getParcelableExtra(MediaStore.EXTRA_OUTPUT), is(Uri.fromFile(new File("file.tmp"))));
	}

	@Test
	public void testNullOutputAfterWithDefaultHandlers() {
		final ImageIntent intent = new ImageIntent();
		intent.withDefaultHandlers(mApplication);
		intent.output(new File("file.tmp"));
		final ContentIntent.ContentHandler cameraHandler = intent.handlers().get(1);
		final Intent cameraIntent = cameraHandler.intent();
		intent.output((Uri) null);
		assertThat(cameraIntent.<Uri>getParcelableExtra(MediaStore.EXTRA_OUTPUT), is(nullValue()));
	}
}
