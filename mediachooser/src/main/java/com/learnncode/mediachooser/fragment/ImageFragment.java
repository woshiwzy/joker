

package com.learnncode.mediachooser.fragment;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


public class ImageFragment extends Fragment {
    private ArrayList<MediaModel> mGalleryModelList;
    private GridView mImageGridView;
    private View mView;
    private OnImageSelectedListener mCallback;
    private GridViewAdapter mImageAdapter;
    private Cursor mImageCursor;


    // Container Activity must implement this interface
    public interface OnImageSelectedListener {
        public void onImageSelected(int count);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnImageSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnImageSelectedListener");
        }
    }

    public ImageFragment() {
        setRetainInstance(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (mView == null) {
            mView = inflater.inflate(R.layout.view_grid_layout_media_chooser, container, false);

            mImageGridView = (GridView) mView.findViewById(R.id.gridViewFromMediaChooser);

            if (getArguments() != null) {
                initPhoneImages(getArguments().getString("name"));
            } else {
                initPhoneImages();
            }

        } else {

//			ViewParent pView = mView.getParent();
//			if(null!=pView){
//				((ViewGroup)pView).removeView(mView);
//			}
//			if(mImageAdapter == null || mImageAdapter.getCount() == 0){
//				Toast.makeText(getActivity(), getActivity().getString(R.string.no_media_file_available), Toast.LENGTH_SHORT).show();
//			}
        }

        //

        if (null != mImageAdapter) {
            mImageAdapter.clearAllSelect();
        }

//		MediaChooserConstants.SELECTED_MEDIA_COUNT=0;

        return mView;
    }


    private void initPhoneImages(String bucketName) {
        try {
            final String orderBy = MediaStore.Images.Media.DATE_TAKEN;
            String searchParams = null;
            String bucket = bucketName;
            searchParams = "bucket_display_name = \"" + bucket + "\"";

            final String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID};
            mImageCursor = getActivity().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, searchParams, null, orderBy + " DESC");

            setAdapter(mImageCursor);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initPhoneImages() {
        try {
            final String orderBy = MediaStore.Images.Media.DATE_TAKEN;
            final String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID};
            mImageCursor = getActivity().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, orderBy + " DESC");

            setAdapter(mImageCursor);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void setAdapter(Cursor imagecursor) {

        if (imagecursor.getCount() > 0) {

            mGalleryModelList = new ArrayList<MediaModel>();

            for (int i = 0; i < imagecursor.getCount(); i++) {
                imagecursor.moveToPosition(i);
                int dataColumnIndex = imagecursor.getColumnIndex(MediaStore.Images.Media.DATA);
                String file=imagecursor.getString(dataColumnIndex).toString();
                if(null!=file && (new File(file)).exists()){
                    MediaModel galleryModel = new MediaModel(file, false);
                    mGalleryModelList.add(galleryModel);
                }

            }

            mImageAdapter = new GridViewAdapter(getActivity(), 0, mGalleryModelList, false);
            mImageGridView.setAdapter(mImageAdapter);
        } else {
            Toast.makeText(getActivity(), getActivity().getString(R.string.no_media_file_available), Toast.LENGTH_SHORT).show();
        }

        mImageGridView.setOnItemLongClickListener(new OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                GridViewAdapter adapter = (GridViewAdapter) parent.getAdapter();
                MediaModel galleryModel = (MediaModel) adapter.getItem(position);
                File file = new File(galleryModel.url);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(file), "image/*");
                startActivity(intent);
                return true;
            }
        });

        mImageGridView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent,
                                    View view, int position, long id) {
                // update the mStatus of each category in the adapter
                GridViewAdapter adapter = (GridViewAdapter) parent.getAdapter();
                MediaModel galleryModel = (MediaModel) adapter.getItem(position);

                if (adapter.isSelect(galleryModel)) {
                    adapter.selectModel(galleryModel);
                } else {
//                    long size = MediaChooserConstants.ChekcMediaFileSize(new File(galleryModel.url.toString()), false);
                    if (adapter.getSlectMode().size() == MediaChooserConstants.MAX_IMG_LIMIT) {
                        Toast.makeText(getActivity(), "Max image files is " + MediaChooserConstants.MAX_IMG_LIMIT, Toast.LENGTH_LONG).show();
                        return;
                    }

//                    if (size > MediaChooserConstants.SELECTED_VIDEO_SIZE_IN_MB) {
//                        Toast.makeText(getActivity(), "This image file too large", Toast.LENGTH_LONG).show();
//                        return;
//                    }
                    adapter.selectModel(galleryModel);
                }


                if (mCallback != null) {
                    mCallback.onImageSelected(adapter.getSlectMode().size());
                    Intent intent = new Intent();
                    intent.putStringArrayListExtra("list", adapter.getSelectList());
                    getActivity().setResult(Activity.RESULT_OK, intent);
                }


            }
        });
    }

    public ArrayList<String> getSelectedImageList() {
        if(null==mImageAdapter){

            return  new ArrayList<String>(0);
        }
        return mImageAdapter.getSelectList();
    }

    public void addItem(String item) {
        if (mImageAdapter != null) {
            MediaModel model = new MediaModel(item, false);
            mGalleryModelList.add(0, model);
            mImageAdapter.notifyDataSetChanged();
        } else {
            initPhoneImages();
        }
    }
}
