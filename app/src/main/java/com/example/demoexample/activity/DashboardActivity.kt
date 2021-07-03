package com.example.demoexample.activity

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.demoexample.R
import com.example.demoexample.adapter.GraphAdapter
import com.example.demoexample.roomdatabase.AppDatabase
import com.example.demoexample.roomdatabase.GraphEntity
import com.example.demoexample.utils.AnimationTranslate
import com.example.demoexample.utils.Glob
import com.example.demoexample.utils.ProgressDialog
import kotlinx.android.synthetic.main.activity_dashboard.*

class DashboardActivity : AppCompatActivity(), View.OnClickListener,Glob.SelectedGraph {
    private var graphAdapter: GraphAdapter? = null
    private var progressDialog: ProgressDialog?=null
    private var ed_title_dialog: EditText? = null
    private var ed_score1_dialog: EditText? = null
    private var ed_score2_dialog: EditText? = null

    private var graphList: ArrayList<GraphEntity> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        Init()
        progressDialog = ProgressDialog(this)
        progressDialog!!.setCancelable(false)
        progressDialog!!.setCanceledOnTouchOutside(false)
        getGraphdata()
    }

    private fun getGraphdata() {
        progressDialog!!.show()
        val db = AppDatabase.getAppDataBase(context = this)
        Thread(Runnable {
            graphList.clear()
            val data = db!!.graphdao().getAllgraph()
            for (i in 0 until data.size) {
                graphList.add(data.get(i))
            }

            runOnUiThread(java.lang.Runnable {
                progressDialog!!.dismiss()
                graphAdapter!!.notifyDataSetChanged()
              Log.d("graphdata",graphList.size.toString())
            })
        }).start()

    }

    private fun Init() {
        txt_logout.setOnClickListener(this)
        rv_add.setOnClickListener(this)
        graph_recycler.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        graphAdapter = GraphAdapter(this, graphList,this)
        graph_recycler.adapter = graphAdapter

    }

    override fun onClick(v: View?) {
        when(v) {
            txt_logout -> {
                logout_dialog()
            }
            rv_add -> {
                add_graphpoints_dialog(false,-1)
            }
        }
    }

    private fun add_graphpoints_dialog(isedit: Boolean, position: Int) {
        val viewGroup: ViewGroup = findViewById(android.R.id.content)
        val OrderDialog: View =
            LayoutInflater.from(this).inflate(R.layout.add_points_dialog, viewGroup, false)
         ed_title_dialog = OrderDialog.findViewById<EditText>(R.id.ed_title)
         ed_score1_dialog = OrderDialog.findViewById<EditText>(R.id.ed_score1)
         ed_score2_dialog = OrderDialog.findViewById<EditText>(R.id.ed_score2)
        val txt_add = OrderDialog.findViewById<TextView>(R.id.txt_add)
        val iv_close = OrderDialog.findViewById<ImageView>(R.id.iv_close)
        if(isedit){
            ed_title_dialog!!.setText(graphList[position].title)
            ed_score1_dialog!!.setText(graphList[position].score1)
            ed_score2_dialog!!.setText(graphList[position].score2)
            txt_add!!.setText(getString(R.string.edit))
        }
        val builder = AlertDialog.Builder(this)
        builder.setView(OrderDialog)
        val addGraphalertDialog = builder.create()
        addGraphalertDialog!!.setCancelable(false)
        addGraphalertDialog.show()
        iv_close.setOnClickListener {
            addGraphalertDialog.dismiss()
        }
        txt_add.setOnClickListener {
            if(validation()){
                addGraphalertDialog.dismiss()
                if(isedit){
                 edit_graph_value(position)
                }else {
                    add_graph_value()
                }
            }
        }

    }

    private fun edit_graph_value(pos: Int) {
        progressDialog!!.show()
        val db = AppDatabase.getAppDataBase(context = this)
        Thread(Runnable {
            db!!.graphdao().updategraph(
                    ed_title_dialog!!.text.toString(),
                    ed_score1_dialog!!.text.toString(),
                    ed_score2_dialog!!.text.toString(),
                    graphList[pos].id.toString()
            )
            runOnUiThread(java.lang.Runnable {
                progressDialog!!.dismiss()
                graphList[pos].score1 = ed_score1_dialog!!.text.toString()
                graphList[pos].score2 = ed_score2_dialog!!.text.toString()
                graphList[pos].title = ed_title_dialog!!.text.toString()
                graphAdapter!!.notifyDataSetChanged()
            })
        }).start()
    }

    private fun add_graph_value() {
        progressDialog!!.show()
        val db = AppDatabase.getAppDataBase(context = this)
        Thread(Runnable {
            db!!.graphdao().insertgraph(
                GraphEntity(
                    null,
                    ed_title_dialog!!.text.toString(),
                    ed_score1_dialog!!.text.toString(),
                    ed_score2_dialog!!.text.toString()
                )
            )
            runOnUiThread(java.lang.Runnable {
                progressDialog!!.dismiss()
                getGraphdata()
            })
        }).start()
    }

    private fun validation(): Boolean {
        if (ed_title_dialog!!.text.isNullOrEmpty()) {
            ed_title_dialog!!.requestFocus()
            ed_title_dialog!!.setError(resources.getString(R.string.errortitle))
            return false
        } else if (ed_score1_dialog!!.text.isNullOrEmpty()) {
            ed_score1_dialog!!.requestFocus()
            ed_score1_dialog!!.setError(getString(R.string.errorscore1))
            return false
        } else if (ed_score1_dialog!!.text.isNullOrEmpty()) {
            ed_score1_dialog!!.requestFocus()
            ed_score1_dialog!!.setError(resources.getString(R.string.errorscore2))
            return false
        } else {
            return true
        }
    }

    private fun logout_dialog() {
        val builder: AlertDialog.Builder
        builder = AlertDialog.Builder(this)
        builder.setMessage(resources.getString(R.string.logoutmessage)).setTitle(resources.getString(R.string.logouttitle))
            .setCancelable(false)
            .setPositiveButton(resources.getString(R.string.yes),
                DialogInterface.OnClickListener { dialog, id ->
                    dialog.dismiss()
                    val sh = getSharedPreferences(Glob().Sh_prefName, Context.MODE_PRIVATE)
                    sh.edit().putBoolean(Glob().isUserLogin,false).commit()
                    sh.edit().clear().commit()
                    val intent = Intent(this,LoginActivity::class.java)
                    startActivity(intent)
                    AnimationTranslate().previewAnimation(this)
                    finishAffinity()
                })
            .setNegativeButton(resources.getString(R.string.no),
                DialogInterface.OnClickListener { dialog, id -> dialog.dismiss() })
        val alert = builder.create()
        if (!alert.isShowing) {
            alert.show()
        }

    }

    override fun deletegraph(pos: Int) {
        progressDialog!!.show()
        val db = AppDatabase.getAppDataBase(context = this)
        Thread(Runnable {
             db!!.graphdao().deletegraph(graphList[pos].id.toString())
            runOnUiThread(java.lang.Runnable {
                progressDialog!!.dismiss()
                graphList.removeAt(pos)
                graphAdapter!!.notifyDataSetChanged()
            })
        }).start()
    }

    override fun editgraph(pos: Int) {
        add_graphpoints_dialog(true, pos)
    }
}