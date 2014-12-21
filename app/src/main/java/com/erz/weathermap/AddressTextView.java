package com.erz.weathermap;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;

import java.io.IOException;
import java.util.List;
import java.util.Vector;

/**
 * Created by edgarramirez on 12/20/14.
 */
public class AddressTextView extends AutoCompleteTextView {

    MyAddressAdapter addressAdapter;
    private static final int MESSAGE_TEXT_CHANGED = 0;
    private static final int AUTOCOMPLETE_DELAY = 500;
    private static final int THRESHOLD = 3;
    private Handler messageHandler;
    private Address address;

    public AddressTextView(Context context) {
        super(context);
        init(context);
    }

    public AddressTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public void init(Context context){

        messageHandler = new MyMessageHandler(context);
        addressAdapter = new MyAddressAdapter(context, new Vector<Address>());

        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                messageHandler.removeMessages(MESSAGE_TEXT_CHANGED);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                String value = charSequence.toString();
                if (!"".equals(value) && value.length() >= THRESHOLD) {
                    Message msg = Message.obtain(messageHandler, MESSAGE_TEXT_CHANGED, charSequence.toString());
                    messageHandler.sendMessageDelayed(msg, AUTOCOMPLETE_DELAY);
                } else {
                    addressAdapter.clear();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i < addressAdapter.getCount()) {
                    Address selected = addressAdapter.getItem(i);
                    address = selected;
                    String str = String.format(
                            "%s, %s, %s",
                            // If there's a street address, add it
                            selected.getMaxAddressLineIndex() > 0 ?
                                    selected.getAddressLine(0) : "",
                            // Locality is usually address city
                            selected.getLocality(),
                            // The country of the address
                            selected.getCountryName());
                    setText(str);
                }
            }
        });

        setThreshold(THRESHOLD);
        setAdapter(addressAdapter);
    }

    public Address getAddress(){
        return address;
    }

    private void notifyResult(List<Address> suggestions) {
        //endAddress = null;
        addressAdapter.clear();
        for (Address a : suggestions) {
            addressAdapter.add(a);
        }
        addressAdapter.notifyDataSetChanged();
    }

    private class MyMessageHandler extends Handler {

        private Context context;

        public MyMessageHandler(Context context) {
            this.context = context;
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MESSAGE_TEXT_CHANGED) {
                String enteredText = (String) msg.obj;

                try {
                    notifyResult(new Geocoder(context).getFromLocationName(enteredText, 10));
                } catch (IOException ignore) {
                }
            }
        }
    }
}
