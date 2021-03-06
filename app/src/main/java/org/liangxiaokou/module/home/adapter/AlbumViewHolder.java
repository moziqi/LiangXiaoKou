package org.liangxiaokou.module.home.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ms.square.android.expandabletextview.ExpandableTextView;

import org.liangxiaokou.module.R;
import org.liangxiaokou.widget.view.TouchImageView;

/**
 * Created by moziqi on 2015/12/17.
 */
public class AlbumViewHolder extends RecyclerView.ViewHolder {
    public View view_line;
    public ImageView iv_line_ico;
    public TextView tv_line_time;
    public View view_line_bottom;
    public TextView tv__album_user;
    public TextView expandable_text;
    public ExpandableTextView epxand_text_view;
    public TouchImageView iv_album_one;
    public TouchImageView iv_album_two;
    public TouchImageView iv_album_three;
    public TouchImageView iv_album_four;
    public TextView tv_album_address;

    public AlbumViewHolder(View itemView) {
        super(itemView);
        view_line = itemView.findViewById(R.id.view_line);
        iv_line_ico = (ImageView) itemView.findViewById(R.id.iv_line_ico);
        tv_line_time = (TextView) itemView.findViewById(R.id.tv_line_time);
        view_line_bottom = itemView.findViewById(R.id.view_line_bottom);
        tv__album_user = (TextView) itemView.findViewById(R.id.tv__album_user);
        expandable_text = (TextView) itemView.findViewById(R.id.expandable_text);
        epxand_text_view = (ExpandableTextView) itemView.findViewById(R.id.expand_text_view);
        iv_album_one = (TouchImageView) itemView.findViewById(R.id.iv_album_one);
        iv_album_two = (TouchImageView) itemView.findViewById(R.id.iv_album_two);
        iv_album_three = (TouchImageView) itemView.findViewById(R.id.iv_album_three);
        iv_album_four = (TouchImageView) itemView.findViewById(R.id.iv_album_four);
        tv_album_address = (TextView) itemView.findViewById(R.id.tv_album_address);
    }
}
