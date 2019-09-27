package com.example.theliers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.bluetooth_item.view.*

class BleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    class ViewAdapter() : RecyclerView.Adapter<BleViewHolder>(){

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BleViewHolder {
            return BleViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.bluetooth_item,
                    parent,
                    false
                ) as ConstraintLayout
            )
        }

        override fun getItemCount() = BluetoothDevices.list.size

        override fun onBindViewHolder(holder: BleViewHolder, position: Int) {
            val item = BluetoothDevices.list[position]
            println("xxxxxxxxxxx")
            if(item.name != null ){
                holder.itemView.deviceName.text = item.name
            }
            holder.itemView.deviceAddress.text = item.address

            holder.itemView.setOnClickListener {
                println("----------------cnnecting")
            }
        }

    }
}