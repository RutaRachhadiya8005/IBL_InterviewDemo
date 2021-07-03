package com.example.demoexample.adapter

import android.app.Activity
import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnLongClickListener
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.demoexample.R
import com.example.demoexample.roomdatabase.GraphEntity
import com.example.demoexample.utils.Glob


class GraphAdapter(
    val activity: Activity,
    val graphList: ArrayList<GraphEntity>,
    val selectgraph: Glob.SelectedGraph
) :
    RecyclerView.Adapter<GraphAdapter.ViewHolder>() {

    class ViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        var txt_title: TextView? = null
        var txt_score2: TextView? = null
        var txt_score1: TextView? = null
        var ly_graph: LinearLayout? = null

        init {
            txt_score1 = itemview.findViewById<TextView>(R.id.txt_score1)
            txt_score2 = itemview.findViewById<TextView>(R.id.txt_score2)
            txt_title = itemview.findViewById<TextView>(R.id.txt_title)
            ly_graph = itemview.findViewById<LinearLayout>(R.id.ly_graph)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(activity).inflate(R.layout.row_of_graph, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.txt_score1!!.text = graphList[position].score1
        holder.txt_score2!!.text = graphList[position].score2
        holder.txt_title!!.text = graphList[position].title
        holder.ly_graph!!.setOnLongClickListener(OnLongClickListener {
            opendialog(position)
            false
        })
    }

    private fun opendialog(position: Int) {
        val viewGroup: ViewGroup = activity.findViewById(android.R.id.content)
        val OrderDialog: View =
            LayoutInflater.from(activity).inflate(R.layout.add_edit_delete_dialog, viewGroup, false)

        val txt_edit = OrderDialog.findViewById<TextView>(R.id.txt_edit)
        val txt_delete = OrderDialog.findViewById<TextView>(R.id.txt_delete)
        val iv_close = OrderDialog.findViewById<ImageView>(R.id.iv_close)
        val builder = AlertDialog.Builder(activity)
        builder.setView(OrderDialog)
        val addGraphalertDialog = builder.create()
        addGraphalertDialog!!.setCancelable(false)
        addGraphalertDialog.show()
        iv_close.setOnClickListener {
            addGraphalertDialog.dismiss()
        }
        txt_edit.setOnClickListener {
            addGraphalertDialog.dismiss()
            selectgraph.editgraph(position)
        }
        txt_delete.setOnClickListener {
            addGraphalertDialog.dismiss()
            selectgraph.deletegraph(position)
        }
    }

    override fun getItemCount(): Int {
        return graphList.size
    }

}