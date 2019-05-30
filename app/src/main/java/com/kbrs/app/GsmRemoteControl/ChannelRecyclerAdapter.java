package com.kbrs.app.GsmRemoteControl;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Switch;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;
import static com.kbrs.app.GsmRemoteControl.MainActivity.AllOff;
import static com.kbrs.app.GsmRemoteControl.MainActivity.AllOn;
import static com.kbrs.app.GsmRemoteControl.SettingsActivity.SAVED_TEXT_GSM;
import static com.kbrs.app.GsmRemoteControl.MainActivity.customToast;


public class ChannelRecyclerAdapter extends ListAdapter<Channels, ChannelRecyclerAdapter.ViewHolder> {

    private OnItemLongClickListener listener;

    private OnSwitchClickListener listenerSwitch;

    private OnSwitchCheckedListener listenerSwitchChecked;

    private String phoneNumber;
    SharedPreferences phone;
    private int cnt;
    private final String defaultDesignator = "…"; // was "..."


//////////////////////////////////////////////////////////// Animation ////////////////////////////////////////////////////////////////////////////////////////////////////

    ChannelRecyclerAdapter() {
        super(DIFF_CALLBACK);
    }


    static final DiffUtil.ItemCallback<Channels> DIFF_CALLBACK = new DiffUtil.ItemCallback<Channels>() {

        @Override
        public boolean areItemsTheSame(Channels oldItem, Channels newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(Channels oldItem, Channels newItem) {
            return oldItem.getChName() == (newItem.getChName())
                    && oldItem.getChDesign().equals(newItem.getChDesign())
                    && oldItem.isChSwitch() == newItem.isChSwitch();
        }

        @Nullable
        @Override
        public Object getChangePayload(@NonNull Channels oldItem, @NonNull Channels newItem) {
            if (oldItem.isChSwitch() != newItem.isChSwitch()
            || oldItem.getChName() != newItem.getChName()
            || oldItem.getChDesign().equals(newItem.getChDesign()))  {
                return Boolean.TRUE;
            }else {
                return null;
            }
        }
    };

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Create the basic adapter extending from RecyclerView.Adapter
    // Note that we specify the custom ViewHolder which gives us access to our views


    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access

    class ViewHolder extends RecyclerView.ViewHolder {
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

            channelNumberTextView = itemView.findViewById(R.id.labelSwitchCast);
            channelDescriptionTextView = itemView.findViewById(R.id.dsgSwitchCast);
            channelSwitch = itemView.findViewById(R.id.switchCast);

            Typeface customFont = Typeface.createFromAsset(itemView.getContext().getAssets(), "font/Circe-Bold.ttf");
            channelDescriptionTextView.setTypeface(customFont);

            ///////////////////////////       Обработка  долгого нажатия на item в RecyclerView       /////////////////////////////////////////////////

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onItemLongClick(getItem(position));
                        v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                    }
                    return false;
                }
            });
        }

    }


    @NonNull
    @Override
    public ChannelRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, int viewType) {

        /*Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View customRowView = inflater.inflate(R.layout.custom_edittext_layout, parent, false);

        // Return a new holder instance
        // ViewHolder viewHolder = new ViewHolder(customRowView);
        return new ViewHolder(customRowView);*/

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

//////////////////////////////////////////////////////////////////////////////////   Обработка нажатий switch   /////////////////////////////////////////////////////////////////////////////////////

        viewHolder.channelSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = viewHolder.getAdapterPosition();
                int size = getItemCount();
                String designator = getItem(position).getChDesign();
                cnt = 0;
                phone = v.getContext().getSharedPreferences(SAVED_TEXT_GSM, MODE_PRIVATE);
                phoneNumber = phone.getString(SAVED_TEXT_GSM, "Номер не задан");
                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);

                if (phoneNumber.equals("Номер не задан")) {
                    customToast(viewHolder.itemView.getContext(), "Номер GSM устройства не задан,\nперейдите в \"Настройки\"");
                    channelSwitch.setChecked(getItem(position).isChSwitch());
                } else {

                    if (getItem(position).getChDesign().equals("...")) {
                        if (channelSwitch.isChecked()) {
                            channelSwitch.setChecked(true);
                            getItem(position).setChSwitch(true);
                            try {
                                SmsManager.getDefault().sendTextMessage(phoneNumber, null, "#1#" +
                                        String.valueOf(getItem(position).getChName()) + "#1#", null, null);
                                customToast(v.getContext(),
                                        "Канал " + String.valueOf(getItem(position).getChName()) + " будет включен");
                                /*customToast(v.getContext(),
                                        "SMS отправлено");*/
                            } catch (Exception e) {
                                customToast(v.getContext(),
                                        "SMS не отправлено");
                            }
                            notifyItemChanged(position);

                        } else {
                            channelSwitch.setChecked(false);
                            getItem(position).setChSwitch(false);
                            try {
                                SmsManager.getDefault().sendTextMessage(phoneNumber, null, "#1#" +
                                        String.valueOf(getItem(position).getChName()) + "#0#", null, null);
                                customToast(v.getContext(),
                                        "Канал " + String.valueOf(getItem(position).getChName()) + " будет выключен");
                                /*customToast(v.getContext(),
                                        "SMS отправлено");*/
                            } catch (Exception e) {
                                customToast(v.getContext(),
                                        "SMS не отправлено");
                            }

                            notifyItemChanged(position);
                        }
                        listenerSwitch.OnSwitchClick(getItem(position));


                    } else {

                        for (int i = 0; i <= size - 1; i++) {                                           // В цикле проходим по позициям всех item в RecyclerView для выявления необходимости действия
                            if (getItem(i).getChDesign().equalsIgnoreCase(designator)
                                    & !getItem(i).getChDesign().equals(defaultDesignator)) {            // в условии запрашиваем через getter состояние switch в item из таблицы БД
                                cnt++;                                                                  // если удовлетворяет условию инкременируем переменную счетчика cnt.
                            }
                        }

                        if (cnt > 1 & channelSwitch.isChecked()) {                                                                  // Если переменная cnt равна нулю
                            for (int j = 0; j <= size - 1; j++) {                                       // в цикле проходим по позициям всех item в RecyclerView для обновления состояний switch
                                if (getItem(j).getChDesign().equalsIgnoreCase(designator)
                                        & channelSwitch.isChecked()) {                                  // в условии запрашиваем через getter состояние switch в item из таблицы БД
                                    try {
                                        getItem(j).setChSwitch(true);                                       // и через setter устанавливаем новое значение
                                        listenerSwitch.OnSwitchClick(getItem(j));                           // далее вешаем слушатель.
                                        notifyItemChanged(j);                                               // ОБЯЗАТЕЛЬНО обновляем
                                    } catch (IndexOutOfBoundsException ex) {
                                        Log.e("group_switch_error", "Group switch enable error.");
                                    }
                                }
                            }
                            try {
                                SmsManager.getDefault().sendTextMessage(phoneNumber, null, "#1#" +
                                        designator + "#1#", null, null);
                                customToast(v.getContext(), "Группа каналов " + designator.substring(0, 1).toUpperCase() + designator.substring(1).toLowerCase() + " будет включена");    // designator first letter capitalize
                            } catch (Exception e) {
                                customToast(v.getContext(), "SMS не отправлено");
                            }

                        } else if (cnt > 1 & !channelSwitch.isChecked()) {
                            for (int j = 0; j <= size - 1; j++) {
                                if (getItem(j).getChDesign().equalsIgnoreCase(designator)
                                        & !channelSwitch.isChecked()) {
                                    try {
                                        getItem(j).setChSwitch(false);                                      // и через setter устанавливаем новое значение
                                        listenerSwitch.OnSwitchClick(getItem(j));                           // далее вешаем слушатель.
                                        notifyItemChanged(j);                                               // ОБЯЗАТЕЛЬНО обновляем через notifyItemChanged()
                                    } catch (IndexOutOfBoundsException ex) {
                                        Log.e("group_switch_error", "Group switch disable error.");
                                    }
                                }
                            }
                            try {
                                SmsManager.getDefault().sendTextMessage(phoneNumber, null, "#1#" +
                                        designator + "#0#", null, null);
                                customToast(v.getContext(), "Группа каналов " + designator.substring(0, 1).toUpperCase() + designator.substring(1).toLowerCase() + " будет выключена");
                                /*customToast(v.getContext(),
                                        "SMS отправлено");*/
                            } catch (Exception e) {
                                customToast(v.getContext(),
                                        "SMS не отправлено");
                            }
                        } else {
                            if (!getItem(position).isChSwitch()) {
                                try {
                                    getItem(position).setChSwitch(true);
                                    listenerSwitch.OnSwitchClick(getItem(position));
                                    notifyItemChanged(position);
                                } catch (IndexOutOfBoundsException ex) {
                                    Log.e("identified_switch_error", "Identified switch enable error.");
                                }
                                try {
                                    SmsManager.getDefault().sendTextMessage(phoneNumber, null, "#1#" +
                                            designator + "#1#", null, null);
                                    customToast(v.getContext(), "Канал " + designator + "  будет включен");
                                } catch (Exception e) {
                                    customToast(v.getContext(),
                                            "SMS не отправлено");
                                }
                            } else if (getItem(position).isChSwitch()) {
                                try {
                                    getItem(position).setChSwitch(false);
                                    listenerSwitch.OnSwitchClick(getItem(position));
                                    notifyItemChanged(position);
                                } catch (IndexOutOfBoundsException ex) {
                                    Log.e("identified_switch_error", "Identified switch disable error.");
                                }
                                SmsManager.getDefault().sendTextMessage(phoneNumber, null, "#1#" +
                                        designator + "#0#", null, null);
                                customToast(v.getContext(), "Канал " + designator + " будет выключен");
                            }
                        }

                    /*/// Alternative method
                    if (!currentChannels.isChSwitch()) {
                        //currentChannels.setChSwitch(true);
                        for (int i = 0; i <= size - 1; i++) {
                            if (getItem(i).getChDesign().equals(designator)) {
                                if (!getItem(i).isChSwitch()){
                                    getItem(i).setChSwitch(true);
                                    notifyItemChanged(i);
                                    listenerSwitch.OnSwitchClick(getItem(i));
                                }
                            }
                        }
                        customToast(v.getContext(), designator + " is Enable");
                        v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                    } else {
                        //currentChannels.setChSwitch(false);
                        for (int i = 0; i <= size - 1; i++) {
                            if (getItem(i).getChDesign().equals(designator) & !getItem(i).getChDesign().equals(defaultDesignator)) {
                                if (getItem(i).isChSwitch()) {
                                    getItem(i).setChSwitch(false);
                                    notifyItemChanged(i);
                                    listenerSwitch.OnSwitchClick(getItem(i));
                                }
                            }
                        }
                        customToast(v.getContext(), designator + " is Disable");
                        v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                    }*/

                    }
                }

            }
        });


        viewHolder.channelSwitch.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int position = viewHolder.getAdapterPosition();
                int size = getItemCount();
                String designator = getItem(position).getChDesign();
                cnt = 0;
                phone = v.getContext().getSharedPreferences(SAVED_TEXT_GSM, MODE_PRIVATE);
                phoneNumber = phone.getString(SAVED_TEXT_GSM, "Номер не задан");
                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);

                if (phoneNumber.equals("Номер не задан")) {
                    customToast(viewHolder.itemView.getContext(), "Номер GSM устройства не задан,\nперейдите в \"Настройки\"");
                    channelSwitch.setChecked(getItem(position).isChSwitch());
                } else {

                    if (getItem(position).getChDesign().equals("...")) {
                        if (channelSwitch.isChecked()) {
                            channelSwitch.setChecked(false);
                            getItem(position).setChSwitch(false);
                            customToast(v.getContext(),
                                    "Канал " + String.valueOf(getItem(position).getChName()) + " изменен");
                            notifyItemChanged(position);

                        } else {
                            channelSwitch.setChecked(true);
                            getItem(position).setChSwitch(true);
                            customToast(v.getContext(),
                                    "Канал " + String.valueOf(getItem(position).getChName()) + " изменен");
                            notifyItemChanged(position);
                        }
                        listenerSwitch.OnSwitchClick(getItem(position));


                    } else {

                        for (int i = 0; i <= size - 1; i++) {                                           // В цикле проходим по позициям всех item в RecyclerView для выявления необходимости действия
                            if (getItem(i).getChDesign().equalsIgnoreCase(designator)
                                    & !getItem(i).getChDesign().equals(defaultDesignator)) {            // в условии запрашиваем через getter состояние switch в item из таблицы БД
                                cnt++;                                                                  // если удовлетворяет условию инкременируем переменную счетчика cnt.
                            }
                        }

                        if (cnt > 1 & channelSwitch.isChecked()) {                                                                  // Если переменная cnt равна нулю
                            for (int j = 0; j <= size - 1; j++) {                                       // в цикле проходим по позициям всех item в RecyclerView для обновления состояний switch
                                if (getItem(j).getChDesign().equalsIgnoreCase(designator)
                                        & channelSwitch.isChecked()) {                                  // в условии запрашиваем через getter состояние switch в item из таблицы БД
                                    try {
                                        getItem(j).setChSwitch(true);                                       // и через setter устанавливаем новое значение
                                        listenerSwitch.OnSwitchClick(getItem(j));                           // далее вешаем слушатель.
                                        notifyItemChanged(j);                                               // ОБЯЗАТЕЛЬНО обновляем
                                    } catch (IndexOutOfBoundsException ex) {
                                        Log.e("group_switch_error", "Group switch enable error.");
                                    }
                                }
                            }
                            customToast(v.getContext(), "Группа каналов " + designator + " изменена");
                        } else if (cnt > 1 & !channelSwitch.isChecked()) {
                            for (int j = 0; j <= size - 1; j++) {
                                if (getItem(j).getChDesign().equalsIgnoreCase(designator)
                                        & !channelSwitch.isChecked()) {
                                    try {
                                        getItem(j).setChSwitch(false);                                      // и через setter устанавливаем новое значение
                                        listenerSwitch.OnSwitchClick(getItem(j));                           // далее вешаем слушатель.
                                        notifyItemChanged(j);                                               // ОБЯЗАТЕЛЬНО обновляем через notifyItemChanged()
                                    } catch (IndexOutOfBoundsException ex) {
                                        Log.e("group_switch_error", "Group switch disable error.");
                                    }
                                }
                            }
                            customToast(v.getContext(), "Группа каналов " + designator + " изменена");
                        } else {
                            if (!getItem(position).isChSwitch()) {
                                try {
                                    getItem(position).setChSwitch(true);
                                    listenerSwitch.OnSwitchClick(getItem(position));
                                    notifyItemChanged(position);
                                } catch (IndexOutOfBoundsException ex) {
                                    Log.e("identified_switch_error", "Identified switch enable error.");
                                }
                                customToast(v.getContext(), "Канал " + designator + " изменен");
                            } else if (getItem(position).isChSwitch()) {
                                try {
                                    getItem(position).setChSwitch(false);
                                    listenerSwitch.OnSwitchClick(getItem(position));
                                    notifyItemChanged(position);
                                } catch (IndexOutOfBoundsException ex) {
                                    Log.e("identified_switch_error", "Identified switch disable error.");
                                }
                                customToast(v.getContext(), "Канал " + designator + " изменен");
                            }
                        }

                    /*/// Alternative method
                    if (!currentChannels.isChSwitch()) {
                        //currentChannels.setChSwitch(true);
                        for (int i = 0; i <= size - 1; i++) {
                            if (getItem(i).getChDesign().equals(designator)) {
                                if (!getItem(i).isChSwitch()){
                                    getItem(i).setChSwitch(true);
                                    notifyItemChanged(i);
                                    listenerSwitch.OnSwitchClick(getItem(i));
                                }
                            }
                        }
                        customToast(v.getContext(), designator + " is Enable");
                        v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                    } else {
                        //currentChannels.setChSwitch(false);
                        for (int i = 0; i <= size - 1; i++) {
                            if (getItem(i).getChDesign().equals(designator) & !getItem(i).getChDesign().equals(defaultDesignator)) {
                                if (getItem(i).isChSwitch()) {
                                    getItem(i).setChSwitch(false);
                                    notifyItemChanged(i);
                                    listenerSwitch.OnSwitchClick(getItem(i));
                                }
                            }
                        }
                        customToast(v.getContext(), designator + " is Disable");
                        v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
                    }*/

                    }
                }

                return true;
            }
        });


