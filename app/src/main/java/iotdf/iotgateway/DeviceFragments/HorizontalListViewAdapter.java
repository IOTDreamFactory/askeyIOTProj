package iotdf.iotgateway.DeviceFragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import iotdf.iotgateway.R;

public class HorizontalListViewAdapter extends BaseAdapter{
    //private int[] mIconIDs;
    private String[] mTitles;
    private String[] numTitles;
    private Context mContext;
    private LayoutInflater mInflater;
    //Bitmap iconBitmap;
    private int selectIndex = -1;

    public HorizontalListViewAdapter(Context context, String[] titles, String[] numberTitles){
        this.mContext = context;
        //this.mIconIDs = ids;
        this.mTitles = titles;
        this.numTitles = numberTitles;
        mInflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);//LayoutInflater.from(mContext);
    }
    @Override
    public int getCount() {
        return mTitles.length;
    }
    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if(convertView==null){
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.horizontal_list_item, null);
            //holder.mImage=(ImageView)convertView.findViewById(R.id.img_list_item);
            holder.mTitle=(TextView)convertView.findViewById(R.id.text_list_item);
            holder.numTitle=(TextView)convertView.findViewById(R.id.text_list_item_num);
            convertView.setTag(holder);
        }else{
            holder=(ViewHolder)convertView.getTag();
        }
        if(position == selectIndex){
            convertView.setSelected(true);
        }else{
            convertView.setSelected(false);
        }

        holder.mTitle.setText(mTitles[position]);
        holder.numTitle.setText(numTitles[position]);
        //iconBitmap = getPropThumnail(mIconIDs[position]);
        //holder.mImage.setImageBitmap(iconBitmap);

        return convertView;
    }

    private static class ViewHolder {
        private TextView mTitle ;
        private TextView numTitle;
        //private ImageView mImage;
    }
    /*
    private Bitmap getPropThumnail(int id){
        Drawable d = mContext.getResources().getDrawable(id);
        Bitmap b = BitmapUtil.drawableToBitmap(d);
//      Bitmap bb = BitmapUtil.getRoundedCornerBitmap(b, 100);
        int w = mContext.getResources().getDimensionPixelOffset(R.dimen.thumnail_default_width);
        int h = mContext.getResources().getDimensionPixelSize(R.dimen.thumnail_default_height);

        Bitmap thumBitmap = ThumbnailUtils.extractThumbnail(b, w, h);

        return thumBitmap;
    }
    */
    public void setSelectIndex(int i){
        selectIndex = i;
    }
}