package com.example.budget;

import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
public class ExampleAdapter extends RecyclerView.Adapter<ExampleAdapter.ExampleViewHolder> {
    private ArrayList<ExampleItem> mExampleList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemDelete(int position, ImageView edit, ImageView delete,
                          ImageView ok, EditText mTextView1, EditText mTextView2);

        void onItemEdit(int position, ImageView edit, ImageView delete, ImageView ok,
                        EditText mTextView1, EditText mTextView2 );

        void onItemOk(int position, ImageView edit, ImageView delete, ImageView ok, EditText mTextView1, EditText mTextView2);
    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public static class ExampleViewHolder extends RecyclerView.ViewHolder{
        public ImageView mImageView;
        public EditText mTextView1;
        public EditText mTextView2;
        public ImageView edit;
        public ImageView delete;
        public ImageView ok;
        public ImageView left,right;
        public TextView setDate;

        public ExampleViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.imageView);
            mTextView1 = itemView.findViewById(R.id.textView);
            mTextView2 = itemView.findViewById(R.id.textView2);
            edit = itemView.findViewById(R.id.fabedit);
            delete = itemView.findViewById(R.id.fabdelete);
            ok = itemView.findViewById(R.id.fabok);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                    }
                }
            });
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemDelete(position, edit, delete, ok, mTextView1, mTextView2);
                        }
                    }
                }
            });
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mTextView1.setEnabled(true);
                            listener.onItemEdit(position, edit, delete, ok, mTextView1, mTextView2);
                        }
                    }
                }
            });
            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mTextView1.setEnabled(true);
                            listener.onItemOk(position, edit, delete, ok, mTextView1, mTextView2);
                        }
                    }
                }
            });
        }
    }

    public ExampleAdapter(ArrayList<ExampleItem> exampleList) {
        mExampleList = exampleList;
    }

    @Override
    public ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.example_item, parent, false);
        ExampleViewHolder evh = new ExampleViewHolder(v, mListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(ExampleViewHolder holder, int position) {
        ExampleItem currentItem = mExampleList.get(position);
        holder.mImageView.setImageResource(currentItem.getImageResource());
        holder.mTextView1.setText(currentItem.getText1());
        holder.mTextView1.setEnabled(false);
        holder.mTextView2.setText(currentItem.getText2());
        holder.mTextView2.setEnabled(false);
        holder.edit.setImageResource(currentItem.getedit());
        holder.delete.setImageResource(currentItem.getdelete());
        holder.ok.setImageResource(currentItem.getok());
        holder.ok.setVisibility(View.INVISIBLE);
    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }
}
