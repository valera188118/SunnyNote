package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.adapter.ListItem;
import com.example.myapplication.adapter.MainAdapter;
import com.example.myapplication.db.AppExecutor;
import com.example.myapplication.db.MyDbManager;
import com.example.myapplication.db.OnDataReceived;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class MainActivity extends AppCompatActivity implements OnDataReceived {
    private MyDbManager myDbManager;
    private EditText edTitle, edDesc;
    private TextView tvTest;
    private RecyclerView rcView;
    private MainAdapter mainAdapter;

    // private TextView ttx;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        MenuItem item = menu.findItem(R.id.id_search);
        SearchView sv = (SearchView) item.getActionView();
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(final String newText) {
                readFromDb(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void init() {
        myDbManager = new MyDbManager(this);
        edTitle = findViewById(R.id.edTitle);
        edDesc = findViewById(R.id.edDesc);
        rcView = findViewById(R.id.rcView);
        mainAdapter = new MainAdapter(this);
        rcView.setLayoutManager(new LinearLayoutManager(this));
        getItemTouchHelper().attachToRecyclerView(rcView);
        rcView.setAdapter(mainAdapter);
        // ttx = findViewById(R.id.textView);
        // ttx.setText("dfsfsdf");
    }

    //можно сделать шнягу чтобы все удалялось в дескрипшона
// на всяки можно сделать резервную копию
    protected void onResume() {
        super.onResume();
        myDbManager.openDb();
        readFromDb("");
    }

    public void onCLickAdd(View view) {
        Intent i = new Intent(MainActivity.this, EditActivity.class);
        startActivity(i);

    }

    public void onDestroy() {
        super.onDestroy();
        myDbManager.closeDb();
    }

    private ItemTouchHelper getItemTouchHelper() {
        return new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NotNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NotNull RecyclerView.ViewHolder viewHolder, int direction) {
                mainAdapter.RemoveFromList(viewHolder.getAdapterPosition(), myDbManager);
            }
        });

    }

    private void readFromDb(final String text) {
        AppExecutor.getInstance().getSubThread().execute(new Runnable() {
            @Override
            public void run() {
                myDbManager.getFromDb(text, MainActivity.this);
            }
        });
    }

    @Override

    public void onReceived(List<ListItem> list) {
        AppExecutor.getInstance().getMainThread().execute(new Runnable() {
            @Override
            public void run() {
                mainAdapter.updateAdapter(list);
            }
        });
    }
}

