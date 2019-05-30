package com.kbrs.app.GsmRemoteControl;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ImageSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.concurrent.atomic.AtomicBoolean;

public class SmsEventRecycler extends ListAdapter<SmsListTable, SmsEventRecycler.ViewHolder> {

    SmsEventRecycler() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<SmsListTable> DIFF_CALLBACK = new DiffUtil.ItemCallback<SmsListTable>() {
        @Override
        public boolean areItemsTheSame(SmsListTable oldItem, SmsListTable newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(SmsListTable oldItem, SmsListTable newItem) {
            return oldItem.getSmsBody().equals(newItem.getSmsBody()) &&
                    oldItem.getSmsDate().equals(newItem.getSmsDate()) &&
                    oldItem.getSmsTime().equals(newItem.getSmsTime());
        }
    };

    // Create the basic adapter extending from RecyclerView.Adapter
    // Note that we specify the custom ViewHolder which gives us access to our views

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        TextView smsBody;
        TextView smsDate;
        TextView smsTime;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            smsBody = itemView.findViewById(R.id.smsBodyItem);
            smsDate = itemView.findViewById(R.id.smsDateItem);
            smsTime = itemView.findViewById(R.id.smsTimeItem);
        }
    }


    @NonNull
    @Override
    public SmsEventRecycler.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sms_event_item, parent, false);

        return new ViewHolder(itemView);
    }


    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(@NonNull SmsEventRecycler.ViewHolder viewHolder, final int position) {

        // Get the data model based on position
        final SmsListTable currentSmsItem = getItem(position);

        // Set item views based on your views and data model
        viewHolder.smsBody.setText(String.valueOf(currentSmsItem.getSmsBody()));
        viewHolder.smsDate.setText(String.valueOf(currentSmsItem.getSmsDate()));
        viewHolder.smsTime.setText(String.valueOf(currentSmsItem.getSmsTime()));

        justify(viewHolder.smsBody);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    // Р‘РµР· getItemViewType РїРѕРІС‚РѕСЂСЏРµС‚ Р·РЅР°С‡РµРЅРёСЏ switch.isChecked() С‡РµСЂРµР· РєР°Р¶РґС‹Рµ 10 СЃС‚СЂРѕРє
    @Override
    public int getItemViewType(int position) {
        return position;
    }

    //////////////////////////////////////////       Форматирование текста sms в item по ширине      ////////////////////////////////////////////

    static void justify(final TextView textView) {

        final AtomicBoolean isJustify = new AtomicBoolean(false);

        final String textString = textView.getText().toString();

        final TextPaint textPaint = textView.getPaint();

        final SpannableStringBuilder builder = new SpannableStringBuilder();

        textView.post(new Runnable() {
            @Override
            public void run() {

                if (!isJustify.get()) {

                    final int lineCount = textView.getLineCount();
                    final int textViewWidth = textView.getWidth();

                    for (int i = 0; i < lineCount; i++) {

                        int lineStart = textView.getLayout().getLineStart(i);
                        int lineEnd = textView.getLayout().getLineEnd(i);

                        String lineString = textString.substring(lineStart, lineEnd);

                        if (i == lineCount - 1) {
                            builder.append(new SpannableString(lineString));
                            break;
                        }

                        String trimSpaceText = lineString.trim();
                        String removeSpaceText = lineString.replaceAll(" ", "");

                        float removeSpaceWidth = textPaint.measureText(removeSpaceText);
                        float spaceCount = trimSpaceText.length() - removeSpaceText.length();

                        float eachSpaceWidth = (textViewWidth - removeSpaceWidth) / spaceCount;

                        SpannableString spannableString = new SpannableString(lineString);
                        for (int j = 0; j < trimSpaceText.length(); j++) {
                            char c = trimSpaceText.charAt(j);
                            if (c == ' ') {
                                Drawable drawable = new ColorDrawable(0x00ffffff);
                                drawable.setBounds(0, 0, (int) eachSpaceWidth, 0);
                                ImageSpan span = new ImageSpan(drawable);
                                spannableString.setSpan(span, j, j + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            }
                        }

                        builder.append(spannableString);
                    }

                    textView.setText(builder);
                    isJustify.set(true);
                }
            }
        });
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


}










