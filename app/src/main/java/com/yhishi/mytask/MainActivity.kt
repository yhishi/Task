package com.yhishi.mytask

import android.os.Bundle
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast
import io.realm.Realm
import io.realm.kotlin.where

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import org.jetbrains.anko.startActivity
import com.yhishi.mytask.R.id.listView



class MainActivity : AppCompatActivity() {

    /* paramator */
    private lateinit var realm: Realm

    /* method */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        // Realmインスタンス作成
        realm = Realm.getDefaultInstance()

        // Realmから保存レコード取得
        val Tasks = realm.where<Task>().findAll()

        // リストビューにアダプター設定
        listView.adapter = TaskAdapter(Tasks)

        // タスク編集画面に遷移
        fab.setOnClickListener { view ->
            startActivity<TaskEditActivity>()
        }

        // 詳細画面遷移
        listView.setOnItemClickListener { parent, view, position, id -> // SAM変換
            // タップしたタスクのデータ位置取得・編集画面に渡す
            val task = parent.getItemAtPosition(position) as Task
            startActivity<TaskEditActivity>("task_id" to task.id)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }

}
