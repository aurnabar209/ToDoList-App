package com.example.doit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText editTextTask;
    private Button buttonAdd;
    private ListView listViewTasks;
    private ListView listViewCompletedTasks;
    private ArrayList<String> tasks;
    private ArrayList<String> completedTasks;
    private TaskAdapter adapter;
    private CompletedTaskAdapter completedTaskAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextTask = findViewById(R.id.editTextTask);
        buttonAdd = findViewById(R.id.buttonAdd);
        listViewTasks = findViewById(R.id.listViewTasks);
        listViewCompletedTasks = findViewById(R.id.listViewCompleatedTasks);

        tasks = new ArrayList<>();
        completedTasks = new ArrayList<>();
        adapter = new TaskAdapter(tasks);
        completedTaskAdapter = new CompletedTaskAdapter(completedTasks);
        listViewTasks.setAdapter(adapter);
        listViewCompletedTasks.setAdapter(completedTaskAdapter);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String task = editTextTask.getText().toString();
                if (!task.isEmpty()) {
                    tasks.add(task);
                    adapter.notifyDataSetChanged();
                    editTextTask.setText(""); // Clear the input field
                } else {
                    Toast.makeText(MainActivity.this, "Please enter a task", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private class TaskAdapter extends ArrayAdapter<String> {
        public TaskAdapter(ArrayList<String> tasks) {
            super(MainActivity.this, 0, tasks);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
                holder = new ViewHolder();
                holder.textViewTask = convertView.findViewById(R.id.textViewTask);
                holder.checkBoxTask = convertView.findViewById(R.id.checkBoxTask);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            String task = getItem(position);
            holder.textViewTask.setText(task);

            holder.checkBoxTask.setOnCheckedChangeListener(null); // Clear listener to avoid unwanted behavior
            holder.checkBoxTask.setChecked(false); // Reset checkbox state
            holder.checkBoxTask.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked) {
                    // Move the task to completed tasks
                    completedTasks.add(task);
                    tasks.remove(position);
                    adapter.notifyDataSetChanged();
                    completedTaskAdapter.notifyDataSetChanged();
                }
            });

            return convertView;
        }

        private class ViewHolder {
            TextView textViewTask;
            CheckBox checkBoxTask;
        }
    }

    private class CompletedTaskAdapter extends ArrayAdapter<String> {
        public CompletedTaskAdapter(ArrayList<String> completedTasks) {
            super(MainActivity.this, 0, completedTasks);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
                holder = new ViewHolder();
                holder.textViewTask = convertView.findViewById(R.id.textViewTask);
                holder.checkBoxTask = convertView.findViewById(R.id.checkBoxTask);
                holder.checkBoxTask.setEnabled(false); // Disable checkbox for completed tasks
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            String task = getItem(position);
            holder.textViewTask.setText(task);
            holder.checkBoxTask.setVisibility(View.GONE); // Hide checkbox for completed tasks

            return convertView;
        }

        private class ViewHolder {
            TextView textViewTask;
            CheckBox checkBoxTask;
        }
    }
}