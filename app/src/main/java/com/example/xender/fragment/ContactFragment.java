package com.example.xender.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.xender.Dialog.MyApplication;
import com.example.xender.Loader.ContactsLoader;
import com.example.xender.R;
import com.example.xender.activity.ChooseActivity;
import com.example.xender.adapter.ContactAdapter;
import com.example.xender.db.FileSendDatabaseHandler;
import com.example.xender.handler.SendReceiveHandler;
import com.example.xender.model.Contact;
import com.example.xender.model.FileSend;
import com.example.xender.wifi.MyWifi;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ContactFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContactFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ListView listView;
    private ContactAdapter contactAdapter;
    ContactsLoader loader;


    public ContactFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ContactFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ContactFragment newInstance(String param1, String param2) {
        ContactFragment fragment = new ContactFragment();
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
        return inflater.inflate(R.layout.fragment_contact, container, false);

    }


    @Override
    public void onActivityCreated(@Nullable Bundle saveInstanceState) {
        super.onActivityCreated(saveInstanceState);

        if (ContextCompat.checkSelfPermission(
                getActivity(), android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.READ_CONTACTS},
                    ChooseActivity.READ_CONTACTS_PERMISSION);
        } else {
            loadContacts();
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        ChooseActivity sendActivity = (ChooseActivity) getActivity();
        sendActivity.contactFragment = this;
    }

    public void loadContacts() {
        loader = new ContactsLoader(getActivity());
        loader.getContactList();
        contactAdapter  = new ContactAdapter(getActivity(),R.layout.contact,loader.getContacts());
        listView = getActivity().findViewById(R.id.list_contacts);
        listView.setAdapter(contactAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Contact contact = contactAdapter.contacts.get(position);
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        AlertDialog.Builder b = new AlertDialog.Builder(MyApplication.getActivity());
                        b.setTitle("Confirm");
                        b.setMessage("Do you send this contact?");
                        b.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                               // MyApplication.getActivity().startForegroundService(intent);
                                sendContact(contact);
                            }
                        });
                        b.setCancelable(true);
                        b.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        dialog.cancel();
                                    }
                                }).start();

                            }
                        });
                        AlertDialog al = b.create();
                        al.show();

                    }
                });

            }
        });
    }

    private void sendContact(Contact contact){
        SendReceiveHandler handler =null;

        if (MyWifi.socket != null)
        {
            handler = new SendReceiveHandler(MyWifi.socket);
         //   connectionAddress = "Wifi";
        }
        else if (MyWifi.bluetoothSocket != null)
        {
            handler = new SendReceiveHandler(MyWifi.bluetoothSocket);
          //  connectionAddress = "Bluetooth";
        }


        if (handler != null) {
            try {
                handler.writeUTF("Contact");
                handler.writeUTF(contact.getName());
                handler.writeUTF(contact.getPhoneNumber());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }
}