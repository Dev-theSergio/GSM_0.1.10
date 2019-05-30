/*
package com.example.sergey.gsm;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Gravity;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SpinnerAdapter;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import static android.content.Context.MODE_PRIVATE;
import static com.example.sergey.gsm.MainActivity.AllOff;
import static com.example.sergey.gsm.MainActivity.AllOn;
import static SettingsActivity.SAVED_TEXT_GSM;
import static com.example.sergey.gsm.MainActivity.customToast;




    private OnItemLongClickListener listener;

    private OnSwitchClickListener listenerSwitch;

    private OnSwitchCheckedListener listenerSwitchChecked;

    private String phoneNumber;
    SharedPreferences phone;
    private int cnt;
    private final String defaultDesignator = "...";


    class ViewHolder
            extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        TextView channelNumberTextView;
        TextView channelDescriptionTextView;
        Switch channelSwitch;


        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(final View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);
            //Context context = itemView.getContext();


            channelNumberTextView = itemView.findViewById(R.id.labelSwitchCast);
            channelDescriptionTextView = itemView.findViewById(R.id.dsgSwitchCast);
            channelSwitch = itemView.findViewById(R.id.switchCast);


        }

    }


    @NonNull
    @Override
    public PickerAdapterChannel.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        */
/*Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View customRowView = inflater.inflate(R.layout.custom_edittext_layout, parent, false);

        // Return a new holder instance
        // ViewHolder viewHolder = new ViewHolder(customRowView);
        return new ViewHolder(customRowView);*//*


        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.channels_item, parent, false);

        return new ViewHolder(itemView);

    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, @SuppressLint("RecyclerView") final int position) {

        // Get the data model based on position
        final Channels currentChannels = getItem(position);

        // Set item views based on your views and data model
        viewHolder.channelNumberTextView.setText(String.valueOf(currentChannels.getChName()));
        viewHolder.channelDescriptionTextView.setText(currentChannels.getChDesign());
        viewHolder.channelSwitch.setChecked(currentChannels.isChSwitch());
        final Switch channelSwitch = viewHolder.channelSwitch;


    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    // Без getItemViewType повторяет значения switch.isChecked() через каждые 10 строк
    @Override
    public int getItemViewType(int position) {
        return position;
    }

//////////////////////////////////////////////    RecyclerView Item Position for Swipe Item Delete    //////////////////////////////////////////

    Channels getChannelAt(int position) {
        return getItem(position);
    }

////////////////////////////////////////////////////    For Edit Item, Click Switch, Click Button in Recycler View    //////////////////////////////////////////////////////

    public interface OnItemLongClickListener {
        void onItemLongClick(Channels channels);
    }

    public interface OnSwitchClickListener {
        void OnSwitchClick(Channels channels);
    }

    public interface OnSwitchCheckedListener {
        void OnSwitchChecked(Channels channels);
    }

    void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.listener = listener;
    }

    void setOnSwitchClickListener(OnSwitchClickListener listenerSwitch) {
        this.listenerSwitch = listenerSwitch;
    }

    void setOnSwitchCheckedListener(OnSwitchCheckedListener listenerSwitchChecked) {
        this.listenerSwitchChecked = listenerSwitchChecked;
    }

}







*/
