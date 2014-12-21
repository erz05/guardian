package com.erz.weathermap;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.List;
import java.util.Vector;

/**
 * Created by edgarramirez on 12/18/14.
 */
public class MyAddressAdapter extends BaseAdapter implements Filterable {

    Context context;
    Vector<Address> data;

    public MyAddressAdapter(Context context, Vector<Address> data){
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        if(data != null) return data.size();
        return 0;
    }

    @Override
    public Address getItem(int i) {
        if(data != null) return data.get(i);
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        view = inflater.inflate(R.layout.list_item, viewGroup, false);

        TextView title = (TextView) view.findViewById(R.id.title);

        Address address = getItem(i);
        if(address != null){
            String str = String.format(
                    "%s, %s, %s",
                    // If there's a street address, add it
                    address.getMaxAddressLineIndex() > 0 ?
                            address.getAddressLine(0) : "",
                    // Locality is usually address city
                    address.getLocality(),
                    // The country of the address
                    address.getCountryName());
            title.setText(str);
        }

        return view;
    }

    private static final NoFilter NO_FILTER = new NoFilter();

    @Override
    public Filter getFilter() {
        return NO_FILTER;
    }

    public void clear() {
        if(data != null) data.clear();
    }

    public void setData(List<Address> addressList){
        if(data == null){
            data = new Vector<Address>();
        }
        data.addAll(addressList);
    }

    public void add(Address address){
        if(data != null){
            data.add(address);
        }
    }

    private static class NoFilter extends Filter {
        protected Filter.FilterResults performFiltering(CharSequence prefix) {
            return new FilterResults();
        }

        protected void publishResults(CharSequence constraint, FilterResults results) {
            // Do nothing
        }
    }
}
