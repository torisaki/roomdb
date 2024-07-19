package com.example.roomdb2;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ModuleAdapter extends RecyclerView.Adapter<ModuleAdapter.MyViewHolder> {
    private Context context;
    private List<Module> mModuleList;

    public ModuleAdapter(Context context) {
        this.context = context;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i){
        View view = LayoutInflater.from(context).inflate(R.layout.module_item, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ModuleAdapter.MyViewHolder myViewHolder, int i){
        myViewHolder.name.setText(mModuleList.get(i).getName());
        myViewHolder.desc.setText(mModuleList.get(i).getDescription());
        myViewHolder.os.setText(mModuleList.get(i).getOs());
        String imgPath = mModuleList.get(i).getImage();
        if(imgPath.startsWith("data:image/")){
            Glide.with(context)
                    .load(imgPath)
                    .into(myViewHolder.image);
        } else{
            int imageResourceId = context.getResources().getIdentifier(imgPath, "drawable", context.getPackageName());
            myViewHolder.image.setImageResource(imageResourceId);
        }
    }

    @Override
    public int getItemCount(){
        if(mModuleList == null){
            return 0;
        }
        return mModuleList.size();
    }
    public void setTasks(List<Module> moduleList){
        mModuleList = moduleList;
        notifyDataSetChanged();
    }
    public List<Module> getTasks(){
        return mModuleList;
    }
    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView name, desc, os;
        ImageView image;

        MyViewHolder(@NonNull final View itemView){
            super(itemView);

            name = itemView.findViewById(R.id.moduleName);
            desc = itemView.findViewById(R.id.moduleDes);
            os = itemView.findViewById(R.id.moduleOs);
            image = itemView.findViewById(R.id.moduleImage);
        }
    }
}
