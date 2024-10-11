package com.example.socialapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.socialapp.R
import com.example.socialapp.apiservices.UserDetails

class ListAdapter(private val list: List<UserDetails>, val completion: (UserDetails) -> Unit) :
    RecyclerView.Adapter<ListAdapter.ListViewHolder>() {
    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.tv_name)
        val status: TextView = itemView.findViewById(R.id.tv_status)
        private val listItem: ConstraintLayout = itemView.findViewById(R.id.list_item)

        init {
            listItem.setOnClickListener {
                completion.invoke(list[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val item = list[position]
        holder.name.text = "Name: " + item.name
        holder.status.text = "Status: " + item.status
    }

    override fun getItemCount(): Int {
        return list.size
    }
}