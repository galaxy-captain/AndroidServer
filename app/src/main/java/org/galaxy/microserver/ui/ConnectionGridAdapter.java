package org.galaxy.microserver.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.galaxy.microserver.R;
import org.galaxy.microserver.server.MicroConnection;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by galaxy on 2017/1/24.
 */

public class ConnectionGridAdapter extends BaseAdapter {

    private Context mContext;

    private Holder holder;

    private List<MicroConnection> list = new ArrayList<>();

    public ConnectionGridAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {

            holder = new Holder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_connection, parent, false);

            holder.nameTv = (TextView) convertView.findViewById(R.id.item_connection_name_tv);

            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        MicroConnection connection = list.get(position);

        holder.nameTv.setText(connection.getName());

        return convertView;
    }

    public void remove(MicroConnection connection) {
        list.remove(connection);
        notifyDataSetChanged();
    }

    public void add(MicroConnection connection) {
        list.add(connection);
        notifyDataSetChanged();
    }

    public void update(MicroConnection connection) {

        int index = list.indexOf(connection);

        list.add(index, connection);

        list.remove(index + 1);

        notifyDataSetChanged();
    }

    class Holder {
        TextView nameTv;
    }

}
