package com.example.codegladiators;
import android.content.Context;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.codegladiators.R;
import com.example.codegladiators.ResultListAdapter;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.HttpsCallableResult;
import com.google.gson.JsonObject;
import com.google.zxing.WriterException;
import com.here.android.mpa.common.GeoCoordinate;
import com.here.android.mpa.common.GeoPosition;
import com.here.android.mpa.common.Image;
import com.here.android.mpa.common.OnEngineInitListener;
import com.here.android.mpa.common.PositioningManager;
import com.here.android.mpa.mapping.Map;
import com.here.android.mpa.mapping.MapFragment;
import com.here.android.mpa.mapping.MapMarker;
import com.here.android.mpa.mapping.MapObject;
import com.here.android.mpa.mapping.SupportMapFragment;
import com.here.android.mpa.search.DiscoveryResult;
import com.here.android.mpa.search.DiscoveryResultPage;
import com.here.android.mpa.search.ErrorCode;
import com.here.android.mpa.search.PlaceLink;
import com.here.android.mpa.search.ResultListener;
import com.here.android.mpa.search.SearchRequest;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

import static android.content.Context.WINDOW_SERVICE;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private PositioningManager posManager;
    boolean paused;
    String address;
    String lat,lng;
    private List<MapObject> m_mapObjectList = new ArrayList<>();
    ResultListAdapter listAdapter;
    DatabaseReference myRef;
    CardView finding_card;
    RelativeLayout details;
    TextView finding;
    ProgressBar pg_bar;
    RatingBar ratingBar;
    ImageView qr,call;
    QRGEncoder qrgEncoder;

    public static List<DiscoveryResult> s_ResultList;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    ListView listView;
    Button book_now;
    MapFragment m_mapFragment;
    Map m_map;
    private OnFragmentInteractionListener mListener;
    FirebaseDatabase database;
    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, null);

        listView = view.findViewById(R.id.listview);
        finding_card = view.findViewById(R.id.finding_card);
        finding_card.setBackgroundResource(R.drawable.card_top_rounded);
        details = view.findViewById(R.id.details);
        finding = view.findViewById(R.id.finding_text);
        pg_bar = view.findViewById(R.id.pg_hr);
        ratingBar = view.findViewById(R.id.rating);
        call = view.findViewById(R.id.call);
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_CALL);
                i.setData(Uri.parse("tel:"+"7904531832"));
                startActivity(i);
            }
        });
        qr = view.findViewById(R.id.qr);
        qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Toast.makeText(getActivity(),"Hello",Toast.LENGTH_SHORT).show();
                android.support.v7.app.AlertDialog.Builder alertbuilder = new android.support.v7.app.AlertDialog.Builder(getActivity());
                alertbuilder.setCancelable(true);
                View v = LayoutInflater.from(getActivity()).inflate(R.layout.qr_generator,null);
                v.setVerticalScrollBarEnabled(false);
                alertbuilder.setView(v);
                final android.support.v7.app.AlertDialog dialog = alertbuilder.create();
                dialog.show();
                Bitmap bitmap;
                ImageView qrImage = v.findViewById(R.id.QR_Image);
                String inputValue = "567319";
                if (inputValue.length() > 0) {
                    WindowManager manager = (WindowManager) getActivity().getSystemService(WINDOW_SERVICE);
                    Display display = manager.getDefaultDisplay();
                    Point point = new Point();
                    display.getSize(point);
                    int width = point.x;
                    int height = point.y;
                    int smallerDimension = width < height ? width : height;
                    smallerDimension = smallerDimension * 3 / 4;

                    qrgEncoder = new QRGEncoder(
                            inputValue, null,
                            QRGContents.Type.TEXT,
                            smallerDimension);
                    try {
                        bitmap = qrgEncoder.encodeAsBitmap();
                        qrImage.setImageBitmap(bitmap);
                    } catch (WriterException e) {
                        Log.v("Error", e.toString());
                    }
                }
            }
        });
