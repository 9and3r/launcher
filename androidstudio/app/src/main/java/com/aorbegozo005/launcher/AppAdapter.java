package com.aorbegozo005.launcher;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class AppAdapter extends RecyclerView.Adapter<AppAdapter.AppItemHolder> {

    private ArrayList<AppInfo> apps;

    public AppAdapter(Context context){
        this.apps = new ArrayList<>();

        //This is where we build our list of app details, using the app
        //object we created to store the label, package name and icon

        PackageManager pm = context.getPackageManager();

        Intent i = new Intent(Intent.ACTION_MAIN, null);
        i.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> allApps = pm.queryIntentActivities(i, 0);
        for(ResolveInfo ri:allApps) {
            /*
            AppInfo app = new AppInfo();
            app.label = ri.loadLabel(pm);
            app.packageName = ri.activityInfo.packageName;
            app.icon = ri.activityInfo.loadIcon(pm);
            appsList.add(app);

             */
            AppInfo appInfo = new AppInfo(ri.loadLabel(pm).toString(), ri.activityInfo.packageName);
            appInfo.icon = ri.activityInfo.loadIcon(pm);
            //appInfo.icon = ri.activityInfo.loadBanner(pm);
            apps.add(appInfo);
        }
        int a = 0;
    }


    @NonNull
    @Override
    public AppItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.app_item, parent, false);
        return new AppItemHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AppItemHolder holder, int position) {
        holder.update(apps.get(position));
    }

    @Override
    public int getItemCount() {
        return apps.size();
    }

    public static class AppItemHolder extends RecyclerView.ViewHolder{

        private AppInfo appInfo;
        private TextView textViewAppName;
        private ImageView imageView;

        public AppItemHolder(@NonNull View itemView) {
            super(itemView);
            textViewAppName = itemView.findViewById(R.id.textViewAppName);
            imageView = itemView.findViewById(R.id.imageView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent launchIntent = view.getContext().getPackageManager().getLaunchIntentForPackage(appInfo.packageName);
                    view.getContext().startActivity(launchIntent);
                }
            });
        }

        public void update(AppInfo appInfo){
            this.appInfo = appInfo;
            this.textViewAppName.setText(appInfo.label);
            imageView.setImageDrawable(appInfo.icon);
        }

    }

    public static class AppInfo{
        public final String label;
        public final String packageName;
        public Drawable icon;

        public AppInfo(String label, String packageName){
            this.label = label;
            this.packageName = packageName;
        }
    }

}
