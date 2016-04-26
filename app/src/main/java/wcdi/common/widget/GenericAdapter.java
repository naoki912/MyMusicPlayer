package wcdi.common.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class GenericAdapter<T> extends BaseAdapter {

    private Context mContext;

    private int mResource;

    private List<T> mObjects;

    private final Object mLock;

    private LayoutInflater mLayoutInflater;

    public GenericAdapter(Context context, int resource, List<T> objects) {
        mContext = context;

        mResource = resource;

        mObjects = objects;

        mLock = new Object();

        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public GenericAdapter(Context context, int resource) {
        this(context, resource, new ArrayList<T>());
    }

    @Override
    public T getItem(int position) {
        return mObjects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return (convertView != null)
                ? convertView
                : mLayoutInflater.inflate(mResource, parent, false);
        /*
        継承側のgetViewの頭に
        View view = super.getView(position, convertView, parent);
         */
    }

    @Override
    public int getCount() {
        return mObjects.size();
    }

    public Context getContext() {
        return mContext;
    }

    public int getPosition(T object) {
        return mObjects.indexOf(object);
    }

    public void add(T object) {
        synchronized (mLock) {
            mObjects.add(object);
        }

        notifyDataSetChanged();
    }

    public void addAll(Collection<? extends T> objects) {
        synchronized (mLock) {
            mObjects.addAll(objects);
        }

        notifyDataSetChanged();
    }

    public void addAll(T ... items) {
        synchronized (mLock) {
            Collections.addAll(mObjects, items);
        }

        notifyDataSetChanged();
    }

    public void remove(T object) {
        synchronized (mLock) {
            mObjects.remove(object);
        }

        notifyDataSetChanged();
    }

    public void clear() {
        synchronized (mLock) {
            mObjects.clear();
        }

        notifyDataSetChanged();
    }

    public void sort(Comparator<? super T> comparator) {
        synchronized (mLock) {
            Collections.sort(mObjects, comparator);
        }
    }

    public List<T> getAll() {
        return mObjects;
    }
}
