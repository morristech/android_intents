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
package universum.studios.android.samples.intent.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import universum.studios.android.widget.adapter.SimpleRecyclerAdapter;

/**
 * @author Martin Albedinsky
 */
public final class IntentsAdapter extends SimpleRecyclerAdapter<IntentSample, IntentsAdapter.ItemHolder> {

	@SuppressWarnings("unused")
	private static final String TAG = "IntentsAdapter";

	public IntentsAdapter(@NonNull Context context) {
		super(context);
	}

	@Override
	public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		return null;
	}

	@Override
	public void onBindViewHolder(@NonNull ItemHolder holder, int position) {

	}

	final class ItemHolder extends RecyclerView.ViewHolder {



		private ItemHolder(View itemView) {
			super(itemView);
		}
	}
}