//        LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
//        stars.getDrawable(2).setColorFilter(Color.YELLOW,PorterDuff.Mode.SRC_ATOP);
//        stars.getDrawable(0).setColorFilter(getResources().getColor(R.color.colorLightYello), PorterDuff.Mode.SRC_ATOP);
//        stars.getDrawable(1).setColorFilter(Color.TRANSPARENT, PorterDuff.Mode.SRC_ATOP);
        // Write a message to the database

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("message");


        initBottomSheet(view);
        initMap(view);


        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void initMap(View view){
        final HashMap<String,String> map = new HashMap<String,String>();

        m_mapFragment=(MapFragment)getActivity().getFragmentManager().findFragmentById(R.id.mapfragment);


        boolean success = com.here.android.mpa.common.MapSettings.setIsolatedDiskCacheRootPath(
                getActivity().getExternalFilesDir(null) + File.separator + ".here-maps",
                "intentone");

        if (success) {


            m_mapFragment.init(new OnEngineInitListener() {
                @Override
                public void onEngineInitializationCompleted(OnEngineInitListener.Error error) {

                    /*
                     * If no error returned from map fragment initialization, the map will be
                     * rendered on screen at this moment.Further actions on map can be provided
                     * by calling Map APIs.
                     */

                    if (error == Error.NONE) {
                        posManager = PositioningManager.getInstance();
                        if (posManager != null) {
                            posManager.start(
                                    PositioningManager.LocationMethod.GPS_NETWORK);
                        }
                        m_map = m_mapFragment.getMap();

                        m_map.setTrafficInfoVisible(true);
                        posManager.addListener(
                                new WeakReference<PositioningManager.OnPositionChangedListener>(positionListener));
                        m_map.getPositionIndicator().setVisible(true);

                        myRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                // This method is called once with the initial value and again
                                // whenever data at this location is updated.
//                                    String value = dataSnapshot.getValue(String.class);
                                Log.d("changed", "Value is: " + dataSnapshot.getValue());
                                for (DataSnapshot a:dataSnapshot.getChildren()){
                                    System.out.println("for"+a.getValue());

                                    map.put(a.getKey(),a.getValue()+"");
                                    map.put(a.getKey(),a.getValue()+"");
                                }



                                GeoCoordinate geoCoordinate=new GeoCoordinate(Double.parseDouble(map.get("latitude")),Double.parseDouble(map.get("longitude")));


                                Image img = new Image();

                                try {
                                    img.setImageResource(R.drawable.ma);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                MapMarker mapMarker = new MapMarker();
                                mapMarker.setIcon(img);

                                mapMarker.setCoordinate(geoCoordinate);
                                m_map.addMapObject(mapMarker);
                                m_mapObjectList.add(mapMarker);

                                m_map.setCenter(geoCoordinate, Map.Animation.NONE);
                                m_map.getPositionIndicator();

                            }

                            @Override
                            public void onCancelled(DatabaseError error) {
                                // Failed to read value
                                Log.w("changed", "Failed to read value.", error.toException());
                            }
                        });


                        posManager.addListener(
                                new WeakReference<PositioningManager.OnPositionChangedListener>(positionListener));

                    }
                    else
                    {
                        System.out.println("ERROR"+error.toString());
                    }
                }

            });
        }



    }


    @Override
    public void onResume() {
        super.onResume();
        paused = false;
        if (posManager != null) {
            posManager.start(
                    PositioningManager.LocationMethod.GPS_NETWORK);
        }
    }

    // To pause positioning listener
    @Override
    public void onPause() {
        if (posManager != null) {
            posManager.stop();
        }
        super.onPause();
        paused = true;
    }

    // To remove the positioning listener
    @Override
    public void onDestroy() {
        if (posManager != null) {
            // Cleanup
            posManager.removeListener(
                    positionListener);
        }
        m_map = null;
        super.onDestroy();
    }

    private PositioningManager.OnPositionChangedListener positionListener = new
            PositioningManager.OnPositionChangedListener() {

                public void onPositionUpdated(PositioningManager.LocationMethod method,
                                              GeoPosition position, boolean isMapMatched) {
                    // set the center only when the app is in the foreground
                    // to reduce CPU consumption
                    if (!paused)
                        m_map.setCenter(position.getCoordinate(),
                                Map.Animation.BOW);

                }


                public void onPositionFixChanged(PositioningManager.LocationMethod method,
                                                 PositioningManager.LocationStatus status) {
                }
            };

    private void initBottomSheet(final View view){
        final LinearLayout llBottomSheet = (LinearLayout) view.findViewById(R.id.bottom_sheet);
        final EditText editText=view.findViewById(R.id.pickup_et);
        final EditText clothes_et=view.findViewById(R.id.clothes);
        book_now=view.findViewById(R.id.book_now_btn);

        final BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(llBottomSheet);


        bottomSheetBehavior.setPeekHeight(140);

        bottomSheetBehavior.setHideable(false);

        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {

            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                cleanMap();
                SearchRequest searchRequest = new SearchRequest(s.toString());
                searchRequest.setSearchCenter(m_map.getCenter());
                searchRequest.execute(discoveryResultPageListener);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                editText.setText(s_ResultList.get(position).getTitle());

                address = s_ResultList.get(position).getTitle();
               PlaceLink placeLink= (PlaceLink) s_ResultList.get(position);
               addDestinationMarker(placeLink.getPosition().getLatitude(),placeLink.getPosition().getLongitude());
            }
        });

        book_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                llBottomSheet.setVisibility(View.GONE);
                findingcard();
                view.findViewById(R.id.notify_card).setVisibility(View.VISIBLE);
                Snackbar.make(view,"Successfully Booked your truck is on the way",Toast.LENGTH_LONG).show();
                AddBooking.insert("adithya",address,lat,lng,clothes_et.getText().toString());
                sendNotification();
            }
        });
    }

    //
