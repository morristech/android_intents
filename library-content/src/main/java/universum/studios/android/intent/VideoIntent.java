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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.File;

/**
 * A {@link ContentIntent} implementation providing API for building and starting of intents to obtain
 * or preview a video content.
 *
 * @author Martin Albedinsky
 */
public class VideoIntent extends ContentIntent<VideoIntent> {

	/*
	 * Constants ===================================================================================
	 */

	/**
	 * Log TAG.
	 */
	// private static final String TAG = "VideoIntent";

	/**
	 * Flag to identify request code used to obtain video from gallery.
	 */
	public static final int REQUEST_CODE_GALLERY = 0x5011;

	/**
	 * Flag to identify request code used to obtain video using camera.
	 */
	public static final int REQUEST_CODE_CAMERA = 0x5012;

	/**
	 * Default format for video file name.
	 * <p>
	 * Constant value: <b>VIDEO_%s</b>
	 */
	public static final String VIDEO_FILE_NAME_FORMAT = "VIDEO_%s";

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
	 * Camera intent handler.
	 */
	private ContentHandler mCameraHandler;

	/*
	 * Constructors ================================================================================
	 */

	/*
	 * Methods =====================================================================================
	 */

	/**
	 * Creates a new instance of Intent with {@link Intent#ACTION_GET_CONTENT} and {@link MimeType#VIDEO}
	 * MIME type that can be used to launch a gallery app (depends on user's choice) to pick one of
	 * available videos.
	 *
	 * @return New gallery intent instance.
	 */
	@NonNull
	public static Intent createGalleryIntent() {
		return new Intent(Intent.ACTION_GET_CONTENT).setType(MimeType.VIDEO);
	}

	/**
	 * Same as {@link #createCameraIntent(File)} with {@code null} for <var>outputFile</var>
	 * parameter.
	 */
	@NonNull
	public static Intent createCameraIntent() {
		return createCameraIntent((Uri) null);
	}

	/**
	 * Same as {@link #createCameraIntent(Uri)} with <var>outputUri</var> created from the given
	 * <var>outputFile</var> if not {@code null}.
	 *
	 * @param outputFile The desired file used to crate the output Uri.
	 * @see #createCameraIntent()
	 */
	@NonNull
	public static Intent createCameraIntent(@Nullable final File outputFile) {
		return createCameraIntent(outputFile == null ? null : Uri.fromFile(outputFile));
	}

	/**
	 * Creates a new instance of Intent with {@link MediaStore#ACTION_VIDEO_CAPTURE} that can be used
	 * to launch a camera app to capture a single video.
	 *
	 * @param outputUri If not {@code null}, it will be attached to intent as {@link MediaStore#EXTRA_OUTPUT},
	 *                  so when the result is received in for example {@link Activity#onActivityResult(int, int, Intent)},
	 *                  the <var>outputUri</var> will address to a file which contains the currently
	 *                  captured video.
	 * @return New camera intent instance.
	 * @see #createCameraIntent(File)
	 */
	@NonNull
	public static Intent createCameraIntent(@Nullable final Uri outputUri) {
		final Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
		if (outputUri != null) {
			intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
		}
		return intent;
	}

	/**
	 * Same as {@link #createVideoFile(String)} with <var>fileName</var> in {@link #VIDEO_FILE_NAME_FORMAT}
	 * format with the current time stamp provided by {@link #createContentFileTimeStamp() }.
	 */
	@Nullable
	public static File createVideoFile() {
		return createVideoFile(String.format(VIDEO_FILE_NAME_FORMAT, createContentFileTimeStamp()));
	}

	/**
	 * Same as {@link #createContentFile(String,  String)} with <b>.mp4</b> suffix for the specified
	 * <var>fileName</var> (if it does not contain any) and {@link Environment#DIRECTORY_MOVIES} as
	 * <var>externalDirectoryType</var>.
	 *
	 * @param fileName The desired name for the video file.
	 * @see #createVideoFile()
	 */
	@Nullable
	public static File createVideoFile(@NonNull final String fileName) {
		return createContentFile(appendDefaultFileSuffixIfNotPresented(fileName, ".mp4"), Environment.DIRECTORY_MOVIES);
	}

	/**
	 * Adds two default {@link ContentHandler}s. One for {@link #REQUEST_CODE_GALLERY} and second one
	 * for {@link #REQUEST_CODE_CAMERA}.
	 */
	@Override
	@SuppressWarnings("ConstantConditions")
	public VideoIntent withDefaultHandlers(@NonNull final Context context) {
		withHandlers(
				onCreateGalleryHandler(context.getResources()),
				mCameraHandler = onCreateCameraHandler(context.getResources())
		);
		if (mUri == null) {
			mCameraHandler.intent.removeExtra(MediaStore.EXTRA_OUTPUT);
		} else {
			mCameraHandler.intent.putExtra(MediaStore.EXTRA_OUTPUT, mUri);
		}
		return this;
	}

	/**
	 * Invoked from {@link #withDefaultHandlers(Context)} to create gallery intent handler.
	 * <p>
	 * This method may be override to create a handler with a localized name.
	 *
	 * @param resources Application resources.
	 * @return Content handler with gallery intent.
	 * @see #onCreateCameraHandler(Resources)
	 */
	@NonNull
	protected ContentHandler onCreateGalleryHandler(@NonNull final Resources resources) {
		return new ContentHandler(
				"Gallery",
				createGalleryIntent()
		).requestCode(REQUEST_CODE_GALLERY);
	}

	/**
	 * Invoked from {@link #withDefaultHandlers(Context)} to create camera intent handler.
	 * <p>
	 * This method may be override to create a handler with a localized name.
	 *
	 * @param resources Application resources.
	 * @return Content handler with camera intent.
	 * @see #onCreateGalleryHandler(Resources)
	 */
	@NonNull
	protected ContentHandler onCreateCameraHandler(@NonNull final Resources resources) {
		return new ContentHandler(
				"Camera",
				createCameraIntent()
		).requestCode(REQUEST_CODE_CAMERA);
	}

	/**
	 * If the passed <var>uri</var> is not {@code null}, the current data (MIME) type will be by
	 * default set to {@link MimeType#VIDEO}.
	 */
	@Override
	public VideoIntent input(@Nullable Uri uri) {
		super.input(uri);
		if (uri != null) {
			this.mDataType = MimeType.VIDEO;
		}
		return this;
	}

	/**
	 * Sets an output uri for a video to be captured by the camera.
	 *
	 * @param uri If not {@code null}, it will be attached to {@link ContentHandler} holding intent
	 *            with the {@link #REQUEST_CODE_CAMERA} request code, so when the camera handler's
	 *            item will be selected within a <b>chooser dialog</b> and user confirms his captured
	 *            video, result for this action will be received in for example {@link Activity#onActivityResult(int, int, Intent)}.
	 *            The passed <var>uri</var> will then address to a file which contains the currently
	 *            captured video.
	 * @return This intent builder to allow methods chaining.
	 */
	@Override
	public VideoIntent output(@Nullable final Uri uri) {
		super.output(uri);
		if (mCameraHandler != null) {
			if (mUri == null) {
				mCameraHandler.intent.removeExtra(MediaStore.EXTRA_OUTPUT);
			} else {
				mCameraHandler.intent.putExtra(MediaStore.EXTRA_OUTPUT, mUri);
			}
		}
		return this;
	}

	/*
	 * Inner classes ===============================================================================
	 */
}