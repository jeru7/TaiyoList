// imports
package com.example.taiyomarket.main;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.example.taiyomarket.R;
import com.example.taiyomarket.adapters.ListAdapter;
import com.example.taiyomarket.classes.Item;
import com.example.taiyomarket.classes.ListItem;
import com.example.taiyomarket.classes.User;
import com.example.taiyomarket.database.DBHelper;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import com.example.taiyomarket.fragments.AddListPage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class LandingPage extends AppCompatActivity implements AddListPage.OnAddListListener, ListAdapter.OnItemLongClickListener {

//    variable declarations
    RecyclerView recyclerView;
    ListAdapter listAdapter;
    LinearLayout emptyLayout, listLayout, profileLayout;
    RelativeLayout mainPage, topBar;
    FrameLayout switchableContainer;
    ExtendedFloatingActionButton addNewListBtn;
    ImageView sortBtn;
    DBHelper db;
    User currentUser;
    String userEmail;
    AddListPage addListFragment;
    List<Item> itemList = new ArrayList<>();
    boolean sortAsc, sortDesc,defaultSeq = true;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);

//        initializations, mostly for ui elements (buttons, layouts, etc.) reason: para maging dynamic yung app natin.
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        emptyLayout = (LinearLayout) findViewById(R.id.empty_layout_display);
        topBar = (RelativeLayout) findViewById(R.id.relative_1);
        mainPage = (RelativeLayout) findViewById(R.id.relative_2);
        switchableContainer = (FrameLayout) findViewById(R.id.switchable_view);

        addNewListBtn = (ExtendedFloatingActionButton) findViewById(R.id.add_new_list);
        sortBtn = (ImageView) findViewById(R.id.sort_list);
        listLayout = (LinearLayout) findViewById(R.id.list_layout);
        profileLayout = (LinearLayout) findViewById(R.id.profile_layout);

        itemList = new ArrayList<>();

        db = new DBHelper(this);
        Intent i = getIntent();

//        get the user's "email" na pinasa nung nag-login yung user. reason: yung email na 'yan gagamitin natin sa database.
        userEmail = getIntent().getStringExtra("email");

//        checker lang if null 'yung email, tho hindi naman possible since may validation tayo sa sign in natin nilagay ko lang para malinis yung error messages habang cinocode ko.
        if (userEmail != null) {
//            dito na yung pag check sa database using the "email" extra
            currentUser = db.getUser(userEmail);
            if (currentUser != null) {
//              fetch ng lists ng user using the "email"
                displayList(userEmail);
            }
        }

//        methods na need mag run agad
        handleBackPress(); // set up ng pag handle sa back press
        attachButtonEvents(); // attach lang ng listeners sa mga buttons or button like elements
    }

    public void attachButtonEvents() {
        addNewListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                para mawala lang yung top bar and main page at matira yung fragment na pinasok natin tsaka yung nav bar sa ilalim (since naka above ng nav bar yung switchable view (refer to xml file ng landingpage), hindi mapapatungan ng addlistpage yung navbar)
                topBar.setVisibility(View.GONE);
                mainPage.setVisibility(View.GONE);
                startAddList(); // set up method para ma-connect yung fragment na addlistpage
            }
        });

//        method lang para ma-toggle yung values ng boolean variables na need for sorting the display list.
        sortBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (defaultSeq) {
                    sortAsc = true;
                    sortDesc = false;
                    defaultSeq = false;
                    sortBtn.setImageResource(R.drawable.sort_asc);
                } else if (sortAsc) {
                    sortAsc = false;
                    sortDesc = true;
                    sortBtn.setImageResource(R.drawable.sort_desc);
                } else if (sortDesc) {
                    sortAsc = false;
                    sortDesc = false;
                    defaultSeq = true;
                    sortBtn.setImageResource(R.drawable.sort_default);
                }
                displayList(userEmail);
            }
        });
    }

    public void startAddList() {
        addListFragment = new AddListPage(); // new instance lang ng addlistpage then auto set siya as a fragment sa constructor ni addlistpage

//        bundle, para lang mapasa natin yung "email" na extra sa fragment ng addlistpage as "email" na rin para pag tinawag siya ng addlistpage "email" din
        Bundle args = new Bundle();
        args.putString("email", userEmail);
        addListFragment.setArguments(args);

//      dito na papalitan yung switchable_view ng addlistpage fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.switchable_view, addListFragment);
        transaction.commit();
    }

