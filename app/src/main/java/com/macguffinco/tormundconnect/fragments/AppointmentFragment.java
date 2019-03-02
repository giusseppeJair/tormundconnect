package com.macguffinco.tormundconnect.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.macguffinco.tormundconnect.Logic.AppointmentManager;
import com.macguffinco.tormundconnect.Logic.TormundManager;
import com.macguffinco.tormundconnect.R;
import com.macguffinco.tormundconnect.Utilities.SwipeButtonsControls.SwipeControllerActions;
import com.macguffinco.tormundconnect.Utilities.SwipeButtonsControls.SwipesController;
import com.macguffinco.tormundconnect.fragments.CameraControl.CameraFragment;
import com.macguffinco.tormundconnect.fragments.CameraControl.TaskDownloadCustomerImagesToday;
import com.macguffinco.tormundconnect.fragments.dummy.DummyContent;
import com.macguffinco.tormundconnect.fragments.dummy.DummyContent.DummyItem;
import com.macguffinco.model.appointments.AppointmentDC;
import com.macguffinco.model.comercial.CustomerDC;
import com.macguffinco.model.comercial.ServiceDC;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.jar.Attributes;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class AppointmentFragment extends Fragment {

    // TODO: Customize parameters
    private int mColumnCount = 1;
    public ArrayList <AppointmentDC> lista=new ArrayList<AppointmentDC>();
    AppointmentRecyclerViewAdapter adapter=null;
    RecyclerView recyclerView;
    private static final int REQUEST_CAMERA_PERMISSION = 1;
    private static final int REQUEST_READ_EXT_STORAGE_PERMISSION = 2;
    private static final int REQUEST_WRITE_EXT_STORAGE_PERMISSION = 3;

    private OnListFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public AppointmentFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  requestCameraPermission();

    }



    private void requestCameraPermission() {
        if (!shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        }
        if (!shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_READ_EXT_STORAGE_PERMISSION);
        }
        if (!shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_EXT_STORAGE_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length != 1 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                CameraFragment.ErrorDialog.newInstance(getString(R.string.request_permission))
                        .show(getChildFragmentManager(), "Dialog");
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

        if (requestCode == REQUEST_READ_EXT_STORAGE_PERMISSION) {
            if (grantResults.length != 1 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                CameraFragment.ErrorDialog.newInstance(getString(R.string.request_permission))
                        .show(getChildFragmentManager(), "Dialog");
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

        if (requestCode == REQUEST_WRITE_EXT_STORAGE_PERMISSION) {
            if (grantResults.length != 1 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                CameraFragment.ErrorDialog.newInstance(getString(R.string.request_permission))
                        .show(getChildFragmentManager(), "Dialog");
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }


            new TaskAppointmentFragment(getContext(),this,mListener).execute("", "", "");
           // new TaskDownloadCustomerImagesToday(getContext(),this,mListener).execute("", "", "");





            final SwipesController swipeController = new SwipesController(new SwipeControllerActions() {
                @Override
                public void onRightClicked(int position) {
//                    adapter.players.remove(position);
//                    adapter.notifyItemRemoved(position);
//                    adapter.notifyItemRangeChanged(position, adapter.getItemCount());
                }

                @Override
                public void onLeftClicked(int position) {
                    //Open Camera
                    AppointmentDC dateDC=lista.get(position);
                    TormundManager.goCameraActivity(getContext(),dateDC);

//                    adapter.players.remove(position);
//                    adapter.notifyItemRemoved(position);
//                    adapter.notifyItemRangeChanged(position, adapter.getItemCount());
                }

            });

            ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeController);
            itemTouchhelper.attachToRecyclerView(recyclerView);

            recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
                @Override
                public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                    swipeController.onDraw(c);
                }
            });

        }



        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }



    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(AppointmentDC itemAppointment);
    }
}
