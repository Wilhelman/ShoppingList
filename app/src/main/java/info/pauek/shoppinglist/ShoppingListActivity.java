package info.pauek.shoppinglist;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ShoppingListActivity extends AppCompatActivity {

    // TODO: 1. Afegir un CheckBox a cada ítem, per marcar o desmarcar els ítems (al model també!) done
    // TODO: 2. Que es puguin afegir elements (+ treure els inicials)
    // TODO: 3. Afegir un menú amb una opció per esborrar de la llista tots els marcats.
    // TODO: 4. Que es pugui esborrar un element amb LongClick (cal fer OnLongClickListener)

    // Model
    List<ShoppingItem> items;

    // Referències a elements de la pantalla
    private RecyclerView items_view;
    private ImageButton btn_add;
    private EditText edit_box;
    private ShoppingListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        items = new ArrayList<>();

        items_view = findViewById(R.id.items_view);
        btn_add = findViewById(R.id.btn_add);
        edit_box = findViewById(R.id.edit_box);

        adapter = new ShoppingListAdapter(this, items);

        items_view.setLayoutManager(new LinearLayoutManager(this));
        items_view.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        );
        items_view.setAdapter(adapter);

        adapter.setOnClickListener(new ShoppingListAdapter.OnClickListener() {
            @Override
            public void onClick(int position, boolean checked) {
                String msg = "Has clicat: " + items.get(position).getName();
                Toast.makeText(ShoppingListActivity.this, msg, Toast.LENGTH_SHORT).show();
                items.get(position).setChecked(!items.get(position).isChecked());
            }
        });

        adapter.setOnLongClickListener(new ShoppingListAdapter.OnLongClickListener() {
            @Override
            public void onLongClick(int position) {
                EraseItemIn(position);
            }
        });
    }

    private void EraseItemIn(final int del_pos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Eliminar item");
        builder.setMessage("Segur que vols esborrar l'item?");
        builder.setPositiveButton("Esborrar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                items.remove(del_pos);
                adapter.notifyItemRemoved(del_pos);
            }
        });
        builder.setNegativeButton(android.R.string.cancel, null);
        builder.create().show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.shopping_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_view:
                for (int i = 0; i < items.size(); i++){
                    if (items.get(i).isChecked())
                    {
                        adapter.notifyItemRemoved(i);
                        items.remove(i);
                    }
                }
                return super.onOptionsItemSelected(item);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onAddClicked(View view) {
        String edit_text = edit_box.getText().toString();
        if(!edit_text.matches("")){
            items.add(new ShoppingItem(edit_text,false));
            adapter.notifyItemInserted(items.size()-1);
            edit_box.setText("");
        }
    }
}
