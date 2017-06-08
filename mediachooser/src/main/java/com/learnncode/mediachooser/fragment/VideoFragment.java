/*
 * Copyright 2013 - learnNcode (learnncode@gmail.com)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */


package com.learnncode.mediachooser.fragment;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.GridView;
import android.widget.Toast;

import com.learnncode.mediachooser.MediaChooserConstants;
import com.learnncode.mediachooser.MediaModel;
import com.learnncode.mediachooser.R;
import com.learnncode.mediachooser.adapter.GridViewAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class VideoFragment extends Fragment implements OnScrollListener {

    private final static Uri MEDIA_EXTERNAL_CONTENT_URI = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
    private final static String MEDIA_DATA = MediaStore.Video.Media.DATA;

    private GridViewAdapter mVideoAdapter;
    private GridView mVideoGridView;
    private Cursor mCursor;
    private int mDataColumnIndex;
    private ArrayList<MediaModel> mGalleryModelList;
    private View mView;
    private OnVideoSelectedListener mCallback;
    private MediaMetadataRetriever retriever = null;


    // Container Activity must implement this interface
    public interface OnVideoSelectedListener {
        public void onVideoSelected(int count);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnVideoSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnVideoSelectedListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public VideoFragment() {
        setRetainInstance(true);
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (mView == null) {
            mView = inflater.inflate(R.layout.view_grid_layout_media_chooser, container, false);

            mVideoGridView = (GridView) mView.findViewById(R.id.gridViewFromMediaChooser);

            if (getArguments() != null) {
                initVideos(getArguments().getString("name"));
            } else {
                initVideos();
            }

        } else {

//			ViewParent pView = mView.getParent();
//			if(null!=pView){
//				((ViewGroup) pView).removeView(mView);
//			}
//			if(mVideoAdapter == null || mVideoAdapter.getCount() == 0){
//				Toast.makeText(getActivity(), getActivity().getString(R.string.no_media_file_available), Toast.LENGTH_SHORT).show();
//			}
        }

        return mView;
    }


    private void initVideos(String bucketName) {

        try {
            final String orderBy = MediaStore.Video.Media.DATE_TAKEN;
            String searchParams = null;
            searchParams = "bucket_display_name = \"" + bucketName + "\"";

            final String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Video.Media._ID};
            mCursor = getActivity().getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, columns, searchParams, null, orderBy + " DESC");
            setAdapter();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initVideos() {

        try {
            final String orderBy = MediaStore.Images.Media.DATE_TAKEN;
            //Here we set up a string array of the thumbnail ID column we want to get back

            String[] proj = {MediaStore.Video.Media.DATA, MediaStore.Video.Media._ID};

            mCursor = getActivity().getContentResolver().query(MEDIA_EXTERNAL_CONTENT_URI, proj, null, null, orderBy + " DESC");
            setAdapter();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setAdapter() {
        int count = mCursor.getCount();

        if (count > 0) {
            mDataColumnIndex = mCursor.getColumnIndex(MEDIA_DATA);
            //move position to first element
            mCursor.moveToFirst();

            mGalleryModelList = new ArrayList<MediaModel>();
            for (int i = 0; i < count; i++) {
                mCursor.moveToPosition(i);

                String url = mCursor.getString(mDataColumnIndex);
                if (url.endsWith("mp4") && (new File(url)).exists() && !url.endsWith("compres.mp4")) {
                    mGalleryModelList.add(new MediaModel(url, false));
                }

            }

            mVideoAdapter = new GridViewAdapter(getActivity(), 0, mGalleryModelList, true);
            mVideoAdapter.videoFragment = this;
            mVideoGridView.setAdapter(mVideoAdapter);
            mVideoGridView.setOnScrollListener(this);
        } else {
            Toast.makeText(getActivity(), getActivity().getString(R.string.no_media_file_available), Toast.LENGTH_SHORT).show();

        }


        mVideoGridView.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                GridViewAdapter adapter = (GridViewAdapter) parent.getAdapter();
                MediaModel galleryModel = (MediaModel) adapter.getItem(position);
                File file = new File(galleryModel.url);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(file), "video/*");
                startActivity(intent);
                return false;
            }
        });


        mVideoGridView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // update the mStatus of each category in the adapter
                GridViewAdapter adapter = (GridViewAdapter) parent.getAdapter();
                MediaModel galleryModel = (MediaModel) adapter.getItem(position);

                if (adapter.isSelect(galleryModel)) {
                    adapter.selectModel(galleryModel);
                } else {


                    int second = 0;

                    try {
                        if (null == retriever) {
                            retriever = new MediaMetadataRetriever();
                        }

                        if (galleryModel.url.startsWith("http")) {
                            retriever.setDataSource(galleryModel.url, new HashMap<String, String>());
                        } else {
                            retriever.setDataSource(galleryModel.url);
                        }

                        long mmseoncds = Long.parseLong(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
                        second = (int) (mmseoncds / 1000);
                    } catch (Exception e) {
                    }

                    if (adapter.getSlectMode().size() == MediaChooserConstants.MAX_VIDEO_LIMIT) {
                        Toast.makeText(getActivity(), "Max video files is " + MediaChooserConstants.MAX_VIDEO_LIMIT, Toast.LENGTH_LONG).show();
                        return;
                    }


                    long size = MediaChooserConstants.ChekcMediaFileSize(new File(galleryModel.url.toString()), true);

//                    if (size == 0) {
//                        Toast.makeText(getActivity(), "This is file is video", Toast.LENGTH_LONG).show();
//                        return;
//                    }

                    if (/*size > MediaChooserConstants.SELECTED_VIDEO_SIZE_IN_MB ||*/ second > MediaChooserConstants.SELECTED_VIDEO_SIZE_DURATION) {
                        Toast.makeText(getActivity(), "Please select a video within 60 seconds！", Toast.LENGTH_LONG).show();
                        return;
                    }
                    adapter.selectModel(galleryModel);
                }

                if (mCallback != null) {
                    mCallback.onVideoSelected(adapter.getSlectMode().size());
                    Intent intent = new Intent();
                    intent.putStringArrayListExtra("list", adapter.getSelectList());
                    getActivity().setResult(Activity.RESULT_OK, intent);
                }

            }
        });

    }

    public void addItem(String item) {
        if (mVideoAdapter != null) {
            MediaModel model = new MediaModel(item, false);
            mGalleryModelList.add(0, model);
            mVideoAdapter.notifyDataSetChanged();
        } else {
            initVideos();
        }
    }


    public GridViewAdapter getAdapter() {
        if (mVideoAdapter != null) {
            return mVideoAdapter;
        }
        return null;
    }

    public ArrayList<String> getSelectedVideoList() {
        if (null == mVideoAdapter) {
            return new ArrayList<String>();
        }
        return mVideoAdapter.getSelectList();
    }

    public void onScrollStateChanged(AbsListView view, int scrollState) {
        //		if (view.getId() == android.R.id.list) {
        if (view == mVideoGridView) {
            // Set scrolling to true only if the user has flinged the
            // ListView away, hence we skip downloading a series
            // of unnecessary bitmaps that the user probably
            // just want to skip anyways. If we scroll slowly it
            // will still download bitmaps - that means
            // that the application won't wait for the user
            // to lift its finger off the screen in order to
            // download.
            if (scrollState == SCROLL_STATE_FLING) {
                //chk
            } else {
                mVideoAdapter.notifyDataSetChanged();
            }
        }
    }

    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {

    }


}

