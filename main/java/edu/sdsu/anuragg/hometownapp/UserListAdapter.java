package edu.sdsu.anuragg.hometownapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by AnuragG on 19-Mar-17.
 */

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.UserListHolder> {

    private ArrayList<UserDataModel> userList;

    public class UserListHolder extends RecyclerView.ViewHolder {
        public TextView mNickname, mLocation, mYear;

        public UserListHolder(View view) {
            super(view);
            mNickname = (TextView) view.findViewById(R.id.usernicknameid);
            mLocation = (TextView) view.findViewById(R.id.user_location);
            mYear = (TextView) view.findViewById(R.id.user_year);
        }
    }

    public UserListAdapter(ArrayList<UserDataModel> userList) {
        this.userList = userList;
    }

    @Override
    public UserListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.users_list_row, parent, false);

        return new UserListHolder(itemView);
    }

    @Override
    public void onBindViewHolder(UserListHolder holder, int position) {
        UserDataModel user = userList.get(position);
        holder.mNickname.setText(user.nickname);
        holder.mLocation.setText((user.city!=null?user.city:"")+", "+user.state+", "+user.country);
        holder.mYear.setText(String.valueOf(user.year));
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
}