//////////////////////////////////////////////////////////////////////////////////   Обработка нажатий ВКЛ/ВЫКЛ все каналы   ////////////////////////////////////////////////////////////////////////

        // todo: ВКЛЮЧИТЬ ПОСЛЕ NAVIGATION BOTTOM VIEW !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        /*AllOn.setOnClickListener(new View.OnClickListener() {                                   // Кнопка должна быть объявлена static для обработки в RecyclerView.Adapter
            @Override
            public void onClick(View v) {
                int size = getItemCount();                                                      // Присваиваем size количество созданных item в RecyclerView
                cnt = 0;                                                                        // Обнуляем переменную счетчика cnt
                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);                   // Вибро на кнопку

                phone = v.getContext().getSharedPreferences(SAVED_TEXT_GSM, MODE_PRIVATE);
                phoneNumber = phone.getString(SAVED_TEXT_GSM, "Номер не задан");

                if (phoneNumber.equals("Номер не задан")) {
                    customToast(viewHolder.itemView.getContext(), "Номер GSM устройства не задан,\nперейдите в \"Настройки\"");
                    channelSwitch.setChecked(getItem(position).isChSwitch());

                } else {

                    for (int i = 0; i <= size - 1; i++) {                                           // В цикле проходим по позициям всех item в RecyclerView для выявления необходимости действия
                        if (!getItem(i).isChSwitch()) {                                             // в условии запрашиваем через getter состояние switch в item из таблицы БД
                            cnt++;                                                                  // если удовлетворяет условию инкременируем переменную счетчика cnt.
                        }
                    }
                    if (cnt > 0) {                                                                  // Если переменная cnt равна нулю
                        for (int j = 0; j <= size - 1; j++) {                                       // в цикле проходим по позициям всех item в RecyclerView для обновления состояний switch
                            if (!getItem(j).isChSwitch()) {                                         // в условии запрашиваем через getter состояние switch в item из таблицы БД
                                getItem(j).setChSwitch(true);                                       // и через setter устанавливаем новое значение
                                listenerSwitchChecked.OnSwitchChecked(getItem(j));                  // далее вешаем слушатель.
                            }
                        }
                        notifyDataSetChanged();                                                     // ОБЯЗАТЕЛЬНО обновляем
                        SmsManager.getDefault().sendTextMessage(phoneNumber, null,
                                "#1#00#1#", null, null);
                        customToast(v.getContext(), "Все каналы включены");
                    } else {                                                                        // Если cnt==0, т.е. состояние всех switch уже соответствует нажатой кнопке
                        Toast toast = Toast.makeText(v.getContext(),                         // выводим соответствующее сообщение.
                                "Действие не требуется.", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.BOTTOM,
                                0, 250);
                        toast.show();
                    }
                }
            }
        });

        AllOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int size = getItemCount();
                cnt = 0;
                v.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);

                phone = v.getContext().getSharedPreferences(SAVED_TEXT_GSM, MODE_PRIVATE);
                phoneNumber = phone.getString(SAVED_TEXT_GSM, "Номер не задан");

                if (phoneNumber.equals("Номер не задан")) {
                    customToast(viewHolder.itemView.getContext(), "Номер GSM устройства не задан,\nперейдите в \"Настройки\"");
                    channelSwitch.setChecked(getItem(position).isChSwitch());

                } else {

                    for (int i = 0; i <= size - 1; i++) {
                        if (getItem(i).isChSwitch()) {
                            cnt++;
                        }
                    }
                    if (cnt > 0) {
                        for (int j = 0; j <= size - 1; j++) {

                            if (getItem(j).isChSwitch()) {
                                getItem(j).setChSwitch(false);
                                listenerSwitchChecked.OnSwitchChecked(getItem(j));
                            }
                        }
                        notifyDataSetChanged();
                        SmsManager.getDefault().sendTextMessage(phoneNumber, null,
                                "#1#00#0#", null, null);
                        customToast(v.getContext(), "Все каналы выключены");
                    } else {
                        Toast toast = Toast.makeText(v.getContext(),
                                "Действие не требуется.", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER_VERTICAL | Gravity.BOTTOM,
                                0, 250);
                        toast.show();
                    }

                }
            }
        });*/
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


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

    public void updateAdapter(List<Channels> newList){
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtilCallback(new ArrayList<Channels>(), newList));
        diffResult.dispatchUpdatesTo(this);
    }

}







