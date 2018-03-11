package com.example.pablo.contactlistextraction;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by pablo on 3/7/18.
 */

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> {
    private Context mContext;
    private List<ContactsModel> contactsModelList;

    public ContactsAdapter(Context context, List<ContactsModel> contactsModelList) {
        mContext = context;
        this.contactsModelList = contactsModelList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ContactsModel item = contactsModelList.get(position);
        holder.nametextView.setText(item.getName());
        holder.Contact_textView.setText(item.getPhoneNumber());
    }

    @Override
    public int getItemCount() {
        return contactsModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nametextView;
        TextView Contact_textView;

        public ViewHolder(View itemView) {
            super(itemView);
            nametextView = itemView.findViewById(R.id.edit_name);
            Contact_textView = itemView.findViewById(R.id.editContNumber);

        }
    }
}
