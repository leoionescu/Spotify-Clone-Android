package com.mangoplay.yeezymusic.ui.genre;

import android.content.Context;
import android.graphics.Rect;
import android.util.TypedValue;
import android.view.View;

import com.mangoplay.yeezymusic.MainActivity;

import androidx.recyclerview.widget.RecyclerView;

public class GenreItemDecoration  extends RecyclerView.ItemDecoration{

    private final int decorationHeight = Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, MainActivity.r.getDisplayMetrics()));;
    private Context context;

    public GenreItemDecoration(Context context) {
        this.context = context;
//        decorationHeight = context.getResources().getDimensionPixelSize(R.dimen.decoration_height);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        if (parent != null && view != null) {

            int itemPosition = parent.getChildAdapterPosition(view);
            int totalCount = parent.getAdapter().getItemCount();

            if (itemPosition >= 0 && itemPosition < totalCount - 1) {
                outRect.bottom = decorationHeight;
                outRect.top = decorationHeight;
                outRect.left = decorationHeight;
                outRect.right = decorationHeight;
            }

        }

    }

}