//    method to display the list, gumamit ako ng collections para sa sort method.
    public void displayList(String email) {
//        fetch na latest list na meron si user
        List<ListItem> list = db.getLists(email);

        if (list != null && !list.isEmpty()) {
            //        logic for asc
            if (sortAsc) {
                Collections.sort(list, new Comparator<ListItem>() {
                    @Override
                    public int compare(ListItem item1, ListItem item2) {
                        return item1.getListName().compareToIgnoreCase(item2.getListName());
                    }
                });
                //        logic for desc (binaliktad lang yung sa return)
            } else if (sortDesc) {
                Collections.sort(list, new Comparator<ListItem>() {
                    @Override
                    public int compare(ListItem item1, ListItem item2) {
                        return item2.getListName().compareToIgnoreCase(item1.getListName());
                    }
                });
            }

//            rerender lang si recycler view (eto yung view na nagdidisplay nung list)
            recyclerView.setVisibility(View.VISIBLE);
//            inalis yung view for empty layout
            emptyLayout.setVisibility(View.GONE);

//          adapter na nagccreate nung container ng list (per list item)
            listAdapter = new ListAdapter(list, this);
//            set up lang ng recycler view (siya nagdidisplay nung list adapters, linearlayout since gusto ko naka vertical niya ididisplay)
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(listAdapter);
        } else {
//            if empty, balik yung layout na for empty tas alisin si recycler view
            emptyLayout.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }

// handles back press, dinedetect niya lang kung may nag back press tas may callback siya na pwedeng lagyan ng function na gusto mong i-run pag nag backpress si user within the landingpage
    public void handleBackPress() {
        OnBackPressedDispatcher dispatcher = getOnBackPressedDispatcher();

//        callback na magrrun once na nagbackpress
        dispatcher.addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
//                 checks kung may child count (laman) yung switchable view natin,
//                - pag meron: reremove niya lang si addlistpage sa switchable view.
//                - pag wala: do nothing, it means pag nasa landingpage si user, kahit mag back press siya hindi na babalik sa last activity which is yung sign in ^^
                if(switchableContainer.getChildCount() > 0) {
                    switchableContainer.removeAllViews();
                    topBar.setVisibility(View.VISIBLE);
                    mainPage.setVisibility(View.VISIBLE);
                }
            }
        });
    }

//    method lang siya to display the delete dialog natin pag ni-long press yung list container
    private void showDeleteDialog(int position) {
        new AlertDialog.Builder(this)
                .setTitle("Delete List")
                .setMessage("Are you sure you want to delete this list?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        deleteList(position);
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

//    deletes the list sa listAdapter using position (refer to ListAdapter), deletes the list in the database based on the listId.
    private void deleteList(int position) {
        ListItem listItem = listAdapter.getItems().get(position);
        long listId = listItem.getId();

//        delete methods within our db helper
        db.deleteList(listId);
        db.deleteItemsOfList(listId);

//        dito nangyayare yung removal ng listitem sa list adapter
        listAdapter.getItems().remove(position);
//        notify para mag-update yung listadapter
        listAdapter.notifyItemRemoved(position);

//        again toggles the visibility of the empty layout and recyclerview
        if (listAdapter.getItemCount() == 0) {
            emptyLayout.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }


//    overridden methods galing sa mga interface ng implemented classes/fragments/activities

//    eto yung nagrurun once na nag "create" ng list si user sa addlistpage (refer to addlistpage createBtn onclick)
    @Override
    public void onAddList() {
//        recall ng displaylist, to refresh the list.
        displayList(userEmail);
    }

//    eto yung nagrrun pag may nag-long click sa isang listAdapter
    @Override
    public void onItemLongClicked(int position) {
//        refer to showDeleteDialog method sa taas
        showDeleteDialog(position);
    }

}