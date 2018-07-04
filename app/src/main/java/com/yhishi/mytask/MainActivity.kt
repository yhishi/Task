package com.yhishi.mytask

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import io.realm.Realm
import io.realm.kotlin.where

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import org.jetbrains.anko.startActivity

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
    }

    override fun onDestroy() {
        super.onDestroy()
        realm.close()
    }

}