//    public class FindingDriver extends Thread{
//
//        public void run(){
//            Looper.prepare();
////            finding_card.setVisibility(View.VISIBLE);
//            try{
//                sleep(5000);
//            }catch (InterruptedException e){
//                e.printStackTrace();
//            }
//            Toast.makeText(getActivity(),"asdfasf",Toast.LENGTH_LONG).show();
////            finding.setVisibility(View.GONE);
////            pg_bar.setVisibility(View.GONE);
////            details.setVisibility(View.VISIBLE);
//
//        }
//    }

    private void findingcard() {

        finding_card.setVisibility(View.VISIBLE);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                finding.setVisibility(View.GONE);
                pg_bar.setVisibility(View.GONE);
                details.setVisibility(View.VISIBLE);
            }
        },8000);



    }

    private ResultListener<DiscoveryResultPage> discoveryResultPageListener = new ResultListener<DiscoveryResultPage>() {
        @Override
        public void onCompleted(DiscoveryResultPage discoveryResultPage, ErrorCode errorCode) {
            if (errorCode == ErrorCode.NONE) {
                /* No error returned,let's handle the results */

                /*
                 * The result is a DiscoveryResultPage object which represents a paginated
                 * collection of items.The items can be either a PlaceLink or DiscoveryLink.The
                 * PlaceLink can be used to retrieve place details by firing another
                 * PlaceRequest,while the DiscoveryLink is designed to be used to fire another
                 * DiscoveryRequest to obtain more refined results.
                 */
                s_ResultList = discoveryResultPage.getItems();
                listView.setVisibility(View.VISIBLE);
                listAdapter = new ResultListAdapter(getContext(),
                        android.R.layout.simple_list_item_1,s_ResultList);
                listView.setAdapter(listAdapter);


                for (DiscoveryResult item : s_ResultList) {
                    /*
                     * Add a marker for each result of PlaceLink type.For best usability, map can be
                     * also adjusted to display all markers.This can be done by merging the bounding
                     * box of each result and then zoom the map to the merged one.
                     */

                    Log.e( "onCompleted: ",item.getTitle() );
                    if (item.getResultType() == DiscoveryResult.ResultType.PLACE) {
                        PlaceLink placeLink = (PlaceLink) item;
                        lat=placeLink.getPosition().getLatitude()+"";
                        lng=placeLink.getPosition().getLongitude()+"";
                        addMarkerAtPlace(placeLink);
                    }
                }
            } else {
                Toast.makeText(getContext(),
                        "ERROR:Discovery search request returned return error code+ " + errorCode,
                        Toast.LENGTH_SHORT).show();
            }
        }

    };

    private void addMarkerAtPlace(PlaceLink placeLink) {
        Image img = new Image();
        try {
            img.setImageResource(R.drawable.ma);
        } catch (IOException e) {
            e.printStackTrace();
        }

        MapMarker mapMarker = new MapMarker();
        mapMarker.setIcon(img);
        mapMarker.setCoordinate(new GeoCoordinate(placeLink.getPosition()));
        m_map.addMapObject(mapMarker);
        m_mapObjectList.add(mapMarker);
    }

    private void cleanMap() {
        if (!m_mapObjectList.isEmpty()) {
            m_map.removeMapObjects(m_mapObjectList);
            m_mapObjectList.clear();
        }
    }

    private void addDestinationMarker(Double d1,Double d2){
        Image img = new Image();
        try {
            img.setImageResource(R.drawable.dest_marker);
        } catch (IOException e) {
            e.printStackTrace();
        }

        MapMarker mapMarker = new MapMarker();
        mapMarker.setIcon(img);
        mapMarker.setCoordinate(new GeoCoordinate(d1,d2));
        m_map.addMapObject(mapMarker);
        m_mapObjectList.add(mapMarker);
    }

    public void sendNotification(){
        FirebaseFunctions mFunctions = FirebaseFunctions.getInstance();
        java.util.Map<String,Object> data = new HashMap<>();
        data.put("one",1);
        data.put("two",2);
        mFunctions.getHttpsCallable("addMessage").call(data).continueWith(new Continuation<HttpsCallableResult, Object>() {

            @Override
            public Object then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                String result;
                if (task.isSuccessful()) {
                    result = task.getResult().getData().toString();
                }
                else {
                    Log.e("Error",task.getException().getMessage());
                    result = "Failed";
                }
                Toast.makeText(getContext(), "Result:"+result, Toast.LENGTH_SHORT).show();
                return result;
            }
        });
    }


}

